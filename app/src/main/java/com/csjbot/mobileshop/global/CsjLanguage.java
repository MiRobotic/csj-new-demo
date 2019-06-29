package com.csjbot.mobileshop.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Copyright (c) 2019, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2019/03/25 0025-21:47.
 * Email: puyz@example.com
 */

public class CsjLanguage {
    // 中文
    public final static int CHINESE = 0;
    // 英语-美国
    public final static int ENGELISH_US = 1;
    // 英语-英国
    public final static int ENGELISH_UK = 2;
    // 英语-澳大利亚
    public final static int ENGELISH_AUSTRALIA = 3;
    public final static int ENGELISH_CANADA = 3;
    // 英语-印度
    public final static int ENGELISH_INDIA = 4;
    // 日语
    public final static int JAPANESE = 5;
    // 韩语
    public final static int KOREAN = 6;
    // 法语-法国
    public final static int FRANCH_FRANCE = 7;
    // 法语-加拿大
    public final static int FRANCH_CANADA = 8;
    // 西班牙语-西班牙
    public final static int SPANISH_SPAIN = 9;
    // 西班牙语-拉美
    public final static int SPANISH_LATAM = 10;
    // 葡萄牙语-葡萄牙
    public static final int PORTUGUESE_PORTUGAL = 11;
    // 葡萄牙语-巴西
    public static final int PORTUGUESE_BRAZIL = 12;
    // 印尼语
    public static final int INDONESIA = 13;
    // 俄语
    public static final int RUSSIAN = 14;
    // 芬兰语
    public static final int FINNISH = 15;
    // 阿拉伯语
    public static final int ARABIC = 16;

    public static int CURRENT_LANGUAGE = CHINESE;

    public class NuanceLanguage {
        //Egypt埃及
        public static final String Arabic_Egypt = "ara-EGY";
        //Saudi Arabia沙特阿拉伯
        public static final String Arabic_SaudiArabia = "ara-SAU";
        //Middle East Area 中东地区
        public static final String Arabic_International = "ara-XWW";
        //Indonesia 印度尼西亚
        public static final String Bahasa_Indonesia = "ind-IDN";
        //中国南部，香港，等东南亚地区
        public static final String Cantonese_Traditional = "yue-CHN";
        //Catalan西班牙 加泰罗尼亚
        public static final String Catalan = "cat-ESP";
        //Croatia克罗地亚
        public static final String Croatian = "hrv-HRV";
        //Czech 捷克
        public static final String Czech = "ces-CZE";
        //Danish 丹麦
        public static final String Danish = "dan-DNK";
        //Dutch荷兰
        public static final String Dutch = "nld-NLD";
        //Australia澳大利亚
        public static final String English_Australia = "eng-AUS";
        //UK英国
        public static final String English_GB = "eng-GBR";
        //US美国
        public static final String English_US = "eng-USA";
        //India印度
        public static final String English_India = "eng-IND";
        //Finland芬兰
        public static final String Finnish = "fin-FIN";
        //Canada加拿大
        public static final String French_Canada = "fra-CAN";
        //France法国
        public static final String French_France = "fra-FRA";
        //Germany 德国
        public static final String German = "deu-DEU";
        //Greece希腊
        public static final String Greek = "ell-GRC";
        //以色列
        public static final String Hebrew = "heb-ISR";
        //印度
        public static final String Hindi = "hin-IND";
        //Hungary匈牙利
        public static final String Hungarian = "hun-HUN";
        //Italy意大利
        public static final String Italian = "ita-ITA";
        //Japan日本
        public static final String Japanese = "jpn-JPN";
        //Korea韩国
        public static final String Korean = "kor-KOR";
        //Malaysia马来西亚
        public static final String Malay = "zlm-MYS";
        //中国大陆
        public static final String Mandarin_China = "cmn-CHN";
        //中国台湾
        public static final String Mandarin_Taiwan = "cmn-TWN";
        //挪威
        public static final String Norwegian = "nor-NOR";
        //波兰
        public static final String Polish = "pol-POL";
        //巴西
        public static final String Portuguese_Brazil = "por-BRA";
        //葡萄牙
        public static final String Portuguese_Portugal = "por-PRT";
        //罗马尼亚
        public static final String Romanian = "ron-ROU";
        //俄罗斯
        public static final String Russian = "rus-RUS";
        //斯洛伐克
        public static final String Slovak = "slk-SVK";
        //西班牙
        public static final String Spanish_Spain = "spa-ESP";
        //南美州地区
        public static final String Spanish_LatAm = "spa-XLA";
        //瑞典
        public static final String Swedish = "swe-SWE";
        //泰国
        public static final String Thai = "tha-THA";
        //土耳其
        public static final String Turkish = "tur-TUR";
        //乌克兰
        public static final String Ukrainian = "ukr-UKR";
        //越南
        public static final String Vietnamese = "vie-VNM";
    }

    public static boolean isChinese() {
        return CURRENT_LANGUAGE == CHINESE;
    }

    public static boolean isEnglish() {
        return isEnglishUS() || isEnglishUK() || isEnglishAustralia() || isEnglishIndia();
    }

    public static boolean isEnglishUS() {
        return CURRENT_LANGUAGE == ENGELISH_US;
    }

    public static boolean isEnglishUK() {
        return CURRENT_LANGUAGE == ENGELISH_UK;
    }

    public static boolean isEnglishAustralia() {
        return CURRENT_LANGUAGE == ENGELISH_AUSTRALIA;
    }

    public static boolean isEnglishIndia() {
        return CURRENT_LANGUAGE == ENGELISH_INDIA;
    }

    public static boolean isJapanese() {
        return CURRENT_LANGUAGE == JAPANESE;
    }

    public static boolean isKOREAN() {
        return CURRENT_LANGUAGE == KOREAN;
    }

    public static boolean isFrenchFrance() {
        return CURRENT_LANGUAGE == FRANCH_FRANCE;
    }

    public static boolean isFranchCanada() {
        return CURRENT_LANGUAGE == FRANCH_CANADA;
    }

    public static boolean isSpanishSpain() {
        return CURRENT_LANGUAGE == SPANISH_SPAIN;
    }

    public static boolean isSpanishLatAm() {
        return CURRENT_LANGUAGE == SPANISH_LATAM;
    }

    public static boolean isPortuguesePortugal() {
        return CURRENT_LANGUAGE == PORTUGUESE_PORTUGAL;
    }

    public static boolean isPortugueseBrazil() {
        return CURRENT_LANGUAGE == PORTUGUESE_BRAZIL;
    }

    public static boolean isIndonesia() {
        return CURRENT_LANGUAGE == INDONESIA;
    }

    public static boolean isRussian() {
        return CURRENT_LANGUAGE == RUSSIAN;
    }

    public static boolean isFinnish() {
        return CURRENT_LANGUAGE == FINNISH;
    }

    public static class ConstantsLanguageBean {
        private int languageDef;
        private String iSOLanguage;
        private String nuanceLanguage;
        private Locale androidLocale;

        public int getLanguageDef() {
            return languageDef;
        }

        public String getiSOLanguage() {
            return iSOLanguage;
        }

        public String getNuanceLanguage() {
            return nuanceLanguage;
        }

        public String getShowLanguage() {
            return showLanguage;
        }

        private String showLanguage;

        public ConstantsLanguageBean(int languageDef, String iSOLanguage, String nuanceLanguage, String showLanguage, Locale androidLocale) {
            this.languageDef = languageDef;
            this.iSOLanguage = iSOLanguage;
            this.nuanceLanguage = nuanceLanguage;
            this.showLanguage = showLanguage;
            this.androidLocale = androidLocale;
        }

        public Locale getAndroidLocale() {
            return androidLocale;
        }

    }

    private static List<ConstantsLanguageBean> supportLanguages = new ArrayList<>();

    static {
        if (Constants.isI18N()) {

            supportLanguages.add(new ConstantsLanguageBean(
                    ENGELISH_US, "en", NuanceLanguage.English_US,
                    "English(US)", Locale.US));

            supportLanguages.add(new ConstantsLanguageBean(
                    CHINESE, "zh", NuanceLanguage.Mandarin_China,
                    "中文（简体）", Locale.SIMPLIFIED_CHINESE));

            supportLanguages.add(new ConstantsLanguageBean(
                    JAPANESE, "ja", NuanceLanguage.Japanese,
                    "日本語", Locale.JAPAN));

            supportLanguages.add(new ConstantsLanguageBean(
                    FRANCH_FRANCE, "fr", NuanceLanguage.French_France,
                    "Français(France)", Locale.FRANCE));

            supportLanguages.add(new ConstantsLanguageBean(
                    SPANISH_SPAIN, "es", NuanceLanguage.Spanish_Spain,
                    "Español(España)", new Locale("es", "ES")));

            supportLanguages.add(new ConstantsLanguageBean(
                    PORTUGUESE_PORTUGAL, "pt", NuanceLanguage.Portuguese_Portugal,
                    "Português(Portugal)", new Locale("pt", "PT")));

            supportLanguages.add(new ConstantsLanguageBean(
                    INDONESIA, "in", NuanceLanguage.Bahasa_Indonesia,
                    "Indonesia", new Locale("in", "ID")));

            supportLanguages.add(new ConstantsLanguageBean(
                    RUSSIAN, "ru", NuanceLanguage.Russian,
                    "Pусский", new Locale("ru", "RU")));

            supportLanguages.add(new ConstantsLanguageBean(
                    FINNISH, "fi", NuanceLanguage.Finnish,
                    "suomalainen", new Locale("fi", "FI")));

            supportLanguages.add(new ConstantsLanguageBean(
                    ARABIC, "ar", NuanceLanguage.Arabic_International,
                    "عربي", new Locale("ar", "BN")));
        } else {
            supportLanguages.add(new ConstantsLanguageBean(
                    CHINESE, "zh", NuanceLanguage.Mandarin_China,
                    "中文（简体）", Locale.SIMPLIFIED_CHINESE));

            supportLanguages.add(new ConstantsLanguageBean(
                    ENGELISH_US, "en", NuanceLanguage.English_US,
                    "English(US)", Locale.US));
        }
    }

    public static List<ConstantsLanguageBean> getSupportLanguages() {
        return supportLanguages;
    }

    public static String getNuanceLangueByDef(int def) {
        String language = "eng-USA";
        for (ConstantsLanguageBean bean : supportLanguages) {
            if (def == bean.getLanguageDef()) {
                return bean.getNuanceLanguage();
            }
        }
        return language;
    }

    public static Locale getLocaleByDef(int def) {
        Locale locale = Locale.US;
        for (ConstantsLanguageBean bean : supportLanguages) {
            if (def == bean.getLanguageDef()) {
                return bean.getAndroidLocale();
            }
        }
        return locale;
    }

    public static String getShowLanguageByDef(int def) {
        String language = "English(US)";
        for (ConstantsLanguageBean bean : supportLanguages) {
            if (def == bean.getLanguageDef()) {
                return bean.getShowLanguage();
            }
        }
        return language;
    }


    public static String getISOLanguage(int def) {
        String language = "zh";
        for (ConstantsLanguageBean bean : supportLanguages) {
            if (bean.getLanguageDef() == def) {
                return bean.getiSOLanguage();
            }
        }

        return language;
    }

    public static String getISOLanguage() {
        return getISOLanguage(CsjLanguage.CURRENT_LANGUAGE);
    }

    public static String getLanguageStrForTimeZone() {
        String language = "zh";
        switch (CURRENT_LANGUAGE) {
            case CHINESE:
                language = "zh";
                break;
            case ENGELISH_US:
            case ENGELISH_UK:
            case ENGELISH_AUSTRALIA:
            case ENGELISH_INDIA:
                language = "en";
                break;
            case JAPANESE:
                language = "ja";
                break;
            case FRANCH_FRANCE:
            case FRANCH_CANADA:
                language = "fr";
                break;
            case SPANISH_SPAIN:
            case SPANISH_LATAM:
                language = "es";
                break;
            case PORTUGUESE_PORTUGAL:
            case PORTUGUESE_BRAZIL:
                language = "pt";
                break;
            case INDONESIA:
                language = "id";
                break;
            case RUSSIAN:
                language = "ru-RU";
                break;
            case FINNISH:
                language = "fi";
                break;
        }
        return language;
    }

}
