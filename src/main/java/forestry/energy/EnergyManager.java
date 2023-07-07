package forestry.energy;

import forestry.api.core.INbtReadable;
import forestry.api.core.INbtWritable;
import forestry.core.config.Config;
import forestry.core.network.IStreamable;
import forestry.core.network.PacketBufferForestry;
import forestry.energy.compat.EnergyStorageWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyManager extends EnergyStorage implements IStreamable, INbtReadable, INbtWritable {
	private EnergyTransferMode externalMode = EnergyTransferMode.BOTH;

	public EnergyManager(int maxTransfer, int capacity) {
		super(EnergyHelper.scaleForDifficulty(capacity), EnergyHelper.scaleForDifficulty(maxTransfer), EnergyHelper.scaleForDifficulty(maxTransfer));
	}

	public void setExternalMode(EnergyTransferMode externalMode) {
		this.externalMode = externalMode;
	}

	public EnergyTransferMode getExternalMode() {
		return externalMode;
	}

	@Override
	public void read(CompoundTag nbt) {
		final int energy;
		if (nbt.contains("EnergyManager")) { // legacy
			CompoundTag energyManagerNBT = nbt.getCompound("EnergyManager");
			CompoundTag energyStorageNBT = energyManagerNBT.getCompound("EnergyStorage");
			energy = energyStorageNBT.getInt("Energy");
		} else {
			energy = nbt.getInt("Energy");
		}

		setEnergyStored(energy);
	}

	@Override
	public CompoundTag write(CompoundTag nbt) {
		nbt.putInt("Energy", energy);
		return nbt;
	}

	@Override
	public void writeData(PacketBufferForestry data) {
		data.writeVarInt(this.energy);
	}

	@Override
	public void readData(PacketBufferForestry data) {
		int energyStored = data.readVarInt();
		setEnergyStored(energyStored);
	}

	public int getMaxEnergyReceived() {
		return this.maxReceive;
	}

	/**
	 * Drains an amount of energy, due to decay from lack of work or other factors
	 */
	public void drainEnergy(int amount) {
		setEnergyStored(energy - amount);
	}

	/**
	 * Creates an amount of energy, generated by engines
	 */
	public void generateEnergy(int amount) {
		setEnergyStored(energy + amount);
	}

	public void setEnergyStored(int energyStored) {
		this.energy = energyStored;
		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	public boolean hasCapability(Capability<?> capability) {
		return Config.enableRF && capability == CapabilityEnergy.ENERGY ||
			Config.enableTesla && hasTeslaCapability(capability) ||
			Config.enableMJ && hasMjCapability(capability);
	}

	private boolean hasTeslaCapability(Capability<?> capability) {
		return false;
		//		return capability == TeslaHelper.TESLA_PRODUCER && externalMode.canExtract() ||
		//			capability == TeslaHelper.TESLA_CONSUMER && externalMode.canReceive() ||
		//			capability == TeslaHelper.TESLA_HOLDER;
	}

	private boolean hasMjCapability(Capability<?> capability) {
		return false;//capability == MjHelper.CAP_READABLE ||
		//			capability == MjHelper.CAP_CONNECTOR ||
		//			capability == MjHelper.CAP_PASSIVE_PROVIDER && externalMode.canExtract() ||
		//			capability == MjHelper.CAP_REDSTONE_RECEIVER && externalMode.canReceive() ||
		//			capability == MjHelper.CAP_RECEIVER && externalMode.canReceive();
	}

	public <T> LazyOptional<T> getCapability(Capability<T> capability) {
		if (!hasCapability(capability)) {
			return LazyOptional.empty();
		}
		if (capability == CapabilityEnergy.ENERGY) {
			IEnergyStorage energyStorage = new EnergyStorageWrapper(this, externalMode);
			return LazyOptional.of(() -> energyStorage).cast();
		}
		return LazyOptional.empty();
	}

	public int calculateRedstone() {
		return Mth.floor(((float) energy / (float) capacity) * 14.0F) + (energy > 0 ? 1 : 0);
	}

}
