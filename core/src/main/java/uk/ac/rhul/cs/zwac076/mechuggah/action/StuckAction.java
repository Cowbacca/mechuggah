package uk.ac.rhul.cs.zwac076.mechuggah.action;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;

import com.badlogic.gdx.scenes.scene2d.Action;

public class StuckAction extends Action {

    private boolean hasFrozen;

    @Override
    public boolean act(float delta) {
        if (!hasFrozen) {
            ((Player) actor).freeze();
            hasFrozen = true;
        }
        return hasFrozen;
    }

}
