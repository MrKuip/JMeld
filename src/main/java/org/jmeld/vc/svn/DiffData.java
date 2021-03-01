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
package org.jmeld.vc.svn;

import java.util.ArrayList;
import java.util.List;
import org.jmeld.diff.JMRevision;
import org.jmeld.vc.DiffIF;

public class DiffData
    implements DiffIF
{
  private List<Target> targetList = new ArrayList<>();

  public DiffData()
  {
  }

  public void addTarget(String path,
      JMRevision revision)
  {
    targetList.add(new Target(path,
                              revision));
  }

  @Override
  public List<Target> getTargetList()
  {
    return targetList;
  }

  static class Target
      implements DiffIF.TargetIF
  {
    private String path;
    private JMRevision revision;

    public Target(String path,
        JMRevision revision)
    {
      this.path = path;
      this.revision = revision;
    }

    @Override
    public String getPath()
    {
      return path;
    }

    @Override
    public JMRevision getRevision()
    {
      return revision;
    }
  }
}
