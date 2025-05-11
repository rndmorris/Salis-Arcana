package dev.rndmorris.salisarcana.config.settings;

import java.util.Arrays;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class EnumSetting<E extends Enum<E>> extends Setting {

    protected final Class<E> enumClass;
    protected final String name;
    protected final String comment;
    protected E value;

    public EnumSetting(IEnabler dependency, String name, String comment, @Nonnull E defaultValue) {
        super(dependency);
        this.name = name;
        enumClass = defaultValue.getDeclaringClass();
        value = defaultValue;

        final var sb = new StringBuilder();
        final var $vals = Arrays.stream(enumClass.getEnumConstants())
            .iterator();
        while ($vals.hasNext()) {
            sb.append(
                $vals.next()
                    .toString());
            if ($vals.hasNext()) {
                sb.append(", ");
            }
        }

        this.comment = comment + " Valid values: [" + sb + "]";
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        final var validValues = Arrays.stream(enumClass.getEnumConstants())
            .map(Enum::toString)
            .toArray(String[]::new);
        final var valueString = configuration.getString(name, getCategory(), value.toString(), comment, validValues);

        try {
            value = Enum.valueOf(enumClass, valueString);
        } catch (IllegalArgumentException e) {
            // Don't do anything, just use the default value
        }
    }
}
