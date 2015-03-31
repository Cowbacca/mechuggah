package uk.ac.rhul.cs.zwac076.mechuggah.desktop;

import uk.ac.rhul.cs.zwac076.mechuggah.input.Shaker;

import com.badlogic.gdx.Gdx;

// Based loosely on example here:
// http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it

public class DesktopShaker extends Shaker {
    private static final int ACCEL_NEEDED_FOR_SHAKE = 2000;

    public DesktopShaker() {
        super(0f);
    }

    @Override
    public void calculateAcceleration(float deltaTime) {
        float x = Gdx.input.getDeltaX() / deltaTime;
        float y = Gdx.input.getDeltaY() / deltaTime;
        float lowPassFilteredAcceleration = calculateLowPassFilteredAcceleration(deltaTime, x, y);
        float accelerationLastRead = getAccelerationLastRead();

        boolean isRightLeftShake = lowPassFilteredAcceleration > ACCEL_NEEDED_FOR_SHAKE
                && accelerationLastRead < -ACCEL_NEEDED_FOR_SHAKE;
        boolean isLeftRightShake = lowPassFilteredAcceleration < -ACCEL_NEEDED_FOR_SHAKE
                && accelerationLastRead > ACCEL_NEEDED_FOR_SHAKE;
        if (isRightLeftShake || isLeftRightShake) {
            onShake();
        }

    }

}
