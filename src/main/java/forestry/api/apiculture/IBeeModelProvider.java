/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture;

import forestry.api.apiculture.genetics.EnumBeeType;
import net.minecraft.client.resources.model.ModelResourceLocation;

public interface IBeeModelProvider {

	ModelResourceLocation getModel(EnumBeeType type);
}
