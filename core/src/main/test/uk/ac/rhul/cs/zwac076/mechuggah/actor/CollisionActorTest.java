package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EventManager.class })
public class CollisionActorTest {

    private static final float TEST_Y = 1;
    private static final float TEST_WIDTH = 2;
    private static final float TEST_X = 3;
    private static final float TEST_HEIGHT = 4;
    private static final Rectangle BOUNDING_RECTANGLE = new Rectangle(TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT);
    private CollisionActor collisionActor;
    @Mock
    private CollisionActor mockCollisionActor;
    @Mock
    private CollisionActor mockAnotherCollisionActor;
    @Mock
    private Rectangle mockRectangle;
    @Mock
    private IntersectionChecker mockIntersectionChecker;
    @Mock
    private EventManager mockEventManager;

    private void mockIntersectionChecking(final boolean intersects) {
        when(mockIntersectionChecker.checkForIntersection(mockRectangle, BOUNDING_RECTANGLE)).thenReturn(intersects);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        collisionActor = new CollisionActor(TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT, mockIntersectionChecker);
    }

    @Test
    public void testCheckForCollision() {
        mockIntersectionChecking(true);
        assertTrue(collisionActor.checkForCollision(mockRectangle, 0, false));

    }

    @Test
    public void testCheckForCollisionNoCollision() {
        mockIntersectionChecking(false);
        assertFalse(collisionActor.checkForCollision(mockRectangle, 0, false));
    }

    @Test
    public void testCheckForCollisions() {
        PowerMockito.mockStatic(EventManager.class);
        PowerMockito.when(EventManager.getInstance()).thenReturn(mockEventManager);
        when(mockCollisionActor.checkForCollision(BOUNDING_RECTANGLE, 0, false)).thenReturn(true);
        collisionActor.checkForCollisions(mockCollisionActor, mockAnotherCollisionActor);
        verify(mockAnotherCollisionActor).checkForCollision(BOUNDING_RECTANGLE, 0, false);
        verify(mockEventManager).publishEvent(isA(Event.class));
    }
}
