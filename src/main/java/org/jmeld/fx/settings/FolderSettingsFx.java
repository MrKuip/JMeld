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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jmeld.util.conf.AbstractConfigurationElement;

public class FolderSettingsFx
    extends AbstractConfigurationElement
{
  // Instance variables:
  public SimpleObjectProperty<FolderView> viewProperty = new SimpleObjectProperty<>(FolderView.packageView);
  public SimpleBooleanProperty onlyLeftProperty = new SimpleBooleanProperty(true);
  public SimpleBooleanProperty leftRightChangedProperty = new SimpleBooleanProperty(true);
  public SimpleBooleanProperty onlyRightProperty = new SimpleBooleanProperty(false);
  public SimpleBooleanProperty leftRightUnChangedProperty = new SimpleBooleanProperty(false);

  public FolderSettingsFx()
  {
  }

  public FolderView getView()
  {
    return viewProperty.get();
  }

  public void setView(FolderView view)
  {
    this.viewProperty.set(view);
    fireChanged();
  }

  public void setOnlyLeft(boolean onlyLeft)
  {
    this.onlyLeftProperty.set(onlyLeft);
    fireChanged();
  }

  public boolean getOnlyLeft()
  {
    return onlyLeftProperty.get();
  }

  public void setLeftRightChanged(boolean leftRightChanged)
  {
    this.leftRightChangedProperty.set(leftRightChanged);
    fireChanged();
  }

  public boolean getLeftRightChanged()
  {
    return leftRightChangedProperty.get();
  }

  public void setOnlyRight(boolean onlyRight)
  {
    this.onlyRightProperty.set(onlyRight);
    fireChanged();
  }

  public boolean getOnlyRight()
  {
    return onlyRightProperty.get();
  }

  public void setLeftRightUnChanged(boolean leftRightUnChanged)
  {
    this.leftRightUnChangedProperty.set(leftRightUnChanged);
    fireChanged();
  }

  public boolean getLeftRightUnChanged()
  {
    return leftRightUnChangedProperty.get();
  }

  public enum FolderView
  {
    fileView("File view"),
    directoryView("Folder view"),
    packageView("Package view");

    // instance variables:
    private String text;

    private FolderView(String text)
    {
      this.text = text;
    }

    @Override
    public String toString()
    {
      return text;
    }
  }
}
