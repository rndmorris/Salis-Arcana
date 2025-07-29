package dev.rndmorris.salisarcana.common.commands;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.AspectHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.network.MessageForgetAspects;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;

public class ForgetAspectCommand extends ArcanaCommandBase<ForgetAspectCommand.Arguments> {

    public ForgetAspectCommand() {
        super(SalisConfig.commands.forgetAspects);
    }

    @Nonnull
    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            ForgetAspectCommand.Arguments.class,
            ForgetAspectCommand.Arguments::new,
            new IArgumentHandler[] { AspectHandler.INSTANCE, FlagHandler.INSTANCE, PlayerHandler.INSTANCE });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 2;
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        if (arguments.targetPlayer == null) {
            arguments.targetPlayer = getCommandSenderAsPlayer(sender);
        }
        if (!(arguments.reset || arguments.forget)) {
            sender.addChatMessage(new ChatComponentTranslation("salisarcana:command.forget-aspect.no-action"));
            return;
        }
        final var playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        int removedCount = 0;
        final var playerAspects = playerKnowledge.aspectsDiscovered.get(arguments.targetPlayer.getCommandSenderName());
        if (playerAspects != null) {
            if (arguments.reset) {
                removedCount = resetAspects(playerAspects, arguments, arguments.targetPlayer);
            } else if (arguments.forget) {
                removedCount = forgetAspects(playerAspects, arguments, arguments.targetPlayer);
            }
        }
        String playerName = arguments.targetPlayer.getCommandSenderName();
        if (removedCount > 0) {
            sender.addChatMessage(
                new ChatComponentTranslation("salisarcana:command.forget-aspect.success", removedCount, playerName));
        } else {
            sender
                .addChatMessage(new ChatComponentTranslation("salisarcana:command.forget-aspect.failure", playerName));
        }
    }

    private int resetAspects(AspectList aspects, Arguments arguments, EntityPlayerMP player) {
        int removedCount = 0;
        if (arguments.all) {
            NetworkHandler.instance.sendTo(new MessageForgetAspects(MessageForgetAspects.RESET_ACTION), player);
            for (final var aspect : aspects.getAspects()) {
                aspects.aspects.put(aspect, 1);
                removedCount++;
            }
        } else if (arguments.aspects != null && !arguments.aspects.isEmpty()) {
            NetworkHandler.instance
                .sendTo(new MessageForgetAspects(arguments.aspects, MessageForgetAspects.RESET_ACTION), player);
            for (final var aspect : arguments.aspects) {
                aspects.aspects.put(aspect, 1);
                removedCount++;
            }
        }
        return removedCount;
    }

    private int forgetAspects(AspectList aspects, Arguments arguments, EntityPlayerMP player) {
        int removedCount = 0;
        if (arguments.all) {
            NetworkHandler.instance.sendTo(new MessageForgetAspects(MessageForgetAspects.FORGET_ACTION), player);
            removedCount = Math.max(
                aspects.size() - Aspect.getPrimalAspects()
                    .size(),
                0);
            aspects.aspects.clear();
        } else if (arguments.aspects != null && !arguments.aspects.isEmpty()) {
            NetworkHandler.instance
                .sendTo(new MessageForgetAspects(arguments.aspects, MessageForgetAspects.FORGET_ACTION), player);
            for (final var aspect : arguments.aspects) {
                if (aspects.aspects.remove(aspect) != null) {
                    removedCount++;
                }
            }
        }
        return removedCount;
    }

    public static class Arguments {

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP targetPlayer;

        @FlagArg(name = "--all", excludes = { "--aspect" }, descLangKey = "all")
        public boolean all;

        @NamedArg(name = "--aspect", handler = AspectHandler.class, descLangKey = "aspect", excludes = "--all")
        public ArrayList<Aspect> aspects;

        @FlagArg(name = "--reset", descLangKey = "reset", excludes = "--forget")
        public boolean reset;

        @FlagArg(name = "--forget", descLangKey = "forget", excludes = "--reset")
        public boolean forget;
    }
}
