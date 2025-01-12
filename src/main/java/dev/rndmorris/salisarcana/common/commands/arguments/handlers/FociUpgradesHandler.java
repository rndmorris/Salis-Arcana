package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import static dev.rndmorris.salisarcana.lib.WandFocusHelper.getAppliedUpgrades;
import static dev.rndmorris.salisarcana.lib.WandFocusHelper.getFocusFrom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;

public class FociUpgradesHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final FociUpgradesHandler INSTANCE = new FociUpgradesHandler();

    private static Map<FocusUpgradeType, String> upgradesToKeys;
    private static Map<String, FocusUpgradeType> keysToUpgrades;

    private static Map<FocusUpgradeType, String> upgradesToKeys() {
        if (upgradesToKeys == null) {
            upgradesToKeys = new TreeMap<>(Comparator.comparingInt(upgrade -> upgrade.id));
            for (var index = 0; index < FocusUpgradeType.types.length; ++index) {
                final var type = FocusUpgradeType.types[index];
                final var cleanedName = type.getLocalizedName()
                    .toLowerCase()
                    .replaceAll(" +", "-")
                    .replaceAll("[^\\w-]", "");
                // final var cleanedName = type.getLocalizedName().replaceAll();
                final var key = String.format("%d-%s", index, cleanedName);
                upgradesToKeys.put(type, key);
            }
        }
        return upgradesToKeys;
    }

    private static Map<String, FocusUpgradeType> keysToUpgrades() {
        if (keysToUpgrades == null) {
            keysToUpgrades = new TreeMap<>(String::compareToIgnoreCase);
            for (var entry : upgradesToKeys().entrySet()) {
                keysToUpgrades.put(entry.getValue(), entry.getKey());
            }
        }
        return keysToUpgrades;
    }

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var resultList = new ArrayList<FocusUpgradeType>(5);
        String peeked = null;
        while (args.hasNext() && (peeked = args.peek()) != null && !peeked.startsWith("-")) {
            final var key = args.next();
            final var upgrade = keysToUpgrades().get(key);
            if (upgrade == null) {
                throw new CommandException("salisarcana:error.invalid_focus_upgrade", key);
            }
            resultList.add(upgrade);
        }

        return resultList;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        final var player = CommandBase.getCommandSenderAsPlayer(sender);
        ItemStack heldItem;
        ItemFocusBasic heldFocus;
        if ((heldItem = player.getCurrentEquippedItem()) == null || (heldFocus = getFocusFrom(heldItem)) == null) {
            return new ArrayList<>(upgradesToKeys().values());
        }

        // copied, so we can modify it as we work
        final var workingItem = heldItem.copy();
        final var appliedUpgrades = getAppliedUpgrades(heldFocus, workingItem);

        if (appliedUpgrades.size() >= 5) {
            return null;
        }

        String peeked = null;
        while (appliedUpgrades.size() < 5 && args.hasNext()
            && (peeked = args.peek()) != null
            && !peeked.startsWith("-")) {
            final var key = args.next();
            final var upgrade = keysToUpgrades().get(key);
            if (upgrade != null) {
                appliedUpgrades.add(upgrade);
                heldFocus.applyUpgrade(workingItem, upgrade, appliedUpgrades.size());
            }
        }

        if (peeked != null && peeked.startsWith("-")) {
            return null;
        }

        final var possibleUpgrades = heldFocus.getPossibleUpgradesByRank(heldItem, appliedUpgrades.size() + 1);
        if (possibleUpgrades == null) {
            return null;
        }
        final var upgradeKeys = upgradesToKeys();
        final var result = new ArrayList<String>(possibleUpgrades.length);
        for (var upgrade : possibleUpgrades) {
            if (heldFocus
                .canApplyUpgrade(workingItem, ResearchHelper.knowItAll(), upgrade, appliedUpgrades.size() + 1)) {
                result.add(upgradeKeys.get(upgrade));
            }
        }

        return result;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return List.class;
    }

}
