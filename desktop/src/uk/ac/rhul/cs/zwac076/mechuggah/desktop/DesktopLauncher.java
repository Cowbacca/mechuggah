package uk.ac.rhul.cs.zwac076.mechuggah.desktop;

import uk.ac.rhul.cs.zwac076.mechuggah.MechuggahGame;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    private static final String UNKNOWN_USER_NAME = "???";

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 585;
        config.height = 1040;
        config.title = "Jell E. Fish";
        config.addIcon("large_icon.png", FileType.Internal);
        config.addIcon("medium_icon.png", FileType.Internal);
        config.addIcon("small_icon.png", FileType.Internal);

        DesktopShaker shaker = new DesktopShaker();
        DesktopUserNameService userNameService = new DesktopUserNameService(UNKNOWN_USER_NAME);
        new LwjglApplication(new MechuggahGame(shaker, userNameService),
                config);
    }
}
