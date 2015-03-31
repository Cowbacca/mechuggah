package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import uk.ac.rhul.cs.zwac076.mechuggah.action.StuckAction;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Collidable;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.PowerUp;
import uk.ac.rhul.cs.zwac076.mechuggah.event.CollisionType;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfMultiPlayerRoundEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LocalPlayerCollidedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.MultiPlayerGameShowStateChanged;
import uk.ac.rhul.cs.zwac076.mechuggah.event.RemotePlayerCollidedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.input.Shaker;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MultiPlayerGameScreen extends SinglePlayerGameScreen {

    private static final int SEED = 666;
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.3;

    private final class RemotePlayerCollidedEventListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            RemotePlayerCollidedEvent remotePlayerCollidedEvent = (RemotePlayerCollidedEvent) event;
            switch (remotePlayerCollidedEvent.getCollisionType()) {
            case ENEMY:
                if (!gameOver) {
                    EventManager.getInstance().publishEvent(new EndOfMultiPlayerRoundEvent(true));
                    gameOver = true;
                }
                break;
            default:
                break;

            }
            return false;
        }
    }

    private boolean gameOver;
    private RemotePlayerCollidedEventListener remotePlayerCollidedEventListener;

    public MultiPlayerGameScreen(Skin skin, int worldWidth, int worldHeight, Shaker shaker) {
        super(skin, worldWidth, worldHeight, shaker);
        gameOver = false;

        remotePlayerCollidedEventListener = new RemotePlayerCollidedEventListener();

    }

    @Override
    protected void playerHitEnemy() {
        if (!gameOver) {
            gameOver();
        }
    }

    private void gameOver() {
        EventManager.getInstance().publishEvent(new LocalPlayerCollidedEvent(CollisionType.ENEMY));
        EventManager.getInstance().publishEvent(new EndOfMultiPlayerRoundEvent(false));
        gameOver = true;

    }

    @Override
    protected void playerHitPowerUp(Player player, PowerUp powerUp) {
        CollisionType collisionType;
        if (StuckAction.class.isInstance(powerUp.getAction())) {
            collisionType = CollisionType.STUCK;
        } else {
            collisionType = CollisionType.SPEED_UP;
        }
        EventManager.getInstance().publishEvent(new LocalPlayerCollidedEvent(collisionType));
        super.playerHitPowerUp(player, powerUp);
    }

    @Override
    protected void setupPlayers() {
        actorFactory.createRemotePlayer(worldWidth, stage);
        Player player = (Player) actorFactory.createPlayerActor(worldWidth, stage);
        setupControls(player);

    }

    @Override
    public void show() {
        EventManager.getInstance().publishEvent(new MultiPlayerGameShowStateChanged(true));
        EventManager.getInstance().registerListener(RemotePlayerCollidedEvent.class, remotePlayerCollidedEventListener);
        super.show();
    }

    @Override
    public void hide() {
        EventManager.getInstance().unregisterListener(RemotePlayerCollidedEvent.class,
                remotePlayerCollidedEventListener);
        EventManager.getInstance().publishEvent(new MultiPlayerGameShowStateChanged(false));
        super.hide();
    }

    @Override
    public void resetGame() {
        random.setSeed(SEED);
        gameOver = false;
        super.resetGame();
    }

    @Override
    protected void onLaneGroupCreation(int y) {
        if (random.nextDouble() <= POWER_UP_SPAWN_PROBABILITY) {
            spawnAssociatedPowerUp(y);
        }
    }

    private void spawnAssociatedPowerUp(final int y) {
        Actor powerUpActor;
        int xPosition = worldWidth / 2;
        int yPosition = y + 100;
        if (random.nextBoolean()) {
            powerUpActor = actorFactory.createSpeedUpPowerUpActor(xPosition, yPosition);
        } else {
            powerUpActor = actorFactory.createStuckPowerUpActor(xPosition, yPosition);
        }

        stage.addCollidableActor((Collidable) powerUpActor);
        powerUpActor.setZIndex(1);
    }

    @Override
    protected void addUIComponentsToTable() {

    }
}
