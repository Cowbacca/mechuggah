package uk.ac.rhul.cs.zwac076.mechuggah.actor;

/**
 * Responsible for building LaneGroups.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class LaneBuilder {

	private static final int DEFAULT_NUMBER_OF_ENEMIES = 2;
	private static final int DEFAULT_ENEMY_DISTANCE = 200;
	private static final int DEFAULT_ENEMY_WIDTH = 100;
	private static final int DEFAULT_SPEED = 100;
	private static final float DEFAULT_STARTING_DELAY = 0;
	private final CollisionDetectingStage stage;
	private float speed;
	private int widthOfEnemies;
	private int numberOfEnemies;
	private int distanceBetweenEnemies;
	private final ActorFactory actorFactory;
	private final int yPosition;
	private float startingDelay;

	public LaneBuilder(final CollisionDetectingStage stage,
			final ActorFactory actorFactory, final int yPosition) {
		this.stage = stage;
		this.actorFactory = actorFactory;
		this.yPosition = yPosition;
		speed = DEFAULT_SPEED;
		widthOfEnemies = DEFAULT_ENEMY_WIDTH;
		distanceBetweenEnemies = DEFAULT_ENEMY_DISTANCE;
		numberOfEnemies = DEFAULT_NUMBER_OF_ENEMIES;
		startingDelay = DEFAULT_STARTING_DELAY;
	}

	public LaneGroup build() {
		return new LaneGroup(stage, actorFactory, yPosition, speed,
				startingDelay, widthOfEnemies, numberOfEnemies,
				distanceBetweenEnemies);
	}

	public LaneBuilder distanceBetweenEnemies(final int distanceBetweenEnemies) {
		this.distanceBetweenEnemies = distanceBetweenEnemies;
		return this;
	}

	public LaneBuilder numberOfEnemies(final int numberOfEnemies) {
		this.numberOfEnemies = numberOfEnemies;
		return this;
	}

	public LaneBuilder speed(final float speed) {
		this.speed = speed;
		return this;
	}

	public LaneBuilder startingDelay(final float startingDelay) {
		this.startingDelay = startingDelay;
		return this;
	}

	public LaneBuilder widthOfEnemies(final int widthOfEnemies) {
		this.widthOfEnemies = widthOfEnemies;
		return this;
	}

}
