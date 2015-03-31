package uk.ac.rhul.cs.zwac076.mechuggah.input;

import lombok.AllArgsConstructor;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;

@AllArgsConstructor
public abstract class InputHandlingStrategy {

    protected Player player;

    public abstract void handleInput();

}
