package uk.ac.rhul.cs.zwac076.mechuggah.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * An event representing the Player moving.
 * 
 * @author Angus J. Goldsmith
 * 
 */
@AllArgsConstructor
public class PlayerMovedEvent extends Event {

    @Getter
    private final float x;
    @Getter
    private final float y;
    @Getter
    private final boolean local;

}
