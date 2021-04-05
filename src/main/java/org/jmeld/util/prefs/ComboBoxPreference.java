/*
   JMeld is a visual diff and merge tool.
   Copyright (C) 2007  Kees Kuip
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.
   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA  02110-1301  USA
 */
package org.jmeld.util.prefs;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;

public class ComboBoxPreference
  extends Preference
{
  // Class variables:
  private static String ITEMS = "ITEMS";

  // Instance variables:
  private int maxItems = 10;

  public ComboBoxPreference(String preferenceName, ComboBox<String> target)
  {
    super("ComboBox-" + preferenceName);
    init(target);
  }

  private void init(ComboBox<String> target)
  {
    getListOfString(ITEMS, maxItems).forEach(item -> target.getItems().add(item));
    target.getSelectionModel().select(0);
    target.setOnAction((e) -> {
      List<String> list;
      String item;

      list = new ArrayList<String>();

      // Put the selectedItem on top.
      item = target.getSelectionModel().getSelectedItem();
      if (item != null)
      {
        list.add(item);
      }

      target.getItems().forEach(i -> {
        if (!list.contains(i))
        {
          list.add(i);
        }
      });

      putListOfString(ITEMS, maxItems, list);
    });
  }
}
