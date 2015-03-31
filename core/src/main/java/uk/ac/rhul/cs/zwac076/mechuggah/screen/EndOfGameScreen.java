package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.event.BackToMenuEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.HighScoresRecievedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.StartGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.highscore.HighScore;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class EndOfGameScreen extends UIScreen {

    private final class HighScoresRecievedEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            HighScoresRecievedEvent highScoresRecievedEvent = (HighScoresRecievedEvent) event;
            List<HighScore> remoteHighscores = highScoresRecievedEvent.getRemoteHighscores();
            HighScore lowestRemoteHighScore = remoteHighscores.get(remoteHighscores.size() - 1);
            String lowestHighscoreString = lowestRemoteHighScore.getScore();
            lowestHighScore = Float.valueOf(lowestHighscoreString);
            lowestHighScoreIsSet = true;
            return false;
        }
    }

    private static final int BOTTOM_PADDING = 20;
    private static final String HIGH_SCORE_FORMAT = "%.2f";
    private Label scoreLabel;
    private boolean lowestHighScoreIsSet;
    private Float lowestHighScore;
    private boolean newHighScore;
    private Actor newHighScoreLabel;
    private Label highscoreText;
    private TextButton playAgainButton;
    private TextButton backButton;

    public EndOfGameScreen(final Skin skin, final int width, final int height, List<Actor> bubbles) {
        super(skin, width, height, bubbles);
        lowestHighScoreIsSet = false;
        setupLabels(skin);
        setupButtons(skin);
        EventManager.getInstance().registerListener(HighScoresRecievedEvent.class,
                new HighScoresRecievedEventListener());
    }

    private void setupButtons(final Skin skin) {
        playAgainButton = new TextButton("Play Again", skin);
        backButton = new TextButton("Back to Menu", skin);
        playAgainButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new StartGameEvent(GameType.SINGLE_PLAYER));

            }

        });
        backButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new BackToMenuEvent());

            }

        });
    }

    private void setupLabels(final Skin skin) {
        scoreLabel = new Label("", skin);
        newHighScoreLabel = new Label("New Online High Score!", skin);
        highscoreText = new Label("Time Survived:", skin);
    }

    public void setHighscore(final float highScore) {
        scoreLabel.setText(String.format(HIGH_SCORE_FORMAT, highScore));
        if (lowestHighScoreIsSet && highScore > lowestHighScore) {
            newHighScore = true;
        } else {
            newHighScore = false;
        }
    }

    @Override
    protected void addUIComponentsToTable(Table table, Skin skin) {

        if (newHighScore) {
            table.add(newHighScoreLabel).padBottom(BOTTOM_PADDING);
            table.row();
        }
        table.add(highscoreText);
        table.row();
        table.add(scoreLabel).padBottom(BOTTOM_PADDING);
        table.row();
        table.add(backButton).padBottom(BOTTOM_PADDING);
        table.row();
        table.add(playAgainButton);

    }
}
