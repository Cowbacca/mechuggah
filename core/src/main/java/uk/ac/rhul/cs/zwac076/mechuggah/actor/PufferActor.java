package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import uk.ac.rhul.cs.zwac076.mechuggah.event.CollisionEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LaneGroupDeletedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEventHandler;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PoolableNoLongerOnScreenEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEventListener;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PufferActor extends CollisionActor implements Poolable {

    private final class WithDeletionPlayerMovedEventHandler extends PlayerMovedEventHandler {
        private WithDeletionPlayerMovedEventHandler(Actor actor) {
            super(actor);
        }

        @Override
        protected void handleActorMovingOffScreen() {
            EventManager.getInstance().publishEvent(new LaneGroupDeletedEvent());
            EventManager.getInstance().publishEvent(new PoolableNoLongerOnScreenEvent((Poolable) actor));
            super.handleActorMovingOffScreen();
        }
    }

    private AnimationComponent animationComponent;
    private PlayerMovedEventHandler playerMovedEventListener;

    public PufferActor(AnimationComponent animationComponent, float x, float y, float width, float height,
            IntersectionChecker intersectionChecker) {
        super(x, y, width, height, intersectionChecker);
        this.animationComponent = animationComponent;
        EventManager.getInstance().registerListener(ResetGameEvent.class, new ResetGameEventListener(this));
        playerMovedEventListener = new WithDeletionPlayerMovedEventHandler(this);
        EventManager.getInstance().registerListener(PlayerMovedEvent.class, playerMovedEventListener);

    }

    @Override
    public void act(float delta) {
        animationComponent.updateAnimation(delta);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        animationComponent.drawAnimation(batch, getX(), getY());
    }

    @Override
    public boolean checkForCollision(Rectangle rectangle, float z, boolean ignoreZ) {
        return super.checkForCollision(rectangle, this.z, true);
    }

    @Override
    public void checkForCollisions(Collidable... collisionActors) {
        for (final Collidable collisionActor : collisionActors) {
            if (collisionActor.checkForCollision(boundingRectangle, z, true)) {
                EventManager.getInstance().publishEvent(new CollisionEvent(this, collisionActor));
            }
        }
    }

    public void init(int x, int y) {
        setPosition(x - animationComponent.getWidth() / 2, y);
        playerMovedEventListener.init();
    }

    @Override
    public void reset() {
        animationComponent.reset();

    }
}
