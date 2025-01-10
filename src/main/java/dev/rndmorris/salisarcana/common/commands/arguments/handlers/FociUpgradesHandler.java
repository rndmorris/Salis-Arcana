package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.INamedArgumentHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FociUpgradesHandler implements INamedArgumentHandler {

    public static final FociUpgradesHandler INSTANCE = new FociUpgradesHandler();

    private static Map<FocusUpgradeType, String> upgradesToKeys;

    private static Map<FocusUpgradeType, String> upgradesToKeys() {
        if (upgradesToKeys == null) {
            upgradesToKeys = new TreeMap<>(Comparator.comparingInt(upgrade -> upgrade.id));
            for (var index = 0; index < FocusUpgradeType.types.length; ++index) {
                final var type = FocusUpgradeType.types[index];
                //final var cleanedName = type.getLocalizedName().replaceAll();
                final var key = String.format("%s-%d", type.getLocalizedName().toLowerCase().replace(" ", "-"), index);
                upgradesToKeys.put(type, key);
            }
        }
        return upgradesToKeys;
    }

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        return 0;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            return null;
        }
        final var heldItem = heldItem(sender);
        final var heldFocus = heldFocus(heldItem);

        if (heldFocus == null) {
            return new ArrayList<>(upgradesToKeys().values());
        }

        final var nextUpgradeIndex = nextUpgradeIndex(heldFocus, heldItem);
        if (nextUpgradeIndex == -1) {
            return null;
        }
        final var possibleUpgrades = heldFocus.getPossibleUpgradesByRank(heldItem, nextUpgradeIndex + 1);
        final var upgradeKeys = upgradesToKeys();
        final var result = new ArrayList<String>(possibleUpgrades.length);
        for (var upgrade : possibleUpgrades) {
            result.add(upgradeKeys.get(upgrade));
        }

        return result;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return int.class;
    }

    private @Nullable ItemStack heldItem(ICommandSender sender) {
        final var player = CommandBase.getCommandSenderAsPlayer(sender);
        return player.getCurrentEquippedItem();
    }

    private @Nullable ItemFocusBasic heldFocus(@Nullable ItemStack heldItem) {
        if (heldItem != null && heldItem.getItem() instanceof ItemFocusBasic focus) {
            return focus;
        }
        return null;
    }

    private int nextUpgradeIndex(ItemFocusBasic focus, ItemStack itemStack) {
        final var applied = focus.getAppliedUpgrades(itemStack);
        for (var index = 0; index < applied.length; ++index) {
            if (applied[index] == -1) {
                return index;
            }
        }
        return -1;
    }

}
