package me.opkarol.opc.api.map;

/*
 = Copyright (c) 2021-2022.
 = [OpPets] ThisKarolGajda
 = Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 = http://www.apache.org/licenses/LICENSE-2.0
 = Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class OpMap<K, V> implements IMap<K, V>, Serializable {
    protected HashMap<K, V> map = new HashMap<>();

    public OpMap(HashMap<K, V> map) {
        this.map = map;
    }

    public OpMap(OpMap<K, V> map) {
        this.map = map.map;
    }

    public OpMap() {

    }

    @Override
    public void remove(K k) {
        if (this.isEmpty()) {
            return;
        }
        this.map.remove(k);
    }

    @Override
    public void remove(K k, V v) {
        if (this.isEmpty()) {
            return;
        }
        this.map.remove(k, v);
    }

    @Override
    public void put(K k, V v) {
        this.map.put(k, v);
    }

    @Override
    public void set(K k, V v) {
        if (this.containsKey(k)) {
            this.map.replace(k, v);
        } else {
            this.map.put(k, v);
        }
    }

    @Override
    public boolean containsKey(K k) {
        if (this.isEmpty()) {
            return false;
        }
        return this.map.containsKey(k);
    }

    @Override
    public boolean containsValue(V v) {
        if (this.isEmpty()) {
            return false;
        }
        return this.map.containsValue(v);
    }

    @Override
    public Map<K, V> getMap() {
        return this.map;
    }

    @Override
    public void setMap(HashMap<K, V> map) {
        this.map = map;
    }

    @Override
    public Optional<V> getByKey(K k) {
        if (k == null || this.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.get(k));
    }

    @Override
    public Collection<V> getValues() {
        if (this.isEmpty()) {
            return new OpList<>();
        }
        return getMap().values();
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        if (this.isEmpty()) {
            return defaultValue;
        }
        V get = getMap().get(key);
        return VariableUtil.getOrDefault(get, defaultValue);
    }

    @Override
    public V replace(K key, V value) {
        this.set(key, value);
        Optional<V> val = this.getByKey(key);
        if (val.isPresent() && val.get().equals(value)) {
            return value;
        }
        return null;
    }

    @Override
    public Set<K> keySet() {
        if (this.isEmpty()) {
            return new HashSet<>();
        }
        return getMap().keySet();
    }

    @Override
    public boolean isEmpty() {
        return map == null || map.isEmpty();
    }

    @Override
    public Optional<K> getFromIndex(int index) {
        return Optional.empty();
    }

    public V unsafeGet(K k) {
        return getMap().get(k);
    }

    public void addAll(@NotNull OpMap<K, V> map) {
        map.keySet()
                .forEach(key -> set(key, map.unsafeGet(key)));
    }

    @Override
    @Deprecated
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (K key : keySet()) {
            V value = unsafeGet(key);
            builder.append(key).append("=").append(value).append(" -");
        }
        return builder.toString();
    }

    public void putAll(@NotNull OpMap<K, V> tempReplacements) {
        tempReplacements.getMap().forEach(this::set);
    }
}
