package net.hycrafthd.easy_crafting_book.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.hycrafthd.easy_crafting_book.EasyCraftingBookMod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.recipe.Recipe;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	
	@Shadow
	private ClientPlayNetworkHandler networkHandler;
	
	@Inject(method = "clickRecipe(ILnet/minecraft/recipe/Recipe;Z)V", cancellable = true, at = @At("HEAD"))
	private void handleSpaceKeyDownWhenRecipeBookIsCalled(int syncId, Recipe<?> recipe, boolean craftAll, CallbackInfo callback) {
		callback.cancel();
		craftAll = EasyCraftingBookMod.handleRecipeBookClick(recipe, craftAll);
		networkHandler.sendPacket(new CraftRequestC2SPacket(syncId, recipe, craftAll));
	}
}
