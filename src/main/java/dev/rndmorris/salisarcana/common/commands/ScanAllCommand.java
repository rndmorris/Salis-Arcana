package dev.rndmorris.salisarcana.common.commands;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.oredict.OreDictionary;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;

public class ScanAllCommand extends ArcanaCommandBase<ScanAllCommand.Arguments> {

    public ScanAllCommand() {
        super(SalisConfig.commands.scanAll);
    }

    @Nonnull
    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            ScanAllCommand.Arguments.class,
            ScanAllCommand.Arguments::new,
            new IArgumentHandler[] { PlayerHandler.INSTANCE });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 0;
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        if (arguments.targetPlayer == null) {
            arguments.targetPlayer = getCommandSenderAsPlayer(sender);
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;
        String playerName = player.getCommandSenderName();

        // code from
        // https://github.com/SpitefulFox/Unthaumic/blob/master/src/main/java/fox/spiteful/unthaumic/Unthaumic.java
        // under the WTFPL license

        for (Aspect aspect : Aspect.getCompoundAspects()) {
            Thaumcraft.proxy.playerKnowledge.addDiscoveredAspect(playerName, aspect);
        }
        for (List<?> list : ThaumcraftApi.objectTags.keySet()) {
            if (list.size() == 2 && list.get(0) instanceof Item item) {
                if (list.get(1) instanceof Integer) {
                    int meta = (Integer) list.get(1);
                    if (meta == OreDictionary.WILDCARD_VALUE) {
                        for (meta = 0; meta < 16; meta++) {
                            ResearchManager.completeScannedObjectUnsaved(
                                playerName,
                                "@" + ScanManager.generateItemHash(item, meta));
                        }
                    } else ResearchManager
                        .completeScannedObjectUnsaved(playerName, "@" + ScanManager.generateItemHash(item, meta));
                } else if (list.get(1) instanceof int[]metas) {
                    for (int meta : metas) {
                        ResearchManager
                            .completeScannedObjectUnsaved(playerName, "@" + ScanManager.generateItemHash(item, meta));
                    }
                }
            }
        }
        ResearchManager.scheduleSave(player);

        sender.addChatMessage(new ChatComponentTranslation("salisarcana:command.scanall.success"));
    }

    public static class Arguments {

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP targetPlayer;
    }
}
