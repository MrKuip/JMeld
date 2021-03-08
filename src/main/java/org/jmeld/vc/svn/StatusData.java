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
import java.util.Date;
import java.util.List;

public class StatusData
{
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "target")
  private List<Target> targetList = new ArrayList<>();

  public StatusData()
  {
  }

  public List<Target> getTargetList()
  {
    return targetList;
  }

  static class Target
  {
    @JacksonXmlProperty(isAttribute = true)
    private String path;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entry")
    private List<Entry> entryList = new ArrayList<>();
    private Against against;

    public Target()
    {
    }

    public String getPath()
    {
      return path;
    }

    public List<Entry> getEntryList()
    {
      return entryList;
    }
  }

  static class Entry
  {
    @JacksonXmlProperty(isAttribute = true)
    private String path;
    @JacksonXmlProperty(localName = "wc-status")
    private WcStatus wcStatus;
    @JacksonXmlProperty(localName = "repos-status")
    private ReposStatus reposStatus;

    public Entry()
    {
    }

    public String getPath()
    {
      return path;
    }

    public WcStatus getWcStatus()
    {
      return wcStatus;
    }
  }

  static class WcStatus
  {
    @JacksonXmlProperty(isAttribute = true)
    private ItemStatus item;
    @JacksonXmlProperty(isAttribute = true)
    private String props;
    @JacksonXmlProperty(isAttribute = true)
    private Integer revision;
    @JacksonXmlProperty(localName = "wc-locked", isAttribute = true)
    private Boolean wcLocked;
    @JacksonXmlProperty(isAttribute = true)
    private Boolean copied;
    @JacksonXmlProperty(isAttribute = true)
    private Boolean switched;
    private Commit commit;
    private Lock lock;

    public WcStatus()
    {
    }

    public String getProps()
    {
      return props;
    }

    public String getRevision()
    {
      if (revision == null)
      {
        return "0";
      }

      return revision.toString();
    }

    public ItemStatus getItem()
    {
      return item;
    }
  }

  static class ReposStatus
  {
    @JacksonXmlProperty(isAttribute = true)
    private String item;
    @JacksonXmlProperty(isAttribute = true)
    private String props;
    private Lock lock;

    public ReposStatus()
    {
    }

    public String getProps()
    {
      return props;
    }

    public String getItem()
    {
      return item;
    }
  }

  static class Against
  {
    @JacksonXmlProperty(isAttribute = true)
    private Integer revision;

    public Against()
    {
    }
  }

  static class Commit
  {
    @JacksonXmlProperty(isAttribute = true)
    private Integer revision;
    private String author;
    private Date date;

    public Commit()
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
  }

  static class Lock
  {
    private String token;
    private String owner;
    private String comment;
    private Date created;
    private Date expires;

    public Lock()
    {
    }
  }

  public static enum ItemStatus
  {
    added('A'),
    conflicted('C'),
    deleted('D'),
    ignored('I'),
    modified('M'),
    replaced('R'),
    external('X'),
    unversioned('?'),
    incomplete('!'),
    obstructed('-'),
    normal(' '),
    none(' '),
    missing('!');

    private char shortText;

    ItemStatus(char shortText)
    {
      this.shortText = shortText;
    }

    public char getShortText()
    {
      return shortText;
    }
  }
}
