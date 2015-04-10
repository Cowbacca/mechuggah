package uk.ac.rhul.cs.zwac076.mechuggah.actor.component;


import uk.ac.rhul.cs.zwac076.mechuggah.actor.CollisionActor;

public abstract class ElevationComponentFactory {
    public static ElevationComponentFactory newFactory() {
        return new DefaultElevationComponentFactory();
    }

    public abstract ElevationComponent createElevationComponent(float upXScale, float
            upYScale, CollisionActor collisionActor);

    protected static class DefaultElevationComponentFactory extends ElevationComponentFactory {
        @Override
        public ElevationComponent createElevationComponent(float upXScale, float upYScale,
                                                           CollisionActor collisionActor) {
            return new DefaultElevationComponent(upXScale, upYScale, collisionActor);
        }
    }
}
