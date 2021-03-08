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

public class InfoData
{
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "entry")
  private List<Entry> entryList = new ArrayList<>();

  public InfoData()
  {
    entryList = new ArrayList<Entry>();
  }

  public List<Entry> getEntryList()
  {
    return entryList;
  }

  static class Entry
  {
    @JacksonXmlProperty(isAttribute = true)
    private String path;
    @JacksonXmlProperty(isAttribute = true)
    private String dir;
    @JacksonXmlProperty(isAttribute = true)
    private String file;
    @JacksonXmlProperty(isAttribute = true)
    private Integer revision;
    private String url;
    private Repository repository;
    @JacksonXmlProperty(localName = "wc-info")
    private WcInfo wcInfo;
    private Commit commit;
    private Lock lock;

    public Entry()
    {
    }

    public String getDir()
    {
      return dir;
    }

    public String getFile()
    {
      return file;
    }

    public String getPath()
    {
      return path;
    }

    public Integer getRevision()
    {
      return revision;
    }

    public String getUrl()
    {
      return url;
    }

    public Repository getRepository()
    {
      return repository;
    }

    public WcInfo getWcInfo()
    {
      return wcInfo;
    }

    public Commit getCommit()
    {
      return commit;
    }
  }

  static class Repository
  {
    private String root;
    private String uuid;

    public Repository()
    {
    }

    public String getRoot()
    {
      return root;
    }

    public String getUUID()
    {
      return uuid;
    }
  }

  static class WcInfo
  {
    private String schedule;
    @JacksonXmlProperty(localName = "copy-from-url")
    private String copyFromUrl;
    @JacksonXmlProperty(localName = "copy-from-rev")
    private String copyFromRev;
    @JacksonXmlProperty(localName = "text-updated")
    private Date textUpdated;
    @JacksonXmlProperty(localName = "prop-updated")
    private Date propUpdated;
    private String checksum;
    private Confict conflict;

    public WcInfo()
    {
    }

    public String getSchedule()
    {
      return schedule;
    }

    public Date getTextUpdated()
    {
      return textUpdated;
    }

    public String getChecksum()
    {
      return checksum;
    }
  }

  static class Confict
  {
    @JacksonXmlProperty(localName = "prev-base-file")
    private String prevBaseFile;
    @JacksonXmlProperty(localName = "prev-wc-file")
    private String prevWcFile;
    @JacksonXmlProperty(localName = "cur-base-file")
    private String curBaseFile;
    @JacksonXmlProperty(localName = "prop-file")
    private String propFile;

    public Confict()
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
}
