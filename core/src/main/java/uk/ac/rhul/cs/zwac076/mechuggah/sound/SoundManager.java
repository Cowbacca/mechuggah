package uk.ac.rhul.cs.zwac076.mechuggah.sound;

import java.util.HashMap;

import uk.ac.rhul.cs.zwac076.mechuggah.event.CollisionType;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfGameEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EndOfMultiPlayerRoundEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.EventManager;
import uk.ac.rhul.cs.zwac076.mechuggah.event.LocalPlayerCollidedEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.event.PlayerInputEvent;
import uk.ac.rhul.cs.zwac076.mechuggah.input.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class SoundManager {
    private static final String BACKGROUND_MUSIC_PATH = "edorian.mp3";
    private static final String STUCK_PATH = "stuck.wav";
    private static final String JUMP_PATH = "jump.wav";
    private static final String EXPLOSION_PATH = "explosion.wav";
    private static final String SLIDE_PATH = "slide.wav";
    private static final String SPEED_UP_PATH = "speed.wav";
    private static final float MUSIC_VOLUME = 0.7f;

    private final class LocalPlayerCollidedEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            LocalPlayerCollidedEvent localPlayerCollidedEvent = (LocalPlayerCollidedEvent) event;
            CollisionType collisionType = localPlayerCollidedEvent.getCollisionType();
            if (collisionType == CollisionType.SPEED_UP) {
                playerSpeedUpSound.play();
            } else if (collisionType == CollisionType.STUCK) {
                playerStuckSound.play();
            }
            return false;
        }
    }

    private final class PlayerInputEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            PlayerInputEvent playerInputEvent = (PlayerInputEvent) event;
            Input inputType = playerInputEvent.getInputType();
            if (inputType == Input.SLIDE_LEFT || inputType == Input.SLIDE_RIGHT) {
                playerSlideSound.play();
            } else if (inputType == Input.JUMP) {
                playerJumpSound.play();
            }
            return false;
        }
    }

    private final class EndOfGameEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            backgroundMusic.stop();
            playerDeathSound.play();
            return false;
        }
    }

    private Sound playerDeathSound;
    private HashMap<Class<? extends Event>, EventListener> listeners;
    private Sound playerSlideSound;
    private Sound playerSpeedUpSound;
    private Sound playerJumpSound;
    private Sound playerStuckSound;
    private Music backgroundMusic;

    public SoundManager() {
        loadSounds();
        setupListeners();
    }

    private void setupListeners() {
        listeners = new HashMap<Class<? extends Event>, EventListener>();
        EndOfGameEventListener endOfGameEventListener = new EndOfGameEventListener();
        listeners.put(EndOfGameEvent.class, endOfGameEventListener);
        listeners.put(EndOfMultiPlayerRoundEvent.class, endOfGameEventListener);
        listeners.put(PlayerInputEvent.class, new PlayerInputEventListener());
        listeners.put(LocalPlayerCollidedEvent.class, new LocalPlayerCollidedEventListener());
    }

    private void loadSounds() {
        playerDeathSound = loadSoundFromPath(EXPLOSION_PATH);
        playerSlideSound = loadSoundFromPath(SLIDE_PATH);
        playerSpeedUpSound = loadSoundFromPath(SPEED_UP_PATH);
        playerJumpSound = loadSoundFromPath(JUMP_PATH);
        playerStuckSound = loadSoundFromPath(STUCK_PATH);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(BACKGROUND_MUSIC_PATH));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(MUSIC_VOLUME);
    }

    private Sound loadSoundFromPath(String path) {
        return Gdx.audio.newSound(Gdx.files.internal(path));
    }

    public void registerListeners() {
        for (Class<? extends Event> eventClass : listeners.keySet()) {
            EventManager.getInstance().registerListener(eventClass, listeners.get(eventClass));
        }
    }

    public void unregisterListeners() {
        for (Class<? extends Event> eventClass : listeners.keySet()) {
            EventManager.getInstance().unregisterListener(eventClass, listeners.get(eventClass));
        }
    }

    public void playBackgroundMusic() {
        backgroundMusic.play();
    }
}
