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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;
import org.jmeld.vc.BlameIF;

public class BlameData
    implements BlameIF
{
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "target")
  private List<Target> targetList;

  public BlameData()
  {
    targetList = new ArrayList<>();
  }

  @Override
  public List<Target> getTargetList()
  {
    return targetList;
  }

  static class Target
      implements BlameIF.TargetIF
  {
    @JacksonXmlProperty(isAttribute = true)
    private String path;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entry")
    private List<Entry> entryList;

    public Target()
    {
    }

    @Override
    public String getPath()
    {
      return path;
    }

    @Override
    public List<Entry> getEntryList()
    {
      return entryList;
    }
  }

  static class Entry
      implements BlameIF.EntryIF
  {
    @JacksonXmlProperty(localName = "line-number", isAttribute = true)
    private Integer lineNumber;
    private Commit commit;

    public Entry()
    {
    }

    @Override
    public Integer getLineNumber()
    {
      return lineNumber;
    }

    @Override
    public Commit getCommit()
    {
      return commit;
    }
  }

  static class Commit
      implements BlameIF.CommitIF
  {
    @JacksonXmlProperty(isAttribute = true)
    private Integer revision;
    private String author;
    private String date;

    public Commit()
    {
    }

    @Override
    public Integer getRevision()
    {
      return revision;
    }

    @Override
    public String getAuthor()
    {
      return author;
    }

    @Override
    public String getDate()
    {
      return date;
    }
  }
}
