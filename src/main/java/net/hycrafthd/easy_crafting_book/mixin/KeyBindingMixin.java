package net.hycrafthd.easy_crafting_book.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Mixin(KeyBinding.class)
public interface KeyBindingMixin {
	
	@Accessor("boundKey")
	public abstract InputUtil.Key getBoundKey();
	
}
