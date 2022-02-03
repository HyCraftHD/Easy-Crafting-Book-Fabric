package net.hycrafthd.easy_crafting_book;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.hycrafthd.easy_crafting_book.mixin.KeyBindingMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public class EasyCraftingBookMod implements ClientModInitializer {
	
	public static final Logger LOGGER = LogManager.getLogger("Easy Crafting Book");
	
	public static boolean CRAFT_AND_DROP_NEXT_RECIPE;
	
	public static KeyBinding FAST_CRAFTING_KEY;
	public static KeyBinding FAST_CRAFTING_KEY_STACK;
	
	@Override
	public void onInitializeClient() {
		LOGGER.info("Initialize easy crafting book mod");
		
		new RecipeBookResults();
		
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		
		FAST_CRAFTING_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.easy_crafting_book.key", Type.KEYSYM, GLFW.GLFW_KEY_SPACE, "category.easy_crafting_book"));
		FAST_CRAFTING_KEY_STACK = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.easy_crafting_book.key_stack", Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_3, "category.easy_crafting_book"));
	}
	
	public static boolean handleRecipeBookClick(Recipe<?> recipe, boolean craftAll) {
		final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		
		final Identifier id = recipe.getId();
		
		LOGGER.info("Current recipe id is: {}", id);
		
		if (config.allowedRecipes.contains(id.toString())) {
			final boolean pressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyBindingMixin) (Object) FAST_CRAFTING_KEY).getBoundKey().getCode());
			final boolean pressedStack = GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyBindingMixin) (Object) FAST_CRAFTING_KEY_STACK).getBoundKey().getCode()) == 1;
			
			CRAFT_AND_DROP_NEXT_RECIPE = pressed || pressedStack;
			
			if (pressedStack) {
				craftAll = true;
			}
		}
		return craftAll;
	}
	
	public static void handleCrafting(CraftingScreenHandler instance) {
		final MinecraftClient minecraft = MinecraftClient.getInstance();
		final ClientPlayerEntity player = minecraft.player;
		
		if (EasyCraftingBookMod.CRAFT_AND_DROP_NEXT_RECIPE) {
			EasyCraftingBookMod.CRAFT_AND_DROP_NEXT_RECIPE = false;
			
			System.out.println("Crafting fast now");
			
			for (int index = 0; index < 64; index++) {
				minecraft.interactionManager.clickSlot(instance.syncId, 0, 1, SlotActionType.THROW, player);
			}
			
			minecraft.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(instance.syncId, instance.nextRevision(), 0, 0, SlotActionType.QUICK_MOVE, new ItemStack(Items.BEDROCK), new Int2ObjectOpenHashMap<ItemStack>()));
		}
	}
}
