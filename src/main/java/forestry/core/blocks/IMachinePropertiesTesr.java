/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.blocks;

import forestry.core.render.IForestryRendererProvider;
import forestry.core.tiles.TileForestry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface IMachinePropertiesTesr<T extends TileForestry> extends IMachineProperties<T> {
	ResourceLocation getParticleTexture();

	@Nullable
	IForestryRendererProvider<? super T> getRenderer();
	
	@Nullable
	ModelLayerLocation getModelLayer();
}
