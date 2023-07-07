package genetics.organism;

import genetics.api.individual.IIndividual;
import genetics.api.organism.IOrganismHandler;
import genetics.api.root.IIndividualRoot;
import genetics.api.root.IRootDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Supplier;

public class OrganismHandler<I extends IIndividual> implements IOrganismHandler<I> {
	public static final String INDIVIDUAL_KEY = "Individual";
	private final IRootDefinition<? extends IIndividualRoot<I>> optionalRoot;
	private final Supplier<ItemStack> stack;

	public OrganismHandler(IRootDefinition<? extends IIndividualRoot<I>> optionalRoot, Supplier<ItemStack> stack) {
		this.optionalRoot = optionalRoot;
		this.stack = stack;
	}

	@Override
	public ItemStack createStack(I individual) {
		ItemStack itemStack = stack.get();
		itemStack.addTagElement(INDIVIDUAL_KEY, individual.write(new CompoundTag()));
		return itemStack;
	}

	@Override
	public Optional<I> createIndividual(ItemStack itemStack) {
		CompoundTag tagCompound = itemStack.getTagElement(INDIVIDUAL_KEY);
		if (tagCompound == null || !optionalRoot.isPresent()) {
			return Optional.empty();
		}
		IIndividualRoot<I> root = this.optionalRoot.get();
		return Optional.of(root.create(tagCompound));
	}

	@Override
	public boolean setIndividual(ItemStack itemStack, I individual) {
		itemStack.addTagElement(INDIVIDUAL_KEY, individual.write(new CompoundTag()));
		return true;
	}

	@Override
	public void setIndividualData(ItemStack itemStack, CompoundTag compound) {
		itemStack.addTagElement(INDIVIDUAL_KEY, compound);
	}

	@Override
	public CompoundTag getIndividualData(ItemStack itemStack) {
		return itemStack.getTagElement(INDIVIDUAL_KEY);
	}
}
