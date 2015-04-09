package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

import com.badlogic.gdx.scenes.scene2d.Actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class MovingComponentFactoryTest {

    public static final int ORIGINAL_SPEED = 10;
    public static final int ACCELERATION = 11;
    private MovingComponentFactory movingComponentFactory;
    @Mock
    private Actor mockActor;

    @Before
    public void setUp() {
        initMocks(this);
        movingComponentFactory = MovingComponentFactory.newFactory();
    }

    @Test
    public void testCreateMovingComponent() throws Exception {
        assertEquals(new DefaultMovingComponent(ORIGINAL_SPEED, ACCELERATION, mockActor),
                movingComponentFactory.createMovingComponent(
                        ORIGINAL_SPEED, ACCELERATION, mockActor));
    }
}