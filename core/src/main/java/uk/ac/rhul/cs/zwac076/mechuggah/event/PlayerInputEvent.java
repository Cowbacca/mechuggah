package uk.ac.rhul.cs.zwac076.mechuggah.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.ac.rhul.cs.zwac076.mechuggah.input.Input;

import com.badlogic.gdx.scenes.scene2d.Event;

@AllArgsConstructor
public class PlayerInputEvent extends Event {
    @Getter
    private Input inputType;
}
