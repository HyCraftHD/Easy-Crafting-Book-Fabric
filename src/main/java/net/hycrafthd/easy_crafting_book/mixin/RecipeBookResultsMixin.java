package net.hycrafthd.easy_crafting_book.mixin;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.hycrafthd.easy_crafting_book.EasyCraftingBookMod;
import net.minecraft.client.gui.screen.recipebook.AnimatedResultButton;
import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.recipe.Recipe;

@Mixin(RecipeBookResults.class)
public abstract class RecipeBookResultsMixin {
	
	@Shadow
	private List<AnimatedResultButton> resultButtons;
	@Shadow
	private Recipe<?> lastClickedRecipe;
	@Shadow
	private RecipeResultCollection resultCollection;
	
	@Inject(method = "mouseClicked", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookResults;resultButtons:Ljava/util/List;", shift = Shift.BY, by = -2, ordinal = 0))
	private void changeTrigger(double mouseX, double mouseY, int button, int areaLeft, int areaTop, int areaWidth, int areaHeight, CallbackInfoReturnable<Boolean> callback) {
		int code = ((KeyBindingMixin) (Object) EasyCraftingBookMod.FAST_CRAFTING_KEY_STACK).getBoundKey().getCode();
		
		if (code < 1) {
			code = 2;
		}
		
		if (button == code) {
			final Iterator<AnimatedResultButton> iterator = this.resultButtons.iterator();
			
			AnimatedResultButton animatedResultButton = null;
			
			do {
				if (!iterator.hasNext()) {
					break;
				}
				
				animatedResultButton = (AnimatedResultButton) iterator.next();
			} while (!animatedResultButton.mouseClicked(mouseX, mouseY, 0));
			
			if (animatedResultButton != null && animatedResultButton.getResultCollection() != null) {
				lastClickedRecipe = animatedResultButton.currentRecipe();
				resultCollection = animatedResultButton.getResultCollection();
				
				callback.setReturnValue(true);
			} else {
				callback.setReturnValue(false);
			}
		}
	}
	
}
