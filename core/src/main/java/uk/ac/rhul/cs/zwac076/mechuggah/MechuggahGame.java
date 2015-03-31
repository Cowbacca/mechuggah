package uk.ac.rhul.cs.zwac076.mechuggah;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.java_websocket.WebSocket.READYSTATE;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.ActorFactory;
import uk.ac.rhul.cs.zwac076.mechuggah.event.AllPlayersReadyEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.BackToMenuEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfMultiPlayerRoundEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LeaveMultiplayerRoomEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.RequestOnlineHighscoresEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.StartGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.StartGameHandler;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ViewControlsEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ViewHighScoreEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.WaitForGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.WebSocketConnectionChangedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.highscore.UserNameService;
import uk.ac.rhul.cs.zwac076.mechuggah.input.Shaker;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.ControlsScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.EndOfGameScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.EndOfMultiPlayerRoundScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.HighScoreScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.MenuScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.MultiPlayerGameScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.RetryConnectionEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.SinglePlayerGameScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.SplashScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.WaitingForPlayersScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.websocket.MechuggahWSClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MechuggahGame extends Game implements EventListener {

    private final class RetryConnectionEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            attemptConnectionToServer();
            return false;
        }
    }

    private final class WebSocketConnectionChangedEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            WebSocketConnectionChangedEvent webSocketConnectionChangedEvent = (WebSocketConnectionChangedEvent) event;
            if (webSocketConnectionChangedEvent.isServerConnected()) {
                EventManager.getInstance().publishEvent(new RequestOnlineHighscoresEvent());
            }
            return false;
        }
    }

    private final class ViewControlsEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            setScreen(controlsScreen);
            return false;
        }
    }

    private final class ViewHighScoreEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            EventManager.getInstance().publishEvent(new RequestOnlineHighscoresEvent());
            setScreen(highScoreScreen);
            return true;
        }
    }

    private final class EndOfMultiPlayerRoundEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            EndOfMultiPlayerRoundEvent endOfMultiPlayerRoundEvent = (EndOfMultiPlayerRoundEvent) event;
            setScreen(endOfMultiPlayerRoundScreen);
            endOfMultiPlayerRoundScreen.setWinner(endOfMultiPlayerRoundEvent.isWinner());
            return false;
        }
    }

    private final class BackToMenuEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            setScreen(menuScreen);
            return false;
        }
    }

    private final class EndOfGameEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            final EndOfGameEvent endOfGameEvent = (EndOfGameEvent) event;
            endOfGameScreen.setHighscore(endOfGameEvent.getHighscore());
            setScreen(endOfGameScreen);
            return false;
        }
    }

    private final class AllPlayersReadyEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            multiPlayerScreen.setupGame();
            setScreen(multiPlayerScreen);
            return true;
        }
    }

    private static final int WORLD_WIDTH = 720;
    private static final int WORLD_HEIGHT = 1280;
    private static final String SERVER_URI = "ws://104.155.15.132/:80";
    private static final int NUM_BUBBLES = 7;

    private Skin skin;
    private SinglePlayerGameScreen singlePlayerScreen;
    private MultiPlayerGameScreen multiPlayerScreen;
    private EndOfGameScreen endOfGameScreen;
    private MenuScreen menuScreen;
    private WaitingForPlayersScreen waitingForPlayersScreen;
    private EndOfMultiPlayerRoundScreen endOfMultiPlayerRoundScreen;
    private MechuggahWSClient websocketClient;
    private Shaker shaker;
    private SplashScreen splashScreen;
    private HighScoreScreen highScoreScreen;
    private ControlsScreen controlsScreen;
    private UserNameService userNameService;

    public MechuggahGame(Shaker shaker, UserNameService userNameService) {
        this.shaker = shaker;
        this.userNameService = userNameService;
        new HashMap<Class<? extends Event>, EventListener>();

    }

    /**
     * Called when the game is created. Sets up everything necessary for the
     * game to run.
     */
    @Override
    public void create() {
        float pixelDensity = calculateDensity();

        loadSkin(pixelDensity);

        initialiseScreens();
        setScreen(splashScreen);
        registerListeners();
        attemptConnectionToServer();
    }

    private void setupWebSocketClient() {
        URI uri;
        try {
            uri = new URI(SERVER_URI);
        } catch (URISyntaxException e) {
            Gdx.app.log("Websocket", e.getMessage());
            return;
        }
        websocketClient = new MechuggahWSClient(uri, userNameService);
    }

    private void registerListeners() {
        BackToMenuEventListener backToMenuEventListener = new BackToMenuEventListener();
        registerListener(StartGameEvent.class, new StartGameHandler(this, singlePlayerScreen, waitingForPlayersScreen));
        registerListener(AllPlayersReadyEvent.class, new AllPlayersReadyEventListener());
        registerListener(EndOfGameEvent.class, new EndOfGameEventListener());
        registerListener(BackToMenuEvent.class, backToMenuEventListener);
        registerListener(LeaveMultiplayerRoomEvent.class, backToMenuEventListener);
        registerListener(WaitForGameEvent.class, this);
        registerListener(EndOfMultiPlayerRoundEvent.class, new EndOfMultiPlayerRoundEventListener());
        registerListener(ViewHighScoreEvent.class, new ViewHighScoreEventListener());
        registerListener(ViewControlsEvent.class, new ViewControlsEventListener());
        registerListener(WebSocketConnectionChangedEvent.class, new WebSocketConnectionChangedEventListener());
        registerListener(RetryConnectionEvent.class, new RetryConnectionEventListener());
    }

    private void registerListener(Class<? extends Event> eventClass, EventListener eventListener) {
        EventManager.getInstance().registerListener(eventClass, eventListener);

    }

    private void initialiseScreens() {
        ActorFactory actorFactory = new ActorFactory();
        List<Actor> bubbles = initaliseBubbles(WORLD_WIDTH, WORLD_HEIGHT, actorFactory, NUM_BUBBLES);
        splashScreen = new SplashScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, bubbles);
        highScoreScreen = new HighScoreScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, bubbles, userNameService);
        singlePlayerScreen = new SinglePlayerGameScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, shaker);
        endOfGameScreen = new EndOfGameScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, bubbles);
        menuScreen = new MenuScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, bubbles);
        multiPlayerScreen = new MultiPlayerGameScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, shaker);
        waitingForPlayersScreen = new WaitingForPlayersScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, bubbles);
        endOfMultiPlayerRoundScreen = new EndOfMultiPlayerRoundScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, bubbles);
        controlsScreen = new ControlsScreen(skin, WORLD_WIDTH, WORLD_HEIGHT, bubbles);

    }

    private List<Actor> initaliseBubbles(int width, int height, ActorFactory actorFactory, int numBubbles) {
        List<Actor> bubbles = new ArrayList<Actor>();
        for (int i = 0; i < numBubbles; i++) {
            bubbles.add(actorFactory.createBubbleActor(width / numBubbles * i, -height / numBubbles * i, height));
        }
        return bubbles;
    }

    private void attemptConnectionToServer() {
        if (websocketClient == null || websocketClient.getReadyState() != READYSTATE.OPEN) {
            setupWebSocketClient();
            websocketClient.connect();
        }
    }

    // Based on example code from
    // https://github.com/libgdx/libgdx/wiki/Gdx-freetype
    private void loadSkin(float density) {
        skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-CondLight.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        addFontToSkin("default-font", 32, density, generator, parameter);
        addFontToSkin("small-font", 20, density, generator, parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        FileHandle fileHandle = Gdx.files.internal("uiskin.json");
        FileHandle atlasFile = fileHandle.sibling("uiskin.atlas");
        if (atlasFile.exists()) {
            skin.addRegions(new TextureAtlas(atlasFile));
        }
        skin.load(fileHandle);
    }

    private void addFontToSkin(String fontName, int size, float density, FreeTypeFontGenerator generator,
            FreeTypeFontParameter parameter) {
        parameter.size = (int) (size * density);
        BitmapFont font = generator.generateFont(parameter);
        addTextureFiltersToFont(font);
        skin.add(fontName, font, BitmapFont.class);
    }

    private void addTextureFiltersToFont(BitmapFont font) {
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    private float calculateDensity() {
        float density = Gdx.graphics.getDensity();
        if (Gdx.graphics.getDensity() < 1) {
            density = 1 / Gdx.graphics.getDensity();
        }
        return density;
    }

    @Override
    public boolean handle(final Event event) {
        setScreen(waitingForPlayersScreen);
        return false;
    }

    /**
     * Main loop of the game.
     */
    @Override
    public void render() {
        final float deltaTime = Gdx.graphics.getDeltaTime();
        getScreen().render(deltaTime);
    }

    @Override
    public void dispose() {
        if (websocketClient != null && websocketClient.getReadyState() == READYSTATE.OPEN) {
            websocketClient.close();
        }
        EventManager.getInstance().dispose();
        super.dispose();
    }

    @Override
    public void resume() {
        attemptConnectionToServer();
        super.resume();
    }

}
