/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.storage;

import forestry.core.config.Constants;
import forestry.storage.BackpackMode;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum EnumBackpackType implements StringRepresentable {
	NORMAL, WOVEN, NATURALIST;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	public ModelResourceLocation getLocation(BackpackMode mode) {
		String typeName = getSerializedName();
		if (this == NATURALIST) {
			typeName = NORMAL.getSerializedName();
		}
		return new ModelResourceLocation(Constants.MOD_ID + ":backpacks/" + typeName + "_" + mode.getSerializedName(), "inventory");
	}
}
