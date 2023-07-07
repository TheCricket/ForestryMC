package forestry.core.render;

import forestry.api.core.ISpriteRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Uploads the forestry gui icon texture sprites to the forestry gui atlas texture.
 *
 * @see TextureManagerForestry
 */
public class ForestrySpriteUploader extends TextureAtlasHolder implements ISpriteRegistry {
	private final Set<ResourceLocation> registeredSprites = new HashSet<>();

	public ForestrySpriteUploader(TextureManager manager, ResourceLocation atlasLocation, String prefix) {
		super(manager, atlasLocation, prefix);
	}

	public boolean addSprite(ResourceLocation location) {
		return this.registeredSprites.add(location);
	}

	@Override
	protected Stream<ResourceLocation> getResourcesToLoad() {
		return Collections.unmodifiableSet(this.registeredSprites).stream();
	}

	@Override
	public TextureAtlasSprite getSprite(ResourceLocation location) {
		return super.getSprite(location);
	}
}
