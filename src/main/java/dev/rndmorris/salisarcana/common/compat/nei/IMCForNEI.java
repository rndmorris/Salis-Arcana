package dev.rndmorris.salisarcana.common.compat.nei;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;
import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.SalisConfig;

public class IMCForNEI {

    @Optional.Method(modid = "NotEnoughItems")
    public static void IMCSender() {
        if (SalisConfig.features.replaceWandCoreSettings.isEnabled()
            && SalisConfig.modCompat.aspectRecipeIndex.coreReplacementNEIHandler.isEnabled()) {
            registerHandlerInfo(WandCoreSubstitutionHandler.OVERLAY, "Thaumcraft:WandRod", 144);
            registerCatalystInfo(WandCoreSubstitutionHandler.OVERLAY, "Thaumcraft:blockTable:15");
        }
        if (SalisConfig.features.replaceWandCapsSettings.isEnabled()
            && SalisConfig.modCompat.aspectRecipeIndex.capReplacementNEIHandler.isEnabled()) {
            registerHandlerInfo(WandCapSubstitutionHandler.OVERLAY, "Thaumcraft:WandCap:1", 144);
            registerCatalystInfo(WandCapSubstitutionHandler.OVERLAY, "Thaumcraft:blockTable:15");
        }
    }

    @Optional.Method(modid = "NotEnoughItems")
    private static void registerHandlerInfo(String name, String stack, int height) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("handler", name);
        nbt.setString("modName", "Salis Arcana");
        nbt.setString("modId", SalisArcana.MODID);
        nbt.setBoolean("modRequired", true);
        nbt.setString("itemName", stack);
        nbt.setInteger("handlerHeight", height);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", nbt);
    }

    @Optional.Method(modid = "NotEnoughItems")
    private static void registerCatalystInfo(String handlerName, String stack) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("handlerID", handlerName);
        nbt.setString("itemName", stack);
        nbt.setInteger("priority", 0);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", nbt);
    }
}
