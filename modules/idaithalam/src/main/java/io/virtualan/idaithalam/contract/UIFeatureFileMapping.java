package io.virtualan.idaithalam.contract;

import io.virtualan.idaithalam.core.domain.UiItem;

import java.util.List;

public class UIFeatureFileMapping {

  public UIFeatureFileMapping(String feature,
                              List<UiItem> items) {
    this.feature = feature;
    this.uiItems = items;
  }


  /**
   * The Feature.
   */
  String feature;
  /**
   * The Items.
   */
  List<UiItem> uiItems;

}
