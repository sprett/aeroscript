package no.uio.aeroscript.type;

import java.util.Random;

public class Range {
    private final float min;
    private final float max;
    private final Random random;

    public Range(float min, float max) {
        this.min = min;
        this.max = max;
        this.random = new Random();
    }

    public float getRandomValue() {
        return min + random.nextFloat() * (max - min);
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "[" + min + ", " + max + "]";
    }
}
