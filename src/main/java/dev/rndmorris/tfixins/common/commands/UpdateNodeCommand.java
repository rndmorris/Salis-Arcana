package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.CoordinateArgument;
import dev.rndmorris.tfixins.common.commands.arguments.NodeModifierArgument;
import dev.rndmorris.tfixins.common.commands.arguments.NodeTypeArgument;
import dev.rndmorris.tfixins.common.commands.arguments.QuantitativeAspectArgument;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.AspectHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.CoordinateHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeModifierHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeTypeHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.QuantitativeAspectHandler;
import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileNode;

public class UpdateNodeCommand extends FixinsCommandBase<UpdateNodeCommand.Arguments> {

    public UpdateNodeCommand() {
        super(FixinsConfig.commandsModule.updateNode);
    }

    protected void process(ICommandSender sender, String[] args) {
        Minecraft.getMinecraft()
            .displayGuiScreen(new GuiChat());
        final var arguments = argumentProcessor.process(sender, args);

        final var pos = arguments.updateAt;

        final var world = sender.getEntityWorld();
        final var tile = world.getTileEntity(pos.x, pos.y, pos.z);
        if (!(tile instanceof TileNode node)) {
            throw new CommandException("tfixins:command.update-node.not_found", pos.x, pos.y, pos.z);
        }

        if (arguments.nodeModifier != null) {
            node.setNodeModifier(arguments.nodeModifier.modifier);
        }
        if (arguments.nodeType != null) {
            node.setNodeType(arguments.nodeType.nodeType);
        }

        if (!arguments.removeAspects.isEmpty()) {
            for (var aspect : arguments.removeAspects) {
                node.getAspectsBase()
                    .remove(aspect);
                node.getAspects()
                    .remove(aspect);
            }
        }

        if (!arguments.addAspects.isEmpty()) {
            for (var aspect : arguments.addAspects) {
                node.getAspectsBase()
                    .remove(aspect.aspect);
                node.getAspects()
                    .remove(aspect.aspect);

                node.getAspectsBase()
                    .add(aspect.aspect, aspect.amount);
                node.getAspects()
                    .add(aspect.aspect, aspect.amount);
            }
        }

        if (node.getAspects()
            .visSize() <= 0) {
            world.setBlockToAir(pos.x, pos.y, pos.z);
            return;
        }

        node.markDirty();
        world.markBlockForUpdate(pos.x, pos.y, pos.z);
    }

    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { CoordinateHandler.INSTANCE, NodeTypeHandler.INSTANCE, NodeModifierHandler.INSTANCE,
                QuantitativeAspectHandler.INSTANCE, AspectHandler.INSTANCE });
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = CoordinateHandler.class, descLangKey = "coord")
        public CoordinateArgument updateAt;

        @NamedArg(name = "-t", handler = NodeTypeHandler.class, descLangKey = "type")
        public NodeTypeArgument nodeType;

        @NamedArg(name = "-m", handler = NodeModifierHandler.class, descLangKey = "modifier")
        public NodeModifierArgument nodeModifier;

        @NamedArg(name = "--set", handler = QuantitativeAspectHandler.class, descLangKey = "set")
        public List<QuantitativeAspectArgument> addAspects = new ArrayList<>();

        @NamedArg(name = "--rem", handler = AspectHandler.class, descLangKey = "rem")
        public List<Aspect> removeAspects = new ArrayList<>();
    }
}
