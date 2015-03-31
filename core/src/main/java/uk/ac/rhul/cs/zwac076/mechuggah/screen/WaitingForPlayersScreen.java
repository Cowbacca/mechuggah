package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LeaveMultiplayerRoomEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerReadyToStartEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.WebSocketConnectionChangedDuringMultiplayerEventListener;
import uk.ac.rhul.cs.zwac076.mechuggah.event.WebSocketConnectionChangedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.ui.LostConnectionDialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class WaitingForPlayersScreen extends UIScreen {

    private Dialog dialog;
    private WebSocketConnectionChangedDuringMultiplayerEventListener eventListener;

    public WaitingForPlayersScreen(final Skin skin, int width, int height, List<Actor> bubbles) {
        super(skin, width, height, bubbles);
        dialog = new LostConnectionDialog(skin);
        eventListener = new WebSocketConnectionChangedDuringMultiplayerEventListener(getStage(), dialog);

    }

    @Override
    protected void addUIComponentsToTable(Table table, Skin skin) {
        Label label = new Label("Waiting for players...", skin);
        TextButton readyButton = new TextButton("I am ready!", skin);
        TextButton backButton = new TextButton("Back", skin);
        readyButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new PlayerReadyToStartEvent());

            }

        });
        backButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new LeaveMultiplayerRoomEvent());

            }

        });
        table.add(label).colspan(2);
        table.row();
        table.add(backButton);
        table.add(readyButton);

    }

    @Override
    public void show() {
        EventManager.getInstance().registerListener(WebSocketConnectionChangedEvent.class, eventListener);
        super.show();
    }

    @Override
    public void hide() {
        EventManager.getInstance().unregisterListener(WebSocketConnectionChangedEvent.class, eventListener);
        super.hide();
    }
}
