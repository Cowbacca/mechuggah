package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import uk.ac.rhul.cs.zwac076.mechuggah.event.CollisionEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

/**
 * Actor with collision handling.
 *
 * @author Angus J. Goldsmith
 */
public class CollisionActor extends Actor implements Collidable {
    protected final Rectangle boundingRectangle;
    private final IntersectionChecker intersectionChecker;
    protected float z;

    public CollisionActor(final float x, final float y, final float width, final float height,
                          final IntersectionChecker intersectionChecker) {
        setBounds(x, y, width, height);
        boundingRectangle = new Rectangle(x - width, y - height, width, height);
        this.intersectionChecker = intersectionChecker;
        z = 0;
    }

    /**
     * Updates the bounding rectangle of the collision actor with absolute
     * coordinates.
     */
    @Override
    public void act(final float delta) {
        super.act(delta);
        float x, y;
        if (getParent() != null && getParent().getParent() != null) {
            final Vector2 stageCoords = localToStageCoordinates(new Vector2(getX(), getY()));
            x = getX();
            y = stageCoords.y;

        } else {
            x = getX();
            y = getY();
        }
        updateBoundingRectangle(x, y);

    }

    protected void updateBoundingRectangle(float x, float y) {
        boundingRectangle.set(x, y, getWidth(), getHeight());
    }

    @Override
    public boolean checkForCollision(final Rectangle rectangle, final float z, boolean ignoreZ) {

        final boolean checkForIntersection = intersectionChecker.checkForIntersection(rectangle, boundingRectangle);
        if (!ignoreZ) {
            return this.z == z && checkForIntersection;
        } else {
            return checkForIntersection;
        }

    }

    @Override
    public void checkForCollisions(final Collidable... collisionActors) {
        for (final Collidable collisionActor : collisionActors) {
            if (collisionActor.checkForCollision(boundingRectangle, z, false)) {
                EventManager.getInstance().publishEvent(new CollisionEvent(this, collisionActor));
            }
        }

    }


    public void resetZ() {
        z = 0;
    }

    public void decrementZ() {
        z--;
    }

    public void incrementZ() {
        z++;
    }
}