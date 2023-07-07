package forestry.core.gui.elements.text;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FormattedCharSequence;

class ProcessorText extends AbstractTextElement<FormattedCharSequence, ProcessorText> {

	public ProcessorText(FormattedCharSequence component) {
		super(component);
	}

	public ProcessorText(int xPos, int yPos, int width, int height, FormattedCharSequence component, boolean fitText) {
		super(xPos, yPos, width, height, component, fitText);
	}

	@Override
	public LabelElement setValue(Object text) {
		if (text instanceof FormattedCharSequence) {
			this.text = (FormattedCharSequence) text;
		}
		requestLayout();
		return this;
	}

	@Override
	protected int calcWidth(Font font) {
		return font.width(text);
	}

	@Override
	public void drawElement(PoseStack transform, int mouseX, int mouseY) {
		if (shadow) {
			FONT_RENDERER.drawShadow(transform, text, 0, 0, 0);
		} else {
			FONT_RENDERER.draw(transform, text, 0, 0, 0);
		}
	}
}
