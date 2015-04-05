package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by angus on 4/5/15.
 */
public interface ElevationComponent extends Component {

    void moveUp();

    void moveDown();

    Action getMoveUpAction(float duration);

    Action getMoveDownAction(float duration);
}
