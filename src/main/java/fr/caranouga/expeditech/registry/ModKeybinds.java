package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public class ModKeybinds {
    public static final Map<KeyBinding, BiConsumer<KeyBinding, KeyAction>> KEYBINDS = new HashMap<>();

    private static KeyBinding testKey;

    // region Utility methods
    private static KeyBinding registerKey(String name, int keyCode, String category, BiConsumer<KeyBinding, KeyAction> action) {
        KeyBinding key = new KeyBinding("key." + Expeditech.MODID + "." + name, keyCode, "key.category." + Expeditech.MODID + "." + category);
        ClientRegistry.registerKeyBinding(key);

        KEYBINDS.put(key, action);

        return key;
    }
    // endregion

    public static void register(){
        testKey = registerKey("test", KeyEvent.VK_G, "test", (key, action) -> {
            // Action to perform when the key is pressed
            Expeditech.LOGGER.info("Test key pressed: {} with action: {}", key.getName(), action);
        });
    }

    public static void handleKey(int keyCode, int action) {
        for(Map.Entry<KeyBinding, BiConsumer<KeyBinding, KeyAction>> entry : KEYBINDS.entrySet()) {
            KeyBinding key = entry.getKey();
            BiConsumer<KeyBinding, KeyAction> actionConsumer = entry.getValue();

            if(key.getKey().getValue() == keyCode) {
                actionConsumer.accept(key, KeyAction.fromValue(action));
            }
        }
    }

    protected enum KeyAction {
        HOLD(2),
        PRESS(1),
        RELEASE(0);

        private final int value;

        KeyAction(int value) {
            this.value = value;
        }

        public static KeyAction fromValue(int value) {
            for (KeyAction action : values()) {
                if (action.value == value) {
                    return action;
                }
            }
            throw new IllegalArgumentException("Invalid key action value: " + value);
        }
    }
}
