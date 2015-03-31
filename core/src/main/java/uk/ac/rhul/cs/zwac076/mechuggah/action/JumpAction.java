package uk.ac.rhul.cs.zwac076.mechuggah.action;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class JumpAction extends SequenceAction implements InputAction {

    private static final float DEFAULT_JUMP_DISTANCE = 125 * 4 / 3f;
    private static final float DEFAULT_JUMP_SCALE = 1.2f;

    private float jumpDistance;
    private float jumpScale;

    public JumpAction(final Player player) {
        jumpDistance = DEFAULT_JUMP_DISTANCE;
        jumpScale = DEFAULT_JUMP_SCALE;
        final float jumpDuration = jumpDistance / player.getSpeed();
        float originalScaleX = player.getScaleX();
        float originalScaleY = player.getScaleY();
        addAction(sequence(new Action() {

            @Override
            public boolean act(final float delta) {
                player.moveUp();
                return true;
            }
        }, scaleTo(jumpScale, jumpScale, jumpDuration / 4), delay(jumpDuration / 2),
                scaleTo(originalScaleX, originalScaleY, jumpDuration / 4), new Action() {

                    @Override
                    public boolean act(final float delta) {
                        player.moveDown();
                        return true;
                    }
                }));
    }
}
