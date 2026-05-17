package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.items.ItemEldritchObject;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(ItemEldritchObject.class)
public class MixinItemEldritchObject_FakePlayerFix {

    @WrapMethod(method = "onItemRightClick")
    private ItemStack cancelFakePlayer(ItemStack stack, World par2World, EntityPlayer player,
        Operation<ItemStack> original) {
        final String name = player.getCommandSenderName();
        if (name == null) return stack;

        if (!(player instanceof EntityPlayerMP) || player instanceof FakePlayer) {
            if (!par2World.isRemote) {
                ResearchManager.completeResearchUnsaved(name, "CRIMSON");
            }
            return stack;
        }

        return original.call(stack, par2World, player);
    }
}
