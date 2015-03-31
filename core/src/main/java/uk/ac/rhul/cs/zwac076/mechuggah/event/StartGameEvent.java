package uk.ac.rhul.cs.zwac076.mechuggah.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.GameType;

import com.badlogic.gdx.scenes.scene2d.Event;

@AllArgsConstructor
public class StartGameEvent extends Event {
    @Getter
    private GameType gameType;

}
