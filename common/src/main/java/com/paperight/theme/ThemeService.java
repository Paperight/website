package com.paperight.theme;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

	public static final String DEFAULT_THEME_NAME = "default";
	
	protected final Log logger = LogFactory.getLog(getClass());

	@Value("${theme.name:#{T(com.paperight.theme.ThemeService).DEFAULT_THEME_NAME}}")
	private String currentThemeName;
	
	private PaperightTheme currentTheme;
	private PaperightTheme defaultTheme;

	public PaperightTheme getCurrentTheme() {
		if (this.currentTheme == null) {
			this.currentTheme = initialiseTheme(getCurrentThemeName());
		}
		return this.currentTheme;
	}

	public void setCurrentTheme(PaperightTheme currentTheme) {
		this.currentTheme = currentTheme;
	}
	
	public PaperightTheme getDefaultTheme() {
		if (this.defaultTheme == null) {
			this.defaultTheme = initialiseTheme(DEFAULT_THEME_NAME);
		}
		return this.defaultTheme;
	}

	public String getCurrentThemeName() {
		return currentThemeName;
	}

	public void setCurrentThemeName(String currentThemeName) {
		this.currentThemeName = currentThemeName;
	}

	private PaperightTheme createTheme(String themeName) {
		PaperightTheme theme = new PaperightTheme();
		theme.setName(themeName);
		theme.persist();
		return theme;
	}
	
	private PaperightTheme initialiseTheme(String themeName) {
		if (StringUtils.isBlank(themeName)) {
			return null;
		}
		PaperightTheme theme = PaperightTheme.findByName(themeName);
		if (theme == null) {
			theme = createTheme(themeName);
		}
		return theme;
	}
}
