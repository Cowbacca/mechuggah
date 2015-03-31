package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.event.BackToMenuEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SplashScreen extends UIScreen {

    private static final float SPLASH_SCREEN_DURATION = 2;
    private float timePassed;
    private Label titleLabel;
    private Label byLabel;
    private Label developerLabel;

    public SplashScreen(Skin skin, int width, int height, List<Actor> bubbles) {
        super(skin, width, height, bubbles);
        titleLabel = new Label("JELL. E. FISH", skin);
        byLabel = new Label("by", skin);
        developerLabel = new Label("Steakhouse Games", skin);
    }

    @Override
    protected void addUIComponentsToTable(Table table, Skin skin) {
        table.add(titleLabel);
        table.row();
        table.add(byLabel);
        table.row();
        table.add(developerLabel);

    }

    @Override
    public void render(float delta) {
        timePassed += delta;
        super.render(delta);
        if (timePassed > SPLASH_SCREEN_DURATION) {
            EventManager.getInstance().publishEvent(new BackToMenuEvent());
        }
    }

    @Override
    public void show() {

        timePassed = 0;
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }
}
