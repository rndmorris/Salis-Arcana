package dev.rndmorris.salisarcana.lib;

import net.minecraftforge.event.entity.living.LivingAttackEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.lib.utils.EntityUtils;

public class GolemVisorDartInterceptor {

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if ("arrow".equals(event.source.damageType) && event.source.getEntity() instanceof EntityGolemBase golem) {
            // If the golem is wearing a visor
            if (golem.decoration.contains("V")) {
                EntityUtils.setRecentlyHit(event.entityLiving, 100);
            }
        }
    }
}
