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
package org.jmeld.fx.settings;

import org.jmeld.util.conf.AbstractConfiguration;
import org.jmeld.util.conf.ConfigurationManager;

public class JMeldSettingsFx
    extends AbstractConfiguration
{
  // class variables:
  public static JMeldSettingsFx instance;

  // Instance variables:
  private EditorSettingsFx editor = new EditorSettingsFx();
  private FilterSettingsFx filter = new FilterSettingsFx();
  private FolderSettingsFx folder = new FolderSettingsFx();

  public JMeldSettingsFx()
  {
  }

  public static synchronized JMeldSettingsFx getInstance()
  {
    return (JMeldSettingsFx) ConfigurationManager.getInstance().get(JMeldSettingsFx.class);
  }

  @Override
  public void init()
  {
    editor.init(this);
    filter.init(this);
    folder.init(this);
  }

  public EditorSettingsFx getEditor()
  {
    return editor;
  }

  public FilterSettingsFx getFilter()
  {
    return filter;
  }

  public FolderSettingsFx getFolder()
  {
    return folder;
  }
}
