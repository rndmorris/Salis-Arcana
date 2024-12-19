package dev.rndmorris.salisarcana.config;

public enum ConfigPhase {
    EARLY, // Will be loaded before mixins are loaded
    LATE // Will be loaded during preinit
}
