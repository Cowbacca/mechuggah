package uk.ac.rhul.cs.zwac076.mechuggah.websocket;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import uk.ac.rhul.cs.zwac076.mechuggah.event.AllPlayersReadyEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.CollisionType;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.HighScoresRecievedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LeaveMultiplayerRoomEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LocalPlayerCollidedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.MultiPlayerGameShowStateChanged;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerReadyToStartEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.RemotePlayerCollidedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.RemotePlayerInputEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.RequestOnlineHighscoresEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.StartGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.WebSocketConnectionChangedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.highscore.HighScore;
import uk.ac.rhul.cs.zwac076.mechuggah.highscore.UserNameService;
import uk.ac.rhul.cs.zwac076.mechuggah.input.Input;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.GameType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class MechuggahWSClient extends WebSocketClient {
    private static final String REQUEST_HIGHSCORES_MESSAGE = "REQUESTNAME";
    private static final String COLLISION_MESSAGE = "COLLISION";
    private static final String SPLITTING_CHARACTER = ":";
    private static final String JOIN_ROOM_MESSAGE = "JOIN";
    private static final String READY_MESSAGE = "READY";
    private static final String LEAVE_ROOM_MESSAGE = "LEAVE";
    private static final String HIGHSCORE_MESSAGE = "HIGHSCORE";

    private final class PlayerReadyToStartEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            send(READY_MESSAGE);
            return false;
        }
    }

    private final class PlayerInputEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            PlayerInputEvent playerInputEvent = (PlayerInputEvent) event;
            send(playerInputEvent.getInputType().name());
            return false;
        }
    }

    private final class RequestOnlineHighscoresEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            send(REQUEST_HIGHSCORES_MESSAGE);
            return false;
        }
    }

    private final class EndOfGameEventListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            EndOfGameEvent endOfGameEvent = (EndOfGameEvent) event;
            String name = userNameService.getUserName();
            send(HIGHSCORE_MESSAGE + SPLITTING_CHARACTER + name + SPLITTING_CHARACTER + endOfGameEvent.getHighscore());
            return false;
        }
    }

    private final class LocalPlayerCollidedEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            LocalPlayerCollidedEvent localPlayerCollidedEvent = (LocalPlayerCollidedEvent) event;
            send(COLLISION_MESSAGE + SPLITTING_CHARACTER + localPlayerCollidedEvent.getCollisionType().name());
            return false;
        }
    }

    private final class StartGameEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            StartGameEvent startGameEvent = (StartGameEvent) event;
            if (startGameEvent.getGameType() == GameType.MULTI_PLAYER) {
                send(JOIN_ROOM_MESSAGE);
            }
            return false;
        }
    }

    private final class LeaveMultiplayerRoomEventListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            send(LEAVE_ROOM_MESSAGE);
            return false;
        }
    }

    private boolean idSet;
    @Getter
    private int id;
    private Map<Class<? extends Event>, EventListener> openListenersMap;
    private Map<Class<? extends Event>, EventListener> multiPlayerListenersMap;
    private UserNameService userNameService;

    public MechuggahWSClient(URI serverURI, UserNameService userNameService) {
        super(serverURI);
        this.userNameService = userNameService;
        idSet = false;

        setupOpenListenersMap();

        setUpMultiPlayerListenersMap();
    }

    private void setUpMultiPlayerListenersMap() {
        multiPlayerListenersMap = new HashMap<Class<? extends Event>, EventListener>();
        multiPlayerListenersMap.put(PlayerInputEvent.class, new PlayerInputEventListener());
        multiPlayerListenersMap.put(LocalPlayerCollidedEvent.class, new LocalPlayerCollidedEventListener());
    }

    private void setupOpenListenersMap() {
        openListenersMap = new HashMap<Class<? extends Event>, EventListener>();
        openListenersMap.put(LeaveMultiplayerRoomEvent.class, new LeaveMultiplayerRoomEventListener());
        openListenersMap.put(StartGameEvent.class, new StartGameEventListener());
        openListenersMap.put(EndOfGameEvent.class, new EndOfGameEventListener());
        openListenersMap.put(RequestOnlineHighscoresEvent.class, new RequestOnlineHighscoresEventListener());
        openListenersMap.put(PlayerReadyToStartEvent.class, new PlayerReadyToStartEventListener());
        openListenersMap.put(MultiPlayerGameShowStateChanged.class, new EventListener() {

            @Override
            public boolean handle(Event event) {
                MultiPlayerGameShowStateChanged multiPlayerGameShowStateChanged = (MultiPlayerGameShowStateChanged) event;
                if (multiPlayerGameShowStateChanged.isShown()) {
                    registerMultiPlayerListeners();
                } else {
                    unregisterMultiplayerListeners();
                }
                return false;
            }
        });
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        registerAllOpenListeners();
        EventManager.getInstance().publishEvent(new WebSocketConnectionChangedEvent(true));

    }

    private void registerAllOpenListeners() {
        registerListeners(openListenersMap);

    }

    private void registerListeners(Map<Class<? extends Event>, EventListener> map) {
        for (Class<? extends Event> eventClass : map.keySet()) {
            EventManager.getInstance().registerListener(eventClass, map.get(eventClass));
        }
    }

    private void unregisterListeners(Map<Class<? extends Event>, EventListener> map) {
        for (Class<? extends Event> eventClass : map.keySet()) {
            EventManager.getInstance().unregisterListener(eventClass, map.get(eventClass));
        }
    }

    private void unregisterAllOpenListeners() {
        for (Class<? extends Event> eventClass : openListenersMap.keySet()) {
            EventManager.getInstance().unregisterListener(eventClass, openListenersMap.get(eventClass));
        }

    }

    @Override
    public void onMessage(String message) {
        if (!idSet) {
            setId(message);
        } else if (message.equals(READY_MESSAGE)) {
            handleReadyMessage();
        } else if (message.startsWith(HIGHSCORE_MESSAGE)) {
            handleHighScoreMessage(message);

        } else {
            try {
                handleInputMessage(message);
            } catch (Exception e) {
                handleCollisionMessage(message);
            }
        }

    }

    private void handleCollisionMessage(String message) {
        final String[] messageParts = message.split(SPLITTING_CHARACTER);
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                EventManager.getInstance().publishEvent(
                        new RemotePlayerCollidedEvent(CollisionType.valueOf(messageParts[1])));
            }
        });
    }

    private void handleInputMessage(String message) {
        Input opponentInput = Input.valueOf(message);
        EventManager.getInstance().publishEvent(new RemotePlayerInputEvent(opponentInput));
    }

    private void handleHighScoreMessage(String message) {
        final String[] messageParts = message.split(SPLITTING_CHARACTER);
        final List<HighScore> remoteHighscores = new ArrayList<HighScore>();
        for (int i = 1; i < messageParts.length; i += 2) {
            remoteHighscores.add(new HighScore(messageParts[i + 1], messageParts[i]));
        }
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                EventManager.getInstance().publishEvent(new HighScoresRecievedEvent(remoteHighscores));
            }
        });
    }

    private void handleReadyMessage() {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                EventManager.getInstance().publishEvent(new AllPlayersReadyEvent());

            }
        });
    }

    private void setId(String message) {
        id = Integer.parseInt(message);
        idSet = true;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        EventManager.getInstance().publishEvent(new WebSocketConnectionChangedEvent(false));
        unregisterAllOpenListeners();

    }

    @Override
    public void onError(Exception ex) {

    }

    private void registerMultiPlayerListeners() {
        registerListeners(multiPlayerListenersMap);

    }

    private void unregisterMultiplayerListeners() {
        unregisterListeners(multiPlayerListenersMap);

    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        try {
            super.send(text);
        } catch (WebsocketNotConnectedException e) {

        }
    }

    @Override
    public void send(byte[] data) throws NotYetConnectedException {
        try {
            super.send(data);
        } catch (WebsocketNotConnectedException e) {

        }
    }
}
