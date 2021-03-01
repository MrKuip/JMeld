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
package org.jmeld.ui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.jmeld.ui.action.ActionHandler;
import org.jmeld.ui.swing.table.JMTreeTableModel;
import org.jmeld.util.conf.ConfigurationListenerIF;
import org.jmeld.util.file.FolderDiff;

public class VersionControlPanel
    extends AbstractContentPanel
    implements ConfigurationListenerIF
{
  private JMeldPanel mainPanel;
  private FolderDiff diff;
  private ActionHandler actionHandler;
  private JPanel bufferDiffPanelHolder;

  VersionControlPanel(JMeldPanel mainPanel,
      FolderDiff diff)
  {
    this.mainPanel = mainPanel;
    this.diff = diff;

    init();
  }

  private void init()
  {
    FolderDiffPanel folderDiffPanel;
    JSplitPane splitPane;

    folderDiffPanel = new FolderDiffPanel(mainPanel,
                                          diff)
    {
      @Override
      protected JMTreeTableModel createTreeTableModel()
      {
        return new VersionControlTreeTableModel();
      }
    };
    bufferDiffPanelHolder = new JPanel();
    bufferDiffPanelHolder.setLayout(new BorderLayout());

    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                               folderDiffPanel,
                               bufferDiffPanelHolder);

    setLayout(new BorderLayout());
    add(BorderLayout.CENTER,
        splitPane);
  }

  public void configurationChanged()
  {
  }
}
