package forestry.core.genetics.root;

import com.mojang.authlib.GameProfile;
import genetics.api.alleles.IAlleleSpecies;
import genetics.api.individual.IIndividual;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IResearchPlugin {
	float getResearchSuitability(IAlleleSpecies species, ItemStack itemstack);

	default NonNullList<ItemStack> getResearchBounty(IAlleleSpecies species, Level world, GameProfile researcher, IIndividual individual, int bountyLevel) {
		return NonNullList.create();
	}
}
