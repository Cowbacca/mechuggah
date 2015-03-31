package uk.ac.rhul.cs.zwac076.mechuggah.android;

import uk.ac.rhul.cs.zwac076.mechuggah.MechuggahGame;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    private static final String UNKNOWN_USER_NAME = "???";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        AndroidShaker shaker = new AndroidShaker();
        AndroidUserNameService userNameService = new AndroidUserNameService(UNKNOWN_USER_NAME, getContext());
        initialize(new MechuggahGame(shaker, userNameService),
                config);
    }
}
