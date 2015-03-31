package uk.ac.rhul.cs.zwac076.mechuggah.input;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEvent;
//Based on example here:
//http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it

public abstract class Shaker {

    private float accelerationLastRead;
    private float accelerationCurrentRead;
    private float lowPassFilteredAcceleration;

    public Shaker(float startingAcceleration) {
        accelerationLastRead = startingAcceleration;
        accelerationCurrentRead = startingAcceleration;
    }

    public abstract void calculateAcceleration(float delta);

    protected float calculateLowPassFilteredAcceleration(float deltaTime, float... accelerationComponents) {

        double accelerationSquared = 0;
        for (float accelerationComponent : accelerationComponents) {
            accelerationSquared += accelerationComponent * accelerationComponent;
        }
        accelerationLastRead = accelerationCurrentRead;
        accelerationCurrentRead = (float) Math.sqrt(accelerationSquared);
        float delta = accelerationCurrentRead - accelerationLastRead;
        lowPassFilteredAcceleration = lowPassFilteredAcceleration * 0.9f + delta;
        return lowPassFilteredAcceleration;
    }

    protected float getAccelerationLastRead() {
        return accelerationLastRead;
    }

    protected void onShake() {
        EventManager.getInstance().publishEvent(new PlayerInputEvent(Input.SHAKE));
    }

}
