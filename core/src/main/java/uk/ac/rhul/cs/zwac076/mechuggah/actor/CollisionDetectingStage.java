package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Stage that deals with Collidables.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class CollisionDetectingStage extends Stage implements EventListener {

    private final List<Collidable> collidables;

    public CollisionDetectingStage(final Viewport viewport) {
        super(viewport);
        collidables = new ArrayList<Collidable>();
        EventManager.getInstance().registerListener(ResetGameEvent.class, this);
    }

    /**
     * Along with usual stage duties, also checks for collisions among
     * Collidables.
     */
    @Override
    public void act(final float delta) {
        super.act(delta);
        for (int i = 0; i < collidables.size(); i++) {
            for (int j = i + 1; j < collidables.size(); j++) {
                collidables.get(i).checkForCollisions(collidables.get(j));
            }
        }

    }

    /**
     * Adds a Collidable actor to the list of Collidables, along with adding to
     * the stage as usual.
     * 
     * @param collidableActor
     *            the Collidable actor to add.
     */
    public void addCollidableActor(final Collidable collidableActor) {
        collidables.add(collidableActor);
        super.addActor((Actor) collidableActor);
    }

    @Override
    public boolean handle(Event event) {
        for (Iterator<Collidable> iterator = collidables.iterator(); iterator.hasNext();) {
            Collidable collidable = (Collidable) iterator.next();
            if (!Player.class.isInstance(collidable) && !BottomOfScreenActor.class.isInstance(collidable)) {
                iterator.remove();
            }

        }
        return false;
    }

}
