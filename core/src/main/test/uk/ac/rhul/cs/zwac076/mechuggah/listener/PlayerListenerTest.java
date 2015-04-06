package uk.ac.rhul.cs.zwac076.mechuggah.listener;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;
import uk.ac.rhul.cs.zwac076.mechuggah.input.PlayerGestureListener;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class PlayerListenerTest {

    private static final float TEST_Y = 1;
    private static final int TEST_COUNT = 0;
    private static final float TEST_X = 2;
    private static final int TEST_BUTTON = 0;
    private static final float TEST_JUMP_DISTANCE = 3;
    private static final float TEST_SLIDE_DURATION = 6;
    private static final float TEST_X_SLIDE_BY = 7;
    private static final float TEST_SLIDE_DELAY = 8;
    private static final float TEST_Y_SLIDE_BY = 9;
    private static final float TEST_JUMP_SCALE = 10;
    private static final float TEST_PLAYER_SPEED = 11;
    private PlayerGestureListener playerListener;
    @Mock
    private InputEvent mockEvent;
    @Mock
    private Player mockPlayer;

    private void assertCorrectAction(final MoveByAction actionAdded, final float expectedXMoveBy,
                                     final float expectedYMoveBy, final float expectedDuration) {
        assertEquals(expectedXMoveBy, actionAdded.getAmountX(), 0.0f);
        assertEquals(expectedYMoveBy, actionAdded.getAmountY(), 0.0f);
        assertEquals(expectedDuration, actionAdded.getDuration(), 0.0f);
    }

    private void assertCorrectSequenceAction(final SequenceAction actionAdded, final boolean isRight) {
        final Array<Action> actions = actionAdded.getActions();
        int signModifier;
        if (isRight) {
            signModifier = 1;
        } else {
            signModifier = -1;
        }
        assertCorrectAction((MoveByAction) actions.get(0), signModifier * TEST_X_SLIDE_BY, TEST_Y_SLIDE_BY,
                TEST_SLIDE_DURATION);
        assertEquals(TEST_SLIDE_DELAY, ((DelayAction) actions.get(1)).getDuration(), 0.0f);
        assertCorrectAction((MoveByAction) actions.get(2), signModifier * -TEST_X_SLIDE_BY, TEST_Y_SLIDE_BY,
                TEST_SLIDE_DURATION);
    }

    private void assetScaleToActionIsCorrect(final ScaleToAction scaleToAction, final float scale) {
        assertEquals(scale, scaleToAction.getX(), 0.001f);
        assertEquals(scale, scaleToAction.getY(), 0.001f);
        assertEquals(TEST_JUMP_DISTANCE / TEST_PLAYER_SPEED / 4, scaleToAction.getDuration(), 0.001f);
    }

    private <T extends Action> T getActionAdded(final Class<T> actionClass) {
        final ArgumentCaptor<T> actionCaptor = ArgumentCaptor.forClass(actionClass);
        verify(mockPlayer).addAction(actionCaptor.capture());
        return actionCaptor.getValue();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        playerListener = new PlayerGestureListener();
    }

    @Test
    public void testFlingXVelocityGreaterThanYVelocityAndNegative() {
        playerListener.fling(-1, 0, TEST_BUTTON);
        final SequenceAction actionAdded = getActionAdded(SequenceAction.class);
        assertCorrectSequenceAction(actionAdded, false);
    }

    @Test
    public void testFlingXVelocityGreaterThanYVelocityAndPositive() {
        playerListener.fling(1, 0, TEST_BUTTON);
        final SequenceAction actionAdded = getActionAdded(SequenceAction.class);
        assertCorrectSequenceAction(actionAdded, true);
    }

    @Test
    public void testTap() {
        //when(mockPlayer.getSpeed()).thenReturn(TEST_PLAYER_SPEED);
        playerListener.tap(TEST_X, TEST_Y, TEST_COUNT, TEST_BUTTON);

        final SequenceAction actionAdded = getActionAdded(SequenceAction.class);

        final Array<Action> actions = actionAdded.getActions();
        assertEquals(5, actions.size);
        assetScaleToActionIsCorrect((ScaleToAction) actions.get(1), TEST_JUMP_SCALE);
        assertEquals(TEST_JUMP_DISTANCE / TEST_PLAYER_SPEED / 2, ((DelayAction) actions.get(2)).getDuration(), 0.001f);
        assetScaleToActionIsCorrect((ScaleToAction) actions.get(4), 1 / TEST_JUMP_SCALE);

    }
}
