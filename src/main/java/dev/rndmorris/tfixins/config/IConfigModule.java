package dev.rndmorris.tfixins.config;

import net.minecraftforge.common.config.Configuration;

public interface IConfigModule {

    boolean enabledByDefault();

    String getModuleName();

    String getModuleComment();

    void loadModuleFromConfig(Configuration configuration);

}
