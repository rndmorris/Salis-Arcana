package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import dev.rndmorris.tfixins.common.commands.parsing.ArgParser;
import dev.rndmorris.tfixins.common.commands.parsing.FlagParser;
import dev.rndmorris.tfixins.common.commands.parsing.IArgType;
import dev.rndmorris.tfixins.common.commands.parsing.NamedArg;
import dev.rndmorris.tfixins.lib.EnumHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.parsing.CoordinateParser;
import dev.rndmorris.tfixins.common.commands.parsing.PositionalArg;
import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileNode;

public class CreateNodeCommand extends FixinsCommandBase {

    public CreateNodeCommand() {
        super(FixinsConfig.commandsModule.createNode);
    }

    protected void process(ICommandSender sender, String[] args) {
        final var parser = CmdArgs.getParser();
        final var arguments = parser.parse(sender, args);

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
            node.setNodeType(arguments.nodeType);
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

    private static class CmdArgs {

        private static ArgParser<CmdArgs> parser = null;

        public static ArgParser<CmdArgs> getParser() {
            if (parser == null) {
                parser = new ArgParser<>(CmdArgs.class, CmdArgs::new, CoordinateParser.X.INSTANCE,
                    CoordinateParser.Y.INSTANCE,
                    CoordinateParser.Z.INSTANCE,
                    NodeTypeParser.INSTANCE,
                    NodeModifierParser.INSTANCE,
                    FlagParser.INSTANCE,
                    AspectParser.INSTANCE);
            }
            return parser;
        }


        @PositionalArg(index = 0, parser = CoordinateParser.X.class)
        public int x;
        @PositionalArg(index = 1, parser = CoordinateParser.Y.class)
        public int y;
        @PositionalArg(index = 2, parser = CoordinateParser.Z.class)
        public int z;

        @NamedArg(name = "--silverwood", parser = FlagParser.class)
        public boolean silverwood;
        @NamedArg(name = "--eerie", parser = FlagParser.class)
        public boolean eerie;
        @NamedArg(name = "--small", parser = FlagParser.class)
        public boolean small;

        @NamedArg(name = "-t", parser = NodeTypeParser.class)
        public NodeType nodeType;
        @NamedArg(name = "-m", parser = NodeModifierParser.class)
        public Modifier nodeModifier;

        @NamedArg(name = "-a", parser = AspectParser.class)
        public List<AspectEntry> aspects = new ArrayList<>();

        private static class NodeTypeParser implements IArgType {

            public static final IArgType INSTANCE = new NodeTypeParser();

            @Override
            public Object parse(ICommandSender sender, String current, Iterator<String> args) {
                NodeType result = null;

                if (args.hasNext()) {
                    final var typeName = args.next();
                    result = EnumHelper.tryParseEnum(NodeType.values(), typeName);
                }

                if (result == null) {
                    throw new CommandException("");
                }
                return result;
            }

            @Override
            public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
                if (args.hasNext()) {
                    args.next();
                    if (!args.hasNext()) {
                        return Arrays.stream(NodeType.values()).map(NodeType::toString).collect(Collectors.toList());
                    }
                }
                return null;
            }
        }

        private static class NodeModifierParser implements IArgType {

            public static final IArgType INSTANCE = new NodeModifierParser();

            @Override
            public Object parse(ICommandSender sender, String current, Iterator<String> args) {
                Modifier result = null;

                if (args.hasNext()) {
                    final var typeName = args.next();
                    result = EnumHelper.tryParseEnum(Modifier.values(), typeName);
                }

                if (result == null) {
                    throw new CommandException("");
                }
                return result;
            }

            @Override
            public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
                if (args.hasNext()) {
                    args.next();
                    if (!args.hasNext()) {
                        return Arrays.stream(Modifier.values()).map(Modifier::toString).collect(Collectors.toList());
                    }
                }
                return null;
            }
        }

        private static class AspectParser implements IArgType {

            public static final IArgType INSTANCE = new AspectParser();

            @Override
            public Object parse(ICommandSender sender, String current, Iterator<String> args) {

                current = "";
                if (args.hasNext()) {
                    current = args.next();
                }
                final var aspect = getAspect(current);

                current = "";
                if (args.hasNext()) {
                    current = args.next();
                }
                final var amount = getAmount(sender, current);

                return new AspectEntry(aspect, amount);
            }

            private Aspect getAspect(String input) {
                if (input != null && !input.isEmpty()) {
                    for (var kv : Aspect.aspects.entrySet()) {
                        if (kv.getKey().equalsIgnoreCase(input)) {
                            return kv.getValue();
                        }
                    }
                }
                throw new CommandException("tfixins:error.invalid_aspect", input);
            }

            private int getAmount(ICommandSender sender, String input) {
                return CommandBase.parseIntWithMin(sender, input, 1);
            }

            @Override
            public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
                if (args.hasNext()) {
                    args.next();
                    if (!args.hasNext()) {
                        return new ArrayList<>(Aspect.aspects.keySet());
                    }
                    args.next();
                    if (!args.hasNext()) {
                        return Collections.singletonList("1");
                    }
                }

                return null;
            }
        }

        private enum Modifier {

            NONE(null),
            BRIGHT(NodeModifier.BRIGHT),
            PALE(NodeModifier.PALE),
            FADING(NodeModifier.FADING);

            public final NodeModifier modifier;

            Modifier(NodeModifier modifier) {
                this.modifier = modifier;
            }
        }

        private static class AspectEntry {

            public Aspect aspect;
            public int amount;

            public AspectEntry(Aspect aspect, int amount) {
                this.aspect = aspect;
                this.amount = amount;
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, CmdArgs.getParser().getTabOptions(sender, args));
    }

}
