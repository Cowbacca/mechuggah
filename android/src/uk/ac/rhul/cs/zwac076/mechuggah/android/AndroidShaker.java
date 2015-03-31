package uk.ac.rhul.cs.zwac076.mechuggah.android;

import uk.ac.rhul.cs.zwac076.mechuggah.input.Shaker;

import com.badlogic.gdx.Gdx;

//Based on example here:
// http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it

public class AndroidShaker extends Shaker {
    private static final float EARTHS_GRAVITY = 9.80665f;

    public AndroidShaker() {
        super(EARTHS_GRAVITY);
    }

    @Override
    public void calculateAcceleration(float deltaTime) {
        float x = Gdx.input.getAccelerometerX();
        float y = Gdx.input.getAccelerometerY();
        float z = Gdx.input.getAccelerometerY();
        float lowPassFilteredAcceleration = calculateLowPassFilteredAcceleration(deltaTime, x, y, z);

        if (lowPassFilteredAcceleration > 2) {
            onShake();
        }

    }
}
