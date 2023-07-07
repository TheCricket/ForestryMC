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
package forestry.core.proxy;

import forestry.core.models.ClientManager;
import forestry.core.render.TextureManagerForestry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class ProxyClient extends ProxyCommon {

	@Override
	public void registerBlock(Block block) {
		ClientManager.getInstance().registerBlockClient(block);
		if (Minecraft.getInstance() != null) {
			TextureManagerForestry.getInstance().registerBlock(block);
		}
	}

	@Override
	public void registerItem(Item item) {
		ClientManager.getInstance().registerItemClient(item);
		if (Minecraft.getInstance() != null) {
			TextureManagerForestry.getInstance().registerItem(item);
		}
	}

	@Override
	public File getForestryRoot() {
		return Minecraft.getInstance().gameDirectory;
	}

}
