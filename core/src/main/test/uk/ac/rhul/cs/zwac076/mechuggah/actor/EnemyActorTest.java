package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.graphics.g2d.Batch;

public class EnemyActorTest {

    private static final float TEST_PARENT_ALPHA = 0;
    private static final float TEST_Y = 1;
    private static final float TEST_X = 2;
    private static final float TEST_WIDTH = 3;
    private static final float TEST_HEIGHT = 4;
    private static final float TEST_DELTA = 0.1f;
    private static final float TEST_PPS = 100;
    private EnemyActor enemyActor;

    @Mock
    private Batch mockBatch;
    @Mock
    private AnimationComponent mockAnimationComponent;
    @Mock
    private IntersectionChecker mockIntersectionChecker;

    private float calculateExpectedNewX(final float xBeforeAct) {
        return xBeforeAct + TEST_DELTA * TEST_PPS;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        enemyActor = new EnemyActor(mockAnimationComponent, TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT, TEST_PPS,
                mockIntersectionChecker);
    }

    @Test
    public void testAct() {
        testActTemplate(TEST_X);

    }

    private void testActTemplate(final float xBeforeAct) {
        enemyActor.act(TEST_DELTA);
        assertEquals(calculateExpectedNewX(xBeforeAct), enemyActor.getX(), 0.0f);
    }

    @Test
    public void testActTwice() {
        testActTemplate(TEST_X);
        testActTemplate(calculateExpectedNewX(TEST_X));

    }

    @Test
    public void testDraw() {
        enemyActor.draw(mockBatch, TEST_PARENT_ALPHA);
        verify(mockAnimationComponent).drawAnimation(mockBatch, TEST_X, TEST_Y);
    }
}
