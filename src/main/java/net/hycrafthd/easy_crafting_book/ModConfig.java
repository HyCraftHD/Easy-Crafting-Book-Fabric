package net.hycrafthd.easy_crafting_book;

import java.util.ArrayList;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "easy_crafting_books")
public class ModConfig implements ConfigData {
	
	public ArrayList<String> allowedRecipes = new ArrayList<>();
	
}
