package forestry.modules.features;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public enum FeatureType {
	MACHINE(ForgeRegistries.BLOCKS),
	FLUID(ForgeRegistries.FLUIDS),
	BLOCK(ForgeRegistries.BLOCKS),
	ENTITY(ForgeRegistries.ENTITY_TYPES),
	ITEM(ForgeRegistries.ITEMS),
	TILE(ForgeRegistries.BLOCK_ENTITY_TYPES),
	CONTAINER(ForgeRegistries.MENU_TYPES);

	public final IForgeRegistry superType;

	FeatureType(IForgeRegistry superType) {
		this.superType = superType;
	}
}
