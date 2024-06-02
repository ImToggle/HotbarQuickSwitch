package me.imtoggle.quickswitch.mixin;

import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.internal.config.core.KeyBindHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(KeyBindHandler.class)
public interface KeyBindHandlerAccessor {

    @Accessor
    ConcurrentHashMap<Map.Entry<Field, Object>, OneKeyBind> getKeyBinds();

}
