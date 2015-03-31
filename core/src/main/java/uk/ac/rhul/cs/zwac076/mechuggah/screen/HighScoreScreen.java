package uk.ac.rhul.cs.zwac076.mechuggah.screen;

import java.util.ArrayList;
import java.util.List;

import uk.ac.rhul.cs.zwac076.mechuggah.event.BackToMenuEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.HighScoresRecievedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.highscore.HighScore;
import uk.ac.rhul.cs.zwac076.mechuggah.highscore.UserNameService;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class HighScoreScreen extends UIScreen {

    private static final String PREFERNCES_NAME = "Mechuggah";
    private static final String HIGH_SCORE_FORMATTING = "%.2f";
    private static final String BACK_BUTTON_TEXT = "Back";
    private static final String HIGH_SCORE_LABEL_TEXT = "Local High Score";
    private static final String HIGH_SCORE_KEY = "highScore";
    private static final int NAME_MAX_LENGTH = 8;

    private final class HighScoresRecievedEventListener implements EventListener {

        private final Skin skin;

        private HighScoresRecievedEventListener(Skin skin) {
            this.skin = skin;
        }

        @Override
        public boolean handle(Event event) {
            onlineHighScores.clear();
            onlineHighScoreNames.clear();
            HighScoresRecievedEvent highScoresRecievedEvent = (HighScoresRecievedEvent) event;
            for (HighScore highScore : highScoresRecievedEvent.getRemoteHighscores()) {
                float highScoreFloat = Float.parseFloat(highScore.getScore());
                onlineHighScores.add(new Label(createHighScoreText(highScoreFloat), skin));
                String nameWithMaxLength = nameWithMaxLength(highScore.getName());
                onlineHighScoreNames.add(new Label(nameWithMaxLength + ":", skin));
            }
            return false;
        }

    }

    private final class EndOfGameEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            EndOfGameEvent endOfGameEvent = (EndOfGameEvent) event;
            if (endOfGameEvent.getHighscore() > highScore) {
                highScore = endOfGameEvent.getHighscore();
                highScoreLabel.setText(createHighScoreText(highScore));
                preferences.putFloat(HIGH_SCORE_KEY, highScore);
                preferences.flush();

            }
            return false;
        }
    }

    private float highScore;
    private Label labelLabel;
    private Label highScoreLabel;
    private TextButton backButton;
    private Preferences preferences;
    private List<Label> onlineHighScores;
    private List<Label> onlineHighScoreNames;
    private UserNameService userNameService;

    public HighScoreScreen(final Skin skin, int width, int height, List<Actor> bubbles, UserNameService userNameService) {
        super(skin, width, height, bubbles);

        this.userNameService = userNameService;

        EventManager.getInstance().registerListener(EndOfGameEvent.class, new EndOfGameEventListener());
        EventManager.getInstance().registerListener(HighScoresRecievedEvent.class,
                new HighScoresRecievedEventListener(skin));

        preferences = Gdx.app.getPreferences(PREFERNCES_NAME);
        onlineHighScores = new ArrayList<Label>();
        onlineHighScoreNames = new ArrayList<Label>();
        highScore = preferences.getFloat(HIGH_SCORE_KEY, 0);
        labelLabel = new Label(HIGH_SCORE_LABEL_TEXT, skin);
        highScoreLabel = new Label(createHighScoreText(highScore), skin);
        backButton = new TextButton(BACK_BUTTON_TEXT, skin);
        backButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventManager.getInstance().publishEvent(new BackToMenuEvent());

            }
        });

    }

    private String createHighScoreText(float highScore) {
        return String.format(HIGH_SCORE_FORMATTING, highScore);
    }

    @Override
    protected void addUIComponentsToTable(Table table, Skin skin) {
        table.add(labelLabel).colspan(2);
        table.row();
        table.add(new Label(nameWithMaxLength(userNameService.getUserName()) + ":", skin)).padBottom(50);
        table.add(highScoreLabel).padBottom(50);
        table.row();

        table.add(new Label("Online High Scores", skin)).colspan(2);
        table.row();

        int i = 0;
        for (Label onlineHighScoreLabel : onlineHighScores) {
            table.add(onlineHighScoreNames.get(i++));
            table.add(onlineHighScoreLabel);
            table.row();
        }
        table.add(backButton).colspan(2);

    }

    private String nameWithMaxLength(String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            return name.substring(0, NAME_MAX_LENGTH);
        } else {
            return name;
        }
    }
}
