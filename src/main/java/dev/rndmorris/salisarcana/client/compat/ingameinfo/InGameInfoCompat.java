package dev.rndmorris.salisarcana.client.compat.ingameinfo;

import com.github.lunatrius.ingameinfo.tag.registry.TagRegistry;

public class InGameInfoCompat {

    public static void register() {
        TagRegistry.INSTANCE.register(new TagEquippedWarp().setName("saequippedwarp"));
    }
}
