package dev.rndmorris.salisarcana.common.commands;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.CoordinateArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.CoordinateHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class InfusionSymmetryCommand extends ArcanaCommandBase<InfusionSymmetryCommand.Arguments> {

    @Nullable
    private final Method getSurroundings;

    public InfusionSymmetryCommand() {
        super(ConfigModuleRoot.commands.infusionSymmetry);
        Method getSurroundings = null;
        try {
            getSurroundings = TileInfusionMatrix.class.getDeclaredMethod("getSurroundings");
            getSurroundings.setAccessible(true);
        } catch (NoSuchMethodException e) {
            LOG.error("Could not access TileInfusionMatrix.getSurroundings().", e);
        }

        this.getSurroundings = getSurroundings;
    }

    @Nonnull
    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { CoordinateHandler.INSTANCE });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 0;
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        final var player = getCommandSenderAsPlayer(sender);
        TileInfusionMatrix matrix;

        if (arguments.coordinates != null) {
            matrix = getFromCoordinates(player.getEntityWorld(), arguments.coordinates);
            if (matrix == null) {
                throw new CommandException("salisarrcana:command.infusion-symmetry.not_found_coords");
            }
        } else {
            matrix = getNearest(player);
            if (matrix == null) {
                throw new CommandException("salisarrcana:command.infusion-symmetry.not_found_nearby");
            }
        }
        final var symmetry = getMatrixSymmetry(matrix);
        if (symmetry == null) {
            throw new CommandException("salisarrcana:command.infusion-symmetry.accessError");
        }

        sender.addChatMessage(new ChatComponentTranslation("salisarrcana:command.infusion-symmetry.found", -symmetry));
    }

    private @Nullable TileInfusionMatrix getFromCoordinates(@Nonnull World world, @Nonnull CoordinateArgument coords) {
        final var tile = world.getTileEntity(coords.x, coords.y, coords.z);
        return tile instanceof TileInfusionMatrix matrix ? matrix : null;
    }

    private @Nullable TileInfusionMatrix getNearest(@Nonnull EntityPlayer player) {
        final var x = player.posX;
        final var y = player.posY;
        final var z = player.posZ;

        var nearestDist = Double.MAX_VALUE;
        TileInfusionMatrix nearest = null;
        for (var tile : player.worldObj.loadedTileEntityList) {
            if (!(tile instanceof TileInfusionMatrix matrix)) {
                continue;
            }
            final var dist = matrix.getDistanceFrom(x, y, z);
            if (dist <= 64 && dist < nearestDist) {
                nearestDist = dist;
                nearest = matrix;
            }
        }

        return nearest;
    }

    private Integer getMatrixSymmetry(TileInfusionMatrix matrix) {
        if (getSurroundings == null) {
            // should hopefully never happen
            return null;
        }

        try {
            getSurroundings.invoke(matrix);
            return matrix.symmetry;
        } catch (Exception e) {
            LOG.error("An error occurred trying to invoke getSurroundings().", e);
        }
        return null;
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = CoordinateHandler.class, descLangKey = "coord")
        CoordinateArgument coordinates;
    }

}
