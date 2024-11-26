package dev.rndmorris.tfixins.common.commands;

import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.config.FixinsConfig;

public class ForgetScannedCommand extends FixinsCommandBase<ForgetScannedCommand.Arguments> {

    public ForgetScannedCommand() {
        super(FixinsConfig.commandsModule.forgetScanned);
    }

    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return null;
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {

    }

    public static class Arguments {

    }
}
