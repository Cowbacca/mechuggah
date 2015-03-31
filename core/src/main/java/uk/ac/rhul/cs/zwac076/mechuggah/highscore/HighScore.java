package uk.ac.rhul.cs.zwac076.mechuggah.highscore;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class HighScore {
    @Getter
    private String name;
    @Getter
    private String score;

}
