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
package org.jmeld.fx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.jmeld.fx.ui.settings.SettingsPanel;
import org.jmeld.fx.util.FxUtils;
import org.jmeld.ui.util.Icons;

public class JMeldFxPanel
    extends BorderPane
{
  JMeldFxPanel()
  {
    TabPane tabPane;
    Tab tab;

    tabPane = new TabPane();

    tab = new Tab("Settings1");
    tab.setGraphic(FxUtils.getIcon(Icons.SETTINGS.getSmallIcon()));
    tabPane.getTabs().add(tab);

    tab.setContent(new SettingsPanel());
  }
}
