package uk.ac.rhul.cs.zwac076.mechuggah.action;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.MovingActor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SlideAction extends SequenceAction implements InputAction {

    private static final int SLIDE_WIND_UP_RECIPROCAL = 12;
    private static final float DEFAULT_SLIDE_DISTANCE = 250;
    private static final int DEFAULT_Y_SLIDE_MAGNITUDE = 0;
    private static final int DEFAULT_X_SLIDE_MAGNITUDE = 150;

    public SlideAction(MovingActor player, boolean isRight) {
        int xSlideMagnitude = DEFAULT_X_SLIDE_MAGNITUDE;
        float ySlideMagnitude = DEFAULT_Y_SLIDE_MAGNITUDE;
        float totalSlideTime = player.calculateTimeTakenToTravel(DEFAULT_SLIDE_DISTANCE);
        float slideDuration = totalSlideTime / SLIDE_WIND_UP_RECIPROCAL;
        float slideDelay = totalSlideTime - slideDuration * 2;
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
