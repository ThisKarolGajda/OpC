package me.opkarol.opc.api.tools.language.editor;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.opkarol.opc.api.gui.ItemPaletteGUI;
import me.opkarol.opc.api.tools.language.Language;
import me.opkarol.opc.api.tools.language.database.LanguageDatabase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LanguageSelectorInventory {

    public LanguageSelectorInventory(Player player, @NotNull LanguageDatabase languageDatabase) {
        buildAndShowInventory(player, languageDatabase);
    }

    public LanguageSelectorInventory(Player player, @NotNull Language language) {
        this(player, language.getLanguageDatabase());
    }

    private void buildAndShowInventory(Player player, @NotNull LanguageDatabase languageDatabase) {
        ItemPaletteGUI<Language> gui = new ItemPaletteGUI.Builder<Language>("Choose language!")
                .as(this::getDisplay)
                .build(languageDatabase.getLanguageList());

        gui.show(player);
    }

    private GuiItem getDisplay(@NotNull Language language) {
        return language.to(language.getLanguageType().getHeadValue(), event -> {
            Player player = (Player) event.getWhoClicked();

            if (event.isRightClick()) {
                language.setActive();
                player.sendMessage("Switched active language to: " + language.getLanguageType().getLanguage() + ".");
                buildAndShowInventory(player, language.getLanguageDatabase());
            } else if (event.isLeftClick()) {
                new LanguageEditorInventory(player, language);
            }
        });
    }
}
