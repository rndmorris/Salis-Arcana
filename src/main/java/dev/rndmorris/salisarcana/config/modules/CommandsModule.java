package dev.rndmorris.salisarcana.config.modules;

import static dev.rndmorris.salisarcana.common.commands.ListResearchCommand.listOthersReserach;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public class CommandsModule extends BaseConfigModule {

    public final @Nonnull CommandSettings createNode;
    public final @Nonnull CommandSettings forgetResearch;
    public final @Nonnull CommandSettings forgetScanned;
    public final @Nonnull CommandSettings help;
    public final @Nonnull CommandSettings infusionSymmetry;
    public final @Nonnull CommandSettings playerResearch;
    public final @Nonnull CommandSettings prerequisites;
    public final @Nonnull CommandSettings updateNode;
    public final @Nonnull CommandSettings upgradeFocus;
    public final @Nonnull CommandSettings exportResearch;

    public final CommandSettings[] commandsSettings;

    public CommandsModule() {
        commandsSettings = new CommandSettings[] {
            createNode = new CommandSettings("create-node", this).addDefaultAlias()
                .setDescription(
                    "Create a node at specified coordinates, optionally with specified brightness, type, and aspects.")
                .setPermissionLevel(2),
            forgetResearch = new CommandSettings("forget-research", this)
                .setDescription("Remove one or more research item's from a player's knowledge.")
                .addDefaultAlias()
                .setPermissionLevel(2),
            forgetScanned = new CommandSettings("forget-scanned", this)
                .setDescription("Remove entries from a player's list of scanned things")
                .addDefaultAlias()
                .setPermissionLevel(2),
            help = new CommandSettings("help", this).addAlias("arcana-help")
                .setDescription("Get help information about Salis Arcana's commands.")
                .setPermissionLevel(0),
            infusionSymmetry = new CommandSettings("infusion-symmetry", this)
                .setDescription(
                    "Get the symmetry of the nearest runic matrix within 8 blocks, or at the specified coordinates.")
                .addDefaultAlias()
                .setPermissionLevel(0),
            playerResearch = new CommandSettings("list-research", this)
                .setDescription("List and filter through TC4's research.")
                .addDefaultAlias()
                .setPermissionLevel(0)
                .addChildPermissionLevel(
                    listOthersReserach,
                    2,
                    "The permission level required to list another player's research."),
            prerequisites = new CommandSettings("prereqs", this).addDefaultAlias()
                .setDescription(
                    "Lists the prerequisites to unlock a specific research, or the research required to craft a specific item.")
                .setPermissionLevel(0),
            updateNode = new CommandSettings("update-node", this).addDefaultAlias()
                .setDescription("Update the properties of a node at the specified coordinates.")
                .setPermissionLevel(2),
            upgradeFocus = new CommandSettings("upgrade-focus", this).addDefaultAlias()
                .setPermissionLevel(2),
            exportResearch = new CommandSettings("export-research", this).addDefaultAlias()
                .setPermissionLevel(0) };

        addSettings(commandsSettings);
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
}
