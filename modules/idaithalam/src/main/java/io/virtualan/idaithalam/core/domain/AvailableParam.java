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

    boolean isBoolean;

    boolean isInteger;

    boolean  isDecimal;

    boolean isString;

    boolean isCondition;

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

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        isBoolean = aBoolean;
    }

    public boolean isInteger() {
        return isInteger;
    }

    public void setInteger(boolean integer) {
        isInteger = integer;
    }

    public boolean isDecimal() {
        return isDecimal;
    }

    public void setDecimal(boolean decimal) {
        isDecimal = decimal;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean string) {
        isString = string;
    }

    public boolean isCondition() {
        return isCondition;
    }

    public void setCondition(boolean condition) {
        isCondition = condition;
    }

    /** Compares the objects. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableParam that = (AvailableParam) o;
        boolean isEqual = false;
        isEqual =  this.key.equals(that.getKey());
        isEqual = isEqual &&  this.value.equals(that.getValue()) ;
        isEqual = isEqual && (this.parameterType == null ? that.getParameterType() == null : this.parameterType.equals(that.getParameterType()));
        return isEqual;
    }
}