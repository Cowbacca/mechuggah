package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.event.BackToMenuEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ControlsScreen extends UIScreen {

    private Label jumpLabel;
    private TextButton backButton;
    private Label swipeLabel;
    private Label shakeLabel;

    public ControlsScreen(Skin skin, int width, int height, List<Actor> bubbles) {
        super(skin, width, height, bubbles);
        jumpLabel = new Label("Tap to jump.", skin, "small-font", Color.WHITE);
        swipeLabel = new Label("Swipe to temporarily slide.", skin, "small-font", Color.WHITE);
        shakeLabel = new Label("Shake to escape from sticky traps.", skin, "small-font", Color.WHITE);
        backButton = new TextButton("Back", skin);
        backButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new BackToMenuEvent());

            }

        });
    }

    @Override
    protected void addUIComponentsToTable(Table table, Skin skin) {
        addLabel(table, jumpLabel);
        addLabel(table, swipeLabel);
        addLabel(table, shakeLabel);
        table.add(backButton);

    }

    private void addLabel(Table table, Label label) {
        table.add(label).padBottom(20);
        table.row();
    }

}
