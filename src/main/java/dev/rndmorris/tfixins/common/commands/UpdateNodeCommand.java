package dev.rndmorris.tfixins.common.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.UpdateNodeArguments;
import dev.rndmorris.tfixins.config.FixinsConfig;
import net.minecraft.util.ChatComponentTranslation;
import thaumcraft.common.tiles.TileNode;

public class UpdateNodeCommand extends FixinsCommandBase {

    public UpdateNodeCommand() {
        super(FixinsConfig.commandsModule.updateNode);
    }

    protected void process(ICommandSender sender, String[] args) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
        final var arguments = UpdateNodeArguments.getProcessor()
            .process(sender, args);

        final var world = sender.getEntityWorld();
        final var tile = world.getTileEntity(arguments.x, arguments.y, arguments.z);
        if (!(tile instanceof TileNode node)) {
            throw new CommandException("tfixins:command.update-node.not_found", arguments.x, arguments.y, arguments.z);
        }

        if (arguments.nodeModifier != null) {
            node.setNodeModifier(arguments.nodeModifier.modifier);
        }
        if (arguments.nodeType != null) {
            node.setNodeType(arguments.nodeType.nodeType);
        }

        if (!arguments.removeAspects.isEmpty()) {
            for (var aspect : arguments.removeAspects) {
                node.getAspectsBase().remove(aspect);
                node.getAspects().remove(aspect);
            }
        }

        if (!arguments.addAspects.isEmpty()) {
            for (var aspect : arguments.addAspects) {
                node.getAspectsBase().remove(aspect.aspect);
                node.getAspects().remove(aspect.aspect);

                node.getAspectsBase().add(aspect.aspect, aspect.amount);
                node.getAspects().add(aspect.aspect, aspect.amount);
            }
        }

        if (node.getAspects().visSize() <= 0) {
            world.setBlockToAir(arguments.x, arguments.y, arguments.z);
            return;
        }

        node.markDirty();
        world.markBlockForUpdate(arguments.x, arguments.y, arguments.z);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsFromIterableMatchingLastWord(
            args,
            UpdateNodeArguments.getProcessor()
                .getAutocompletionSuggestions(sender, args));
    }

    @Override
    public void printHelp(ICommandSender sender) {
        Arrays.stream(new String[] {
            "tfixins:command.update-node.desc",
            "tfixins:command.usage",
            "tfixins:command.update-node.usage",
            "tfixins:command.args",
            "tfixins:command.update-node.args.01",
            "tfixins:command.update-node.args.02",
            "tfixins:command.update-node.args.03",
            "tfixins:command.update-node.args.04",
            "tfixins:command.update-node.args.05",
        }).map(ChatComponentTranslation::new).forEachOrdered(sender::addChatMessage);
    }
}
