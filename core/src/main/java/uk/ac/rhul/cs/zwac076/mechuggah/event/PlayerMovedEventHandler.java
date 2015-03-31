package uk.ac.rhul.cs.zwac076.mechuggah.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class PlayerMovedEventHandler implements EventListener {

    protected Actor actor;
    private boolean alive;

    public PlayerMovedEventHandler(Actor actor) {
        this.actor = actor;
        alive = true;
    }

    @Override
    public boolean handle(Event event) {
        final PlayerMovedEvent playerMovedEvent = (PlayerMovedEvent) event;
        Stage stage = actor.getStage();
        if (stage != null && alive && playerMovedEvent.getY() > actor.getY() + stage.getHeight() / 2) {
            handleActorMovingOffScreen();
        }
        return false;
    }

    protected void handleActorMovingOffScreen() {
        alive = false;

    }

    public void init() {
        alive = true;
    }

}
