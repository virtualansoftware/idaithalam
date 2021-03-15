package io.virtualan.idaithalam.contract;

import io.virtualan.idaithalam.core.domain.Item;
import java.util.List;

public class FeatureFileMapping {
  public FeatureFileMapping(String feature,
      List<Item> items) {
    this.feature = feature;
    this.items = items;
  }

  /**
   * The Feature.
   */
  String feature;
  /**
   * The Items.
   */
  List<Item> items;

}
