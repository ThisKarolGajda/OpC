package me.opkarol.opc.api.tools.permission;

import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.map.OpMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

public class PermissionManager<K> extends Serialize {
    private OpList<PermissionGroup<K>> list = new OpList<>();

    public PermissionManager(@NotNull Configuration config, String path) {
        super(null);
        path = path.endsWith(".") ? path.substring(0, path.length() - 1) : path;
        String finalPath = path + ".";
        config.useSectionKeys(path, s -> add(new PermissionGroup<>(s, (K) config.getConfig().get(finalPath + s))));
    }

    @SafeVarargs
    public PermissionManager(PermissionGroup<K> @NotNull ... group) {
        super(null);
        if (group.length == 0) {
            return;
        }

        for (PermissionGroup<K> group1 : group) {
            add(group1);
        }
    }

    public PermissionManager(K defaultObject) {
        super(null);
        add(new PermissionGroup<>("default", defaultObject));
    }

    public PermissionManager(String group, K defaultObject) {
        super(null);
        add(new PermissionGroup<>(group, defaultObject));
    }

    public PermissionManager() {
        super(null);
    }

    public PermissionManager(@NotNull OpMap<String, Object> objects) {
        super(null);
        objects.getByKey("list").ifPresent(list1 -> list.addAll((List<? extends PermissionGroup<K>>) list1));
    }

    public Object getPlayerObject(Player player, @NotNull OBJECT_TYPE type) {
        List<PermissionGroup<K>> groups = list.stream().filter(group ->
                        player.hasPermission(group.group())
                        || group.group().equalsIgnoreCase("default")
                        || (group.group().equalsIgnoreCase("op") && player.isOp()))
                .toList();
        if (type.equals(OBJECT_TYPE.INTEGER)) {
            int lowest = Integer.MAX_VALUE;
            for (PermissionGroup<K> group : groups) {
                int object = (int) group.object();
                if (object < lowest) {
                    lowest = object;
                }
            }
            return lowest;
        }
        return groups;
    }

    public OpList<PermissionGroup<K>> getList() {
        return getOrDefault(list, new OpList<>());
    }

    public void add(PermissionGroup<K> group) {
        OpList<PermissionGroup<K>> list = getList();
        list.add(group);
        this.list = list;
    }

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("list", list);
    }

    public enum OBJECT_TYPE {
        INTEGER
    }

    @Override
    public String toString() {
        return "PermissionManager{" +
                "list=" + list.toArrayString() +
                '}';
    }
}
