package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by angus on 4/9/15.
 */
public abstract class MovingComponentFactory {

    public static MovingComponentFactory newFactory() {
        return new DefaultMovingComponentFactory();
    }


    public abstract MovingComponent createMovingComponent(float originalSpeed, float acceleration, Actor actor);

    private static class DefaultMovingComponentFactory extends MovingComponentFactory {

        public MovingComponent createMovingComponent(float originalSpeed, float acceleration, Actor actor) {
            return new DefaultMovingComponent(originalSpeed, acceleration, actor);
        }
    }
}
