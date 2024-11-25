package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dev.rndmorris.tfixins.lib.IntegerHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import dev.rndmorris.tfixins.common.commands.parsing.Arguments;
import dev.rndmorris.tfixins.config.FixinsConfig;
import net.minecraft.util.IChatComponent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileNode;

import static dev.rndmorris.tfixins.lib.EnumHelper.tryParseEnum;

public class CreateNodeCommand extends FixinsCommandBase {

    public CreateNodeCommand() {
        super(FixinsConfig.commandsModule.createNode);
    }

    protected void process(ICommandSender sender, String[] args) {
        final var arguments = ParsedArgs.parse(sender, args);

        final var world = sender.getEntityWorld();
        world.setBlockToAir(arguments.x, arguments.y, arguments.z);
        ThaumcraftWorldGenerator.createRandomNodeAt(world, arguments.x, arguments.y, arguments.z, world.rand, arguments.silverwood, arguments.eerie, arguments.small);

        final var newTile = world.getTileEntity(arguments.x, arguments.y, arguments.z);
        if (!(newTile instanceof TileNode node)) {
            throw new CommandException("tfixins:command.create-node.failure", arguments.x, arguments.y, arguments.z);
        }
        if (arguments.modifier != null) {
            node.setNodeModifier(arguments.modifier.modifier);
        }
        if (arguments.nodeType != null) {
            node.setNodeType(arguments.nodeType);
        }
        if (arguments.aspects.visSize() > 0) {
            node.setAspects(arguments.aspects);
        }
        node.markDirty();
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, ParsedArgs.autocomplete(sender, args));
    }

    private static class ParsedArgs {

        public static ParsedArgs parse(ICommandSender sender, String[] args) {
            final var result = new ParsedArgs(sender);

            result.buildArguments().parse(sender, args);

            return result;
        }

        private static Collection<String> autocomplete(ICommandSender sender, String[] args) {
            final var container = new ParsedArgs(sender);
            return container.buildArguments().autocomplete(args);
        }

        private Arguments buildArguments() {
            final var arguments = new Arguments();
            arguments.addPositional(0, this::parseX, this::tabX);
            arguments.addPositional(1, this::parseY, this::tabY);
            arguments.addPositional(2, this::parseZ, this::tabZ);
            arguments.addNamed("-t", this::parseType, this::tabType, 1);
            arguments.addNamed("-m", this::parseModifier, this::tabModifier, 1);
            arguments.addNamed("-a", this::parseAspect, this::tabAspect);
            arguments.addFlag("--silverwood", this::parseSilverwood);
            arguments.addFlag("--eerie", this::parseEerie);
            arguments.addFlag("--small", this::parseSmall);
            return arguments;
        }

        private final ICommandSender sender;
        private final EntityPlayerMP player;

        public int x;
        public int y;
        public int z;

        public NodeType nodeType;
        public Modifier modifier;

        public final AspectList aspects = new AspectList();
        private Aspect lastAspect;

        public boolean silverwood = false;
        public boolean eerie = false;
        public boolean small = false;

        public ParsedArgs(ICommandSender sender) {
            this.sender = sender;
            player = getCommandSenderAsPlayer(sender);
        }

        private Arguments.Parser parseX(String val) {
            x = (int) func_110666_a(sender, player.posX, val);
            return null;
        }
        private Arguments.AutoComplete tabX(String val, List<String> result) {
            return autoCompleteCoord(val, result);
        }

        private Arguments.Parser parseY(String val) {
            y = (int) func_110666_a(sender, player.posY, val);
            return null;
        }
        private Arguments.AutoComplete tabY(String val, List<String> result) {
            return autoCompleteCoord(val, result);
        }

        private Arguments.Parser parseZ(String val) {
            z = (int) func_110666_a(sender, player.posZ, val);
            return null;
        }
        private Arguments.AutoComplete tabZ(String val, List<String> result) {
            return autoCompleteCoord(val, result);
        }

        private Arguments.AutoComplete autoCompleteCoord(String val, List<String> result) {
            if (result != null) {
                result.add("~");
            }
            return null;
        }

        private Arguments.Parser parseType(String val) {
            return (str) -> {
                nodeType = tryParseEnum(NodeType.values(), str);
                if (nodeType == null) {
                    throw new CommandException("tfixins:error.invalid_node_type", str);
                }
                return null;
            };
        }
        private Arguments.AutoComplete tabType(String val, List<String> result) {
            if (result != null) {
                Arrays.stream(NodeType.values()).map(NodeType::toString).forEach(result::add);
            }
            return null;
        }

        private Arguments.Parser parseModifier(String val) {
            return (str) -> {
                modifier = tryParseEnum(Modifier.values(), str);
                if (modifier == null) {
                    throw new CommandException("tfixins:error.invalid_node_modifier", str);
                }
                return null;
            };
        }
        private Arguments.AutoComplete tabModifier(String val, List<String> result) {
            if (result != null) {
                Arrays.stream(Modifier.values()).map(Modifier::toString).forEach(result::add);
            }
            return null;
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

        private Arguments.Parser parseAspect(String val) {
            return (str) -> {
                for (var aspect : Aspect.aspects.values()) {
                    if (aspect.getTag().equalsIgnoreCase(str)) {
                        lastAspect = aspect;
                        break;
                    }
                }
                if (lastAspect == null) {
                    throw new CommandException("tfixins:error.invalid_aspect", str);
                }

                return this::parseAspectCount;
            };
        }
        private Arguments.AutoComplete tabAspect(String val, List<String> result) {
            if (result != null) {
                result.addAll(Aspect.aspects.keySet());
            }
            return this::tabAspectAmount;
        }

        private Arguments.Parser parseAspectCount(String val) {
            final var amount = parseIntBounded(sender, val, 1, Integer.MAX_VALUE);
            aspects.add(lastAspect, amount);
            lastAspect = null;
            return null;
        }
        private Arguments.AutoComplete tabAspectAmount(String val, List<String> result) {
            if (result != null) {
                result.add("1");
            }
            return null;
        }

        private Arguments.Parser parseSilverwood(String val) {
            silverwood = true;
            return null;
        }
        private Arguments.Parser parseEerie(String val) {
            eerie = true;
            return null;
        }
        private Arguments.Parser parseSmall(String val) {
            small = true;
            return null;
        }
    }

}
