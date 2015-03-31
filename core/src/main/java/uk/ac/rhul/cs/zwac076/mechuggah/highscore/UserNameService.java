package uk.ac.rhul.cs.zwac076.mechuggah.highscore;

import lombok.Getter;

public abstract class UserNameService {

    @Getter
    private String userName;

    protected void init(String unknownUserName) {
        this.userName = calculateUserName(unknownUserName);
    }

    protected abstract String calculateUserName(String unknownUserName);
}
