package forestry.farming;

import forestry.api.farming.*;
import forestry.farming.logic.farmables.FarmableInfo;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class DummyFarmRegistry implements IFarmRegistry {

	@Override
	public void registerFarmables(String identifier, IFarmable... farmable) {
		//Dummy-Implementation
	}

	@Override
	public Collection<IFarmable> getFarmables(String identifier) {
		//Dummy-Implementation
		return Collections.emptyList();
	}

	@Override
	public IFarmableInfo getFarmableInfo(String identifier) {
		return new FarmableInfo(identifier);
	}

	@Override
	public IFarmProperties registerLogic(String identifier, IFarmProperties farmInstance) {
		return null;
	}

	@Override
	public IFarmPropertiesBuilder getPropertiesBuilder(String identifier) {
		//Dummy-Implementation
		return null;
	}

	@Override
	public void registerFertilizer(ItemStack itemStack, int value) {
		//Dummy-Implementation
	}

	@Override
	public int getFertilizeValue(ItemStack itemStack) {
		//Dummy-Implementation
		return 0;
	}

	@Nullable
	@Override
	public IFarmProperties getProperties(String identifier) {
		//Dummy-Implementation
		return null;
	}
}
