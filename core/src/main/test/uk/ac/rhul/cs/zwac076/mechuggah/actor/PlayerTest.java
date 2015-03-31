package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;

public class PlayerTest {

    private static final float TEST_PARENT_ALPHA = 0;
    private static final float TEST_Y = 1;
    private static final float TEST_WIDTH = 2;
    private static final float TEST_HEIGHT = 3;
    private static final float TEST_X = 4;
    private static final float TEST_DELTA = 0.016f;
    private static final float TEST_SPEED = 100;
    private Player player;
    @Mock
    private Batch mockBatch;
    @Mock
    private AnimationComponent mockAnimationComponent;
    @Mock
    private IntersectionChecker mockIntersectionChecker;
    @Mock
    private Action mockAction;
    @Mock
    private Action mockAnotherAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        player = new Player(mockAnimationComponent, TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT, TEST_SPEED,
                mockIntersectionChecker);
    }

    @Test
    public void testAct() {
        final float previousY = player.getY();
        player.act(TEST_DELTA);
        final float newY = player.getY();
        assertEquals(TEST_SPEED * TEST_DELTA, newY - previousY, 0.01f);
    }

    @Test
    public void testAddAction() {
        player.addAction(mockAction);
        player.addAction(mockAnotherAction);
        assertEquals(1, player.getActions().size);
    }

    @Test
    public void testDraw() {

        when(mockAnimationComponent.getWidth()).thenReturn((int) TEST_WIDTH);
        player.draw(mockBatch, TEST_PARENT_ALPHA);
        verify(mockAnimationComponent).drawAnimation(mockBatch, TEST_X, TEST_Y);
    }

    @Test
    public void testIsAbove() {
        assertEquals(true, player.isAbove(TEST_Y - 1));
    }

    public void testIsAboveExactSameYValue() {
        assertEquals(false, player.isAbove(TEST_Y));
    }

    @Test
    public void testIsNotAbove() {
        assertEquals(false, player.isAbove(TEST_Y + 1));
    }
}
