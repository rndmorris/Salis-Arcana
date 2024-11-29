package dev.rndmorris.tfixins.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.common.commands.FixinsCommandBase;

public class CommandSettings extends Setting {

    public final Set<String> aliases = new HashSet<>();
    public final Map<String, String> childPermissionDescription = new TreeMap<>();
    public final Map<String, Byte> childPermissionLevels = new TreeMap<>();
    public final @Nonnull String name;

    private @Nullable Supplier<FixinsCommandBase<?>> commandGetter;
    private @Nonnull String description = "";
    private byte permissionLevel = 4;

    public CommandSettings(@Nonnull String name, Supplier<IConfigModule> parentModule) {
        super(parentModule);
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
}
