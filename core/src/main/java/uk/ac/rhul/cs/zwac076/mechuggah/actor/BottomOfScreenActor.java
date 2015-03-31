package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import uk.ac.rhul.cs.zwac076.mechuggah.event.CollisionEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class BottomOfScreenActor extends CollisionActor {

    private final class FurthestForwardPlayerMovedEventListener implements EventListener {
        private final float worldHeight;

        private FurthestForwardPlayerMovedEventListener(float worldHeight) {
            this.worldHeight = worldHeight;
        }

        @Override
        public boolean handle(Event event) {
            PlayerMovedEvent playerMovedEvent = (PlayerMovedEvent) event;
            float newY = playerMovedEvent.getY() + calculateYPosition(worldHeight);
            if (newY > getY()) {
                setY(newY);
            }
            return false;
        }
    }

    private final class ResetGameEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            setY(initialPosition);
            return false;
        }
    }

    private static final int HEIGHT = 100;
    private float initialPosition;

    public BottomOfScreenActor(float worldWidth, final float worldHeight, IntersectionChecker intersectionChecker) {
        super(0, calculateYPosition(worldHeight), worldWidth, HEIGHT, intersectionChecker);
        initialPosition = getY();
        EventManager.getInstance().registerListener(PlayerMovedEvent.class,
                new FurthestForwardPlayerMovedEventListener(worldHeight));
        EventManager.getInstance().registerListener(ResetGameEvent.class, new ResetGameEventListener());

    }

    private static float calculateYPosition(float worldHeight) {
        return -(worldHeight / 2) - HEIGHT;
    }

    @Override
    public boolean checkForCollision(Rectangle rectangle, float z, boolean ignoreZ) {
        boolean checkForCollision = super.checkForCollision(rectangle, this.z, true);
        return checkForCollision;
    }

    @Override
    public void checkForCollisions(Collidable... collisionActors) {
        for (final Collidable collisionActor : collisionActors) {
            if (collisionActor.checkForCollision(boundingRectangle, z, true)) {
                EventManager.getInstance().publishEvent(new CollisionEvent(this, collisionActor));
            }
        }
    }

}
