package uk.ac.rhul.cs.zwac076.mechuggah.action;

import com.badlogic.gdx.scenes.scene2d.Action;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.component.MovingComponent;

public class StuckAction extends Action {

    @Override
    public boolean act(float delta) {
        ((MovingComponent) actor).freeze();
        return true;
    }

}
