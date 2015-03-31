package uk.ac.rhul.cs.zwac076.mechuggah.event;

import uk.ac.rhul.cs.zwac076.mechuggah.input.Input;

public class RemotePlayerInputEvent extends PlayerInputEvent {

    public RemotePlayerInputEvent(Input inputType) {
        super(inputType);
    }

}
