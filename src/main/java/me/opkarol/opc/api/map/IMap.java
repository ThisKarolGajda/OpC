package me.opkarol.opc.api.map;

/*
 = Copyright (c) 2021-2022.
 = [OpPets] ThisKarolGajda
 = Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 = http://www.apache.org/licenses/LICENSE-2.0
 = Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import java.util.*;
import java.util.stream.Stream;

public interface IMap<K, V> {
    void remove(K k);

    void remove(K k, V v);

    void put(K k, V v);

    void set(K k, V v);

    boolean containsKey(K k);

    boolean containsValue(V v);

    Map<K, V> getMap();

    void setMap(HashMap<K, V> map);

    Optional<V> getByKey(K k);

    Collection<V> getValues();

    default Stream<V> getValuesStream() {
        return getValues().stream();
    }

    V getOrDefault(K key, V defaultValue);

    V replace(K key, V value);

    Set<K> keySet();

    boolean isEmpty();

    Optional<K> getFromIndex(int index);
}
