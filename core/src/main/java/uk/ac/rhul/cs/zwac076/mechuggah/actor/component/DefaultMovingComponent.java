package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

import com.badlogic.gdx.scenes.scene2d.Actor;

import lombok.EqualsAndHashCode;

/**
 * Created by angus on 4/5/15.
 */
@EqualsAndHashCode
public class DefaultMovingComponent implements MovingComponent {
    private final float acceleration;
    private float originalSpeed;
    private float speed;
    private boolean frozen;
    private float preFrozenSpeed;
    private Actor actor;

    protected DefaultMovingComponent(float originalSpeed, float acceleration, Actor actor) {
        this.originalSpeed = originalSpeed;
        speed = originalSpeed;
        frozen = false;
        preFrozenSpeed = speed;
        this.acceleration = acceleration;
        this.actor = actor;
    }

    @Override
    public float calculateTimeTakenToTravel(float distance) {
        return distance / speed;
    }

    @Override
    public void increaseSpeed(float speedIncrease) {
        speed += speedIncrease;
    }

    @Override
    public void freeze() {
        if (!frozen) {
            preFrozenSpeed = speed;
            speed = 0;
            frozen = true;
        }
    }

    @Override
    public void unFreeze() {
        if (frozen) {
            speed = preFrozenSpeed;
            frozen = false;
        }
    }

    @Override
    public void reset() {
        frozen = false;
        speed = originalSpeed;
    }

    @Override
    public void act(float delta) {
        if (!frozen) {
            speed += delta * acceleration;
            actor.setY(actor.getY() + speed * delta);
        }
    }
}
