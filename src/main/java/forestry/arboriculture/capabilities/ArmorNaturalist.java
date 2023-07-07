package forestry.arboriculture.capabilities;

import forestry.api.core.IArmorNaturalist;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArmorNaturalist implements IArmorNaturalist {
	public static final ArmorNaturalist INSTANCE = new ArmorNaturalist();

	protected ArmorNaturalist() {

	}

	@Override
	public boolean canSeePollination(Player player, ItemStack armor, boolean doSee) {
		return true;
	}
}
