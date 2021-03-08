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

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;
import org.jmeld.util.conf.AbstractConfigurationElement;

public class FontSettingFx
    extends AbstractConfigurationElement
{
  public SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<>();

  public FontSettingFx()
  {
  }

  public FontSettingFx(Font font)
  {
    setFont(font);
  }

  public SimpleObjectProperty<Font> fontProperty()
  {
    return fontProperty;
  }

  public Font getFont()
  {
    return fontProperty.get();
  }

  public void setFont(Font font)
  {
    fontProperty.set(font);
  }
}
