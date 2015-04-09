package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import uk.ac.rhul.cs.zwac076.mechuggah.action.SpeedUpAction;
import uk.ac.rhul.cs.zwac076.mechuggah.action.StuckAction;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEventListener;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.RemotePlayerCollidedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.RemotePlayerInputEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

public class RemotePlayer extends Player {

    private Color colour;

    public RemotePlayer(AnimationComponent animationComponent, float x, float y, float width, float height,
                        float speed, final float acceleration, IntersectionChecker intersectionChecker) {
        super(animationComponent, x, y, width, height, speed, acceleration, intersectionChecker);
        EventManager.getInstance().registerListener(RemotePlayerCollidedEvent.class,
                new RemotePlayerCollidedEventListener());
        colour = new Color(1f, 135f / 255f, 211f / 255f, 1);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final Color initialColour = batch.getColor();
        batch.setColor(colour);
        super.draw(batch, parentAlpha);
        batch.setColor(initialColour);
    }

    @Override
    protected void positionChanged() {
        EventManager.getInstance().publishEvent(new PlayerMovedEvent(getX(), getY(), false));
    }

    @Override
    public boolean checkForCollision(Rectangle rectangle, float z, boolean ignoreZ) {
        return false;
    }

    @Override
    public void checkForCollisions(Collidable... collisionActors) {

    }

    @Override
    protected void registerInputEventListener() {
        EventManager.getInstance().registerListener(RemotePlayerInputEvent.class, new PlayerInputEventListener(this));
    }

    private final class RemotePlayerCollidedEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            RemotePlayerCollidedEvent remotePlayerCollidedEvent = (RemotePlayerCollidedEvent) event;
            switch (remotePlayerCollidedEvent.getCollisionType()) {
                case STUCK:
                    addPowerUpAction(new StuckAction());
                case SPEED_UP:
                    addPowerUpAction(new SpeedUpAction(200, 1f));
                    break;
                default:
                    break;

            }
            return false;
        }
    }
}
