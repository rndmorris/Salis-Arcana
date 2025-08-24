package dev.rndmorris.salisarcana;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;
import static dev.rndmorris.salisarcana.config.SalisConfig.commands;

import java.util.ArrayList;
import java.util.function.Supplier;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dev.rndmorris.salisarcana.api.OreDict;
import dev.rndmorris.salisarcana.common.CustomResearch;
import dev.rndmorris.salisarcana.common.DisenchantFocusUpgrade;
import dev.rndmorris.salisarcana.common.blocks.CustomBlocks;
import dev.rndmorris.salisarcana.common.commands.ArcanaCommandBase;
import dev.rndmorris.salisarcana.common.commands.CreateNodeCommand;
import dev.rndmorris.salisarcana.common.commands.ForgetAspectCommand;
import dev.rndmorris.salisarcana.common.commands.ForgetResearchCommand;
import dev.rndmorris.salisarcana.common.commands.ForgetScannedCommand;
import dev.rndmorris.salisarcana.common.commands.HelpCommand;
import dev.rndmorris.salisarcana.common.commands.InfusionSymmetryCommand;
import dev.rndmorris.salisarcana.common.commands.ListResearchCommand;
import dev.rndmorris.salisarcana.common.commands.PrerequisitesCommand;
import dev.rndmorris.salisarcana.common.commands.UpdateNodeCommand;
import dev.rndmorris.salisarcana.common.commands.UpgradeFocusCommand;
import dev.rndmorris.salisarcana.common.compat.ModCompat;
import dev.rndmorris.salisarcana.common.item.PlaceholderItem;
import dev.rndmorris.salisarcana.common.recipes.CustomRecipes;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;
import dev.rndmorris.salisarcana.lib.BlockAiryBucketInterceptor;
import dev.rndmorris.salisarcana.lib.KnowItAll;
import dev.rndmorris.salisarcana.lib.ObfuscationInfo;
import dev.rndmorris.salisarcana.lib.R;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import dev.rndmorris.salisarcana.notifications.StartupNotifications;
import dev.rndmorris.salisarcana.notifications.Updater;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.interact.AIFish;
import thaumcraft.common.items.equipment.ItemPrimalCrusher;

public class CommonProxy {

    // todo: this *should* be safe, but initialization may need moved into preInit?
    public final OreDictIds oreDictIds = new OreDictIds();

    public CommonProxy() {
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)

    public void preInit(FMLPreInitializationEvent event) {
        // for some reason, lapis ore needs a wildcard to be detected in `RenderEventHandler::startScan`.
        // Don't ask me why. This is so inconsequential that I don't think it needs a config option.
        OreDictionary.registerOre("oreLapisLazuli", new ItemStack(Blocks.lapis_ore, 1, Short.MAX_VALUE));

        if (SalisConfig.features.enableFocusDisenchanting.isEnabled()) {
            DisenchantFocusUpgrade.initialize();
        }

        ModCompat.preInit();

        CustomBlocks.registerBlocks();
        PlaceholderItem.registerPlaceholders();

        if (SalisConfig.bugfixes.useForgeFishingLists.isEnabled()) {
            fixGolemFishingLists();
        }

        updateHarvestLevels();

        FMLCommonHandler.instance()
            .bus()
            .register(new Updater());
        FMLCommonHandler.instance()
            .bus()
            .register(new StartupNotifications());
        MinecraftForge.EVENT_BUS.register(KnowItAll.EVENT_COLLECTOR);

        if (SalisConfig.bugfixes.preventBlockAiryFluidReplacement.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(new BlockAiryBucketInterceptor());
        }
    }

    private void updateHarvestLevels() {
        final var features = SalisConfig.features;
        String harvestLevelName = ObfuscationInfo.HARVEST_LEVEL.getName();
        if (features.thaumiumHarvestLevel.isEnabled()) {
            final var toolMatThaumium = new R(ThaumcraftApi.toolMatThaumium);
            toolMatThaumium.set(harvestLevelName, features.thaumiumHarvestLevel.getValue());
        }
        if (features.elementalHarvestLevel.isEnabled()) {
            final var toolMatElemental = new R(ThaumcraftApi.toolMatElemental);
            toolMatElemental.set(harvestLevelName, features.elementalHarvestLevel.getValue());
        }
        if (features.voidHarvestLevel.isEnabled()) {
            final var toolMatVoid = new R(ThaumcraftApi.toolMatVoid);
            toolMatVoid.set(harvestLevelName, features.voidHarvestLevel.getValue());
        }
        if (features.crusherHarvestLevel.isEnabled()) {
            final var material = new R(ItemPrimalCrusher.material);
            material.set(harvestLevelName, features.crusherHarvestLevel.getValue());
            ConfigItems.itemPrimalCrusher.setHarvestLevel("pickaxe", features.crusherHarvestLevel.getValue());
            ConfigItems.itemPrimalCrusher.setHarvestLevel("shovel", features.crusherHarvestLevel.getValue());
        }
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        ModCompat.init();
        CustomRecipes.registerRecipes();
        NetworkHandler.init();
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        CustomRecipes.registerRecipesPostInit();
        CustomResearch.init();
        WandHelper.loadWandParts();
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        maybeRegister(event, commands.createNode, CreateNodeCommand::new);
        maybeRegister(event, commands.forgetResearch, ForgetResearchCommand::new);
        maybeRegister(event, commands.forgetScanned, ForgetScannedCommand::new);
        maybeRegister(event, commands.forgetAspects, ForgetAspectCommand::new);
        maybeRegister(event, commands.help, HelpCommand::new);
        maybeRegister(event, commands.infusionSymmetry, InfusionSymmetryCommand::new);
        maybeRegister(event, commands.prerequisites, PrerequisitesCommand::new);
        maybeRegister(event, commands.playerResearch, ListResearchCommand::new);
        maybeRegister(event, commands.updateNode, UpdateNodeCommand::new);
        maybeRegister(event, commands.upgradeFocus, UpgradeFocusCommand::new);
    }

    private void maybeRegister(FMLServerStartingEvent event, CommandSettings settings,
        Supplier<ArcanaCommandBase<?>> init) {
        if (settings.isEnabled()) {
            event.registerServerCommand(init.get());
        }
    }

    public boolean isSingleplayerClient() {
        return false;
    }

    public World getFakePlayerWorld() {
        return MinecraftServer.getServer()
            .worldServerForDimension(0);
    }

    private void fixGolemFishingLists() {
        try {
            final var fishingHooks = new R(FishingHooks.class);
            final var aiFish = new R(AIFish.class);

            aiFish.set("LOOTCRAP", fishingHooks.get("junk", ArrayList.class));
            aiFish.set("LOOTRARE", fishingHooks.get("treasure", ArrayList.class));
            aiFish.set("LOOTFISH", fishingHooks.get("fish", ArrayList.class));

        } catch (RuntimeException e) {
            LOG.error("An error occurred updating golem fishing lists.", e);
        }
    }

    public static class OreDictIds {

        public final int pickaxeCoreScanExclude = OreDictionary.getOreID(OreDict.ELEMENTAL_PICK_SCAN_EXCLUDE);
        public final int pickaxeCoreScanInclude = OreDictionary.getOreID(OreDict.ELEMENTAL_PICK_SCAN_INCLUDE);
        public final int heatSource = OreDictionary.getOreID(OreDict.HEAT_SOURCE);
        public final int plankGreatwood = OreDictionary.getOreID(OreDict.GREATWOOD_PLANKS);
        public final int plankSilverwood = OreDictionary.getOreID(OreDict.SILVERWOOD_PLANKS);

    }
}
