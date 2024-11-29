package dev.rndmorris.tfixins.config;

public interface ISetting {

    /**
     * Whether the individual setting is enabled.
     * @return
     */
    boolean isEnabled();

    /**
     *
     * @param enabled
     */
    void setEnabled(boolean enabled);

}
