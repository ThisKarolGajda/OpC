package me.opkarol.opc.api.map;


/*
 * Copyright (c) 2021-2022.
 * [OpPets] ThisKarolGajda
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

/*
 * Copyright (c) 2021-2022.
 * [OpPets] ThisKarolGajda
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

public class OpPlaceholderMap extends OpLinkedMap<String, String> {

    public void addEmptyVariable(String variable) {
        set(variable, "");
    }

    public void fillVariable(String variable, String key) {
        set(variable, key);
    }

    public void fillVariable(int variable, String key) {
        fillVariable(getByIndex(variable), key);
    }

    public String getFilledString(String s) {
        if (getValues().size() == 0) {
            return s;
        }
        for (String variable : getValues()) {
            String key = getMap().get(variable);
            if (key == null || key.equals("")) {
                continue;
            }

            s = s.replace(variable, key);
        }
        return s;
    }
}
