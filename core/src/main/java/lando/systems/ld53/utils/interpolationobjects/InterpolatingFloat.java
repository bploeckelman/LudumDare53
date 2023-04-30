package lando.systems.ld53.utils.interpolationobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

/**
 * This class should be used if you need a float that you want to interpolate between
 * often and the Tween Engine won't work, for instance needs to be reset before the old completes
 *
 */
public class InterpolatingFloat {
    private float value;
    private float currentTimer;
    private float duration;
    private Interpolation interpolation;
    private float oldValue;
    private float newValue;

    public InterpolatingFloat(float startingValue) {
        this.currentTimer = 0;
        this.value = startingValue;
        this.newValue = startingValue;
        this.oldValue = startingValue;
        this.duration = 1;
        this.interpolation = Interpolation.linear;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }


    public void update(float dt) {
        currentTimer += dt;
        float percent = MathUtils.clamp(currentTimer/duration, 0, 1f);
        value = interpolation.apply(oldValue, newValue, percent);
    }

    public float getValue() {
        return value;
    }

    public void setNewValue(float v, float duration) {
        if (duration <= 0) {
            Gdx.app.log("InterpolatingFloat","duration = 0. Do NOT do this, setting to .01");
            duration = .01f;
        }
        this.duration = duration;
        this.currentTimer = 0;
        this.oldValue = value;
        this.newValue = v;
    }
}
