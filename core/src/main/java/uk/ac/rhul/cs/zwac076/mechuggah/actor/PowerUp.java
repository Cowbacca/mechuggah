package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import java.util.HashSet;
import java.util.Set;

import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEventHandler;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PoolableNoLongerOnScreenEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.ResetGameEventListener;
import uk.ac.rhul.cs.zwac076.mechuggah.maths.IntersectionChecker;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PowerUp extends CollisionActor implements Poolable {

    private final class PlayerMovedEventHandlerPoolable extends PlayerMovedEventHandler {
        private PlayerMovedEventHandlerPoolable(Actor actor) {
            super(actor);
        }

        @Override
        protected void handleActorMovingOffScreen() {

            super.handleActorMovingOffScreen();
            EventManager.getInstance().publishEvent(new PoolableNoLongerOnScreenEvent((Poolable) actor));
        }
    }

    private TextureRegion texture;
    private Set<Player> playerSet;
    private PowerUpActionCreationStrategy powerUpActionCreationStrategy;
    private PlayerMovedEventHandlerPoolable playerMovedEventListener;

    public PowerUp(TextureRegion texture, float x, float y, float width, float height,
            IntersectionChecker intersectionChecker, PowerUpActionCreationStrategy powerUpActionStrategy) {
        super(x, y, width, height, intersectionChecker);
        this.texture = texture;
        playerSet = new HashSet<Player>();
        this.powerUpActionCreationStrategy = powerUpActionStrategy;
        EventManager.getInstance().registerListener(ResetGameEvent.class, new ResetGameEventListener(this));
        playerMovedEventListener = new PlayerMovedEventHandlerPoolable(this);
        EventManager.getInstance().registerListener(PlayerMovedEvent.class, playerMovedEventListener);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(texture, getX(), getY(), texture.getRegionWidth(), texture.getRegionHeight());
    }

    public boolean hasBeenActivatedBy(Player player) {
        return playerSet.contains(player);
    }

    public void activatedByPlayer(Player player) {
        playerSet.add(player);
    }

    public Action getAction() {
        return powerUpActionCreationStrategy.createPowerUpAction();
    }

    @Override
    public void reset() {
        playerSet.clear();

    }

    public void init(TextureRegion powerUpTexture, int x, int y, PowerUpActionCreationStrategy powerUpActionStrategy) {
        this.powerUpActionCreationStrategy = powerUpActionStrategy;
        texture = powerUpTexture;
        setBounds(x, y, texture.getRegionWidth(), texture.getRegionHeight());
        playerMovedEventListener.init();

    }
}
