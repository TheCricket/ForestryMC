package forestry.core.items.definitions;

import forestry.core.data.ForestryTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ToolTier implements Tier {
	BRONZE(3, 200, 7.0F, 2.5F, 10, () -> {
		return Ingredient.of(ForestryTags.Items.INGOTS_BRONZE);
	});

	private final int level;
	private final int uses;
	private final float speed;
	private final float damage;
	private final int enchantmentValue;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	ToolTier(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
		this.level = level;
		this.uses = uses;
		this.speed = speed;
		this.damage = damage;
		this.enchantmentValue = enchantmentValue;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}

	@Override
	public int getUses() {
		return this.uses;
	}

	@Override
	public float getSpeed() {
		return this.speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.damage;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
}
