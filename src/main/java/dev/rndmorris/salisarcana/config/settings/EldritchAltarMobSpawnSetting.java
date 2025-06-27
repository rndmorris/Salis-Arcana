package dev.rndmorris.salisarcana.config.settings;

import java.util.Random;

import net.minecraft.util.MathHelper;

import dev.rndmorris.salisarcana.config.IEnabler;

public class EldritchAltarMobSpawnSetting extends EnumSetting<EldritchAltarMobSpawnSetting.Options> {

    public EldritchAltarMobSpawnSetting(IEnabler dependency, String name, String comment) {
        super(dependency, name, comment, Options.DEFAULT, Options.DEFAULT);
    }

    public int randomHorizontal(Random random) {
        return switch (value) {
            case EVEN_SPREAD -> horizontalEven(random);
            case CENTER_WEIGHTED -> horizontalWeighted(random);
            default -> throw new RuntimeException(
                String.format(
                    "%s called while its mixin should be disabled.",
                    EldritchAltarMobSpawnSetting.class.getName()));
        };
    }

    public int randomVertical(Random random) {
        return switch (value) {
            case EVEN_SPREAD -> verticalEven(random);
            case CENTER_WEIGHTED -> verticalWeighted(random);
            default -> throw new RuntimeException(
                String.format(
                    "%s called while its mixin should be disabled.",
                    EldritchAltarMobSpawnSetting.class.getName()));
        };
    }

    private int horizontalEven(Random random) {
        return (MathHelper.getRandomIntegerInRange(random, 0, 6) + 4) * (random.nextBoolean() ? 1 : -1);
    }

    private int verticalEven(Random random) {
        return MathHelper.getRandomIntegerInRange(random, -3, 3);
    }

    private int horizontalWeighted(Random random) {
        final var val = MathHelper.getRandomIntegerInRange(random, 0, 6)
            - MathHelper.getRandomIntegerInRange(random, 0, 6);
        if (val == 0) {
            return val + (4 * (random.nextBoolean() ? 1 : -1));
        }
        if (val < 0) {
            return val - 4;
        }
        return val + 4;
    }

    private int verticalWeighted(Random random) {
        return MathHelper.getRandomIntegerInRange(random, 0, 3) - MathHelper.getRandomIntegerInRange(random, 0, 3);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && value != Options.DEFAULT;
    }

    public enum Options {
        DEFAULT,
        EVEN_SPREAD,
        CENTER_WEIGHTED
    }
}
