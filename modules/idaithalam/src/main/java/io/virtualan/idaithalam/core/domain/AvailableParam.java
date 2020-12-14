/*
 *
 *  Copyright (c) 2020.  Virtualan Contributors (https://virtualan.io)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under the License
 *   is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *   or implied. See the License for the specific language governing permissions and limitations under
 *   the License.
 *
 */

package io.virtualan.idaithalam.core.domain;

/**
 * The type Available param.
 */
public class AvailableParam {

    /**
     * The Key.
     */
    String key;
    /**
     * The Value.
     */
    String value;
    /**
     * The Parameter type.
     */
    String parameterType;

    /**
     * Instantiates a new Available param.
     *
     * @param key           the key
     * @param value         the value
     * @param parameterType the parameter type
     */
    public AvailableParam(String key, String value, String parameterType) {
        this.key = key;
        this.value = value;
        this.parameterType = parameterType;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets parameter type.
     *
     * @return the parameter type
     */
    public String getParameterType() {
        return parameterType;
    }

    /**
     * Sets parameter type.
     *
     * @param parameterType the parameter type
     */
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}