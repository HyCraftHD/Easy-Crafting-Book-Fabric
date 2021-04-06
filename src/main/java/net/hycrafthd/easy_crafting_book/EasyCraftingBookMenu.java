package net.hycrafthd.easy_crafting_book;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;

public class EasyCraftingBookMenu implements ModMenuApi {
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		System.out.println("________________________________________________________");
		return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
	}
}