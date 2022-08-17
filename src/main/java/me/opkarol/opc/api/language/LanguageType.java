package me.opkarol.opc.api.language;

public enum LanguageType {
    /*
    List of Language Codes
    as of copy to Microsoft Language Codes - LinkedIn:
    https://docs.microsoft.com/en-us/linkedin/shared/references/reference-tables/language-codes
     */

    en_US("English"),
    ar_AE("Arabic"),
    zh_CN("Chinese - Simplified"),
    zh_TW("Chinese - Traditional"),
    cs_CZ("Czech"),
    da_DK("Danish"),
    in_ID("Indonesian"),
    ms_MY("Malaysian"),
    nl_NL("Dutch"),
    fr_FR("French"),
    fi_FI("Finnish"),
    de_DE("German"),
    it_IT("Italian"),
    ja_JP("Japanese"),
    ko_KR("Korean"),
    no_NO("Norwegian"),
    pl_PL("Polish"),
    pt_BR("Portuguese"),
    ro_RO("Romanian"),
    ru_RU("Russian"),
    es_ES("Spanish"),
    sv_SE("Swedish"),
    th_TH("Thai"),
    tl_PH("Filipino"),
    tr_TR("Turkish");

    private final String language;

    LanguageType(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
