package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent {

    private Animation animation;
    private TextureRegion currentFrame;
    private float stateTime;

    @Getter
    private int width;
    @Getter
    private int height;

    public AnimationComponent(final TextureRegion texture, final int frameColumns, final int frameRows,
            final float framesPerSecond) {
        setUpAnimation(texture, frameColumns, frameRows, framesPerSecond);

    }

    private void setUpAnimation(final TextureRegion texture, final int frameColumns, final int frameRows,
            final float framesPerSecond) {
        final TextureRegion[] frames = extractFrames(texture, frameColumns, frameRows);
        animation = new Animation(1 / framesPerSecond, frames);
        stateTime = 0;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    private TextureRegion[] extractFrames(final TextureRegion texture, final int frameColumns, final int frameRows) {
        width = texture.getRegionWidth() / frameColumns;
        height = texture.getRegionHeight() / frameRows;
        final TextureRegion[][] textureGrid = texture.split(width, height);
        final TextureRegion[] frames = new TextureRegion[frameColumns * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameColumns; j++) {
                frames[index++] = textureGrid[i][j];
            }
        }
        return frames;
    }

    public void updateAnimation(final float delta) {
        stateTime += delta;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    public void drawAnimation(Batch batch, float x, float y) {
        batch.draw(currentFrame, x, y, width, height);

    }

    /**
     * Keeps the sprite rendering centred when scaling, rather than scaling up
     * and to the right as is the default.
     * 
     * @param batch
     * @param x
     * @param y
     * @param scaleX
     * @param scaleY
     * @param actorHeight
     */
    public void drawAnimation(final Batch batch, float x, float y, float scaleX, float scaleY, float actorHeight) {
        float xDrawPosition = x + (width - width * scaleX) / 2;
        batch.draw(currentFrame, xDrawPosition, y - (height - actorHeight), width * scaleX, height * scaleY);
    }

    public void setFPS(final float fps) {
        animation.setFrameDuration(1 / fps);

    }

    public void reset() {
        stateTime = 0;

    }

}
