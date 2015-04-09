package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;

/**
 * Created by angus on 4/5/15.
 */
public interface MovingComponent extends Component {
    float calculateTimeTakenToTravel(float distance);

    void increaseSpeed(float speedIncrease);

    void freeze();

    void unFreeze();
}
