/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture;

import forestry.api.core.IArmorNaturalist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ArboricultureCapabilities {
	/**
	 * Capability for {@link IArmorNaturalist}.
	 */
	public static Capability<IArmorNaturalist> ARMOR_NATURALIST = CapabilityManager.get(new CapabilityToken<>() {});
}
