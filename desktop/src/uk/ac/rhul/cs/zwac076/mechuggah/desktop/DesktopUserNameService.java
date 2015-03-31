package uk.ac.rhul.cs.zwac076.mechuggah.desktop;

import uk.ac.rhul.cs.zwac076.mechuggah.highscore.UserNameService;

final class DesktopUserNameService extends UserNameService {
    private static final String USER_NAME_PROPERTY_NAME = "user.name";

    public DesktopUserNameService(String unknownUserName) {
        init(unknownUserName);
    }

    @Override
    protected String calculateUserName(String unknownUserName) {
        String userName;
        try {
            userName = System.getProperty(USER_NAME_PROPERTY_NAME);
        } catch (Exception e) {
            userName = unknownUserName;
        }
        return userName;
    }
}