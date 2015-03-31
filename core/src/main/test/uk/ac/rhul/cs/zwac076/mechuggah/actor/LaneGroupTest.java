package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Rectangle;

public class LaneGroupTest {

    private static final float TEST_LANE_SPEED = 200;
    private static final float TEST_DELTA = 0.1f;
    private static final float TEST_LANE_WIDTH = 100;
    private static final float TEST_ACTOR_WIDTH = 20;
    private static final float TEST_LANE_X = 1;
    private static final int TEST_DISTANCE_BETWEEN_ENEMIES = 100;
    private static final int TEST_Y_POSITION = 50;
    private static final int TEST_NUMBER_OF_ENEMIES = 2;
    private static final int TEST_WIDTH_OF_ENEMIES = 75;
    private static final float TEST_STARTING_DELAY = 0.7f;
    // Can't test Z-index as it relies on an actual Stage.
    // private static final int EXPECTED_Z_INDEX = 1;

    private LaneGroup laneGroup;
    @Mock
    private EnemyActor mockEnemyActor;
    @Mock
    private EnemyActor mockAnotherEnemyActor;
    @Mock
    private CollisionDetectingStage mockStage;
    @Mock
    private Collidable mockCollisionActor;
    @Mock
    private Collidable mockAnotherCollisionActor;
    @Mock
    private Rectangle mockRectangle;
    @Mock
    private ActorFactory mockActorFactory;

    private void laneSpeedVerfications() {
        verify(mockEnemyActor).setSpeed(TEST_LANE_SPEED);
        verify(mockAnotherEnemyActor).setSpeed(TEST_LANE_SPEED);
    }

    private void mockActorCollisionCheck(final EnemyActor enemyActor, final boolean collides) {
        when(enemyActor.checkForCollision(mockRectangle, 0, collides)).thenReturn(collides);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        laneGroup = new LaneGroup(mockStage);
        laneGroup.addActor(mockEnemyActor);
        laneGroup.addActor(mockAnotherEnemyActor);
        laneGroup.setX(TEST_LANE_X);
        laneGroup.setWidth(TEST_LANE_WIDTH);
    }

    private void stageMocks() {
        when(mockStage.getWidth()).thenReturn(TEST_LANE_WIDTH);
    }

    private void stageVerifications() {
        verify(mockStage).addCollidableActor(laneGroup);
    }

    @Test
    public void testAct() {
        when(mockEnemyActor.isRightOf(TEST_LANE_X + TEST_LANE_WIDTH)).thenReturn(true);
        when(mockEnemyActor.getWidth()).thenReturn(TEST_ACTOR_WIDTH);

        laneGroup.setLaneSpeed(TEST_LANE_SPEED);
        laneGroup.act(TEST_DELTA);

        verify(mockEnemyActor).setX(-(TEST_LANE_SPEED + TEST_ACTOR_WIDTH));

    }

    @Test
    public void testCheckForCollision() {
        mockActorCollisionCheck(mockEnemyActor, true);
        assertTrue(laneGroup.checkForCollision(mockRectangle, 0, false));
    }

    @Test
    public void testCheckForCollisionNoCollision() {
        mockActorCollisionCheck(mockEnemyActor, false);
        mockActorCollisionCheck(mockAnotherEnemyActor, false);
        assertFalse(laneGroup.checkForCollision(mockRectangle, 0, false));
    }

    @Test
    public void testCheckForCollisions() {
        laneGroup.checkForCollisions(mockCollisionActor, mockAnotherCollisionActor);
        verifyCollisionChecks(mockEnemyActor);
        verifyCollisionChecks(mockAnotherEnemyActor);

    }

    @Test
    public void testConstructor() {
        stageMocks();

        laneGroup = new LaneGroup(mockStage);

        stageVerifications();
    }

    @Test
    public void testLargeConstructor() {
        stageMocks();
        when(
                mockActorFactory.createEnemyActor(
                        (int) (-TEST_WIDTH_OF_ENEMIES - (TEST_STARTING_DELAY * TEST_LANE_SPEED)), 0)).thenReturn(
                mockEnemyActor);
        when(
                mockActorFactory.createEnemyActor(
                        (int) (-TEST_WIDTH_OF_ENEMIES * 2 - TEST_DISTANCE_BETWEEN_ENEMIES - TEST_STARTING_DELAY
                                * TEST_LANE_SPEED), 0)).thenReturn(mockAnotherEnemyActor);

        laneGroup = new LaneGroup(mockStage, mockActorFactory, TEST_Y_POSITION, TEST_LANE_SPEED, TEST_STARTING_DELAY,
                TEST_WIDTH_OF_ENEMIES, TEST_NUMBER_OF_ENEMIES, TEST_DISTANCE_BETWEEN_ENEMIES);
        stageVerifications();
        laneSpeedVerfications();
    }

    @Test
    public void testSetLaneSpeed() {
        laneGroup.setLaneSpeed(TEST_LANE_SPEED);
        laneSpeedVerfications();
    }

    private void verifyCollisionChecks(final EnemyActor enemyActor) {
        verify(enemyActor).checkForCollisions(mockCollisionActor, mockAnotherCollisionActor);
    }

}
