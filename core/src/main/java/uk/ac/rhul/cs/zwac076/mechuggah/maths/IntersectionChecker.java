package uk.ac.rhul.cs.zwac076.mechuggah.maths;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Responsible for checking for intersections between two rectangles.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class IntersectionChecker {

    private static final int INTERSECTION_LEEWAY = 2;
    private static final Rectangle INTERSECTION = new Rectangle();

    /**
     * Checks for intersection between two Rectangles, with a small leeway.
     * 
     * @param firstRectangle
     *            the first rectangle to intersect.
     * @param secondRectangle
     *            the second rectangle to intersect.
     * @return whether the rectangles intersect.
     */
    public boolean checkForIntersection(final Rectangle firstRectangle, final Rectangle secondRectangle) {
        boolean isIntersection = Intersector.intersectRectangles(firstRectangle, secondRectangle, INTERSECTION)
                && INTERSECTION.getHeight() > INTERSECTION_LEEWAY;
        return isIntersection;
    }
}
