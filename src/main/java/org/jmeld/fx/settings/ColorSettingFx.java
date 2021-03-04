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
import javafx.scene.paint.Color;
import org.jmeld.util.conf.AbstractConfigurationElement;

public class ColorSettingFx
    extends AbstractConfigurationElement
{
  public SimpleObjectProperty<Color> colorProperty = new SimpleObjectProperty<>();

  public ColorSettingFx()
  {
  }

  public ColorSettingFx(Color color)
  {
    setColor(color);
  }

  public SimpleObjectProperty<Color> colorProperty()
  {
    return colorProperty;
  }

  public Color getColor()
  {
    return colorProperty.get();
  }

  public void setColor(Color color)
  {
    colorProperty.set(color);
  }
}
