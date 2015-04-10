package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.CollisionActor;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class ElevationComponentFactoryTest {

    public static final float UP_Y_SCALE = 1.2f;
    public static final float UP_X_SCALE = 1.1f;
    private ElevationComponentFactory elevationComponentFactory;
    @Mock
    private CollisionActor mockCollisionActor;

    @Before
    public void setUp() {
        initMocks(this);
        elevationComponentFactory = ElevationComponentFactory.newFactory();
    }


    @Test
    public void testCreateElevationComponent() {
        assertEquals(new DefaultElevationComponent(UP_X_SCALE, UP_Y_SCALE, mockCollisionActor),
                elevationComponentFactory.createElevationComponent(UP_X_SCALE, UP_Y_SCALE, mockCollisionActor));


    }
}
