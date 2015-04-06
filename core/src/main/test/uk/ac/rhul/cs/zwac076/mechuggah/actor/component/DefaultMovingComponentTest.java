package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

import com.badlogic.gdx.scenes.scene2d.Actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultMovingComponentTest {

    public static final float ORIGINAL_SPEED = 100;
    public static final float ACCELERATION = 10;
    public static final float DISTANCE = 200;
    public static final float FLOAT_DELTA = 0.0005f;
    public static final float SPEED_INCREASE = 50;
    public static final int FROZEN_SPEED = 0;
    public static final float DELTA = 2f;
    public static final float ACTOR_Y = 11f;

    private MovingComponent movingComponent;
    @Mock
    private Actor mockActor;

    @Before
    public void setUp() {
        initMocks(this);
        movingComponent = new DefaultMovingComponent(ORIGINAL_SPEED, ACCELERATION, mockActor);
    }

    @Test
    public void testCalculateTimeTakenToTravel() throws Exception {
        assertTimeTakenToTravelEqualsDistanceOverSpeed(ORIGINAL_SPEED);
    }

    @Test
    public void testIncreaseSpeed() throws Exception {
        movingComponent.increaseSpeed(SPEED_INCREASE);
        assertTimeTakenToTravelEqualsDistanceOverSpeed((ORIGINAL_SPEED + SPEED_INCREASE));
    }

    private void assertTimeTakenToTravelEqualsDistanceOverSpeed(float speed) {
        final float timeTakenToTravel = movingComponent.calculateTimeTakenToTravel(DISTANCE);

        assertEquals(DISTANCE / speed, timeTakenToTravel, FLOAT_DELTA);
    }

    @Test
    public void testFreeze() throws Exception {
        movingComponent.freeze();

        assertTimeTakenToTravelEqualsDistanceOverSpeed(FROZEN_SPEED);
    }

    @Test
    public void testUnFreeze() throws Exception {
        movingComponent.increaseSpeed(SPEED_INCREASE);
        testFreeze();

        movingComponent.unFreeze();

        assertTimeTakenToTravelEqualsDistanceOverSpeed(ORIGINAL_SPEED + SPEED_INCREASE);

    }

    //This test ensures that calling freeze twice does not set the preFrozenSpeed to 0 in the second
    // freeze.
    @Test
    public void testTwoFreezeThenUnFreeze() throws Exception {
        testFreeze();
        testFreeze();

        movingComponent.unFreeze();

        assertTimeTakenToTravelEqualsDistanceOverSpeed(ORIGINAL_SPEED);

    }

    @Test
    public void testResetResetsSpeed() throws Exception {
        movingComponent.increaseSpeed(SPEED_INCREASE);

        movingComponent.reset();

        assertTimeTakenToTravelEqualsDistanceOverSpeed(ORIGINAL_SPEED);
    }

    @Test
    public void testResetResetsFrozen() throws Exception {
        movingComponent.freeze();

        movingComponent.reset();

        testFreeze();
    }

    @Test
    public void testUnFreezeAfterResetWhileFrozen() throws Exception {
        movingComponent.increaseSpeed(SPEED_INCREASE);
        movingComponent.freeze();

        movingComponent.reset();
        movingComponent.unFreeze();

        assertTimeTakenToTravelEqualsDistanceOverSpeed(ORIGINAL_SPEED);
    }

    @Test
    public void testAct() throws Exception {
        when(mockActor.getY()).thenReturn(ACTOR_Y);

        movingComponent.act(DELTA);

        float newSpeed = ORIGINAL_SPEED + DELTA * ACCELERATION;
        verify(mockActor).setY(ACTOR_Y + DELTA * newSpeed);
    }

    @Test
    public void testActFrozen() {
        movingComponent.freeze();

        movingComponent.act(DELTA);

        verifyZeroInteractions(mockActor);
    }

    @Test
    public void testActUnfrozenAgain() throws Exception {
        movingComponent.freeze();
        movingComponent.unFreeze();

        testAct();
    }
}