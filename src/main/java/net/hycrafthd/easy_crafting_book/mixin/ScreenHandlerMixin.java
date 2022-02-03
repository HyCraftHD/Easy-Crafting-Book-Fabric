package net.hycrafthd.easy_crafting_book.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.hycrafthd.easy_crafting_book.EasyCraftingBookMod;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
	
	private final ScreenHandler instance = ((ScreenHandler) (Object) this);
	
	@Inject(method = "setStackInSlot(IILnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
	private void handleSetItem(int slot, int revision, ItemStack stack, CallbackInfo info) {
		if (instance instanceof CraftingScreenHandler && slot == 0) {
			EasyCraftingBookMod.handleCrafting((CraftingScreenHandler) instance);
		}
	}
	
}
