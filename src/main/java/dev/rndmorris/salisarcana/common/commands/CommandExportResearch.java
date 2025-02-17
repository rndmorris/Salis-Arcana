package dev.rndmorris.salisarcana.common.commands;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.ResearchHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import dev.rndmorris.salisarcana.lib.customresearch.ResearchEntry;
import thaumcraft.api.research.ResearchItem;

public class CommandExportResearch extends ArcanaCommandBase<CommandExportResearch.Arguments> {

    public CommandExportResearch() {
        super(ConfigModuleRoot.commands.exportResearch);
    }

    @Override
    protected @Nonnull ArgumentProcessor<CommandExportResearch.Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            CommandExportResearch.Arguments.class,
            CommandExportResearch.Arguments::new,
            new IArgumentHandler[] { ResearchHandler.INSTANCE, FlagHandler.INSTANCE });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 1;
    }

    @Override
    protected void process(ICommandSender sender, CommandExportResearch.Arguments arguments, String[] args) {
        Path path = Paths.get("config", "salisarcana", "research", "export");
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                LOG.error(e);
                CommandErrors.generic();
                return;
            }
        }
        for (ResearchItem research : arguments.researches) {
            String researchKey = research.key;
            File file = Paths.get("config", "salisarcana", "research", "export", researchKey + ".json")
                .toFile();
            if (file.exists() && !arguments.overwrite) {
                continue;
            }
            try {
                ResearchHelper.exportResearchToJson(new ResearchEntry(research), file);
            } catch (Exception e) {
                LOG.error("Failed to export research: {}", researchKey, e);
                sender.addChatMessage(
                    new ChatComponentTranslation("salisarcana:commands.export-research.failed", researchKey));
                continue;
            }
            sender.addChatMessage(
                new ChatComponentTranslation(
                    "salisarcana:commands.export-research.success",
                    researchKey,
                    file.getPath()));
        }
    }

    public static class Arguments {

        @NamedArg(name = "--research", handler = ResearchHandler.class)
        public ArrayList<ResearchItem> researches = new ArrayList<>();

        @FlagArg(name = "--overwrite")
        public boolean overwrite = false;
    }
}
