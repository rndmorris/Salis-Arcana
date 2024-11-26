package dev.rndmorris.tfixins.config;

import static dev.rndmorris.tfixins.common.commands.ListResearchCommand.listOthersReserach;

import java.util.Collections;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

public class CommandsModule implements IConfigModule {

    private boolean enabled = true;

    public final @Nonnull CommandSettings createNode = new CommandSettings("create-node", this::isEnabled)
        .addDefaultAlias()
        .setDescription(
            "Create a node at specified coordinates, optionally with specified brightness, type, and aspects.")
        .setPermissionLevel(2);

    public final @Nonnull CommandSettings help = new CommandSettings("help", this::isEnabled).addAlias("tf-help")
        .setDescription("Get help information about Thaumic Fixin's commands.")
        .setPermissionLevel(0);

    public final @Nonnull CommandSettings playerResearch = new CommandSettings("list-research", this::isEnabled)
        .addDefaultAlias()
        .setPermissionLevel(0)
        .addChildPermissionLevel(listOthersReserach, 2, "list another player's research.");

    public final @Nonnull CommandSettings updateNode = new CommandSettings("update-node", this::isEnabled)
        .addDefaultAlias()
        .setDescription("Update the properties of a node at the specified coordiantes.")
        .setPermissionLevel(2);

    public final CommandSettings[] commandsSettings = new CommandSettings[] { createNode, help, playerResearch,
        updateNode, };

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
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        for (var settings : commandsSettings) {
            loadCommandSettings(configuration, settings);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void loadCommandSettings(Configuration configuration, CommandSettings settings) {
        var enabled = configuration.getBoolean(
            String.format("Enable %s command", settings.name),
            getModuleId(),
            settings.isEnabled(),
            settings.getDescription());

        settings.setEnabled(enabled);

        if (!settings.isEnabled()) {
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

        for (var childPermName : settings.childPermissionLevels.keySet()) {
            final var childPermissionLevel = configuration.getInt(
                String.format("Permission Level - %s", childPermName),
                category,
                (int) settings.childPermissionLevels.get(childPermName),
                0,
                4,
                String.format(
                    "The permission level required to %s",
                    settings.childPermissionDescription.get(childPermName)));
            settings.childPermissionLevels.put(childPermName, (byte) childPermissionLevel);
        }
    }

}
