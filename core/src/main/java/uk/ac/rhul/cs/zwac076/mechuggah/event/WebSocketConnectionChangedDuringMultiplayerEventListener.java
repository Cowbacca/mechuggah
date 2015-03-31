package uk.ac.rhul.cs.zwac076.mechuggah.event;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

public class WebSocketConnectionChangedDuringMultiplayerEventListener implements EventListener {

    private Dialog dialog;
    private Stage stage;

    public WebSocketConnectionChangedDuringMultiplayerEventListener(Stage stage, Dialog dialog) {
        this.stage = stage;
        this.dialog = dialog;
    }

    @Override
    public boolean handle(Event event) {
        WebSocketConnectionChangedEvent webSocketConnectionChangedEvent = (WebSocketConnectionChangedEvent) event;
        if (!webSocketConnectionChangedEvent.isServerConnected()) {
            dialog.show(stage);
        } else {
            dialog.hide();
        }
        return false;
    }
}