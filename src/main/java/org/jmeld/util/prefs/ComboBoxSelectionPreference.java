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

import java.util.Optional;
import javafx.scene.control.ComboBox;

public class ComboBoxSelectionPreference
  extends Preference
{
  // Class variables:
  private static String SELECTED_ITEM = "SELECTED_ITEM";

  public ComboBoxSelectionPreference(String preferenceName, ComboBox<String> target)
  {
    super("ComboBoxSelection-" + preferenceName);
    init(target);
  }

  private void init(ComboBox<String> target)
  {
    String selectedItem;

    selectedItem = getString(SELECTED_ITEM, null);
    if (selectedItem != null)
    {
      Optional<String> item;

      item = target.getItems().stream().filter(o -> o.toString().contentEquals(selectedItem)).findFirst();
      item.ifPresent(o -> target.getSelectionModel().select(o));
    }

    target.setOnAction((e) -> {
      Object selected;

      selected = ((ComboBox<?>) e.getSource()).getSelectionModel().getSelectedItem();
      if (selected != null)
      {
        putString(SELECTED_ITEM, selected.toString());
      }
    });
  }
}
