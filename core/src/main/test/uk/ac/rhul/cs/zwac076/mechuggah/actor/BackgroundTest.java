package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BackgroundTest {

    private static final int DELTA_DIVISOR = 10;
    private static final float TEST_DELTA = 0.016f;
    private static final float TEST_STARTING_GREYNESS = 0.1f;
    private static final float TEST_UPPER_THRESHOLD = 0.2f;
    private static final boolean TEST_INCREASING = true;
    private static final float TEST_LOWER_THRESHOLD = 0.1f;
    private static final float TEST_PARENT_ALPHA = 0;
    private static final float TEST_X = 1;
    private static final float TEST_Y = 2;
    private static final float TEST_WIDTH = 10;
    private static final float TEST_HEIGHT = 20;

    private Background background;

    @Mock
    private TextureRegion mockTexture;
    @Mock
    private Batch mockBatch;
    @Mock
    private Color mockBatchColor;
    @Mock
    private Color mockColor;
    @Mock
    private Stage mockStage;

    private void callAndVerifyUpdate(final Float... newValues) {

        for (final float newValue : newValues) {
            background.act(TEST_DELTA);
            assertEquals(newValue, mockColor.a, 0.0001f);
        }
    }

    private Background createBackground(final float startingGreyness) {
        return createBackground(startingGreyness, TEST_INCREASING);
    }

    private Background createBackground(final float startingGreyness, final boolean increasing) {
        final Background createdBackground = new Background(mockTexture, mockColor);
        createdBackground.setBounds(TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT);
        return createdBackground;
    }

    private float createOverThresholdValue() {
        final float overThresholdValue = TEST_UPPER_THRESHOLD + TEST_DELTA;
        return overThresholdValue;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        background = createBackground(TEST_STARTING_GREYNESS);

    }

    @Test
    public void testDraw() {
        when(mockBatch.getColor()).thenReturn(mockBatchColor);
        background.draw(mockBatch, TEST_PARENT_ALPHA);
        verifyDraw();
    }

    @Test
    public void testTwoUpdates() {
        callAndVerifyUpdate(TEST_STARTING_GREYNESS + TEST_DELTA / DELTA_DIVISOR, TEST_STARTING_GREYNESS + TEST_DELTA
                / DELTA_DIVISOR + TEST_DELTA / DELTA_DIVISOR);
    }

    @Test
    public void testUpdate() {
        callAndVerifyUpdate(TEST_STARTING_GREYNESS + TEST_DELTA / DELTA_DIVISOR);
    }

    @Test
    public void testUpdateOverThreshold() {
        final float overThresholdValue = createOverThresholdValue();
        background = createBackground(overThresholdValue);
        callAndVerifyUpdate(overThresholdValue - TEST_DELTA / DELTA_DIVISOR);
    }

    @Test
    public void testUpdateOverThresholdThenNormalUpdate() {
        final float overThresholdValue = createOverThresholdValue();
        background = createBackground(overThresholdValue);
        float greynessValue = overThresholdValue;
        final List<Float> valuesList = new ArrayList<Float>();
        while (greynessValue >= TEST_UPPER_THRESHOLD) {
            greynessValue -= TEST_DELTA / DELTA_DIVISOR;
            valuesList.add(greynessValue);
        }
        valuesList.add(greynessValue -= TEST_DELTA / DELTA_DIVISOR);
        callAndVerifyUpdate(valuesList.toArray(new Float[valuesList.size()]));

    }

    @Test
    public void testUpdateUnderLowerThreshold() {
        final float underThresholdGreyness = TEST_LOWER_THRESHOLD - TEST_DELTA / DELTA_DIVISOR;
        background = createBackground(underThresholdGreyness, false);
        callAndVerifyUpdate(underThresholdGreyness + TEST_DELTA / DELTA_DIVISOR);
    }

    private void verifyDraw() {
        verify(mockBatch).setColor(mockColor);
        verify(mockBatch).draw(mockTexture, TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT);
        verify(mockBatch).setColor(mockBatchColor);
    }
}
