package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import com.badlogic.gdx.math.Rectangle;

/**
 * Interface for collidable objects.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public interface Collidable {

    /**
     * Checks whether the Collidable object collides with other Collidable
     * objects.
     * 
     * @param collisionActors
     *            a list of Collidable objects to check.
     */
    public abstract void checkForCollisions(final Collidable... collisionActors);

    /**
     * Returns whether the object collides with a given rectangle and is on the
     * same z plane.
     * 
     * @param boundingRectangle
     *            bounding rectangle of the object to check for collision
     *            against.
     * @param z
     *            z plane of other object.
     * @return whether or not there was a collision.
     */
    public abstract boolean checkForCollision(final Rectangle boundingRectangle, final float z, boolean ignoreZ);

}