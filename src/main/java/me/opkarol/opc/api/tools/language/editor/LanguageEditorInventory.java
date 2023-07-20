package me.opkarol.opc.api.tools.language.editor;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.opkarol.opc.api.gui.ItemPaletteGUI;
import me.opkarol.opc.api.item.ItemBuilder;
import me.opkarol.opc.api.misc.listeners.ChatHandler;
import me.opkarol.opc.api.tools.HeadManager;
import me.opkarol.opc.api.tools.language.Language;
import me.opkarol.opc.api.tools.language.types.LanguageObject;
import me.opkarol.opc.api.tools.runnable.OpRunnable;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LanguageEditorInventory {
    private final Language language;
    private String directory;
    private final String originalDirectory;

    public LanguageEditorInventory(Player player, Language language, String directory) {
        this.language = language;
        this.directory = directory;
        this.originalDirectory = directory;
        buildAndShowInventory(player);
    }

    public LanguageEditorInventory(Player player, Language language) {
        this(player, language, language.getLanguageDatabase().getDirectory());
    }

    private void buildAndShowInventory(@NotNull Player player) {
        List<Object> items = language.getItemsForPath(directory);

        if (!originalDirectory.equals(directory)) {
            String lastSection = removeLastSection(directory);
            items.add(new LanguageEditorSection(lastSection, lastSection, LanguageEditorSection.EDIT_TYPE.SET));
        }

        ItemPaletteGUI<Object> gui = new ItemPaletteGUI.Builder<>("Translations editor!")
                .as(this::getDisplay)
                .controlPane(pane -> {
                    pane.setGap(3);
                    pane.addItem(new GuiItem(new ItemBuilder(HeadManager.getHeadFromMinecraftValueUrl("e0a18ff232a4b5412fef185997e42e0b44891fba97714455035ce8467d96ec96")).setName("&7Home"), event -> {
                        new LanguageSelectorInventory(player, language);
                    }));
                })
                .build(items);

        gui.show(player);
    }

    public String removeLastSection(@NotNull String path) {
        int lastDotIndex = path.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return path.substring(0, lastDotIndex);
        } else {
            return path;
        }
    }

    private @Nullable GuiItem getDisplay(@NotNull Object object) {
        if (object instanceof LanguageObject languageObject) {
            return handleLanguagePathWithObjectValue(languageObject);
        } else if (object instanceof LanguageEditorSection languageEditorSection) {
            return languageEditorSection.to(event -> {
                switch (languageEditorSection.getEditType()) {
                    case ADD -> {
                        directory += "." + languageEditorSection.getSection();
                        buildAndShowInventory((Player) event.getWhoClicked());
                    }
                    case SET -> {
                        directory = languageEditorSection.getSection();
                        buildAndShowInventory((Player) event.getWhoClicked());
                    }
                }
            });
        }
        return null;
    }

    private GuiItem handleLanguagePathWithObjectValue(@NotNull LanguageObject languageObject) {
        languageObject.setLanguage(language);
        return languageObject.to(event -> {
            Player player = (Player) event.getWhoClicked();
            if (event.isLeftClick() && event.isShiftClick()) {
                languageObject.setObject(language.getLanguageDatabase().defaultLanguage.getMessage(languageObject.getPath()));
                buildAndShowInventory(player);
                return;
            }
            if (event.isShiftClick()) {
                return;
            }
            if (event.isLeftClick()) {
                player.closeInventory();
                player.sendMessage(FormatUtils.formatMessage("&7[EDITOR] Enter message for: &f&l" + languageObject.getPath() + "&7. Previous object: &f&l" + languageObject.getObject() + "&7."));
                ChatHandler.add(player, (message) -> {
                    languageObject.setObject(message);
                    new OpRunnable(() -> buildAndShowInventory(player)).runTask();
                });
                return;
            }
            if (event.isRightClick()) {
                player.closeInventory();
                if (languageObject.getObject() != null) {
                    player.sendMessage(languageObject.getObject());
                }
            }
        });
    }
}
