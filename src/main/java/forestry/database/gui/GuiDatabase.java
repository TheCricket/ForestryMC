package forestry.database.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import forestry.core.config.Constants;
import forestry.core.gui.ContainerForestry;
import forestry.core.gui.Drawable;
import forestry.core.gui.GuiAnalyzerProvider;
import forestry.core.gui.slots.SlotFilteredInventory;
import forestry.core.gui.widgets.IScrollable;
import forestry.core.gui.widgets.WidgetScrollBar;
import forestry.database.DatabaseHelper;
import forestry.database.DatabaseItem;
import forestry.database.gui.buttons.DatabaseButton;
import forestry.database.gui.buttons.GuiDatabaseButton;
import forestry.database.gui.widgets.WidgetDatabaseSlot;
import forestry.database.tiles.TileDatabase;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiDatabase extends GuiAnalyzerProvider<ContainerDatabase> implements IScrollable {
	/* Attributes - Constants */
	private static final ResourceLocation CREATIVE_TABS = new ResourceLocation(Constants.TEXTURE_PATH_GUI + "/container/creative_inventory/tabs.png");
	private static final Drawable SCROLLBAR_SLIDER = new Drawable(CREATIVE_TABS, 232, 0, 12, 15);
	/*  Attributes - Final */
	public final TileDatabase tile;
	private final ArrayList<WidgetDatabaseSlot> slots;
	private final ArrayList<DatabaseItem> sorted = new ArrayList<>();
	/* Attributes - Gui Elements */
	@Nullable
	private EditBox searchField;
	private WidgetScrollBar scrollBar;
	/* Attributes - State */
	private boolean markedForSorting;
	@Nullable
	private DatabaseItem selectedItem;

	/* Constructors */
	public GuiDatabase(ContainerDatabase container, Inventory inv, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/database_inventory.png", container, inv, container.getTile(), 7, 140, 20, true, container.getTile().getInternalInventory().getContainerSize(), 0);
		this.tile = container.getTile();

		slots = new ArrayList<>();
		imageWidth = 218;
		imageHeight = 202;
		//Start at index 36, because all slots before 36 are player inventory slots
		Iterator<Slot> slotIterator = container.slots.listIterator(ContainerForestry.PLAYER_INV_SLOTS);
		while (slotIterator.hasNext()) {
			Slot slot = slotIterator.next();
			if (slot instanceof SlotFilteredInventory slotDatabase) {
				slotDatabase.setChangeWatcher(this);
			}
		}
		for (int i = 0; i < 24; i++) {
			WidgetDatabaseSlot slot = new WidgetDatabaseSlot(widgetManager);
			slots.add(slot);
			widgetManager.add(slot);
		}
		widgetManager.add(this.scrollBar = new WidgetScrollBar(widgetManager, 196, 19, 12, 90, SCROLLBAR_SLIDER));
		this.scrollBar.setParameters(this, 0, tile.getContainerSize() / 4 - 6, 1);
		analyzer.init();
		analyzer.setSelectedSlot(-1);
	}


	/* Methods */
	@Nullable
	public DatabaseItem getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @return the count of all valid item in {@link #sorted}
	 */
	public int getSize() {
		return sorted.size() - 1;//subtract one because the last entry is the empty item
	}

	public int getRealSize() {
		return sorted.size();
	}

	public ItemStack getSelectedItemStack() {
		if (selectedItem == null) {
			return ItemStack.EMPTY;
		}
		return selectedItem.itemStack;
	}

	public void markForSorting() {
		markedForSorting = true;
	}

	private void updateItems(String searchText) {
		if (markedForSorting) {
			sorted.clear();
			List<DatabaseItem> items = new ArrayList<>();
			boolean firstEmpty = false;
			for (int invIndex = 0; invIndex < tile.getContainerSize(); invIndex++) {
				ItemStack stack = tile.getItem(invIndex).copy();
				if (!stack.isEmpty()) {
					items.add(new DatabaseItem(stack, invIndex));
					continue;
				}
				if (!firstEmpty) {
					firstEmpty = true;
					items.add(new DatabaseItem(stack, invIndex));
				}
			}
			DatabaseHelper.update(searchText, items, sorted);
			analyzer.updateSelected();
			updateViewedItems();

			markedForSorting = false;
		}
	}

	@Nullable
	public DatabaseItem getItem(int index) {
		if (sorted.isEmpty() || sorted.size() <= index || index < 0) {
			return null;
		}
		return sorted.get(index);
	}

	private void updateViewedItems() {
		int currentRow = scrollBar.getValue();
		if (currentRow < 0) {
			currentRow = 0;
		}
		//The inventory index of the first slot.
		int slotStart = currentRow * 4;
		//The inventory index of the last slot.
		int slotEnd = (currentRow + 6) * 4;
		if (slotEnd > tile.getContainerSize()) {
			slotEnd = tile.getContainerSize();
		}
		//The row of the first slot
		byte startRow = (byte) (currentRow % 2);
		//The index of the empty slot in the list.
		int emptySlot = sorted.size() - 1;
		for (int invIndex = 0; invIndex < tile.getContainerSize(); invIndex++) {
			if (invIndex >= slotStart && invIndex < slotEnd) {
				int x = invIndex % 4;
				int y = invIndex / 4 - currentRow;
				int yOffset;
				int xOffset;
				yOffset = 25;
				if (startRow == 0) {
					xOffset = 17;
					if (y % 2 == 1) {
						xOffset = 38;
						yOffset = 38;
						y--;
					}
				} else {
					xOffset = 38;
					if (y % 2 == 1) {
						yOffset = 38;
						xOffset = 17;
						y--;
					}
				}
				int xPos = xOffset + x * 42;
				int yPos = yOffset + y / 2 * 25;
				//If the index is above the count of the valid items in the list, set the index to the same value like the index of the empty slot.
				int index = invIndex;
				if (sorted.size() <= index || sorted.isEmpty()) {
					index = emptySlot;
				}
				WidgetDatabaseSlot slot = slots.get(invIndex - slotStart);
				slot.update(xPos, yPos, index, index != emptySlot);
			}
		}
	}

	/* Methods - Implement Screen */
	@Override
	public void containerTick() {
		super.containerTick();
		this.searchField.tick();
	}

	@Override
	public void init() {
		super.init();

		this.searchField = new EditBox(this.minecraft.font, this.leftPos + 101, this.topPos + 6, 80, this.minecraft.font.lineHeight, null);
		this.searchField.setMaxLength(50);
		this.searchField.setBordered(false);
		this.searchField.setTextColor(16777215);
		this.renderables.add(searchField);

		addRenderableWidget(new GuiDatabaseButton<>(leftPos - 18, topPos, DatabaseHelper.ascending, this, DatabaseButton.SORT_DIRECTION_BUTTON, b -> ((GuiDatabaseButton) b).onPressed()));    //TODO cast should be safe?

		updateViewedItems();
	}

	@Override
	public void render(PoseStack transform, int mouseX, int mouseY, float partialTicks) {
		String searchText = searchField != null ? searchField.getValue() : "";
		updateItems(searchText);
		super.render(transform, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			this.minecraft.player.closeContainer();
		}
		if (searchField != null && (searchField.keyPressed(key, scanCode, modifiers) || searchField.canConsumeInput())) {
			scrollBar.setValue(0);
			markForSorting();
			return true;
		}
		return super.keyPressed(key, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (super.charTyped(codePoint, modifiers)) {
			if (getFocused() == searchField) {
				scrollBar.setValue(0);
				markForSorting();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		Slot slot = getSlotAtPosition(mouseX, mouseY);
		if (slot != null && slot.getSlotIndex() == -1) {
			return false;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/* Methods - Implement ContainerScreen */
	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(transform, partialTicks, mouseX, mouseY);

		if (searchField != null) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			// RenderSystem.disableLighting();
			this.searchField.render(transform, mouseX, mouseY, partialTicks);
		}
	}

	/* Methods - Implement GuiForestry */
	@Override
	protected void drawBackground(PoseStack transform) {
		bindTexture(textureFile);

		blit(transform, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}

	@Override
	protected void addLedgers() {
	}

	/* Methods - Implement GuiForestryTitled */

	@Override
	protected boolean centeredTitle() {
		return false;
	}

	/* Methods - Implement IGeneticAnalyzerProvider */
	@Override
	protected void drawSelectedSlot(PoseStack transform, int selectedSlot) {
		//Currently not used
	}

	@Override
	public ItemStack getSpecimen(int index) {
		DatabaseItem item = getSelectedItem();
		if (item == null) {
			return ItemStack.EMPTY;
		}
		return item.itemStack;
	}

	@Override
	public boolean onUpdateSelected() {
		int index = selectedItem == null ? -1 : sorted.indexOf(selectedItem);
		if (index >= 0) {
			analyzer.setSelectedSlot(index);
			return true;
		}
		return false;
	}

	@Override
	public void onSelection(int index, boolean changed) {
		if (index < 0) {
			selectedItem = null;
		} else {
			this.selectedItem = sorted != null && index >= sorted.size() ? null : sorted.get(index);
		}
	}

	@Override
	public int getSelectedSlot(int index) {
		DatabaseItem item = getItem(index);
		if (item == null) {
			return -1;
		}
		return 1 + item.invIndex;
	}

	/* Methods - Implement IScrollable */
	@Override
	public void onScroll(int value) {
		updateViewedItems();
	}

	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return isHovering(0, 0, imageWidth, imageHeight, mouseX + leftPos, mouseY + topPos);
	}

	/* Methods - Implement ISlotChangeWatcher */
	@Override
	public void onSlotChanged(Container inventory, int slot) {
		super.onSlotChanged(inventory, slot);
		markForSorting();
	}
}
