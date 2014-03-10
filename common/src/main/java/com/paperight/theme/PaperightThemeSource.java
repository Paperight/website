package com.paperight.theme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;

public class PaperightThemeSource implements ThemeSource {
	
	@Autowired
	private ThemeService themeService;
		
	@Override
	public Theme getTheme(String themeName) {
		return themeService.getCurrentTheme();	
	}

}
