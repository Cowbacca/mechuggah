package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import java.util.Random;

import lombok.Setter;
import uk.ac.rhul.cs.zwac076.mechuggah.action.SpeedUpAction;
import uk.ac.rhul.cs.zwac076.mechuggah.action.StuckAction;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PoolableNoLongerOnScreenEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

/**
 * Responsible for creating Actors.
 *
 * @author Angus J. Goldsmith
 */
public class ActorFactory {

    public static final int DEFAULT_PLAYER_ACCELERATION = 10;
    private static final int DEFAULT_ENEMY_HEIGHT_DIVISOR = 2;
    private static final int DEFAULT_PLAYER_Y = 0;
    private static final float DEFAULT_PLAYER_SPEED = 100;
    private static final int DEFAULT_PLAYER_HEIGHT_DIVISOR = 4;
    private final IntersectionChecker intersectionChecker;
    @Setter
    private TextureRegion enemyTexture;
    @Setter
    private float defaultPlayerY;
    @Setter
    private float defaultPlayerHeight;
    @Setter
    private float defaultPlayerSpeed;
    private TextureRegion backgroundTexture;
    private AnimationComponent playerAnimation;
    private Random bubbleRandom;
    private TextureRegion bubbleTexture;
    private PowerUpActionCreationStrategy speedUpPowerUpActionStrategy;
    private PowerUpActionCreationStrategy stuckPowerUpActionStrategy;
    private TextureRegion speedUpTexture;
    private TextureRegion stuckTexture;
    private TextureRegion pufferTexture;
    private Pool<PowerUp> powerUpPool;
    private Pool<PufferActor> pufferActorPool;
    private Pool<EnemyActor> enemyActorPool;
    @Setter
    private float defaultPlayerAcceleration;

    public ActorFactory() {
        intersectionChecker = new IntersectionChecker();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("mechuggah.pack"));
        backgroundTexture = atlas.findRegion("white-square");
        enemyTexture = atlas.findRegion("turtle_a");
        TextureRegion playerTexture = atlas.findRegion("newjelly_a");
        speedUpTexture = atlas.findRegion("speedup");
        stuckTexture = atlas.findRegion("stuck");
        bubbleTexture = atlas.findRegion("bubble");
        pufferTexture = atlas.findRegion("spike_turtle");
        playerAnimation = new AnimationComponent(playerTexture, 2, 1, 10);
        defaultPlayerY = DEFAULT_PLAYER_Y;
        defaultPlayerSpeed = DEFAULT_PLAYER_SPEED;
        defaultPlayerAcceleration = DEFAULT_PLAYER_ACCELERATION;
        bubbleRandom = new Random();
        speedUpPowerUpActionStrategy = new SpeedUpPowerUpActionStrategy();
        stuckPowerUpActionStrategy = new StuckPowerUpActionStrategy();

        powerUpPool = new PowerUpPool();
        pufferActorPool = new PufferActorPool();
        enemyActorPool = new EnemyActorPool();

        EventManager.getInstance().registerListener(PoolableNoLongerOnScreenEvent.class,
                new PoolableNoLongerOnScreenEventListener());
    }

    /**
     * Creates an Actor representing the background of the game.
     *
     * @param backgroundWidth  the width to make the background.
     * @param backgroundHeight the height to make the background.
     * @return the created background Actor.
     */
    public Actor createBackground(final int backgroundWidth, final int backgroundHeight, boolean listeners) {
        final Background background = new Background(backgroundTexture, backgroundHeight, backgroundWidth);
        if (listeners) {
            EventManager.getInstance().registerListener(PlayerMovedEvent.class, background);
            EventManager.getInstance().registerListener(ResetGameEvent.class, background);
        }
        return background;
    }

    public Actor createBubbleActor(float x, float y, float yBounds) {
        return new BubbleActor(bubbleTexture, x, y, bubbleRandom, yBounds);
    }

    public Actor createBackground(final int backgroundWidth, final int backgroundHeight) {
        return createBackground(backgroundWidth, backgroundHeight, true);
    }

    /**
     * Creates an Actor representing an enemy.
     *
     * @param x the x position to place the enemy at.
     * @param y the y position to place the enemy at.
     * @return the create enemy Actor.
     */
    public Actor createEnemyActor(final int x, final int y) {
        EnemyActor enemyActor = enemyActorPool.obtain();
        enemyActor.init(x, y);

        return enemyActor;
    }

    /**
     * Creates an Actor representing the player, as well as creating the
     * controls for the player.
     *
     * @param worldWidth the width of the game world.
     * @param stage      the game stage.
     * @return the created Actor.
     */
    public Actor createPlayerActor(final int worldWidth, final CollisionDetectingStage stage) {

        final Player player = new Player(playerAnimation, (worldWidth / 2) - (playerAnimation.getWidth() / 2),
                defaultPlayerY, playerAnimation.getWidth(),
                playerAnimation.getHeight() / DEFAULT_PLAYER_HEIGHT_DIVISOR, defaultPlayerSpeed,
                defaultPlayerAcceleration, intersectionChecker);
        stage.addCollidableActor(player);
        return player;
    }

    public Actor createRemotePlayer(final int worldWidth, final CollisionDetectingStage stage) {
        RemotePlayer remotePlayer = new RemotePlayer(playerAnimation, (worldWidth / 2)
                - (playerAnimation.getWidth() / 2), defaultPlayerY, playerAnimation.getWidth(),
                playerAnimation.getHeight() / DEFAULT_PLAYER_HEIGHT_DIVISOR, defaultPlayerSpeed,
                defaultPlayerAcceleration, intersectionChecker);
        stage.addCollidableActor(remotePlayer);
        return remotePlayer;
    }

    private Actor createPowerUpActor(final TextureRegion powerUpTexture, final int x, final int y,
                                     final PowerUpActionCreationStrategy powerUpActionStrategy) {
        PowerUp powerUpActor = powerUpPool.obtain();
        powerUpActor.init(powerUpTexture, x - powerUpTexture.getRegionWidth() / 2, y, powerUpActionStrategy);
        return powerUpActor;
    }

    public Actor createSpeedUpPowerUpActor(final int x, final int y) {
        return createPowerUpActor(speedUpTexture, x, y, speedUpPowerUpActionStrategy);
    }

    public Actor createStuckPowerUpActor(final int x, final int y) {
        return createPowerUpActor(stuckTexture, x, y, stuckPowerUpActionStrategy);
    }

    public Actor createPufferActor(int x, int y) {
        PufferActor pufferActor = pufferActorPool.obtain();
        pufferActor.init(x, y);
        return pufferActor;

    }

    public Actor createBottomOfScreenActor(float worldWidth, int worldHeight) {
        return new BottomOfScreenActor(worldWidth, worldHeight, intersectionChecker);
    }

    private final class EnemyActorPool extends Pool<EnemyActor> {
        @Override
        protected EnemyActor newObject() {

            AnimationComponent enemyAnimationComponent = new AnimationComponent(enemyTexture, 2, 1, 5);
            return new EnemyActor(enemyAnimationComponent, 0, 0, enemyAnimationComponent.getWidth(),
                    enemyAnimationComponent.getHeight() / DEFAULT_ENEMY_HEIGHT_DIVISOR, intersectionChecker);
        }
    }

    private final class PufferActorPool extends Pool<PufferActor> {
        @Override
        protected PufferActor newObject() {
            AnimationComponent animationComponent = new AnimationComponent(pufferTexture, 2, 1, 3);
            return new PufferActor(animationComponent, 0, 0, animationComponent.getWidth(),
                    animationComponent.getHeight(), intersectionChecker);

        }
    }

    private final class PoolableNoLongerOnScreenEventListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            PoolableNoLongerOnScreenEvent poolableNoLongerOnScreenEvent = (PoolableNoLongerOnScreenEvent) event;
            Poolable poolable = poolableNoLongerOnScreenEvent.getPoolable();
            if (PowerUp.class.isInstance(poolable)) {
                powerUpPool.free((PowerUp) poolable);
            } else if (PufferActor.class.isInstance(poolable)) {
                pufferActorPool.free((PufferActor) poolable);
            } else if (EnemyActor.class.isInstance(poolable)) {
                enemyActorPool.free((EnemyActor) poolable);
            }
            return false;

        }
    }

    private final class PowerUpPool extends Pool<PowerUp> {
        @Override
        protected PowerUp newObject() {
            return new PowerUp(null, 0, 0, 0, 0, intersectionChecker, null);
        }
    }

    private final class StuckPowerUpActionStrategy implements PowerUpActionCreationStrategy {
        @Override
        public Action createPowerUpAction() {
            return new StuckAction();
        }
    }

    private final class SpeedUpPowerUpActionStrategy implements PowerUpActionCreationStrategy {
        @Override
        public Action createPowerUpAction() {
            return new SpeedUpAction(200f, 1f);
        }
    }
}
