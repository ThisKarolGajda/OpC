package me.opkarol.opc.api.storage.types;

/*
 = Copyright (c) 2021-2022.
 = [OpPets] ThisKarolGajda
 = Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 = http://www.apache.org/licenses/LICENSE-2.0
 = Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import me.opkarol.opc.api.storage.OpObject;
import me.opkarol.opc.api.storage.builder.OpObjectTypes;

public class OpBooleanObject extends OpObject {
    private final String name;
    private final Object value;

    public OpBooleanObject(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public OpObjectTypes getType() {
        return OpObjectTypes.BOOL;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
