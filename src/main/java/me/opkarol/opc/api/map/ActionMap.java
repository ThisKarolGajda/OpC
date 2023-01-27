package me.opkarol.opc.api.map;

public class ActionMap<K> {
    private final OpMap<Integer, K> map = new OpMap<>();

    public void add(K object) {
        if (onAddObjectAction(object)) {
            map.set(map.getMap().size(), object);
        }
    }

    public void remove(int i) {
        map.getByKey(i).ifPresent(key -> {
            if (onRemoveObjectAction(key)) {
                map.remove(i, key);
            }
        });
    }

    public void set(K object, int index) {
        map.set(index, object);
    }

    public K get(int i) {
        if (i >= map.getMap().size()) {
            return null;
        }
        return map.unsafeGet(i);
    }

    public OpMap<Integer, K> getMap() {
        return map;
    }

    /**
     *
     * @param object that is generic type of K
     * @return true if the generic object should be removed from the list
     */
    public boolean onRemoveObjectAction(K object) {
        return true;
    }

    /**
     *
     * @param object that is generic type of K
     * @return true if the generic object should be added to the list
     */
    public boolean onAddObjectAction(K object) {
        return true;
    }

    public void clear() {
        getMap().getMap().clear();
    }
}
