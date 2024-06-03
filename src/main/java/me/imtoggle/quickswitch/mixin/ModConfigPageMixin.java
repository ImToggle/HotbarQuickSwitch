package me.imtoggle.quickswitch.mixin;

import cc.polyfrost.oneconfig.config.elements.OptionPage;
import cc.polyfrost.oneconfig.gui.pages.ModConfigPage;
import cc.polyfrost.oneconfig.utils.InputHandler;
import me.imtoggle.quickswitch.Selector;
import me.imtoggle.quickswitch.config.ModConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModConfigPage.class, remap = false)
public class ModConfigPageMixin {

    @Shadow @Final private OptionPage page;

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 2)
    private int optionY(int value) {
        if (this.page.mod == ModConfig.INSTANCE.mod) {
            return value + 48;
        }
        return value;
    }

    @Inject(method = "drawStatic", at = @At("HEAD"), cancellable = true)
    private void drawSelector(long vg, int x, int y, InputHandler inputHandler, CallbackInfoReturnable<Integer> cir) {
        if (this.page.mod == ModConfig.INSTANCE.mod) {
            Selector.INSTANCE.draw(vg, x + 32, y + 16, inputHandler);
            cir.setReturnValue(64);
        }
    }
}
