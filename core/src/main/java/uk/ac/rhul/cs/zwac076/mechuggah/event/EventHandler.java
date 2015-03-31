package uk.ac.rhul.cs.zwac076.mechuggah.event;

import com.badlogic.gdx.scenes.scene2d.Event;

public interface EventHandler<T extends Event> {

    public boolean handle(T event);
}
