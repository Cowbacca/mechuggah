package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import org.mockito.Mock;

public class CollisionDetectingStageTest {

	// private CollisionDetectingStage collisionDetectingStage;
	@Mock
	private Collidable mockCollidableActor;
	@Mock
	private Collidable mockAnotherCollidableActor;

	// Don't seem to be able to test this as it won't create outside of the
	// context of an actual game.
	/*
	 * @Before public void setup() { MockitoAnnotations.initMocks(this);
	 * collisionDetectingStage = new CollisionDetectingStage(new FitViewport( 0,
	 * 0)); }
	 * 
	 * @Test public void testAct() {
	 * collisionDetectingStage.addCollidableActor(mockCollidableActor);
	 * collisionDetectingStage.addCollidableActor(mockAnotherCollidableActor);
	 * collisionDetectingStage.act();
	 * verify(mockCollidableActor).checkForCollisions(
	 * mockAnotherCollidableActor); }
	 */
}
