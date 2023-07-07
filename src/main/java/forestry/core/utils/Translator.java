package forestry.core.utils;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Supplier;

public class Translator {
	private Translator() {}

	public static String translateToLocal(String key) {
		return Language.getInstance().getOrDefault(key);
	}

	public static boolean canTranslateToLocal(String key) {
		return Language.getInstance().has(key);
	}

	public static String translateToLocalFormatted(String key, Object... format) {
		return new TranslatableComponent(key, format).getString();
	}

	public static Component tryTranslate(String optionalKey, String defaultKey) {
		return tryTranslate(() -> new TranslatableComponent(optionalKey), () -> new TranslatableComponent(defaultKey));
	}

	public static Component tryTranslate(String optionalKey, Supplier<Component> defaultKey) {
		return tryTranslate(() -> new TranslatableComponent(optionalKey), defaultKey);
	}

	/**
	 * Tries to translate the optional key component. Returns the default key component if the first can't be translated.
	 *
	 * @return The optional component if it can be translated the other component otherwise.
	 */
	private static Component tryTranslate(Supplier<TranslatableComponent> optionalKey, Supplier<Component> defaultKey) {
		TranslatableComponent component = optionalKey.get();
		if (canTranslate(component)) {
			return component;
		} else {
			return defaultKey.get();
		}
	}

	public static boolean canTranslate(TranslatableComponent component) {
		String translatedText = component.getString();
		return !translatedText.startsWith(component.getKey());
	}
}
