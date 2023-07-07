package forestry.farming.compat;

import forestry.api.circuits.ICircuit;
import forestry.api.farming.IFarmProperties;
import net.minecraft.world.item.ItemStack;

public record FarmingInfoRecipe(ItemStack tube,
								IFarmProperties properties,
								ICircuit circuit) {


}
