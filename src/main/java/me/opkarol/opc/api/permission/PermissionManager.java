package me.opkarol.opc.api.permission;

import me.opkarol.opc.api.configuration.CustomConfigurable;
import me.opkarol.opc.api.configuration.CustomConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static me.opkarol.opc.api.utils.Util.getOrDefault;

public class PermissionManager<K> implements CustomConfigurable {
    private List<PermissionGroup<K>> list = new ArrayList<>();

    public List<PermissionGroup<K>> getList() {
        return getOrDefault(list, new ArrayList<>());
    }

    public void add(PermissionGroup<K> group) {
        List<PermissionGroup<K>> list = getList();
        list.add(group);
        this.list = list;
    }

    public PermissionManager(@NotNull me.opkarol.opc.api.files.Configuration config, String path) {
        path = path.endsWith(".") ? path.substring(0, path.length() - 1) : path;
        String finalPath = path + ".";
        config.useSectionKeys(path, s -> add(new PermissionGroup<>(s, (K) config.getConfig().get(finalPath + s))));
    }

    @SafeVarargs
    public PermissionManager(PermissionGroup<K> @NotNull ... group) {
        if (group.length == 0) {
            return;
        }

        for (PermissionGroup<K> group1 : group) {
            add(group1);
        }
    }

    public PermissionManager(K defaultObject) {
        add(new PermissionGroup<>("default", defaultObject));
    }

    public PermissionManager(String group, K defaultObject) {
        add(new PermissionGroup<>(group, defaultObject));
    }

    public PermissionManager() { }

    public PermissionManager(String path) {
        get(path);
    }

    public Object getPlayerObject(Player player, @NotNull OBJECT_TYPE type) {
        List<PermissionGroup<K>> groups = list.stream().filter(group ->
                        player.hasPermission(group.group()) || group.group().equalsIgnoreCase("default"))
                .toList();
        if (type.equals(OBJECT_TYPE.INTEGER)) {
            int lowest = -1;
            for (PermissionGroup<K> group : groups) {
                int object = (int) group.object();
                if (lowest == -1 || object < lowest) {
                    lowest = object;
                }
            }
            return lowest;
        }
        return groups;
    }

    @Override
    public Consumer<CustomConfiguration> get() {
        return get -> get.useSectionKeys(s -> add(new PermissionGroup<>(s, (K) get.get(get.getPath(s)))));
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return save -> save.forEachIterable(list, group -> save.set(group.group(), group.object())).save();
    }


    public enum OBJECT_TYPE {
        INTEGER
    }
}
