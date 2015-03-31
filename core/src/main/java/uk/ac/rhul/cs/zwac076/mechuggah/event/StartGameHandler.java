package uk.ac.rhul.cs.zwac076.mechuggah.event;

import lombok.AllArgsConstructor;
import uk.ac.rhul.cs.zwac076.mechuggah.MechuggahGame;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.GameType;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.SinglePlayerGameScreen;
import uk.ac.rhul.cs.zwac076.mechuggah.screen.WaitingForPlayersScreen;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

@AllArgsConstructor
public class StartGameHandler implements EventListener {

    private final MechuggahGame mechuggahGame;
    private final SinglePlayerGameScreen singlePlayerScreen;
    private final WaitingForPlayersScreen waitingForPlayersScreen;

    @Override
    public boolean handle(final Event event) {
        StartGameEvent startGameEvent = (StartGameEvent) event;
        if (startGameEvent.getGameType() == GameType.MULTI_PLAYER) {
            mechuggahGame.setScreen(waitingForPlayersScreen);
        } else {
            singlePlayerScreen.setupGame();
            mechuggahGame.setScreen(singlePlayerScreen);
        }

        return true;
    }

}
