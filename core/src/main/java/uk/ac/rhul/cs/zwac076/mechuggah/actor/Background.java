package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import lombok.AllArgsConstructor;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerMovedEvent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * Background actor that is responsible for displaying the background.
 * Background brightness oscillates with time.
 * 
 * @author Angus J. Goldsmith
 * 
 */
@AllArgsConstructor
public class Background extends Actor implements EventListener {
    private static final Color DEFAULT_COLOUR = new Color(91f / 255f, 178f / 255f, 1, 1);

    private final TextureRegion texture;
    private final Color colour;

    public Background(final TextureRegion texture, int backgroundHeight, int backgroundWidth) {
        this.texture = texture;
        colour = DEFAULT_COLOUR;
        setHeight(backgroundHeight);
        setWidth(backgroundWidth);
        setY(-getHeight());
    }

    /**
     * Draws the background, filling the screen with the background colour.
     */
    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        final Color initialColour = batch.getColor();
        batch.setColor(colour);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        batch.setColor(initialColour);
    }

    /**
     * Handles PlayerMovedEvents by moving the background along with the player.
     */
    @Override
    public boolean handle(final Event event) {
        if (PlayerMovedEvent.class.isInstance(event)) {
            final PlayerMovedEvent playerMovedEvent = (PlayerMovedEvent) event;
            float newY = playerMovedEvent.getY() - getHeight() / 2;
            if (newY > getY()) {
                setY(newY);
            }
        } else {
            setY(-getHeight());
        }
        return false;
    }

}
