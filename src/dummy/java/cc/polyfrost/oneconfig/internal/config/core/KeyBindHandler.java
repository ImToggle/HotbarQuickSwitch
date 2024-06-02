package cc.polyfrost.oneconfig.internal.config.core;

import cc.polyfrost.oneconfig.config.core.OneKeyBind;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyBindHandler {
    public static final KeyBindHandler INSTANCE = null;
    private final ConcurrentHashMap<Map.Entry<Field, Object>, OneKeyBind> keyBinds = new ConcurrentHashMap<>();
}
