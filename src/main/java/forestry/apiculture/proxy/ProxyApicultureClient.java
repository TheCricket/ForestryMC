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
package forestry.apiculture.proxy;

import forestry.apiculture.entities.ParticleSnow;
import forestry.modules.IClientModuleHandler;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

@OnlyIn(Dist.CLIENT)
public class ProxyApicultureClient extends ProxyApiculture implements IClientModuleHandler {

	@Override
	public void setupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		// event.registerEntityRenderer(ApicultureEntities.APIARY_MINECART.entityType(), MinecartRenderer::new);
		// event.registerEntityRenderer(ApicultureEntities.BEE_HOUSE_MINECART.entityType(), MinecartRenderer::new);
	}

	@Override
	public void handleSprites(TextureStitchEvent.Post event) {
		TextureAtlas map = event.getAtlas();
		if (!map.location().equals(TextureAtlas.LOCATION_PARTICLES)) {
			return;
		}
		for (int i = 0; i < ParticleSnow.sprites.length; i++) {
			ParticleSnow.sprites[i] = map.getSprite(new ResourceLocation("forestry:particle/snow." + (i + 1)));
		}
	}
}
