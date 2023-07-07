package forestry.worktable.tiles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface ICrafterWorktable {

	ItemStack getResult(CraftingContainer inventory, Level world);

	boolean canTakeStack(int slotIndex);

	boolean onCraftingStart(Player player);

	void onCraftingComplete(Player player);

	@Nullable
	Recipe getRecipeUsed();
}
