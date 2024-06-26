package me.imtoggle.quickswitch.element

import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.RawMouseEvent
import cc.polyfrost.oneconfig.gui.OneConfigGui
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.internal.assets.SVGs
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette

@Suppress("UnstableAPIUsage")
class KeyBindButton(
    private val keyBind: OneKeyBind
) : BasicButton(256, 32, SVGs.KEYSTROKE, ALIGNMENT_JUSTIFIED, ColorPalette.SECONDARY) {
    private val allKeysReleased get() = keyBind.size != 0 && !keyBind.isActive
    private val recording get() = isToggled

    private fun stopRecording() {
        isToggled = false
        OneConfigGui.INSTANCE.allowClose = true
    }

    init {
        setToggleable(true)
        setClickAction {
            OneConfigGui.INSTANCE.allowClose = !recording
        }
        EventManager.INSTANCE.register(this)
    }

    @Subscribe
    private fun onMouse(event: RawMouseEvent) {
        if (recording && event.state == 1) {
            keyBind.addKey(event.button, true)
        }
    }

    override fun update(x: Float, y: Float, inputHandler: InputHandler) {
        super.update(x, y, inputHandler)

        if (recording) whileListening()
        text = getDisplayText()
    }

    private fun whileListening() {
        when {
            isClicked -> keyBind.clearKeys()
            allKeysReleased -> stopRecording()
        }
    }

    private fun getDisplayText() = keyBind.display.ifEmpty {
        if (recording) "Recording... (ESC to clear)"
        else "None"
    }

    fun isKeyTyped(keyCode: Int): Boolean {
        if (!recording) return false

        when (keyCode) {
            UKeyboard.KEY_ESCAPE -> {
                keyBind.clearKeys()
                stopRecording()
            }

            else -> keyBind.addKey(keyCode)
        }

        return true
    }
}