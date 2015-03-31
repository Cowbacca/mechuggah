package uk.ac.rhul.cs.zwac076.mechuggah.event;

import java.util.HashMap;
import java.util.Map;

import uk.ac.rhul.cs.zwac076.mechuggah.action.JumpAction;
import uk.ac.rhul.cs.zwac076.mechuggah.action.SlideAction;
import uk.ac.rhul.cs.zwac076.mechuggah.actor.Player;
import uk.ac.rhul.cs.zwac076.mechuggah.input.Input;
import uk.ac.rhul.cs.zwac076.mechuggah.input.InputHandlingStrategy;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class PlayerInputEventListener implements EventListener {

    private final class ShakeInputHandlingStrategy extends InputHandlingStrategy {
        private ShakeInputHandlingStrategy(Player player) {
            super(player);
        }

        @Override
        public void handleInput() {
            player.unFreeze();

        }
    }

    private final class SlideRightInputHandlingStrategy extends InputHandlingStrategy {
        private SlideRightInputHandlingStrategy(Player player) {
            super(player);
        }

        @Override
        public void handleInput() {
            player.addInputAction(new SlideAction(player, true));

        }
    }

    private final class SlideLeftInputHandlingStrategy extends InputHandlingStrategy {
        private SlideLeftInputHandlingStrategy(Player player) {
            super(player);
        }

        @Override
        public void handleInput() {
            player.addInputAction(new SlideAction(player, false));

        }
    }

    private final class JumpInputHandlingStrategy extends InputHandlingStrategy {
        private JumpInputHandlingStrategy(Player player) {
            super(player);
        }

        @Override
        public void handleInput() {
            player.addInputAction(new JumpAction(player));

        }
    }

    private Player player;
    private Map<Input, InputHandlingStrategy> inputHandlingStrategyMap;

    public PlayerInputEventListener(Player player) {
        this.player = player;
        setupInputHandlerMap();
    }

    @Override
    public boolean handle(Event event) {
        PlayerInputEvent playerInputEvent = (PlayerInputEvent) event;
        inputHandlingStrategyMap.get(playerInputEvent.getInputType()).handleInput();
        return false;
    }

    private void setupInputHandlerMap() {
        inputHandlingStrategyMap = new HashMap<Input, InputHandlingStrategy>();
        inputHandlingStrategyMap.put(Input.JUMP, new JumpInputHandlingStrategy(player));
        inputHandlingStrategyMap.put(Input.SLIDE_LEFT, new SlideLeftInputHandlingStrategy(player));
        inputHandlingStrategyMap.put(Input.SLIDE_RIGHT, new SlideRightInputHandlingStrategy(player));
        inputHandlingStrategyMap.put(Input.SHAKE, new ShakeInputHandlingStrategy(player));
    }
}