package dev.rndmorris.salisarcana.common.commands;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.ResearchKeyHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.ResearchHelper;

public class CommandDumpResearch extends ArcanaCommandBase<CommandDumpResearch.Arguments> {

    public CommandDumpResearch() {
        super(ConfigModuleRoot.commands.dumpResearch);
    }

    @Override
    protected @Nonnull ArgumentProcessor<CommandDumpResearch.Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            CommandDumpResearch.Arguments.class,
            CommandDumpResearch.Arguments::new,
            new IArgumentHandler[] { ResearchKeyHandler.INSTANCE, FlagHandler.INSTANCE });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 1;
    }

    @Override
    protected void process(ICommandSender sender, CommandDumpResearch.Arguments arguments, String[] args) {
        for (String researchKey : arguments.researchKeys) {
            String json;
            try {
                json = ResearchHelper.dumpResearchToJson(researchKey);
            } catch (Exception e) {
                sender.addChatMessage(
                    new ChatComponentTranslation("salisarcana:commands.dump_research.failed", researchKey));
                continue;
            }

            File file = new File("config/salisarcana/research/" + researchKey + ".json");
            if (file.exists() && !arguments.overwrite) {
                continue;
            }

            try {
                Files.write(Paths.get(file.toURI()), json.getBytes());
            } catch (Exception e) {
                LOG.error(e);
                CommandErrors.generic();
                return;
            }

            sender.addChatMessage(
                new ChatComponentTranslation(
                    "salisarcana:commands.dump_research.success",
                    researchKey,
                    file.getPath()));
        }
    }

    public static class Arguments {

        @NamedArg(name = "--research", handler = ResearchKeyHandler.class)
        public ArrayList<String> researchKeys = new ArrayList<>();

        @FlagArg(name = "--overwrite")
        public boolean overwrite = false;
    }
}
