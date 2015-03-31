package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LaneGroupDeletedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEventHandler;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PoolableNoLongerOnScreenEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Group representing a lane.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class LaneGroup extends Group implements Collidable, EventListener {

    private final class GroupWithDeletionPlayerMovedEventHandler extends PlayerMovedEventHandler {
        private GroupWithDeletionPlayerMovedEventHandler(Actor actor) {
            super(actor);
        }

        @Override
        protected void handleActorMovingOffScreen() {
            EventManager.getInstance().publishEvent(new LaneGroupDeletedEvent());
            Group laneGroup = (Group) actor;

            for (Actor enemyActor : laneGroup.getChildren()) {
                EventManager.getInstance().publishEvent(new PoolableNoLongerOnScreenEvent((Poolable) enemyActor));
            }

            super.handleActorMovingOffScreen();
            actor.remove();

        }
    }

    private static final int DEFAULT_Z_INDEX = 1;
    private float laneSpeed;

    public LaneGroup(final CollisionDetectingStage stage) {
        stage.addCollidableActor(this);
        setZIndex(DEFAULT_Z_INDEX);
        EventManager eventManager = EventManager.getInstance();
        eventManager.registerListener(PlayerMovedEvent.class, new GroupWithDeletionPlayerMovedEventHandler(this));
        eventManager.registerListener(ResetGameEvent.class, this);
    }

    /**
     * Sets up the lane, creating a number of enemies to make up the lane.
     * 
     * @param stage
     *            the stage of the game.
     * @param actorFactory
     *            the actor factory to create the enemy actors.
     * @param yPosition
     *            the y position of the lane.
     * @param speed
     *            the speed in pixels per second of the lane.
     * @param startingDelay
     *            the delay before enemies appear on screen.
     * @param widthOfEnemies
     *            the width in pixels of enemies.
     * @param numberOfEnemies
     *            the number of enemies.
     * @param distanceBetweenEnemies
     *            the distance in pixels between enemies.
     */
    public LaneGroup(final CollisionDetectingStage stage, final ActorFactory actorFactory, final int yPosition,
            final float speed, final float startingDelay, final int widthOfEnemies, final int numberOfEnemies,
            final int distanceBetweenEnemies) {
        this(stage);
        setBounds(0, yPosition, stage.getWidth(), 0);
        for (int i = 0; i < numberOfEnemies; i++) {
            final int startingX = (int) (-widthOfEnemies - (i * (widthOfEnemies + distanceBetweenEnemies)) - startingDelay
                    * speed);
            addActor(actorFactory.createEnemyActor(startingX, 0));
        }

        setLaneSpeed(speed);
    }

    /**
     * Checks if any of the enemies in the group have moved off the right edge
     * of the screen and places them at the start of the lane if so.
     */
    @Override
    public void act(final float delta) {
        super.act(delta);
        for (final Actor actor : getChildren()) {
            final EnemyActor enemyActor = (EnemyActor) actor;
            checkIfOffRightOfScreen(enemyActor);
        }
    }

    @Override
    public boolean checkForCollision(final Rectangle boundingRectangle, final float z, boolean ignoreZ) {
        for (final Actor enemyActor : getChildren()) {
            if (((EnemyActor) enemyActor).checkForCollision(boundingRectangle, z, ignoreZ)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkForCollisions(final Collidable... collisionActors) {
        for (final Actor enemyActor : getChildren()) {
            ((EnemyActor) enemyActor).checkForCollisions(collisionActors);
        }

    }

    /**
     * Checks if the given enemy actor has moved off the right edge of the
     * screen and places them at the start of the lane if so.
     * 
     * @param enemyActor
     *            the lane actor to check.
     */
    private void checkIfOffRightOfScreen(final EnemyActor enemyActor) {
        if (enemyActor.isRightOf(getRight())) {
            final float oneSecondXOffset = laneSpeed + enemyActor.getWidth();
            enemyActor.setX(-oneSecondXOffset);
        }
    }

    /**
     * Handles PlayerMovedEvents. If the player has moved such that the
     * LaneGroup is now off the screen, delete the LaneGroup.
     */
    @Override
    public boolean handle(final Event event) {
        remove();
        return false;
    }

    /**
     * Sets the speed of the lane and hence the speed of all children.
     * 
     * @param laneSpeed
     *            the speed in pixels per second.
     */
    public void setLaneSpeed(final float laneSpeed) {
        this.laneSpeed = laneSpeed;
        for (final Actor enemyActor : getChildren()) {
            ((EnemyActor) enemyActor).setSpeed(laneSpeed);
        }
    }
}
