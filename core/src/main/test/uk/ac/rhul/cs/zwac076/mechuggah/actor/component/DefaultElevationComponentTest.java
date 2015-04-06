package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.CollisionActor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Actions.class)
public class DefaultElevationComponentTest {

    private static final float UP_X_SCALE = 1.2f;
    private static final float UP_Y_SCALE = 1.3f;
    private static final float DURATION = 2.0f;
    public static final float ALTERNATIVE_ORIGINAL_X_SCALE = 15f;
    private static final float ORIGINAL_Y_SCALE = 0.9f;
    private static final float ORIGINAL_X_SCALE = 0.8f;
    public static final float ALTERNATIVE_ORIGINAL_Y_SCALE = 13f;
    private ElevationComponent testElevationComponent;

    @Mock
    private CollisionActor mockCollisionActor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockGettingScale(ORIGINAL_X_SCALE, ORIGINAL_Y_SCALE);
        mockStatic(Actions.class);

        testElevationComponent = new DefaultElevationComponent(UP_X_SCALE, UP_Y_SCALE, mockCollisionActor);
    }

    private void mockGettingScale(float xScale, float yScale) {
        when(mockCollisionActor.getScaleX()).thenReturn(xScale);
        when(mockCollisionActor.getScaleY()).thenReturn(yScale);
    }

    @Test
    public void testMoveUp() throws Exception {
        testElevationComponent.moveUp();

        verify(mockCollisionActor).incrementZ();
    }

    @Test
    public void testMoveDown() throws Exception {
        testElevationComponent.moveDown();

        verify(mockCollisionActor).decrementZ();
    }

    @Test
    public void testGetMoveUpAction() throws Exception {
        testElevationComponent.getMoveUpAction(DURATION);

        verifyStatic();
        Actions.scaleTo(UP_X_SCALE, UP_Y_SCALE, DURATION);
    }

    @Test
    public void testGetMoveDownAction() throws Exception {
        testElevationComponent.getMoveDownAction(DURATION);

        verifyStatic();
        Actions.scaleTo(ORIGINAL_X_SCALE, ORIGINAL_Y_SCALE, DURATION);
    }

    @Test
    public void testGetMoveUpThenMoveDownAction() throws Exception {
        mockGettingScale(ALTERNATIVE_ORIGINAL_X_SCALE, ALTERNATIVE_ORIGINAL_Y_SCALE);

        testGetMoveUpAction();
        testElevationComponent.getMoveDownAction(DURATION);

        verifyStatic();
        Actions.scaleTo(ALTERNATIVE_ORIGINAL_X_SCALE, ALTERNATIVE_ORIGINAL_Y_SCALE, DURATION);
    }

    @Test
    public void testReset() throws Exception {
        testElevationComponent.reset();

        verify(mockCollisionActor).resetZ();
    }

    @Test
    public void testAct() throws Exception {

    }
}