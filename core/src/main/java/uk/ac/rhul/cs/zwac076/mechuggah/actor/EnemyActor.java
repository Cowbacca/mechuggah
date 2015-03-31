package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Actor representing an enemy.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class EnemyActor extends CollisionActor implements Poolable {

    private static final float DEFAULT_PIXELS_PER_SECOND = 200;
    private float pixelsPerSecondHorizontal;
    private AnimationComponent animationComponent;

    public EnemyActor(final AnimationComponent animationComponent, final float x, final float y, final float width,
            final float height, final float pixelsPerSecond, final IntersectionChecker intersectionChecker) {
        super(x, y, width, height, intersectionChecker);
        this.animationComponent = animationComponent;
        this.pixelsPerSecondHorizontal = pixelsPerSecond;
    }

    public EnemyActor(final AnimationComponent animationComponent, final float x, final float y, final float width,
            final float height, final IntersectionChecker intersectionChecker) {
        this(animationComponent, x, y, width, height, DEFAULT_PIXELS_PER_SECOND, intersectionChecker);
    }

    /**
     * Moves the enemy horizontally based on its speed.
     */
    @Override
    public void act(final float delta) {
        super.act(delta);
        setX(getX() + pixelsPerSecondHorizontal * delta);
        animationComponent.updateAnimation(delta);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        animationComponent.drawAnimation(batch, getX(), getY());
    }

    /**
     * Checks to see whether the enemy is to the right of a given x value.
     * 
     * @param x
     *            the x value to check against.
     * @return whether the enemy is to the right.
     */
    public boolean isRightOf(final float x) {
        return getX() >= x;
    }

    /**
     * Sets the horizontal speed of the enemy.
     * 
     * @param pixelsPerSecond
     *            the speed in pixels per second.
     */
    public void setSpeed(final float pixelsPerSecond) {
        this.pixelsPerSecondHorizontal = pixelsPerSecond;
    }

    @Override
    protected void updateBoundingRectangle(float x, float y) {
        super.updateBoundingRectangle(x, y);
        boundingRectangle.y += animationComponent.getHeight();
        boundingRectangle.set(x + getWidth() / 6, y + animationComponent.getHeight(), getWidth() * 4 / 6, getHeight());
    }

    @Override
    public void reset() {
        animationComponent.reset();
    }

    public void init(int x, int y) {
        setPosition(x, y);

    }

}
