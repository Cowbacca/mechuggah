package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEventListener;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

/**
 * Actor representing the player.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class Player extends ElevationMovingActor implements EventListener {
    private Action powerUpAction;
    private Action inputAction;
    private AnimationComponent animationComponent;

    public Player(final AnimationComponent animationComponent, final float x, final float y, final float width,
            final float height, final float speed, final IntersectionChecker intersectionChecker) {
        super(x, y, width, height, speed, intersectionChecker);
        this.animationComponent = animationComponent;
        EventManager.getInstance().registerListener(ResetGameEvent.class, this);
        registerInputEventListener();
    }

    protected void registerInputEventListener() {
        EventManager.getInstance().registerListener(PlayerInputEvent.class, new PlayerInputEventListener(this));
    }

    /**
     * Moves the player in the y axis based on speed. Also gradually accelerates
     * the player to max speed.
     */
    @Override
    public void act(final float delta) {
        if (inputAction != null && inputAction.act(delta)) {
            inputAction = null;
        }
        if (powerUpAction != null && powerUpAction.act(delta)) {
            powerUpAction = null;
        }
        animationComponent.updateAnimation(delta);
        super.act(delta);
    }

    public void addPowerUpAction(Action action) {
        if (powerUpAction == null) {
            powerUpAction = action;
            action.setActor(this);
        }
    }

    public void addInputAction(Action action) {
        if (inputAction == null) {
            inputAction = action;
            action.setActor(this);
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        animationComponent.drawAnimation(batch, getX(), getY(), getScaleX(), getScaleY(), getHeight());
    }

    /**
     * Checks if the player is above a given y value.
     *
     * @param y
     *            the y value to check against.
     * @return whether or not the player is above.
     */
    public boolean isAbove(final float y) {
        return getY() > y;
    }

    /**
     * Publishes a PlayerMovedEvent when the player's position changes.
     */
    @Override
    protected void positionChanged() {
        super.positionChanged();
        EventManager.getInstance().publishEvent(new PlayerMovedEvent(getX(), getY(), true));
    }

    @Override
    protected void reset() {
        setScale(1);
        powerUpAction = null;
        inputAction = null;
        super.reset();

    }

    @Override
    public boolean handle(Event event) {
        reset();
        return false;
    }

}
