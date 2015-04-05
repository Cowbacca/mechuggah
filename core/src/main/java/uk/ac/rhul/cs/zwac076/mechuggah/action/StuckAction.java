package uk.ac.rhul.cs.zwac076.mechuggah.action;

import com.badlogic.gdx.scenes.scene2d.Action;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.Moving;

public class StuckAction extends Action {

    private boolean hasFrozen;

    public StuckAction() {
        hasFrozen = false;
    }

    @Override
    public boolean act(float delta) {
        if (!hasFrozen) {
            ((Moving) actor).freeze();
            hasFrozen = true;
        }
        return hasFrozen;
    }

}
