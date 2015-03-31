package uk.ac.rhul.cs.zwac076.mechuggah.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.badlogic.gdx.scenes.scene2d.Event;

@AllArgsConstructor
public class RemotePlayerCollidedEvent extends Event {
    @Getter
    private CollisionType collisionType;
}
