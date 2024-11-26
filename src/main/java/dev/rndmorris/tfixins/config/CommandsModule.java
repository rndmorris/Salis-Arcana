package dev.rndmorris.tfixins.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

public class CommandsModule implements IConfigModule {

    public final @Nonnull CommandSettings createNode = new CommandSettings("create-node")
        .setDescription(
            "Create a node at specified coordinates, optionally with specified brightness, type, and aspects.")
        .setPermissionLevel(2);

    public final @Nonnull CommandSettings updateNode = new CommandSettings("update-node")
        .setDescription("Update the properties of a node at the specified coordiantes.")
        .setPermissionLevel(2);

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "commands";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Helper and admin commands.";
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        loadCommandSettings(configuration, createNode);
    }

    private void loadCommandSettings(Configuration configuration, CommandSettings settings) {
        settings.enabled = configuration.getBoolean(
            String.format("Enable %s command", settings.name),
            getModuleId(),
            settings.enabled,
            settings.getDescription());

        if (!settings.enabled) {
            return;
        }

        final var category = String.format("%s_%s", getModuleId(), settings.name.replace('-', '_'));
        configuration.setCategoryComment(category, settings.getDescription());

        final var aliases = configuration.getStringList(
            "Aliases",
            category,
            settings.aliases.toArray(new String[0]),
            "Secondary names that refer to this command.");
        settings.aliases.clear();
        Collections.addAll(settings.aliases, aliases);

        final var permissionLevel = configuration.getInt(
            "Permission Level",
            category,
            settings.getPermissionLevel(),
            0,
            4,
            "The permission level required to execute the command.");
        settings.setPermissionLevel(permissionLevel);
    }

    public static class CommandSettings {

        public final Set<String> aliases = new HashSet<>();
        public boolean enabled = true;
        public final @Nonnull String name;

        private @Nonnull String description = "";
        private int permissionLevel = 4;

        public CommandSettings(@Nonnull String name) {
            this.name = name;
            aliases.add(name);
        }

        public @Nonnull String getDescription() {
            return description;
        }

        public int getPermissionLevel() {
            return permissionLevel;
        }

        public CommandSettings setDescription(@Nonnull String description) {
            this.description = description;
            return this;
        }

        public CommandSettings setPermissionLevel(int permissionLevel) {
            this.permissionLevel = permissionLevel;
            return this;
        }
    }
}
