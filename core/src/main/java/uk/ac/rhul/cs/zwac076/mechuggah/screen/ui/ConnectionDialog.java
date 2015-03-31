package uk.ac.rhul.cs.zwac076.mechuggah.screen.ui;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.StartGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.WebSocketConnectionChangedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.GameType;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.RetryConnectionEvent;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ConnectionDialog extends Dialog {
    private static final String ATTEMPTING_TO_RECONNECT_MESSAGE = "Retrying...";
    private static final String NOT_CONNECTED_MESSAGE = "Can't connect.";

    private final class WebSocketConnectionChangedEventListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            WebSocketConnectionChangedEvent webSocketConnectionChangedEvent = (WebSocketConnectionChangedEvent) event;
            if (busy) {
                if (webSocketConnectionChangedEvent.isServerConnected()) {
                    hide();
                    EventManager.getInstance().publishEvent(new StartGameEvent(GameType.MULTI_PLAYER));
                } else {
                    busy = false;
                    connectionLabel.setText(NOT_CONNECTED_MESSAGE);
                }
            }
            return false;
        }
    }

    private Label connectionLabel;
    private boolean busy;

    public ConnectionDialog(Skin skin) {
        super("", skin);
        connectionLabel = new Label("Can't connect.", skin);
        text(connectionLabel);
        button("Cancel", false);
        button("Retry", true);
        busy = false;
        EventManager.getInstance().registerListener(WebSocketConnectionChangedEvent.class,
                new WebSocketConnectionChangedEventListener());
    }

    protected void result(Object object) {
        if (!busy) {
            boolean retry = (Boolean) object;
            if (retry) {
                cancel();
                EventManager.getInstance().publishEvent(new RetryConnectionEvent());
                connectionLabel.setText(ATTEMPTING_TO_RECONNECT_MESSAGE);
                busy = true;

            }
        } else {
            cancel();
        }
    }
}