package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.actor.ActorFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class UIScreen implements Screen {
    private Stage stage;
    private Table table;
    private Skin skin;
    private Actor background;
    private List<Actor> bubbles;

    public UIScreen(Skin skin, int width, int height, List<Actor> bubbles) {
        this.skin = skin;
        stage = new Stage(new FitViewport(width, height));
        table = new Table();

        ActorFactory actorFactory = new ActorFactory();
        background = actorFactory.createBackground(width, height, false);
        background.setPosition(0, 0);

        this.bubbles = bubbles;

    }

    protected abstract void addUIComponentsToTable(Table table, Skin skin);

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);

    }

    @Override
    public void show() {

        addUIComponentsToTable(table, skin);
        table.setFillParent(true);
        stage.addActor(background);
        for (Actor bubble : bubbles) {
            stage.addActor(bubble);
        }
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void hide() {
        table.clear();
        background.remove();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    protected void refreshTable() {
        table.clear();
        addUIComponentsToTable(table, skin);
    }

    public void showDialog(Dialog dialog) {
        dialog.show(stage);
    }

    protected Stage getStage() {
        return stage;
    }

}
