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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jmeld.fx.util.FxColors;
import org.jmeld.util.Ignore;
import org.jmeld.util.conf.AbstractConfiguration;
import org.jmeld.util.conf.AbstractConfigurationElement;

public class EditorSettingsFx
    extends AbstractConfigurationElement
{
  public SimpleBooleanProperty showLineNumbersProperty = new SimpleBooleanProperty();
  public SimpleIntegerProperty tabSizeProperty = new SimpleIntegerProperty(4);
  public Ignore ignore = new Ignore();
  public SimpleBooleanProperty leftsideReadonlyProperty = new SimpleBooleanProperty();
  public SimpleBooleanProperty rightsideReadonlyProperty = new SimpleBooleanProperty();
  public ColorSettingFx addedColor;
  public ColorSettingFx changedColor;
  public ColorSettingFx deletedColor;
  public SimpleBooleanProperty defaultFontProperty = new SimpleBooleanProperty();
  public SimpleBooleanProperty customFontProperty = new SimpleBooleanProperty();
  public FontSettingFx font;
  public SimpleBooleanProperty antialiasProperty = new SimpleBooleanProperty();
  public SimpleBooleanProperty defaultFileEncodingEnabledProperty = new SimpleBooleanProperty(true);
  public SimpleBooleanProperty detectFileEncodingEnabledProperty = new SimpleBooleanProperty();
  public SimpleBooleanProperty specificFileEncodingEnabledProperty = new SimpleBooleanProperty();
  public SimpleStringProperty specificFileEncodingNameProperty = new SimpleStringProperty();
  public SimpleStringProperty lookAndFeelNameProperty = new SimpleStringProperty();
  public SimpleObjectProperty<ToolbarButtonIcon> toolbarButtonIconProperty = new SimpleObjectProperty<>(ToolbarButtonIcon.LARGE);
  public SimpleBooleanProperty toolbarButtonTextEnabledProperty = new SimpleBooleanProperty(true);
  public SimpleBooleanProperty toolbarButtonIconEnabledProperty = new SimpleBooleanProperty(true);

  public EditorSettingsFx()
  {
  }

  @Override
  public void init(AbstractConfiguration configuration)
  {
    super.init(configuration);

    ignore.init(configuration);
  }

  public boolean getShowLineNumbers()
  {
    return showLineNumbersProperty.get();
  }

  public void setShowLineNumbers(boolean showLineNumbers)
  {
    this.showLineNumbersProperty.set(showLineNumbers);
    fireChanged();
  }

  public int getTabSize()
  {
    return tabSizeProperty.get();
  }

  public void setTabSize(int tabSize)
  {
    this.tabSizeProperty.set(tabSize);
    fireChanged();
  }

  public Ignore getIgnore()
  {
    return ignore;
  }

  public void setIgnoreWhitespaceAtBegin(boolean ignoreWhitespaceAtBegin)
  {
    ignore.setIgnoreWhitespaceAtBegin(ignoreWhitespaceAtBegin);
    fireChanged();
  }

  public void setIgnoreWhitespaceInBetween(boolean ignoreWhitespaceInBetween)
  {
    ignore.setIgnoreWhitespaceInBetween(ignoreWhitespaceInBetween);
    fireChanged();
  }

  public void setIgnoreWhitespaceAtEnd(boolean ignoreWhitespaceAtEnd)
  {
    ignore.setIgnoreWhitespaceAtEnd(ignoreWhitespaceAtEnd);
    fireChanged();
  }

  public void setIgnoreEOL(boolean ignoreEOL)
  {
    ignore.setIgnoreEOL(ignoreEOL);
    fireChanged();
  }

  public void setIgnoreBlankLines(boolean ignoreBlankLines)
  {
    ignore.setIgnoreBlankLines(ignoreBlankLines);
    fireChanged();
  }

  public void setIgnoreCase(boolean ignoreCase)
  {
    ignore.setIgnoreCase(ignoreCase);
    fireChanged();
  }

  public boolean getLeftsideReadonly()
  {
    return leftsideReadonlyProperty.get();
  }

  public void setLeftsideReadonly(boolean leftsideReadonly)
  {
    this.leftsideReadonlyProperty.set(leftsideReadonly);
    fireChanged();
  }

  public boolean getRightsideReadonly()
  {
    return rightsideReadonlyProperty.get();
  }

  public void setRightsideReadonly(boolean rightsideReadonly)
  {
    this.rightsideReadonlyProperty.set(rightsideReadonly);
    fireChanged();
  }

  public boolean getDefaultFileEncodingEnabled()
  {
    return defaultFileEncodingEnabledProperty.get();
  }

  public void setDefaultFileEncodingEnabled(boolean encoding)
  {
    this.defaultFileEncodingEnabledProperty.set(encoding);
    fireChanged();
  }

  public boolean getDetectFileEncodingEnabled()
  {
    return detectFileEncodingEnabledProperty.get();
  }

  public void setDetectFileEncodingEnabled(boolean encoding)
  {
    this.detectFileEncodingEnabledProperty.set(encoding);
    fireChanged();
  }

  public boolean getSpecificFileEncodingEnabled()
  {
    return specificFileEncodingEnabledProperty.get();
  }

  public void setSpecificFileEncodingEnabled(boolean encoding)
  {
    this.specificFileEncodingEnabledProperty.set(encoding);
    fireChanged();
  }

  public String getSpecificFileEncodingName()
  {
    return specificFileEncodingNameProperty.get();
  }

  public void setSpecificFileEncodingName(String encodingName)
  {
    this.specificFileEncodingNameProperty.set(encodingName);
    fireChanged();
  }

  public void restoreColors()
  {
    addedColor = null;
    changedColor = null;
    deletedColor = null;
    fireChanged();
  }

  public void setAddedColor(Color color)
  {
    addedColor = new ColorSettingFx(color);
    fireChanged();
  }

  public Color getAddedColor()
  {
    return getColor(addedColor, FxColors.ADDED);
  }

  public void setChangedColor(Color color)
  {
    changedColor = new ColorSettingFx(color);
    fireChanged();
  }

  public Color getChangedColor()
  {
    return getColor(changedColor, FxColors.CHANGED);
  }

  public void setDeletedColor(Color color)
  {
    deletedColor = new ColorSettingFx(color);
    fireChanged();
  }

  public Color getDeletedColor()
  {
    return getColor(deletedColor, FxColors.DELETED);
  }

  public void setLookAndFeelName(String lookAndFeelName)
  {
    this.lookAndFeelNameProperty.set(lookAndFeelName);
    fireChanged();
  }

  public String getLookAndFeelName()
  {
    return lookAndFeelNameProperty.get();
  }

  public void setToolbarButtonIcon(ToolbarButtonIcon toolbarButtonIcon)
  {
    this.toolbarButtonIconProperty.set(toolbarButtonIcon);
    fireChanged();
  }

  public ToolbarButtonIcon getToolbarButtonIcon()
  {
    return toolbarButtonIconProperty.get();
  }

  public void setToolbarButtonTextEnabled(boolean toolbarButtonTextEnabled)
  {
    this.toolbarButtonTextEnabledProperty.set(toolbarButtonTextEnabled);
    fireChanged();
  }

  public boolean getToolbarButtonTextEnabled()
  {
    return toolbarButtonTextEnabledProperty.get();
  }

  public void setToolbarButtonIconEnabled(boolean toolbarButtonIconEnabled)
  {
    this.toolbarButtonIconEnabledProperty.set(toolbarButtonIconEnabled);
    fireChanged();
  }

  public boolean getToolbarButtonIconEnabled()
  {
    return toolbarButtonIconEnabledProperty.get();
  }

  public void setDefaultFont(boolean defaultFont)
  {
    this.defaultFontProperty.set(defaultFont);
    fireChanged();
  }

  public boolean getDefaultFont()
  {
    return defaultFontProperty.get();
  }

  public void setCustomFont(boolean customFont)
  {
    this.customFontProperty.set(customFont);
    fireChanged();
  }

  public boolean getCustomFont()
  {
    return customFontProperty.get();
  }

  public void setAntialias(boolean antialias)
  {
    this.antialiasProperty.set(antialias);
    fireChanged();
  }

  public boolean getAntialias()
  {
    return antialiasProperty.get();
  }

  public void setFont(Font f)
  {
    font = new FontSettingFx(f);
    fireChanged();
  }

  public Font getFont()
  {
    return font == null ? null : font.getFont();
  }

  private Color getColor(ColorSettingFx cc, Color defaultColor)
  {
    Color c;

    c = null;
    if (cc != null)
    {
      c = cc.getColor();
    }

    if (c == null)
    {
      c = defaultColor;
    }

    return c;
  }

  public enum ToolbarButtonIcon
  {
    NO("no icon"),
    SMALL("small icon"),
    LARGE("large icon");

    // instance variables:
    private String text;

    private ToolbarButtonIcon(String text)
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
