package dev.rndmorris.salisarcana.core.asm.compat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.core.asm.IAsmEditor;

public abstract class ModCompatEditor implements IAsmEditor {

    private static final HashMap<String, IEnabler> compatMap = new HashMap<>();

    private final String className, methodName, methodDesc;

    public ModCompatEditor(String className, String methodName, String methodDesc) {
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getMethodDesc() {
        return methodDesc;
    }

    public ModCompatEditor addConflict(String mixin, IEnabler enabler) {
        compatMap.put(mixin, enabler);
        return this;
    }

    @SuppressWarnings("unused")
    public static List<String> apply_compat(List<String> mixins) {
        for (String mixin : new ArrayList<>(mixins)) {
            if (compatMap.containsKey(mixin)) {
                if (compatMap.get(mixin)
                    .isEnabled()) {
                    mixins.remove(mixin);
                }
            }
        }
        return mixins;
    }
}
