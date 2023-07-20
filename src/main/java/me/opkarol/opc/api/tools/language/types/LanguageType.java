package me.opkarol.opc.api.tools.language.types;

import me.opkarol.opc.api.tools.HeadManager;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.inventory.ItemStack;

public enum LanguageType {
    /*
    List of Language Codes
    as of copy to Microsoft Language Codes - LinkedIn:
    https://docs.microsoft.com/en-us/linkedin/shared/references/reference-tables/language-codes
     */

    en_US("English", "bee5c850afbb7d8843265a146211ac9c615f733dcc5a8e2190e5c247dea32"),
    ar_AE("Arabic", "dd9f2944d48fb967de7aa5d4b8c848307f467323aea93365b7ed2c16a8f1a939"),
    zh_CN("Chinese - Simplified", "7f9bc035cdc80f1ab5e1198f29f3ad3fdd2b42d9a69aeb64de990681800b98dc"),
    zh_TW("Chinese - Traditional", "7f9bc035cdc80f1ab5e1198f29f3ad3fdd2b42d9a69aeb64de990681800b98dc"),
    cs_CZ("Czech", "48152b7334d7ecf335e47a4f35defbd2eb6957fc7bfe94212642d62f46e61e"),
    da_DK("Danish", "10c23055c392606f7e531daa2676ebe2e348988810c15f15dc5b3733998232"),
    in_ID("Indonesian", "cbe63fcaa0203f8046fb96375d079a5c09b4161d0119d7194566e689cab18f68"),
    ms_MY("Malaysian", "754b9041dea6db6db44750f1385a743adf653767b4b8802cad4c585dd3f5be46"),
    nl_NL("Dutch", "c23cf210edea396f2f5dfbced69848434f93404eefeabf54b23c073b090adf"),
    fr_FR("French", "6903349fa45bdd87126d9cd3c6c0abba7dbd6f56fb8d78701873a1e7c8ee33cf"),
    fi_FI("Finnish", "59f2349729a7ec8d4b1478adfe5ca8af96479e983fbad238ccbd81409b4ed"),
    de_DE("German", "5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f"),
    it_IT("Italian", "85ce89223fa42fe06ad65d8d44ca412ae899c831309d68924dfe0d142fdbeea4"),
    ja_JP("Japanese", "d6c2ca7238666ae1b9dd9daa3d4fc829db22609fb569312dec1fb0c8d6dd6c1d"),
    ko_KR("Korean", "795b8c051cd9246a6b2709a5a20e3d1f9730c96b2167d6f92805f1491cb8f621"),
    no_NO("Norwegian", "fda048bc153b38467e76a3347f38396860a8bc68603931e91f7af58bec57383d"),
    pl_PL("Polish", "921b2af8d2322282fce4a1aa4f257a52b68e27eb334f4a181fd976bae6d8eb"),
    pt_BR("Portuguese", "ebd51f4693af174e6fe1979233d23a40bb987398e3891665fafd2ba567b5a53a"),
    ro_RO("Romanian", "dceb1708d5404ef326103e7b60559c9178f3dce729007ac9a0b498bdebe46107"),
    ru_RU("Russian"),
    es_ES("Spanish", "32bd4521983309e0ad76c1ee29874287957ec3d96f8d889324da8c887e485ea8"),
    sv_SE("Swedish", "7d86242b0d97ece9994660f3974d72df7b887f630a4530dadc5b1ab7c2134aec"),
    th_TH("Thai", "2a7916e4a852f7e6f3f3de19c7fb57686a37bce834bd54684a7dbef8d53fb"),
    tl_PH("Filipino", "dabcdb3554ff31759823a12b9c8b9186cd29544d18419dce551cf783ad006cdf"),
    tr_TR("Turkish", "6bbeaf52e1c4bfcd8a1f4c6913234b840241aa48829c15abc6ff8fdf92cd89e"),
    custom("Custom");

    private final String language;
    private ItemStack headMaterial;
    private final ItemStack defaultType = HeadManager.getHeadFromMinecraftValueUrl("2705fd94a0c431927fb4e639b0fcfb49717e412285a02b439e0112da22b2e2ec");

    LanguageType(String language) {
        this.language = language;
    }

    LanguageType(String language, String headValue) {
        this.language = language;
        this.headMaterial = HeadManager.getHeadFromMinecraftValueUrl(headValue);
    }

    public String getLanguage() {
        return language;
    }

    public ItemStack getHeadValue() {
        return VariableUtil.getOrDefault(headMaterial, defaultType);
    }
}
