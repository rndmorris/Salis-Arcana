package dev.rndmorris.tfixins.common.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.CreateNodeArguments;
import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileNode;

public class CreateNodeCommand extends FixinsCommandBase {

    public CreateNodeCommand() {
        super(FixinsConfig.commandsModule.createNode);
    }

    protected void process(ICommandSender sender, String[] args) {
        final var arguments = CreateNodeArguments.getProcessor()
            .process(sender, args);

        final var world = sender.getEntityWorld();
        world.setBlockToAir(arguments.x, arguments.y, arguments.z);
        ThaumcraftWorldGenerator.createRandomNodeAt(
            world,
            arguments.x,
            arguments.y,
            arguments.z,
            world.rand,
            arguments.silverwood,
            arguments.eerie,
            arguments.small);

        final var newTile = world.getTileEntity(arguments.x, arguments.y, arguments.z);
        if (!(newTile instanceof TileNode node)) {
            throw new CommandException("tfixins:command.create-node.failure", arguments.x, arguments.y, arguments.z);
        }
        if (arguments.nodeModifier != null) {
            node.setNodeModifier(arguments.nodeModifier.modifier);
        }
        if (arguments.nodeType != null) {
            node.setNodeType(arguments.nodeType.nodeType);
        }
        if (!arguments.aspects.isEmpty()) {
            final var aspectList = new AspectList();
            for (var a : arguments.aspects) {
                aspectList.add(a.aspect, a.amount);
            }
            node.setAspects(aspectList);
        }
        node.markDirty();
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsFromIterableMatchingLastWord(
            args,
            CreateNodeArguments.getProcessor()
                .getAutocompletionSuggestions(sender, args));
    }

}
