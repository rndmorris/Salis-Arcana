package dev.rndmorris.tfixins.config.commands;

import static dev.rndmorris.tfixins.common.commands.ListResearchCommand.listOthersReserach;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;

public class CommandsModule implements IConfigModule {

    private boolean enabled = true;

    public final @Nonnull CommandSettings createNode;
    public final @Nonnull CommandSettings forgetResearch;
    public final @Nonnull CommandSettings forgetScanned;
    public final @Nonnull CommandSettings help;
    public final @Nonnull CommandSettings playerResearch;
    public final @Nonnull CommandSettings prerequisites;
    public final @Nonnull CommandSettings updateNode;

    public final CommandSettings[] commandsSettings;

    public CommandsModule() {
        final var thisRef = new WeakReference<IConfigModule>(this);
        commandsSettings = new CommandSettings[] {
            createNode = new CommandSettings("create-node", thisRef, ConfigPhase.LATE).addDefaultAlias()
                .setDescription(
                    "Create a node at specified coordinates, optionally with specified brightness, type, and aspects.")
                .setPermissionLevel(2),
            forgetResearch = new CommandSettings("forget-research", thisRef, ConfigPhase.LATE)
                .setDescription("Remove one or more research item's from a player's knowledge.")
                .addDefaultAlias()
                .setPermissionLevel(2),
            forgetScanned = new CommandSettings("forget-scanned", thisRef, ConfigPhase.LATE)
                .setDescription("Remove entries from a player's list of scanned things")
                .addDefaultAlias()
                .setPermissionLevel(2),
            help = new CommandSettings("help", thisRef, ConfigPhase.LATE).addAlias("tf-help")
                .setDescription("Get help information about Thaumic Fixins' commands.")
                .setPermissionLevel(0),
            playerResearch = new CommandSettings("list-research", thisRef, ConfigPhase.LATE)
                .setDescription("List and filter through TC4's research.")
                .addDefaultAlias()
                .setPermissionLevel(0)
                .addChildPermissionLevel(listOthersReserach, 2, "list another player's research."),
            prerequisites = new CommandSettings("prereqs", thisRef, ConfigPhase.LATE).addDefaultAlias()
                .setDescription(
                    "Lists the prerequisites to unlock a specific research, or the research required to craft a specific item.")
                .setPermissionLevel(0),
            updateNode = new CommandSettings("update-node", thisRef, ConfigPhase.LATE).addDefaultAlias()
                .setDescription("Update the properties of a node at the specified coordiantes.")
                .setPermissionLevel(2) };
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
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration, ConfigPhase phase) {
        for (var settings : commandsSettings) {
            if (settings.phase == phase) {
                settings.loadFromConfiguration(configuration);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
