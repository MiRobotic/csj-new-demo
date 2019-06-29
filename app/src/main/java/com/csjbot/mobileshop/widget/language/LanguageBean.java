package com.csjbot.mobileshop.widget.language;

/**
 * @author ShenBen
 * @date 2018/12/13 17:41
 * @email 714081644@qq.com
 */

public class LanguageBean {
    private String language;
    private boolean isChecked;
    private int languageDef;

    public LanguageBean(String language, int languageDef) {
        this.language = language;
        this.languageDef = languageDef;
    }

    public LanguageBean(String language, boolean isChecked) {
        this.language = language;
        this.isChecked = isChecked;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getLanguageDef() {
        return languageDef;
    }
}
