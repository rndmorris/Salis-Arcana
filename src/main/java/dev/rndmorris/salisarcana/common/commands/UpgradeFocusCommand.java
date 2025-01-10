package dev.rndmorris.salisarcana.common.commands;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.FociUpgradesHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;
import net.minecraft.command.ICommandSender;

import javax.annotation.Nonnull;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

public class UpgradeFocusCommand extends ArcanaCommandBase<UpgradeFocusCommand.Arguments>{

    public UpgradeFocusCommand() {
        super(ConfigModuleRoot.commands.upgradeFocus);
    }

    @Nonnull
    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(Arguments.class, Arguments::new, new IArgumentHandler[] {
            FociUpgradesHandler.INSTANCE,
        });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 0;
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        LOG.info("Test! {}", arguments.upgrade);
    }

    public static class Arguments {
        @NamedArg(name = "--upgrade", handler = FociUpgradesHandler.class, excludes = {"--upgrade"})
        int upgrade = -1;
    }
}
