package uk.ac.rhul.cs.zwac076.mechuggah.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.badlogic.gdx.scenes.scene2d.Event;

@AllArgsConstructor
public class EndOfMultiPlayerRoundEvent extends Event {
    @Getter
    private boolean winner;
}
