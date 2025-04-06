package dev.rndmorris.salisarcana.config.group;

import static dev.rndmorris.salisarcana.common.commands.ListResearchCommand.listOthersReserach;

import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public class ConfigCommands extends ConfigGroup {

    public Stream<CommandSettings> getCommandsSettings() {
        return this.settings.stream()
            .map(s -> s instanceof CommandSettings c ? c : null)
            .filter(Objects::nonNull);
    }

    public final CommandSettings createNode = new CommandSettings("create-node", this).addDefaultAlias()
        .setDescription(
            "Create a node at specified coordinates, optionally with specified brightness, type, and aspects.")
        .setPermissionLevel(2);

    public final CommandSettings forgetResearch = new CommandSettings("forget-research", this)
        .setDescription("Remove one or more research item's from a player's knowledge.")
        .addDefaultAlias()
        .setPermissionLevel(2);

    public final CommandSettings forgetScanned = new CommandSettings("forget-scanned", this)
        .setDescription("Remove entries from a player's list of scanned things")
        .addDefaultAlias()
        .setPermissionLevel(2);

    public final CommandSettings forgetAspects = new CommandSettings("forget-aspect", this)
        .setDescription("Forget one or more aspects from a player's knowledge.")
        .addDefaultAlias()
        .setPermissionLevel(2);

    public final CommandSettings help = new CommandSettings("help", this).addAlias("arcana-help")
        .setDescription("Get help information about Salis Arcana's commands.")
        .setPermissionLevel(0);

    public final CommandSettings infusionSymmetry = new CommandSettings("infusion-symmetry", this)
        .setDescription(
            "Get the symmetry of the nearest runic matrix within 8 blocks, or at the specified coordinates.")
        .addDefaultAlias()
        .setPermissionLevel(0);

    public final CommandSettings playerResearch = new CommandSettings("list-research", this)
        .setDescription("List and filter through TC4's research.")
        .addDefaultAlias()
        .setPermissionLevel(0)
        .addChildPermissionLevel(
            listOthersReserach,
            2,
            "The permission level required to list another player's research.");

    public final CommandSettings prerequisites = new CommandSettings("prereqs", this).addDefaultAlias()
        .setDescription(
            "Lists the prerequisites to unlock a specific research, or the research required to craft a specific item.")
        .setPermissionLevel(0);

    public final CommandSettings updateNode = new CommandSettings("update-node", this).addDefaultAlias()
        .setDescription("Update the properties of a node at the specified coordinates.")
        .setPermissionLevel(2);

    public final CommandSettings upgradeFocus = new CommandSettings("upgrade-focus", this).addDefaultAlias()
        .setPermissionLevel(2);

    @Nonnull
    @Override
    public String getGroupName() {
        return "commands";
    }

    @Nonnull
    @Override
    public String getGroupComment() {
        return "Helper and admin commands.";
    }
}
