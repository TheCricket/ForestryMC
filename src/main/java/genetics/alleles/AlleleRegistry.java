package genetics.alleles;

import com.google.common.collect.HashMultimap;
import genetics.Genetics;
import genetics.api.alleles.Allele;
import genetics.api.alleles.*;
import genetics.api.classification.IClassification;
import genetics.api.individual.IChromosomeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.*;

public class AlleleRegistry implements IAlleleRegistry {

	/* ALLELES */
	private final Map<ResourceLocation, IAllele> registry;
	private final HashMultimap<IChromosomeType, IAllele> allelesByType = HashMultimap.create();
	private final HashMultimap<IAllele, IChromosomeType> typesByAllele = HashMultimap.create();
	private final ResourceLocation DEFAULT_NAME = new ResourceLocation(Genetics.MOD_ID, "default");
	private final IAllele defaultAllele = new Allele("default", false).setRegistryName(DEFAULT_NAME);
	/*
	 * Internal Set of all alleleHandlers, which trigger when an allele or branch is registered
	 */
	private final Set<IAlleleHandler> handlers = new HashSet<>();

	public AlleleRegistry() {
		//Cast the registry to the class type so we can get the ids of the alleles
		this.registry = new HashMap<>();
		registerHandler(AlleleHelper.INSTANCE);
	}

	@Override
	public <A extends IAllele> A registerAllele(A allele, IChromosomeType... types) {
		addValidAlleleTypes(allele, types);
		if (!registry.containsKey(allele.getRegistryName())) {
			registry.put(allele.getRegistryName(), allele);
			handlers.forEach(h -> h.onRegisterAllele(allele));
		}
		if (allele instanceof IAlleleSpecies) {
			IClassification branch = ((IAlleleSpecies) allele).getBranch();
			branch.addMemberSpecies((IAlleleSpecies) allele);
		}
		return allele;
	}

	@Override
	public IAlleleRegistry addValidAlleleTypes(ResourceLocation registryName, IChromosomeType... types) {
		Optional<IAllele> alleleOptional = getAllele(registryName);
		alleleOptional.ifPresent(allele -> addValidAlleleTypes(allele, types));
		return this;
	}

	@Override
	public <V> IAlleleValue<V> registerAllele(String category, String valueName, V value, boolean dominant, IChromosomeType... types) {
		return registerAllele(new AlleleCategorizedValue<>(ModLoadingContext.get().getActiveContainer().getModId(), category, valueName, value, dominant), types);
	}

	@Override
	public <V> IAlleleValue<V> registerAllele(IAlleleData<V> value, IChromosomeType... types) {
		IAlleleValue<V> alleleValue = registerAllele(value.createAllele(), types);
		handlers.forEach(handler -> handler.onRegisterData(alleleValue, value));
		return alleleValue;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> IAlleleValue<V>[] registerAlleles(IAlleleData<V>[] values, IChromosomeType... types) {
		IAlleleValue<V>[] alleles = new IAlleleValue[values.length];
		for (int i = 0; i < values.length; i++) {
			alleles[i] = registerAllele(values[i], types);
		}
		return alleles;
	}

	@Override
	public IAlleleRegistry addValidAlleleTypes(IAllele allele, IChromosomeType... types) {
		handlers.forEach(h -> h.onAddTypes(allele, types));
		for (IChromosomeType chromosomeType : types) {
			if (!isValidAllele(allele, chromosomeType)) {
				throw new IllegalArgumentException("Allele (" + allele + ") is not a valid allele for the chromosome type (" + chromosomeType + ").");
			}
			allelesByType.put(chromosomeType, allele);
			typesByAllele.put(allele, chromosomeType);
		}
		return this;
	}

	@Override
	public IAllele getDefaultAllele() {
		return defaultAllele;
	}

	@Override
	public ResourceLocation getDefaultKey() {
		return DEFAULT_NAME;
	}

	@Override
	public Collection<IChromosomeType> getChromosomeTypes(IAllele allele) {
		return typesByAllele.get(allele);
	}

	@Override
	public Collection<IAllele> getRegisteredAlleles(IChromosomeType type) {
		return allelesByType.get(type);
	}

	@Override
	public Collection<IAllele> getRegisteredAlleles() {
		return registry.values();
	}

	@Override
	public Collection<ResourceLocation> getRegisteredNames() {
		return registry.keySet();
	}

	@Override
	public Optional<IAllele> getAllele(ResourceLocation location) {
		return Optional.ofNullable(registry.get(location));
	}

	@Override
	public boolean isValidAllele(IAllele allele, IChromosomeType type) {
		return type.isValid(allele);
	}

	@Override
	public void registerHandler(IAlleleHandler handler) {
		this.handlers.add(handler);
	}

	@Override
	public Set<IAlleleHandler> getHandlers() {
		return handlers;
	}

	/* BLACKLIST */
	private final ArrayList<ResourceLocation> blacklist = new ArrayList<>();

	@Override
	public void blacklistAllele(ResourceLocation registryName) {
		blacklist.add(registryName);
	}

	@Override
	public Collection<ResourceLocation> getAlleleBlacklist() {
		return Collections.unmodifiableCollection(blacklist);
	}

	@Override
	public boolean isBlacklisted(ResourceLocation registryName) {
		return blacklist.contains(registryName);
	}

}
