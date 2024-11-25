package dev.rndmorris.tfixins.config;

import net.minecraftforge.common.config.Configuration;

public interface IConfigModule {

    boolean enabledByDefault();

    String getModuleId();

    String getModuleComment();

    void loadModuleFromConfig(Configuration configuration);

}
