package dev.rndmorris.tfixins.config.commands;

import static dev.rndmorris.tfixins.common.commands.ListResearchCommand.listOthersReserach;

import javax.annotation.Nonnull;

import dev.rndmorris.tfixins.config.BaseConfigModule;
import dev.rndmorris.tfixins.config.ConfigPhase;

public class CommandsModule extends BaseConfigModule {

    public final @Nonnull CommandSettings createNode;
    public final @Nonnull CommandSettings forgetResearch;
    public final @Nonnull CommandSettings forgetScanned;
    public final @Nonnull CommandSettings help;
    public final @Nonnull CommandSettings playerResearch;
    public final @Nonnull CommandSettings prerequisites;
    public final @Nonnull CommandSettings updateNode;

    public final CommandSettings[] commandsSettings;

    public CommandsModule() {
        commandsSettings = new CommandSettings[] {
            createNode = new CommandSettings("create-node", this, ConfigPhase.LATE).addDefaultAlias()
                .setDescription(
                    "Create a node at specified coordinates, optionally with specified brightness, type, and aspects.")
                .setPermissionLevel(2),
            forgetResearch = new CommandSettings("forget-research", this, ConfigPhase.LATE)
                .setDescription("Remove one or more research item's from a player's knowledge.")
                .addDefaultAlias()
                .setPermissionLevel(2),
            forgetScanned = new CommandSettings("forget-scanned", this, ConfigPhase.LATE)
                .setDescription("Remove entries from a player's list of scanned things")
                .addDefaultAlias()
                .setPermissionLevel(2),
            help = new CommandSettings("help", this, ConfigPhase.LATE).addAlias("tf-help")
                .setDescription("Get help information about Thaumic Fixins' commands.")
                .setPermissionLevel(0),
            playerResearch = new CommandSettings("list-research", this, ConfigPhase.LATE)
                .setDescription("List and filter through TC4's research.")
                .addDefaultAlias()
                .setPermissionLevel(0)
                .addChildPermissionLevel(listOthersReserach, 2, "list another player's research."),
            prerequisites = new CommandSettings("prereqs", this, ConfigPhase.LATE).addDefaultAlias()
                .setDescription(
                    "Lists the prerequisites to unlock a specific research, or the research required to craft a specific item.")
                .setPermissionLevel(0),
            updateNode = new CommandSettings("update-node", this, ConfigPhase.LATE).addDefaultAlias()
                .setDescription("Update the properties of a node at the specified coordiantes.")
                .setPermissionLevel(2) };

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
