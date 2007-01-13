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
package org.jmeld.ui.conf;

import org.jmeld.ui.*;
import org.jmeld.ui.util.*;

import javax.swing.*;

import java.awt.*;

public class ConfigurationPanel
       extends AbstractContentPanel
{
  public ConfigurationPanel()
  {
    init();
  }

  private void init()
  {
    JTabbedPane tabbedPane;

    tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
    tabbedPane.addTab(
      "Editor",
      new EmptyIcon(10, 40),
      new EditorPreferencePanel());
    tabbedPane.addTab(
      "Display",
      new EmptyIcon(10, 40),
      new JButton("Display"));

    setLayout(new BorderLayout());
    add(tabbedPane, BorderLayout.CENTER);
  }
}