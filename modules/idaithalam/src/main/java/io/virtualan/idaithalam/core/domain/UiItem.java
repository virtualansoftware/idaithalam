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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Item.
 */
public class UiItem {
  /**
   * The Scenario.
   */
  String scenario;
  /**
   * The Resource.
   */
  String resource;

  String tags;

  /**
   * The Std type.
   */
  boolean isUI;

  String page;

  /**
   * The Step info.
   */
  String stepInfo;


  boolean hasInputParams;

  Set<Map.Entry<String, String>> inputParams;

  public boolean isUI() {
    return isUI;
  }

  public void setUI(boolean UI) {
    isUI = UI;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public boolean isHasInputParams() {
    return hasInputParams;
  }

  public void setHasInputParams(boolean hasInputParams) {
    this.hasInputParams = hasInputParams;
  }

  public  Set<Map.Entry<String, String>> getInputParams() {
    return inputParams;
  }

  public void setInputParams( Set<Map.Entry<String, String>> inputParams) {
    this.inputParams = inputParams;
  }

  /**
   * Gets step info.
   *
   * @return the step info
   */
  public String getStepInfo() {
    return stepInfo;
  }

  /**
   * Sets step info.
   *
   * @param stepInfo the step info
   */
  public void setStepInfo(String stepInfo) {
    this.stepInfo = stepInfo;
  }



  /**
   * Gets scenario.
   *
   * @return the scenario
   */
  public String getScenario() {
    return scenario;
  }

  /**
   * Sets scenario.
   *
   * @param scenario the scenario
   */
  public void setScenario(String scenario) {
    this.scenario = scenario;
  }

  /**
   * Gets resource.
   *
   * @return the resource
   */
  public String getResource() {
    return resource;
  }

  /**
   * Sets resource.
   *
   * @param resource the resource
   */
  public void setResource(String resource) {
    this.resource = resource;
  }


  /**
   * Gets tags.
   *
   * @return the tags
   */
  public String getTags() {
    return tags;
  }

  /**
   * Sets tags.
   *
   * @param tags the tags
   */
  public void setTags(String tags) {
    this.tags = tags;
  }

}
