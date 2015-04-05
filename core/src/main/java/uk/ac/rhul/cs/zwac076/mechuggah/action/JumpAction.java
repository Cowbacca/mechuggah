package uk.ac.rhul.cs.zwac076.mechuggah.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.ElevationMovingActor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class JumpAction extends SequenceAction implements InputAction {

    private static final float DEFAULT_JUMP_DISTANCE = 125 * 4 / 3f;

    public JumpAction(final ElevationMovingActor player) {
        float jumpDistance = DEFAULT_JUMP_DISTANCE;
        final float jumpDuration = player.calculateTimeTakenToTravel(jumpDistance);
        addAction(sequence(new Action() {

            @Override
            public boolean act(final float delta) {
                player.moveUp();
                return true;
            }
                           }, player.getMoveUpAction(jumpDuration / 4), delay(jumpDuration / 2), player.getMoveDownAction(jumpDuration / 4),
                new Action() {

                    @Override
                    public boolean act(final float delta) {
                        player.moveDown();
                        return true;
                    }
                }));
    }
}
