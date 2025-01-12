package dev.rndmorris.salisarcana.common.compat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;
import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.Tags;
import dev.rndmorris.salisarcana.common.item.PlaceholderItem;

@Optional.Interface(iface = "codechicken.nei.api.API", modid = "NotEnoughItems")
public class NEIConfig implements IConfigureNEI {

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public void loadConfig() {
        hidePlaceholder(PlaceholderItem.capPlaceholder);
        hidePlaceholder(PlaceholderItem.rodPlaceholder);
    }

    @Optional.Method(modid = "NotEnoughItems")
    private void hidePlaceholder(PlaceholderItem placeholder) {
        if (placeholder != null) {
            API.hideItem(new ItemStack(placeholder, 0, OreDictionary.WILDCARD_VALUE));
        }
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public String getName() {
        return SalisArcana.MODID;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public String getVersion() {
        return Tags.VERSION;
    }
}
