package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import lombok.EqualsAndHashCode;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.CollisionActor;

/**
 * Created by angus on 4/5/15.
 */
@EqualsAndHashCode
public class DefaultElevationComponent implements ElevationComponent {

    private final CollisionActor collisionActor;
    private final float upXScale;
    private final float upYScale;
    private float originalXScale;
    private float originalYScale;

    protected DefaultElevationComponent(float upXScale, float upYScale, CollisionActor collisionActor) {
        this.upXScale = upXScale;
        this.upYScale = upYScale;
        this.collisionActor
                = collisionActor;
        originalXScale = collisionActor.getScaleX();
        originalYScale = collisionActor.getScaleY();

    }


    @Override
    public void moveUp() {
        collisionActor.incrementZ();
    }

    @Override
    public void moveDown() {
        collisionActor.decrementZ();
    }

    @Override
    public Action getMoveUpAction(float duration) {
        originalXScale = collisionActor.getScaleX();
        originalYScale = collisionActor.getScaleY();
        return Actions.scaleTo(upXScale, upYScale, duration);
    }

    @Override
    public Action getMoveDownAction(float duration) {
        return Actions.scaleTo(originalXScale, originalYScale, duration);
    }

    @Override
    public void reset() {
        collisionActor.resetZ();
    }

    @Override
    public void act(float delta) {

    }
}
