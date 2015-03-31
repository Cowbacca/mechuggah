package uk.ac.rhul.cs.zwac076.mechuggah.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.ac.rhul.cs.zwac076.mechuggah.highscore.HighScore;

import com.badlogic.gdx.scenes.scene2d.Event;

@AllArgsConstructor
public class HighScoresRecievedEvent extends Event {
    @Getter
    List<HighScore> remoteHighscores;

}
