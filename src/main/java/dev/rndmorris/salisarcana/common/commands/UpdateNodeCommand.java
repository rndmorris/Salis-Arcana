package dev.rndmorris.salisarcana.common.commands;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.CoordinateArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.NodeModifierArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.NodeTypeArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.QuantitativeAspectArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.AspectHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.NodeModifierHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.NodeTypeHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.QuantitativeAspectHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.CoordinateHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileNode;

public class UpdateNodeCommand extends ArcanaCommandBase<UpdateNodeCommand.Arguments> {

    public UpdateNodeCommand() {
        super(SalisConfig.commands.updateNode);
    }

    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        final var pos = arguments.updateAt;

        final var world = sender.getEntityWorld();
        final var tile = world.getTileEntity(pos.x, pos.y, pos.z);
        if (!(tile instanceof TileNode node)) {
            throw new CommandException("salisarcana:command.update-node.not_found", pos.x, pos.y, pos.z);
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
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { CoordinateHandler.INSTANCE, NodeTypeHandler.INSTANCE, NodeModifierHandler.INSTANCE,
                QuantitativeAspectHandler.INSTANCE, AspectHandler.INSTANCE });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 3;
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = CoordinateHandler.class, descLangKey = "coord")
        public CoordinateArgument updateAt;

        @NamedArg(name = "-t", handler = NodeTypeHandler.class, descLangKey = "type")
        public NodeTypeArgument nodeType;

        @NamedArg(name = "-m", handler = NodeModifierHandler.class, descLangKey = "modifier")
        public NodeModifierArgument nodeModifier;

        @NamedArg(name = "--set", handler = QuantitativeAspectHandler.class, descLangKey = "set")
        public ArrayList<QuantitativeAspectArgument> addAspects = new ArrayList<>();

        @NamedArg(name = "--rem", handler = AspectHandler.class, descLangKey = "rem")
        public ArrayList<Aspect> removeAspects = new ArrayList<>();
    }
}
