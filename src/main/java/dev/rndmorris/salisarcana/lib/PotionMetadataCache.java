package dev.rndmorris.salisarcana.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public final class PotionMetadataCache<T> {

    public static final int ALL_METADATA_BITS = 32767; // 2^15-1
    private static final int SPLASH_BIT = 1 << 14;

    private final int metadataMask;
    private final Map<Integer, T> values = new HashMap<>();

    public PotionMetadataCache(int metadataMask) {
        this.metadataMask = metadataMask;
    }

    public T get(int metadata, IntFunction<T> loader) {
        // Metadata values that differ only in irrelevant bits produce identical effects.
        int key = metadata & metadataMask;
        if (!values.containsKey(key)) {
            values.put(key, loader.apply(metadata));
        }
        return values.get(key);
    }

    public static int findRelevantBits(Iterable<String> requirements, Iterable<String> amplifiers) {
        // getPotionEffects reads the 15th metadata bit to handle splash effects.
        int relevantBits = addExpressionBits(SPLASH_BIT, requirements);

        if (relevantBits == ALL_METADATA_BITS) return relevantBits;

        return addExpressionBits(relevantBits, amplifiers);
    }

    private static int addExpressionBits(int relevantBits, Iterable<String> expressions) {
        for (String expression : expressions) {
            int index = 0;
            while (index < expression.length()) {
                char token = expression.charAt(index);

                // comparisons inspect the total number of set bits, making every bit relevant.
                if (token == '=' || token == '<' || token == '>') return ALL_METADATA_BITS;

                if (isAsciiDigit(token)) {
                    int numberStart = index;
                    int tokenValue = 0;
                    // Vanilla uses only 0-6 here, but mods may reference metadata bits 10-14.
                    while (index < expression.length() && isAsciiDigit(expression.charAt(index))) {
                        tokenValue = tokenValue * 10 + (expression.charAt(index) - '0');
                        index++;
                    }

                    // A number preceded by '*' is a factor, not a bit index: (index*factor)
                    // Vanilla does not use it but its expression parser supports so do we
                    if (isMultiplicationFactor(expression, numberStart)) continue;

                    int bitIndex = tokenValue;
                    // Match PotionHelper's shift, discarding bits outside potion metadata.
                    relevantBits |= (1 << bitIndex) & ALL_METADATA_BITS;
                    continue;
                }

                if (!Character.isWhitespace(token) && !isExpressionOperator(token)) {
                    // Unknown syntax cannot be reduced safely, so disable the optimization.
                    return ALL_METADATA_BITS;
                }
                index++;
            }
        }
        return relevantBits;
    }

    private static boolean isMultiplicationFactor(String expression, int numberStart) {
        // The factor follows '*', so look backward past optional whitespace to classify this number.
        int previous = numberStart - 1;
        while (previous >= 0 && Character.isWhitespace(expression.charAt(previous))) previous--;
        return previous >= 0 && expression.charAt(previous) == '*';
    }

    private static boolean isExpressionOperator(char token) {
        return "|&!*-+".indexOf(token) >= 0;
    }

    private static boolean isAsciiDigit(char character) {
        return character >= '0' && character <= '9';
    }
}
