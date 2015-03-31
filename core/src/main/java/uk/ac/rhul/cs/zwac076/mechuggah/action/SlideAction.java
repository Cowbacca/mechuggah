package uk.ac.rhul.cs.zwac076.mechuggah.action;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class SlideAction extends SequenceAction implements InputAction {

    private static final int SLIDE_WIND_UP_RECIPROCAL = 12;
    private static final float DEFAULT_SLIDE_DISTANCE = 250;
    private static final int DEFAULT_Y_SLIDE_MAGNITUDE = 0;
    private static final int DEFAULT_X_SLIDE_MAGNITUDE = 150;

    private float slideDuration;
    private float ySlideMagnitude;
    private int xSlideMagnitude;
    private float slideDelay;

    public SlideAction(Player player, boolean isRight) {
        xSlideMagnitude = DEFAULT_X_SLIDE_MAGNITUDE;
        ySlideMagnitude = DEFAULT_Y_SLIDE_MAGNITUDE;
        float totalSlideTime = DEFAULT_SLIDE_DISTANCE / player.getSpeed();
        slideDuration = totalSlideTime / SLIDE_WIND_UP_RECIPROCAL;
        slideDelay = totalSlideTime - slideDuration * 2;
        int signModifier;
        if (isRight) {
            signModifier = 1;
        } else {
            signModifier = -1;
        }
        addAction(sequence(moveBy(signModifier * xSlideMagnitude, ySlideMagnitude, slideDuration), delay(slideDelay),
                moveBy(signModifier * -xSlideMagnitude, ySlideMagnitude, slideDuration)));
    }

}
