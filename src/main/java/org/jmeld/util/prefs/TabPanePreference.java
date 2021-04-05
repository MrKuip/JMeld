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

import org.jmeld.util.StringUtil;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabPanePreference
  extends Preference
{
  // Class variables:
  private static String TEXT = "TEXT";

  // Instance variables:

  public TabPanePreference(String preferenceName, TabPane tabPane)
  {
    super("TabPane-" + preferenceName);
    init(tabPane);
  }

  private void init(TabPane tabPane)
  {
    String text;

    text = getString(TEXT, "");

    if (!StringUtil.isEmpty(text))
    {
      tabPane.getTabs().stream().filter(tab -> tab.getText().contentEquals(text)).findFirst().ifPresent(tab -> {
        tabPane.getSelectionModel().select(tab);
      });
    }

    tabPane.getSelectionModel().selectedItemProperty().addListener((e) -> {
      Tab tab;

      tab = tabPane.getSelectionModel().getSelectedItem();
      if (tab != null)
      {
        putString(TEXT, tab.getText());
      }
    });
  }
}
