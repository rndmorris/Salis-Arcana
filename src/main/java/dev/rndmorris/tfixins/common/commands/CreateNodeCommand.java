package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.CoordinateArgument;
import dev.rndmorris.tfixins.common.commands.arguments.NodeModifierArgument;
import dev.rndmorris.tfixins.common.commands.arguments.NodeTypeArgument;
import dev.rndmorris.tfixins.common.commands.arguments.QuantitativeAspectArgument;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.NodeModifierHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.NodeTypeHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.QuantitativeAspectHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.positional.CoordinateHandler;
import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileNode;

public class CreateNodeCommand extends FixinsCommandBase<CreateNodeCommand.Arguments> {

    public CreateNodeCommand() {
        super(FixinsConfig.commandsModule.createNode);
    }

    protected void process(ICommandSender sender, String[] args) {
        final var arguments = argumentProcessor.process(sender, args);

        final var world = sender.getEntityWorld();
        final var pos = arguments.createAt;
        world.setBlockToAir(pos.x, pos.y, pos.z);
        ThaumcraftWorldGenerator.createRandomNodeAt(
            world,
            pos.x,
            pos.y,
            pos.z,
            world.rand,
            arguments.silverwood,
            arguments.eerie,
            arguments.small);

        final var newTile = world.getTileEntity(pos.x, pos.y, pos.z);
        if (!(newTile instanceof TileNode node)) {
            throw new CommandException("tfixins:command.create-node.failure", pos.x, pos.y, pos.z);
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
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { CoordinateHandler.INSTANCE, NodeTypeHandler.INSTANCE, NodeModifierHandler.INSTANCE,
                FlagHandler.INSTANCE, QuantitativeAspectHandler.INSTANCE });
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = CoordinateHandler.class, descLangKey = "coord")
        public CoordinateArgument createAt;

        @NamedArg(name = "-t", handler = NodeTypeHandler.class, descLangKey = "type")
        public NodeTypeArgument nodeType;

        @NamedArg(name = "-m", handler = NodeModifierHandler.class, descLangKey = "modifier")
        public NodeModifierArgument nodeModifier;

        @FlagArg(name = "--silverwood", excludes = { "--eerie", "--small" }, descLangKey = "silverwood")
        public boolean silverwood;

        @FlagArg(name = "--eerie", excludes = { "--silverwood" }, descLangKey = "eerie")
        public boolean eerie;

        @FlagArg(name = "--small", excludes = { "--silverwood", "-a" }, descLangKey = "small")
        public boolean small;

        @NamedArg(name = "-a", handler = QuantitativeAspectHandler.class, excludes = "--small", descLangKey = "aspect")
        public List<QuantitativeAspectArgument> aspects = new ArrayList<>();

    }

}
