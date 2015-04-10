package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.ElevationComponent;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.ElevationComponentFactory;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.MovingComponent;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.MovingComponentFactory;
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
 */
public class Player extends CollisionActor implements EventListener, MovingComponent, ElevationComponent {
    private Action powerUpAction;
    private Action inputAction;
    private AnimationComponent animationComponent;
    private MovingComponent movingComponent;
    private ElevationComponent elevationComponent;

    public Player(final AnimationComponent animationComponent, final float x, final float y, final float width,
                  final float height, final float speed, float acceleration,
                  float upXScale, float upYScale, final IntersectionChecker intersectionChecker) {
        super(x, y, width, height, intersectionChecker);
        this.animationComponent = animationComponent;
        movingComponent = MovingComponentFactory.newFactory().createMovingComponent(speed, acceleration, this);
        elevationComponent = ElevationComponentFactory.newFactory().createElevationComponent(upXScale, upYScale, this);
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
        movingComponent.act(delta);
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
     * @param y the y value to check against.
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

    public void reset() {
        setScale(1);
        powerUpAction = null;
        inputAction = null;
        movingComponent.reset();
        elevationComponent.reset();

    }

    @Override
    public boolean handle(Event event) {
        reset();
        return false;
    }

    @Override
    public void moveUp() {
        elevationComponent.moveUp();
    }

    @Override
    public void moveDown() {
        elevationComponent.moveDown();
    }

    @Override
    public Action getMoveUpAction(float duration) {
        return elevationComponent.getMoveUpAction(duration);
    }

    @Override
    public Action getMoveDownAction(float duration) {
        return elevationComponent.getMoveDownAction(duration);
    }

    @Override
    public float calculateTimeTakenToTravel(float distance) {
        return movingComponent.calculateTimeTakenToTravel(distance);
    }

    @Override
    public void increaseSpeed(float speedIncrease) {
        movingComponent.increaseSpeed(speedIncrease);
    }

    @Override
    public void freeze() {
        movingComponent.freeze();
    }

    @Override
    public void unFreeze() {
        movingComponent.unFreeze();
    }

}
