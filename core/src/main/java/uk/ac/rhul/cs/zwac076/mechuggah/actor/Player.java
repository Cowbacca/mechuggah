package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import lombok.Getter;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEventListener;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * Actor representing the player.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class Player extends CollisionActor implements EventListener {
    private static final int SPEED_UP_MULTIPLIER = 10;
    private static final float MAX_SPEED = 1000;
    @Getter
    private float speed;
    private final float originalX;
    private final float originalY;
    private final float originalSpeed;
    private Action powerUpAction;
    private Action inputAction;
    private boolean frozen;
    private float preFreezeSpeed;
    private AnimationComponent animationComponent;

    public Player(final AnimationComponent animationComponent, final float x, final float y, final float width,
            final float height, final float speed, final IntersectionChecker intersectionChecker) {
        super(x, y, width, height, intersectionChecker);
        this.animationComponent = animationComponent;
        this.speed = speed;
        originalX = x;
        originalY = y;
        originalSpeed = speed;
        frozen = false;
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
        if (!frozen && speed < MAX_SPEED) {
            speed += delta * SPEED_UP_MULTIPLIER;
        }
        setY(getY() + delta * speed);
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
     * Moves the player down in the z-axis.
     */
    public void moveDown() {
        z--;
    }

    /**
     * Moves the player up in the z-axis.
     */
    public void moveUp() {
        z++;
    }

    /**
     * Publishes a PlayerMovedEvent when the player's position changes.
     */
    @Override
    protected void positionChanged() {
        super.positionChanged();
        EventManager.getInstance().publishEvent(new PlayerMovedEvent(getX(), getY(), true));
    }

    private void reset() {
        setPosition(originalX, originalY);
        speed = originalSpeed;
        z = 0;
        for (Action action : getActions()) {
            removeAction(action);
        }
        setScale(1);
        powerUpAction = null;
        inputAction = null;
        frozen = false;

    }

    @Override
    public boolean handle(Event event) {
        reset();
        return false;
    }

    public void increaseSpeed(float speedIncrease) {
        if (!frozen) {
            speed += speedIncrease;
        }

    }

    public void freeze() {
        if (!frozen) {
            frozen = true;
            preFreezeSpeed = speed;
            speed = 0;
        }
    }

    public void unFreeze() {
        if (frozen) {
            frozen = false;
            speed = preFreezeSpeed;
        }

    }
}
