package dev.rndmorris.salisarcana.mixins.late.tiles;

import dev.rndmorris.salisarcana.lib.BlockEldritchHelper;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.tiles.TileEldritchLock;

@Mixin(TileEldritchLock.class)
public class MixinTileEldritchLock_LimitDimension extends TileThaumcraft {

    @WrapMethod(method = "doBossSpawn", remap = false)
    public void limitDimension(Operation<Void> original) {
        if (this.worldObj.provider.dimensionId == Config.dimensionOuterId) {
            original.call();
        } else {
            BlockEldritchHelper.destroyDoor((TileEldritchLock) (Object) this);
        }
    }
}
