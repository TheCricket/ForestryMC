package forestry.arboriculture;

import forestry.api.arboriculture.*;
import forestry.core.utils.Translator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class WoodHelper {

	public static Component getDisplayName(IWoodTyped wood, IWoodType woodType) {
		WoodBlockKind blockKind = wood.getBlockKind();

		Component displayName;

		if (woodType instanceof EnumForestryWoodType) {
			String customUnlocalizedName = "block.forestry." + blockKind + "." + woodType;
			if (Translator.canTranslateToLocal(customUnlocalizedName)) {
				displayName = new TranslatableComponent(customUnlocalizedName);
			} else {
				displayName = new TranslatableComponent("for." + blockKind + ".grammar", new TranslatableComponent("for.trees.woodType." + woodType));
			}
		} else if (woodType instanceof EnumVanillaWoodType) {
			displayName = TreeManager.woodAccess.getStack(woodType, blockKind, false).getHoverName();
		} else {
			throw new IllegalArgumentException("Unknown wood type: " + woodType);
		}

		if (wood.isFireproof()) {
			displayName = new TranslatableComponent("block.forestry.fireproof", displayName);
		}

		return displayName;
	}
}
