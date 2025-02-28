package org.worksale.models.users;

public class SettingsDTO {
    private Boolean darkMode;
    private Boolean isLanguageGerman;

    public SettingsDTO(boolean darkMode, Boolean isLanguageGerman) {
        this.darkMode = darkMode;
        this.isLanguageGerman = isLanguageGerman;
    }

    public SettingsDTO() {
    }

    public Boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public Boolean isLanguageGerman() {
        return isLanguageGerman;
    }

    public void setLanguageGerman(boolean isLanguageGerman) {
        this.isLanguageGerman = isLanguageGerman;
    }
}