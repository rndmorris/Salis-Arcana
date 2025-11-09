package dev.rndmorris.salisarcana.config.settings;

import java.util.Random;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.lib.IntegerHelper;

public class ProbabilitySetting extends Setting {

    private final String name;
    private final String comment;
    private int numerator;
    private int denominator;

    public ProbabilitySetting(IEnabler dependency, String name, String comment, int antecedent, int consequent) {
        super(dependency);
        if (antecedent < 0) {
            throw new RuntimeException("Numerator must not be negative. Received: " + antecedent);
        }
        if (consequent < 0) {
            throw new RuntimeException("Denominator must not be negative. Received: " + consequent);
        }
        this.name = name;
        this.comment = comment;
        this.numerator = antecedent;
        this.denominator = consequent;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        final var stringVal = configuration
            .getString(name, getCategory(), String.format("%d/%d", numerator, denominator), comment)
            .trim();

        final var success = tryParseZeroOrOne(stringVal) || tryParseFraction(stringVal);

        if (!success) {
            SalisArcana.LOG.error("Unrecognized value \"{}\" for setting \"{}\"", stringVal, name);
        }
    }

    private boolean tryParseZeroOrOne(String stringVal) {
        if ("0".equalsIgnoreCase(stringVal)) {
            numerator = 0;
            denominator = 0;
            return true;
        }
        if ("1".equalsIgnoreCase(stringVal)) {
            numerator = 1;
            denominator = 1;
            return true;
        }
        return false;
    }

    private boolean tryParseFraction(String value) {
        final var firstIndex = value.indexOf("/");
        final var lastIndex = value.lastIndexOf("/");
        // if it's not found at all, or it occurs more than once, or if it's at the end of the string
        if (firstIndex < 0 || firstIndex != lastIndex || lastIndex == value.length() - 1) {
            return false;
        }
        final var numerator = IntegerHelper.tryParse(
            value.substring(0, firstIndex)
                .trim());
        final var denominator = IntegerHelper.tryParse(
            value.substring(firstIndex + 1)
                .trim());
        if (numerator == null || denominator == null) {
            return false;
        }
        if (numerator < 0) {
            SalisArcana.LOG
                .error("Error parsing setting {}. Numerator must not be negative. Received: {}.", name, numerator);
            return true;
        }
        if (denominator < 0) {
            SalisArcana.LOG
                .error("Error parsing setting {}. Denominator must not be negative. Received: {}.", name, denominator);
            return true;
        }

        this.numerator = numerator;
        this.denominator = denominator;

        return true;
    }

    @Override
    public boolean isEnabled() {
        return numerator > 0 && super.isEnabled();
    }

    public boolean roll(Random rand) {
        if (!isEnabled()) {
            return false;
        }
        if (numerator >= denominator) {
            return true;
        }
        return rand.nextInt(denominator) <= numerator - 1;
    }
}
