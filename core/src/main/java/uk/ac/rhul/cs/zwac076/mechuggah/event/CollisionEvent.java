package uk.ac.rhul.cs.zwac076.mechuggah.event;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Collidable;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.CollisionActor;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Event representing a collision between two Collidables.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class CollisionEvent extends Event {

	@Getter
	private final List<Collidable> participants;

	public CollisionEvent(final CollisionActor collisionActor,
			final Collidable collidable) {
		participants = new ArrayList<Collidable>();
		participants.add(collisionActor);
		participants.add(collidable);
	}
}
