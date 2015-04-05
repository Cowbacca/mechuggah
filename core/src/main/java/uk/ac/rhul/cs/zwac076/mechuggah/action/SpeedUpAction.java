package uk.ac.rhul.cs.zwac076.mechuggah.action;

import com.badlogic.gdx.scenes.scene2d.Action;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.Moving;

public class SpeedUpAction extends Action {
    private float speedIncrease;
    private float duration;
    private float time;

    public SpeedUpAction(float speedIncrease, float duration) {
        this.speedIncrease = speedIncrease;
        this.duration = duration;
        time = 0;
    }

    @Override
    public boolean act(float delta) {
        Moving player = (Moving) getActor();
        if (time == 0) {
            player.increaseSpeed(speedIncrease);
        }
        time += delta;
        if (time < duration) {
            return false;
        } else {
            player.increaseSpeed(-speedIncrease);
            return true;
        }
    }

}
