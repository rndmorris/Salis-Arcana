package dev.rndmorris.salisarcana.common.commands;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.FocusUpgradesHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.WandFocusHelper;
import thaumcraft.api.wands.FocusUpgradeType;

public class UpgradeFocusCommand extends ArcanaCommandBase<UpgradeFocusCommand.Arguments> {

    public UpgradeFocusCommand() {
        super(SalisConfig.commands.upgradeFocus);
    }

    @Nonnull
    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { FocusUpgradesHandler.INSTANCE, PlayerHandler.INSTANCE, });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 0;
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        final var executingPlayer = CommandBase.getCommandSenderAsPlayer(sender);
        if (arguments.player == null) {
            arguments.player = executingPlayer;
        }

        if (arguments.upgrades == null || arguments.upgrades.size() < 1) {
            throw new CommandException("salisarcana:command.upgrade-focus.noUpgrades");
        }

        final var heldItem = arguments.player.getCurrentEquippedItem();
        final var heldFocus = WandFocusHelper.getFocusFrom(heldItem);
        if (heldItem == null || heldFocus == null) {
            throw new CommandException("salisarcana:command.upgrade-focus.noItem");
        }

        final var appliedUpgrades = WandFocusHelper.getAppliedUpgrades(heldFocus, heldItem);
        var applied = 0;
        for (var upgrade : arguments.upgrades) {
            if (appliedUpgrades.size() == 5) {
                throw new CommandException(
                    "salisarcana:command.upgrade-focus.tooManyUpgrades",
                    arguments.upgrades.size() - applied);
            }
            appliedUpgrades.add(upgrade);
            heldFocus.applyUpgrade(heldItem, upgrade, appliedUpgrades.size());
            applied += 1;
        }

        sender.addChatMessage(new ChatComponentTranslation("salisarcana:command.upgrade-focus.success"));
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = FocusUpgradesHandler.class, descLangKey = "upgrades")
        public ArrayList<FocusUpgradeType> upgrades;

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP player;

    }
}
