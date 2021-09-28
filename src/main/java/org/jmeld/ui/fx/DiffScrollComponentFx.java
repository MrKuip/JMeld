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
package org.jmeld.ui.fx;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jmeld.diff.JMRevision;
import org.jmeld.fx.settings.EditorSettingsFx;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.scene.ComponentFx;
import org.jmeld.util.node.JMDiffNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DiffScrollComponentFx
  extends ComponentFx
{
  private final JMDiffNode m_diffNode;
  private final VirtualizedScrollPane<CodeArea> m_leftScrollPane;
  private final VirtualizedScrollPane<CodeArea> m_rightScrollPane;
  private boolean leftsideReadonly;
  private boolean rightsideReadonly;

  public DiffScrollComponentFx(JMDiffNode diffNode, VirtualizedScrollPane<CodeArea> fileContentLeftScrollPane,
      VirtualizedScrollPane<CodeArea> fileContentRightScrollPane)
  {
    m_diffNode = diffNode;
    m_leftScrollPane = fileContentLeftScrollPane;
    m_rightScrollPane = fileContentRightScrollPane;

    setMinWidth(60);
    initSettings();
  }

  private void initSettings()
  {
    EditorSettingsFx settings;

    settings = JMeldSettingsFx.getInstance().getEditor();

    leftsideReadonly = settings.getLeftsideReadonly();
    rightsideReadonly = settings.getRightsideReadonly();
  }

  protected void paintComponent(GraphicsContext gc)
  {
    double middle;

    gc.clearRect(0, 0, getWidth(), getHeight());

    middle = getHeight() / 2;
    gc.setStroke(Color.LIGHTGRAY);
    gc.strokeLine(5, middle, getWidth() - 5, middle);

    paintDiffs(gc);
  }

  private void paintDiffs(GraphicsContext g2)
  {
    JMRevision revision;

    revision = m_diffNode.getRevision();
    if (revision == null)
    {
      return;
    }
  }
}
