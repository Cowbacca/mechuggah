package uk.ac.rhul.cs.zwac076.mechuggah.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.Pool.Poolable;

@AllArgsConstructor
public class PoolableNoLongerOnScreenEvent extends Event {
    @Getter
    private Poolable poolable;
}
