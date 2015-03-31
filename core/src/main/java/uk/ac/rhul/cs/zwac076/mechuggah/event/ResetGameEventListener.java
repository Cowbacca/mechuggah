package uk.ac.rhul.cs.zwac076.mechuggah.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class ResetGameEventListener implements EventListener {

    private Actor actor;

    public ResetGameEventListener(Actor actor) {
        this.actor = actor;
    }

    @Override
    public boolean handle(Event event) {
        actor.remove();
        return false;
    }
}
