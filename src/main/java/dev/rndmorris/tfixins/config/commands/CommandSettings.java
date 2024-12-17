package dev.rndmorris.tfixins.config.commands;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.common.commands.FixinsCommandBase;
import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;

public class CommandSettings extends Setting {

    public final Set<String> aliases = new HashSet<>();
    public final Map<String, String> childPermissionDescription = new TreeMap<>();
    public final Map<String, Byte> childPermissionLevels = new TreeMap<>();
    public final @Nonnull String name;

    private @Nullable Supplier<FixinsCommandBase<?>> commandGetter;
    private @Nonnull String description = "";
    private byte permissionLevel = 4;

    public CommandSettings(@Nonnull String name, WeakReference<IConfigModule> parentModule, ConfigPhase phase) {
        super(parentModule, phase);
        this.name = name;
    }

    public CommandSettings addAlias(String alias) {
        aliases.add(alias);
        return this;
    }

    public CommandSettings addChildPermissionLevel(String name, int level, String description) {
        childPermissionLevels.put(name, (byte) level);
        childPermissionDescription.put(name, description);
        return this;
    }

    public CommandSettings addDefaultAlias() {
        aliases.add(name);
        return this;
    }

    public @Nullable FixinsCommandBase<?> getCommand() {
        return commandGetter != null ? commandGetter.get() : null;
    }

    public @Nonnull String getDescription() {
        return description;
    }

    public @Nonnull String getFullName() {
        return String.format("%s-%s", ThaumicFixins.MODID, name);
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setCommandGetter(@Nullable Supplier<FixinsCommandBase<?>> commandGetter) {
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
        IConfigModule module;
        if ((module = moduleRef.get()) == null) {
            return;
        }

        enabled = configuration
            .getBoolean(String.format("Enable %s command", name), module.getModuleId(), enabled, description);

        if (!enabled) {
            return;
        }

        final var category = String.format("%s_%s", module.getModuleId(), name.replace('-', '_'));
        configuration.setCategoryComment(category, description);

        final var configAliases = configuration.getStringList(
            "Aliases",
            category,
            aliases.toArray(new String[0]),
            "Secondary names that refer to this command.");
        aliases.clear();
        Collections.addAll(aliases, configAliases);

        permissionLevel = (byte) configuration.getInt(
            "Permission Level",
            category,
            permissionLevel,
            0,
            4,
            "The permission level required to execute the command.");

        for (var childPermName : childPermissionLevels.keySet()) {
            final var childPermissionLevel = configuration.getInt(
                String.format("Permission Level - %s", childPermName),
                category,
                (int) childPermissionLevels.get(childPermName),
                0,
                4,
                String.format("The permission level required to %s", childPermissionDescription.get(childPermName)));
            childPermissionLevels.put(childPermName, (byte) childPermissionLevel);
        }
    }
}
