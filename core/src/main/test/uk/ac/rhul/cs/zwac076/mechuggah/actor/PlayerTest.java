package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.ElevationComponent;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.ElevationComponentFactory;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.MovingComponent;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.MovingComponentFactory;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MovingComponentFactory.class, ElevationComponentFactory.class})
public class PlayerTest {

    public static final float DISTANCE = 12.3f;
    public static final float UP_X_SCALE = 1.1f;
    public static final float UP_Y_SCALE = 1.2f;
    public static final int DURATION = 123;
    public static final double FLOAT_DELTA = 0.005;
    public static final int STARTING_SCALE = 1;
    private static final float PARENT_ALPHA = 0;
    private static final float Y = 1;
    private static final float WIDTH = 2;
    private static final float HEIGHT = 3;
    private static final float X = 4;
    private static final float DELTA = 0.016f;
    private static final float SPEED = 100;
    private static final float X_SCALE = 1.1f;
    private static final float Y_SCALE = 1.2f;
    private static final float ACCELERATION = 5;
    private static final float SPEED_INCREASE = 0.1f;
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
    @Mock
    private MovingComponentFactory mockMovingComponentFactory;
    @Mock
    private MovingComponent mockMovingComponent;
    @Mock
    private ElevationComponentFactory mockElevationComponentFactory;
    @Mock
    private CollisionActor mockCollisionActor;
    @Mock
    private ElevationComponent mockElevationComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockStatic(MovingComponentFactory.class, ElevationComponentFactory.class);

        PowerMockito.when(MovingComponentFactory.newFactory()).thenReturn(mockMovingComponentFactory);
        when(mockMovingComponentFactory.createMovingComponent(eq(SPEED), eq(ACCELERATION),
                any(Actor.class))).thenReturn(mockMovingComponent);

        PowerMockito.when(ElevationComponentFactory.newFactory()).thenReturn(mockElevationComponentFactory);
        when(mockElevationComponentFactory.createElevationComponent(eq(UP_X_SCALE), eq(UP_Y_SCALE),
                any(CollisionActor.class))).thenReturn(mockElevationComponent);

        player = new Player(mockAnimationComponent, X, Y, WIDTH, HEIGHT, SPEED, ACCELERATION, UP_X_SCALE, UP_Y_SCALE,
                mockIntersectionChecker);
    }

    @Test
    public void testAct() {
        player.act(DELTA);

        verify(mockMovingComponent).act(DELTA);
    }

    @Test
    public void testDraw() {
        when(mockAnimationComponent.getWidth()).thenReturn((int) WIDTH);
        player.setScale(X_SCALE, Y_SCALE);

        player.draw(mockBatch, PARENT_ALPHA);

        verify(mockAnimationComponent).drawAnimation(mockBatch, X, Y, X_SCALE, Y_SCALE, HEIGHT);
    }

    @Test
    public void testIsAbove() {
        assertEquals(true, player.isAbove(Y - 1));
    }

    @Test
    public void testIsAboveExactSameYValue() {
        assertEquals(false, player.isAbove(Y));
    }

    @Test
    public void testIsNotAbove() {
        assertEquals(false, player.isAbove(Y + 1));
    }

    @Test
    public void testReset() {
        player.reset();

        assertEquals(STARTING_SCALE, player.getScaleX(), FLOAT_DELTA);
        assertEquals(STARTING_SCALE, player.getScaleY(), FLOAT_DELTA);
        verify(mockElevationComponent).reset();
        verify(mockMovingComponent).reset();
    }

    @Test
    public void testCalculateTimeTakenToTravel() {
        player.calculateTimeTakenToTravel(DISTANCE);

        verify(mockMovingComponent).calculateTimeTakenToTravel(DISTANCE);
    }

    @Test
    public void testIncreaseSpeed() {
        player.increaseSpeed(SPEED_INCREASE);

        verify(mockMovingComponent).increaseSpeed(SPEED_INCREASE);

    }

    @Test
    public void testFreeze() {
        player.freeze();

        verify(mockMovingComponent).freeze();

    }

    @Test
    public void testUnFreeze() {
        player.unFreeze();

        verify(mockMovingComponent).unFreeze();

    }

    @Test
    public void testMoveUp() {
        player.moveUp();

        verify(mockElevationComponent).moveUp();
    }

    @Test
    public void testMoveDown() {
        player.moveDown();

        verify(mockElevationComponent).moveDown();
    }

    @Test
    public void testGetMoveUpAction() {
        when(mockElevationComponent.getMoveUpAction(DURATION)).thenReturn(mockAction);

        assertEquals(mockAction, player.getMoveUpAction(DURATION));

    }

    @Test
    public void testGetMoveDownAction() {
        when(mockElevationComponent.getMoveDownAction(DURATION)).thenReturn(mockAction);

        assertEquals(mockAction, player.getMoveDownAction(DURATION));
    }

}
