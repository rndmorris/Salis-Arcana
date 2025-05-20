package dev.rndmorris.salisarcana.mixins.late.blocks;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public class MixinBlockEldritch_AddSubBlocks {

    @WrapMethod(method = "getSubBlocks")
    public void addSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List,
        Operation<Void> original) {
        original.call(par1, par2CreativeTabs, par3List);
        par3List.add(new ItemStack(par1, 1, 0)); // Eldritch Altar
        par3List.add(new ItemStack(par1, 1, 1)); // Eldritch Obelisk
        par3List.add(new ItemStack(par1, 1, 3)); // Eldritch Capstone
        par3List.add(new ItemStack(par1, 1, 5)); // Glyphed Stone
        par3List.add(new ItemStack(par1, 1, 7)); // Ancient Doorway
        par3List.add(new ItemStack(par1, 1, 8)); // Ancient Locking Mechanism
        par3List.add(new ItemStack(par1, 1, 9)); // Crusted Opening / Crab Spawner
        par3List.add(new ItemStack(par1, 1, 10)); // Runed Stone / Shock Trap
    }
}
