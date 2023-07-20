package me.opkarol.opc.api.tools.language.editor;

import me.opkarol.opc.api.gui.IGuiItemBuilder;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.tools.language.types.LanguageObject;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LanguageEditorSection implements IGuiItemBuilder {
    private final String section;
    private final String entireSection;
    private final EDIT_TYPE editType;

    public LanguageEditorSection(@NotNull LanguageObject languageObject, int i, EDIT_TYPE editType) {
        this.editType = editType;
        this.section = languageObject.getSections()[i];
        this.entireSection = languageObject.getEntireSection(i);
    }

    public LanguageEditorSection(String section, String entireSection, EDIT_TYPE editType) {
        this.section = section;
        this.entireSection = entireSection;
        this.editType = editType;
    }

    @Override
    public String getItemName() {
        return editType == EDIT_TYPE.SET ? "&7Previous section" : "&7Section: &f&l" + section;
    }

    @Override
    public List<String> getItemLore() {
        return List.of("&7Press &f&lLMB &7to go to this section!", "&7You may find more sections there!", "&7Full path: " + entireSection);
    }

    @Override
    public Material getItemMaterial() {
        return Material.BOOKSHELF;
    }

    @Override
    public Tuple<Enchantment, Integer>[] getItemEnchants() {
        return new Tuple[]{Tuple.of(Enchantment.LUCK, 1)};
    }

    @Override
    public ItemFlag[] getItemFlags() {
        return new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS};
    }

    public String getSection() {
        return section;
    }

    public EDIT_TYPE getEditType() {
        return editType;
    }

    public enum EDIT_TYPE {
        ADD,
        SET
    }
}
