package dev.rndmorris.salisarcana.config.settings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.common.commands.ArcanaCommandBase;
import dev.rndmorris.salisarcana.config.IEnabler;

public class CommandSettings extends Setting {

    public final Set<String> aliases = new HashSet<>();
    public final @Nonnull String name;

    private final Map<String, ChildPermission> childPermissions = new TreeMap<>();

    private @Nullable Supplier<ArcanaCommandBase<?>> commandGetter;
    private @Nonnull String description = "";
    private byte permissionLevel = 4;

    public CommandSettings(@Nonnull String name, IEnabler dependency) {
        super(dependency);
        this.name = name;
        setCategory(name.replace('-', '_'));
    }

    public CommandSettings addAlias(String alias) {
        aliases.add(alias);
        return this;
    }

    public CommandSettings addChildPermissionLevel(String configKey, int level, String description) {
        final var permission = new ChildPermission();
        permission.permissionLevel = (byte) level;
        permission.description = description;
        childPermissions.put(configKey, permission);
        return this;
    }

    public CommandSettings addDefaultAlias() {
        aliases.add(name);
        return this;
    }

    public @Nullable ArcanaCommandBase<?> getCommand() {
        return commandGetter != null ? commandGetter.get() : null;
    }

    public @Nonnull String getDescription() {
        return description;
    }

    public @Nonnull String getFullName() {
        return String.format("%s-%s", SalisArcana.MODID, name);
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setCommandGetter(@Nullable Supplier<ArcanaCommandBase<?>> commandGetter) {
        this.commandGetter = commandGetter;
    }

    public CommandSettings setDescription(@Nonnull String description) {
        this.description = description;
        return this;
    }

    public CommandSettings setPermissionLevel(int permissionLevel) {
        this.permissionLevel = (byte) permissionLevel;
        return this;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        configuration.setCategoryComment(getCategory(), String.format("/%s | %s", getFullName(), getDescription()));

        enabled = configuration.getBoolean("commandEnabled", getCategory(), enabled, "Enable or disable the command.");

        final var configAliases = configuration.getStringList(
            "aliases",
            getCategory(),
            aliases.toArray(new String[0]),
            "Secondary names that refer to this command.");
        aliases.clear();
        Collections.addAll(aliases, configAliases);

        permissionLevel = (byte) configuration.getInt(
            "permissionLevel",
            getCategory(),
            permissionLevel,
            0,
            4,
            "The permission level required to execute the command.");

        for (var childConfig : childPermissions.keySet()) {
            final var childPerm = childPermissions.get(childConfig);
            final var configLevel = configuration
                .getInt(childConfig, getCategory(), childPerm.permissionLevel, 0, 4, childPerm.description);
            childPerm.permissionLevel = (byte) configLevel;
        }
    }

    public byte getChildPermissionLevel(String configName) {
        final var childPerm = childPermissions.get(configName);
        if (childPerm == null) {
            return Byte.MAX_VALUE;
        }
        return childPerm.permissionLevel;
    }

    private static class ChildPermission {

        public String description;
        public byte permissionLevel;
    }
}
