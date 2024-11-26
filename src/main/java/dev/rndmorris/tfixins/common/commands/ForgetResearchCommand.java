package dev.rndmorris.tfixins.common.commands;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.config.FixinsConfig;
import net.minecraft.command.ICommandSender;

public class ForgetResearchCommand extends FixinsCommandBase<ForgetResearchCommand.Arguments> {

    public ForgetResearchCommand() {
        super(FixinsConfig.commandsModule.forgetResearch);
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
