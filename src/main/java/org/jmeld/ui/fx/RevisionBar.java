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

import org.jmeld.diff.JMChunk;
import org.jmeld.diff.JMDelta;
import org.jmeld.diff.JMRevision;
import org.jmeld.fx.settings.FxRevisionUtil;
import org.jmeld.scene.JavaFxComponent;
import org.jmeld.util.node.JMDiffNode;
import org.jmeld.util.node.JMDiffNode.Location;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RevisionBar
  extends JavaFxComponent
{
  private final Location m_location;
  private final JMDiffNode m_diffNode;

  public RevisionBar(Location location, JMDiffNode diffNode)
  {
    m_location = location;
    m_diffNode = diffNode;
  }

  @Override
  protected void paintComponent(GraphicsContext gc)
  {
    JMRevision revision;
    JMChunk chunk;
    double y;
    double height;
    int numberOfLines;

    gc.clearRect(0, 0, getWidth(), getHeight());

    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, getWidth(), getHeight());

    revision = m_diffNode.getRevision();
    if (revision == null)
    {
      return;
    }

    numberOfLines = getNumberOfLines(revision);
    if (numberOfLines <= 0)
    {
      return;
    }

    for (JMDelta delta : revision.getDeltas())
    {
      if (delta.isChange() && !delta.isReallyChanged())
      {
        continue;
      }

      chunk = m_location == Location.LEFT ? delta.getOriginal() : delta.getRevised();

      gc.setFill(FxRevisionUtil.getColor(delta));
      y = (getHeight() * chunk.getAnchor()) / numberOfLines;
      height = (getHeight() * chunk.getSize()) / numberOfLines;
      if (height <= 0)
      {
        height = 1;
      }

      gc.fillRect(0, y, getWidth(), height);
    }

    gc.setStroke(Color.GRAY);
    gc.strokeRect(0, 0, getWidth(), getHeight());
  }

  private int getNumberOfLines(JMRevision revision)
  {
    return m_location == Location.LEFT ? revision.getOrgSize() : revision.getRevSize();
  }
}
