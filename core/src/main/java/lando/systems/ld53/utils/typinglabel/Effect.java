package lando.systems.ld53.utils.typinglabel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

/** Abstract text effect. */
public abstract class Effect {
    private static final float       FADEOUT_SPLIT = 0.25f;
    protected final      TypingLabel label;
    public               int         indexStart    = -1;
    public               int         indexEnd      = -1;
    public               float       duration      = Float.POSITIVE_INFINITY;
    protected            float       totalTime;

    public Effect(TypingLabel label) {
        this.label = label;
    }

    public void update(float delta) {
        totalTime += delta;
    }

    /** Applies the effect to the given glyph. */
    public final void apply(TypingGlyph glyph, int glyphIndex, float delta) {
        int localIndex = glyphIndex - indexStart;
        onApply(glyph, localIndex, delta);
    }

    /** Called when this effect should be applied to the given glyph. */
    protected abstract void onApply(TypingGlyph glyph, int localIndex, float delta);

    /** Returns whether or not this effect is finished and should be removed. Note that effects are infinite by default. */
    public boolean isFinished() {
        return totalTime > duration;
    }

    /** Calculates the fadeout of this effect, if any. Only considers the second half of the duration. */
    protected float calculateFadeout() {
        if(Float.isInfinite(duration)) return 1;

        // Calculate raw progress
        float progress = MathUtils.clamp(totalTime / duration, 0, 1);

        // If progress is before the split point, return a full factor
        if(progress < FADEOUT_SPLIT) return 1;

        // Otherwise calculate from the split point
        return Interpolation.smooth.apply(1, 0, (progress - FADEOUT_SPLIT) / (1f - FADEOUT_SPLIT));
    }

    /**
     * Calculates a linear progress dividing the total time by the given modifier. Returns a value between 0 and 1 that
     * loops in a ping-pong mode.
     */
    protected float calculateProgress(float modifier) {
        return calculateProgress(modifier, 0, true);
    }

    /**
     * Calculates a linear progress dividing the total time by the given modifier. Returns a value between 0 and 1 that
     * loops in a ping-pong mode.
     */
    protected float calculateProgress(float modifier, float offset) {
        return calculateProgress(modifier, offset, true);
    }

    /** Calculates a linear progress dividing the total time by the given modifier. Returns a value between 0 and 1. */
    protected float calculateProgress(float modifier, float offset, boolean pingpong) {
        float progress = totalTime / modifier + offset;
        while(progress < 0.0f) {
            progress += 2.0f;
        }
        if(pingpong) {
            progress %= 2f;
            if(progress > 1.0f) progress = 1f - (progress - 1f);
        } else {
            progress %= 1.0f;
        }
        progress = MathUtils.clamp(progress, 0, 1);
        return progress;
    }

    /** Returns the line height of the label controlling this effect. */
    protected float getLineHeight() {
        return label.getBitmapFontCache().getFont().getLineHeight() * label.getFontScaleY();
    }

    /** Returns a float value parsed from the given String, or the default value if the string couldn't be parsed. */
    protected float paramAsFloat(String str, float defaultValue) {
        return Parser.stringToFloat(str, defaultValue);
    }

    /** Returns a boolean value parsed from the given String, or the default value if the string couldn't be parsed. */
    protected boolean paramAsBoolean(String str) {
        return Parser.stringToBoolean(str);
    }

    /** Parses a color from the given string. Returns null if the color couldn't be parsed. */
    protected Color paramAsColor(String str) {
        return Parser.stringToColor(str);
    }

}
