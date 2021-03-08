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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogData
{
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "logentry")
  private List<Entry> entryList = new ArrayList<>();

  public LogData()
  {
  }

  public List<Entry> getEntryList()
  {
    return entryList;
  }

  public static class Entry
  {
    @JacksonXmlProperty(isAttribute = true)
    private Integer revision;
    private String author;
    private Date date;
    @JacksonXmlElementWrapper(localName = "paths")
    @JacksonXmlProperty(localName = "path")
    private List<Path> pathList = new ArrayList<>();
    private String msg;

    public Entry()
    {
    }

    public Integer getRevision()
    {
      return revision;
    }

    public String getAuthor()
    {
      return author;
    }

    public Date getDate()
    {
      return date;
    }

    public String getMsg()
    {
      return msg;
    }

    public List<Path> getPathList()
    {
      return pathList;
    }
  }

  static public class Path
  {
    @JacksonXmlProperty(isAttribute = true)
    private String action;
    @JacksonXmlProperty(localName = "copyfrom-path", isAttribute = true)
    private String copyFromPath;
    @JacksonXmlProperty(localName = "copyfrom-rev", isAttribute = true)
    private Integer copyFromRev;
    @JacksonXmlText
    private String pathName;

    public Path()
    {
    }

    public String getAction()
    {
      return action;
    }

    public String getPathName()
    {
      return pathName;
    }

    public void setPathName(String pathName)
    {
      if (pathName != null)
      {
        pathName = pathName.trim();
      }

      this.pathName = pathName;
    }
  }
}
