package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.ActorFactory;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.BottomOfScreenActor;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Collidable;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.CollisionDetectingStage;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.EnemyActor;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.LaneBuilder;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.LaneGroup;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.PowerUp;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.PufferActor;
import uk.ac.rhul.cs.zwac076.mechuggah.event.CollisionEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LaneGroupDeletedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.input.PlayerGestureListener;
import uk.ac.rhul.cs.zwac076.mechuggah.input.Shaker;
import uk.ac.rhul.cs.zwac076.mechuggah.sound.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SinglePlayerGameScreen implements Screen, EventListener {

    private static final int SEED = 666;

    private static final String TIME_PASSED_STRING_FORMAT = "%.2f";

    private static final double MOVING_TURTLE_PROBABILITY = 0.75;

    protected CollisionDetectingStage stage;
    protected final ActorFactory actorFactory;
    protected final Skin skin;
    protected Table table;

    private int laneGroupY;
    protected float timePassed;
    protected final int worldWidth;
    private final int worldHeight;
    protected Random random;
    private boolean isAlreadySetup;
    private InputMultiplexer inputMultiplexer;
    private SoundManager soundManager;
    protected boolean paused;
    private Shaker shaker;

    private Collidable bottomOfScreenActor;

    public SinglePlayerGameScreen(final Skin skin, final int worldWidth, final int worldHeight, final Shaker shaker) {

        random = new Random(SEED);

        actorFactory = new ActorFactory();
        this.skin = skin;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.shaker = shaker;
        soundManager = new SoundManager();
        stage = new CollisionDetectingStage(new FitViewport(worldWidth, worldHeight));
        table = new Table();
        isAlreadySetup = false;
        bottomOfScreenActor = (Collidable) actorFactory.createBottomOfScreenActor(worldWidth, worldHeight);
    }

    /**
     * Creates a lane group at a given y value.
     * 
     * @param y
     *            the y value to create the lane group at.
     */
    private void createLaneGroup(final int y) {
        laneGroupY = y;

        onLaneGroupCreation(y);
        if (y == 0 || random.nextDouble() <= MOVING_TURTLE_PROBABILITY) {
            new LaneBuilder(stage, actorFactory, y).numberOfEnemies((int) (2 + random.nextDouble() * 3))
                    .distanceBetweenEnemies((int) (220 - random.nextDouble() * 200))
                    .speed((float) (50 + (random.nextDouble() * 200))).startingDelay((float) random.nextDouble() * 1)
                    .build();
        } else {
            Actor actor = actorFactory.createPufferActor(worldWidth / 2, y);
            stage.addCollidableActor((Collidable) actor);
            actor.setZIndex(1);
        }

    }

    protected void onLaneGroupCreation(final int y) {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    /**
     * Handles events.
     */
    // TODO: refactor so there is one handler for each event type
    public boolean handle(final Event event) {
        if (event.getClass() == LaneGroupDeletedEvent.class) {
            laneGroupY += 450;
            createLaneGroup(laneGroupY);
            return true;
        } else if (event.getClass() == PlayerMovedEvent.class) {
            final PlayerMovedEvent playerMovedEvent = (PlayerMovedEvent) event;
            final Camera camera = stage.getCamera();
            if (playerMovedEvent.getY() > camera.position.y) {
                camera.position.set(camera.position.x, playerMovedEvent.getY(), camera.position.z);
                table.setY(playerMovedEvent.getY() - 100);
                camera.update();
            }
            return false;
        } else {
            final CollisionEvent collisionEvent = (CollisionEvent) event;
            List<Collidable> participants = collisionEvent.getParticipants();
            for (final Collidable collidable : participants) {
                if (Player.class.isInstance(collidable)) {
                    ArrayList<Collidable> tempParticipants = new ArrayList<Collidable>(participants);
                    tempParticipants.remove(collidable);
                    onPlayerHit((Player) collidable, tempParticipants.get(0));
                    return true;
                }
            }
            return false;
        }

    }

    protected void onPlayerHit(Player player, Collidable otherParticipant) {

        if (EnemyActor.class.isInstance(otherParticipant) || LaneGroup.class.isInstance(otherParticipant)
                || PufferActor.class.isInstance(otherParticipant)
                || BottomOfScreenActor.class.isInstance(otherParticipant)) {
            playerHitEnemy();
        } else if (PowerUp.class.isInstance(otherParticipant)) {
            PowerUp powerUp = (PowerUp) otherParticipant;
            if (!powerUp.hasBeenActivatedBy(player)) {
                playerHitPowerUp(player, powerUp);
            }
        }
    }

    protected void playerHitPowerUp(Player player, PowerUp powerUp) {
        powerUp.activatedByPlayer(player);
        player.addPowerUpAction(powerUp.getAction());
    }

    protected void playerHitEnemy() {
        EventManager.getInstance().publishEvent(new EndOfGameEvent(timePassed));
    }

    @Override
    public void hide() {
        final EventManager eventManager = EventManager.getInstance();
        eventManager.unregisterListener(CollisionEvent.class, this);
        eventManager.unregisterListener(PlayerMovedEvent.class, this);
        eventManager.unregisterListener(LaneGroupDeletedEvent.class, this);
        soundManager.unregisterListeners();

    }

    @Override
    public void pause() {

    }

    /**
     * Main loop of the game.
     */
    @Override
    public void render(final float deltaTime) {
        if (!paused) {
            shaker.calculateAcceleration(deltaTime);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act(deltaTime);
            stage.draw();
        }
    }

    /**
     * Called when the screen is resized. Updates the game stage to be in line
     * with screen size.
     */
    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    /**
     * Sets up the UI for the game.
     */
    private void setupUI() {

        addUIComponentsToTable();
        stage.addActor(table);
    }

    protected void addUIComponentsToTable() {
        final Label timeLabel = new Label("", skin) {

            @Override
            public void act(final float delta) {
                super.act(delta);
                timePassed += delta;
                setText(String.format(TIME_PASSED_STRING_FORMAT, timePassed));
            }

        };
        table.add(timeLabel).expandX();
    }

    @Override
    public void show() {
        final EventManager eventManager = EventManager.getInstance();
        eventManager.registerListener(CollisionEvent.class, this);
        eventManager.registerListener(PlayerMovedEvent.class, this);
        eventManager.registerListener(LaneGroupDeletedEvent.class, this);
        soundManager.registerListeners();

    }

    /**
     * Sets up the actors for the game. Called every time the game is restarted.
     */
    public void setupGame() {
        if (isAlreadySetup) {
            resetGame();
        } else {
            timePassed = 0;
            final Actor background = actorFactory.createBackground(worldWidth, worldHeight);

            stage = new CollisionDetectingStage(new FitViewport(worldWidth, worldHeight));
            table = new Table();
            table.setFillParent(true);
            stage.addActor(background);
            background.toBack();
            stage.addCollidableActor(bottomOfScreenActor);

            for (int i = 0; i < worldHeight * 2; i += 300) {
                createLaneGroup(i);
            }

            setupPlayers();
            Camera camera = stage.getCamera();
            camera.position.set(camera.position.x, 0, camera.position.z);
            setupUI();
            isAlreadySetup = true;
            soundManager.playBackgroundMusic();
        }

    }

    protected void setupPlayers() {
        Player player = (Player) actorFactory.createPlayerActor(worldWidth, stage);
        setupControls(player);
    }

    protected void setupControls(Player player) {
        final PlayerGestureListener playerListener = new PlayerGestureListener();
        inputMultiplexer = new InputMultiplexer(new GestureDetector(playerListener), stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void resetGame() {
        EventManager.getInstance().publishEvent(new ResetGameEvent());
        Camera camera = stage.getCamera();
        camera.position.set(camera.position.x, 0, camera.position.z);
        timePassed = 0;
        table.setPosition(0, 0);
        for (int i = 0; i < worldHeight * 2; i += 300) {
            createLaneGroup(i);
        }
        Gdx.input.setInputProcessor(inputMultiplexer);
        soundManager.playBackgroundMusic();
    }

}
