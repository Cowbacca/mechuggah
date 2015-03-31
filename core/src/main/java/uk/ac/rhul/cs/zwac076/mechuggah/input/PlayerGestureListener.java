package uk.ac.rhul.cs.zwac076.mechuggah.input;

import lombok.AllArgsConstructor;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEvent;

import com.badlogic.gdx.input.GestureDetector.GestureAdapter;

/**
 * Input listener for the Player. Responsible for handling user input and
 * performing some actions.
 * 
 * @author Angus J. Goldsmith
 * 
 */
@AllArgsConstructor
public class PlayerGestureListener extends GestureAdapter {

    /**
     * Handles a fling event by causing the player to slide in the horizontal
     * direction of the fling.
     */
    @Override
    public boolean fling(final float velocityX, final float velocityY, final int button) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                EventManager.getInstance().publishEvent(new PlayerInputEvent(Input.SLIDE_RIGHT));
            } else if (velocityX < 0) {
                EventManager.getInstance().publishEvent(new PlayerInputEvent(Input.SLIDE_LEFT));
            }
        }
        return true;
    }

    /**
     * Deals with a tap event by causing the player to jump.
     */
    @Override
    public boolean tap(final float x, final float y, final int count, final int button) {
        EventManager.getInstance().publishEvent(new PlayerInputEvent(Input.JUMP));

        return true;
    }

}
