package uk.ac.rhul.cs.zwac076.mechuggah.screen.ui;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LeaveMultiplayerRoomEvent;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LostConnectionDialog extends Dialog {

    public LostConnectionDialog(Skin skin) {
        super("", skin);
        text(new Label("Connection lost.", skin));
        button("Back", true);
    }

    @Override
    protected void result(Object object) {
        EventManager.getInstance().publishEvent(new LeaveMultiplayerRoomEvent());
    }
}