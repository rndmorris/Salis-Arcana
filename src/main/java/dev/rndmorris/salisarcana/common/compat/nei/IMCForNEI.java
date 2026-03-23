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
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setString("handler", name);
        NBT.setString("modName", "Salis Arcana");
        NBT.setString("modId", SalisArcana.MODID);
        NBT.setBoolean("modRequired", true);
        NBT.setString("itemName", stack);
        NBT.setInteger("handlerHeight", height);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", NBT);
    }

    @Optional.Method(modid = "NotEnoughItems")
    private static void registerCatalystInfo(String handlerName, String stack) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", handlerName);
        aNBT.setString("itemName", stack);
        aNBT.setInteger("priority", 0);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", aNBT);
    }
}
