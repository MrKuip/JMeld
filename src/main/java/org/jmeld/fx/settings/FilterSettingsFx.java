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

import java.util.List;

import org.jmeld.settings.util.Filter;
import org.jmeld.settings.util.FilterRule;
import org.jmeld.util.ObjectUtil;
import org.jmeld.util.conf.AbstractConfigurationElement;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilterSettingsFx
    extends AbstractConfigurationElement
{
  public final SimpleObjectProperty<ObservableList<Filter>> filters = new SimpleObjectProperty<>(FXCollections.observableArrayList());

  public FilterSettingsFx()
  {
  }

  public void init(JMeldSettingsFx parent)
  {
    super.init(parent);

    for (Filter f : filters.get())
    {
      f.init(parent);
    }

    initDefault();
  }

  public void addFilter(Filter filter)
  {
    filter.init(configuration);
    getFilters().add(filter);
    fireChanged();
  }

  public void removeFilter(Filter filter)
  {
    getFilters().remove(filter);
    fireChanged();
  }

  public List<Filter> getFilters()
  {
    return filters.get();
  }

  public Filter getFilter(String name)
  {
    for (Filter f : getFilters())
    {
      if (ObjectUtil.equals(f.getName(),
                            name))
      {
        return f;
      }
    }

    return null;
  }

  private void initDefault()
  {
    Filter filter;

    if (getFilter("default") != null)
    {
      return;
    }

    filter = new Filter("default");
    filter.addRule(new FilterRule("Temporary files",
                                  FilterRule.Rule.excludes,
                                  "**/*~",
                                  true));
    filter.addRule(new FilterRule("Temporary files",
                                  FilterRule.Rule.excludes,
                                  "**/#*#",
                                  true));
    filter.addRule(new FilterRule("Temporary files",
                                  FilterRule.Rule.excludes,
                                  "**/.#*",
                                  true));
    filter.addRule(new FilterRule("Temporary files",
                                  FilterRule.Rule.excludes,
                                  "**/%*%",
                                  true));
    filter.addRule(new FilterRule("Temporary files",
                                  FilterRule.Rule.excludes,
                                  "**/._*",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/.svn",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/.svn/**",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/CVS",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/CVS/**",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/SCCS",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/SCCS/**",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/vssver.scc",
                                  true));
    filter.addRule(new FilterRule("Versioncontrol",
                                  FilterRule.Rule.excludes,
                                  "**/.SYNC",
                                  true));
    filter.addRule(new FilterRule("Mac",
                                  FilterRule.Rule.excludes,
                                  "**/.DS_Store",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.jpg",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.gif",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.png",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.wav",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.mp3",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.ogg",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.xcf",
                                  true));
    filter.addRule(new FilterRule("Media",
                                  FilterRule.Rule.excludes,
                                  "**/.xpm",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.pyc",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.a",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.obj",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.o",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.so",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.la",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.lib",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.dll",
                                  true));
    filter.addRule(new FilterRule("Java",
                                  FilterRule.Rule.excludes,
                                  "**/*.class",
                                  true));
    filter.addRule(new FilterRule("Java",
                                  FilterRule.Rule.excludes,
                                  "**/*.jar",
                                  true));

    addFilter(filter);
    
    filter = new Filter("default2");
    filter.addRule(new FilterRule("haha files",
                                  FilterRule.Rule.excludes,
                                  "**/*~",
                                  true));
    filter.addRule(new FilterRule("Binaries",
                                  FilterRule.Rule.excludes,
                                  "**/.dll",
                                  true));
    filter.addRule(new FilterRule("Java",
                                  FilterRule.Rule.excludes,
                                  "**/*.class",
                                  true));
    filter.addRule(new FilterRule("Java",
                                  FilterRule.Rule.excludes,
                                  "**/*.jar",
                                  true));
    addFilter(filter);
  }
}
