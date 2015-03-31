package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.StartGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ViewControlsEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ViewHighScoreEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.WebSocketConnectionChangedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.ui.ConnectionDialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen extends UIScreen {

    private static final int GAME_TITLE_BOTTOM_PADDING = 200;
    private static final String MULTI_PLAYER_BUTTON_TEXT = "Multi Player";
    private static final String SINGLE_PLAYER_BUTTON_TEXT = "Single Player";
    private static final String HIGH_SCORE_BUTTON_TEXT = "High Score";
    private static final String GAME_TITLE = "JELL E. FISH";

    private final class WebSocketConnectionChangedEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            WebSocketConnectionChangedEvent webSocketConnectionChangedEvent = (WebSocketConnectionChangedEvent) event;
            serverIsConnected = webSocketConnectionChangedEvent.isServerConnected();
            return false;
        }
    }

    private boolean serverIsConnected;
    private TextButton highScoreButton;
    private TextButton controlsButton;
    private Dialog dialog;

    public MenuScreen(Skin skin, int width, int height, List<Actor> bubbles) {
        super(skin, width, height, bubbles);
        EventManager.getInstance().registerListener(WebSocketConnectionChangedEvent.class,
                new WebSocketConnectionChangedEventListener());
        serverIsConnected = false;
        highScoreButton = new TextButton(HIGH_SCORE_BUTTON_TEXT, skin);
        highScoreButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new ViewHighScoreEvent());

            }
        });
        controlsButton = new TextButton("Controls", skin);
        controlsButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new ViewControlsEvent());

            }
        });
        dialog = new ConnectionDialog(skin);
    }

    @Override
    protected void addUIComponentsToTable(Table table, Skin skin) {
        Label titleLabel = new Label(GAME_TITLE, skin);
        table.add(titleLabel).padBottom(GAME_TITLE_BOTTOM_PADDING).row();
        addGameTypeButton(table, skin, SINGLE_PLAYER_BUTTON_TEXT, GameType.SINGLE_PLAYER);
        addGameTypeButton(table, skin, MULTI_PLAYER_BUTTON_TEXT, GameType.MULTI_PLAYER);
        table.add(highScoreButton).padBottom(20);
        table.row();
        table.add(controlsButton);

    }

    private TextButton addGameTypeButton(final Table table, final Skin skin, final String gameName,
            final GameType gameType) {
        TextButton gameTypeButton = new TextButton(gameName, skin);
        gameTypeButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameType == GameType.MULTI_PLAYER && !serverIsConnected) {
                    showDialog(dialog);
                } else {
                    EventManager.getInstance().publishEvent(new StartGameEvent(gameType));
                }

            }
        });
        table.add(gameTypeButton).padBottom(20).row();
        return gameTypeButton;
    }

}
