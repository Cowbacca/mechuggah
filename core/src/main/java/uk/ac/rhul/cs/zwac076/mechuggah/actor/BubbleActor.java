package uk.ac.rhul.cs.zwac076.mechuggah.actor;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BubbleActor extends Actor {

    private static final float DEFAULT_Y_SPEED = 10;
    private static final float DEFAULT_X_SPEED = 5;
    private float ySpeed;
    private Random random;
    private float xSpeed;
    private TextureRegion texture;
    private float yBounds;

    public BubbleActor(TextureRegion texture, float x, float y, Random random, float yBounds) {
        setBounds(x, y, texture.getRegionWidth(), texture.getRegionHeight());
        this.random = random;
        this.texture = texture;
        this.yBounds = yBounds;
        ySpeed = DEFAULT_Y_SPEED;
        xSpeed = DEFAULT_X_SPEED;
    }

    @Override
    public void act(float delta) {
        setY(getY() + ySpeed * random.nextFloat());
        if (getY() > yBounds) {
            setY(-texture.getRegionHeight());
        }

        float speedModifier = random.nextFloat() * 2 - 1;
        setX((getX() + speedModifier * xSpeed));
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }
}
