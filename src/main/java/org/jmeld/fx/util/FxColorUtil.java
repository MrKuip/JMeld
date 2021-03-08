package org.jmeld.fx.util;

import javafx.scene.paint.Color;

public class FxColorUtil
{
  private FxColorUtil()
  {
  }

  public static Color lighter(Color color)
  {
    return lighter(color, -0.10f);
  }

  public static Color brighter(Color color)
  {
    return brighter(color, 0.05f);
  }

  public static Color darker(Color color)
  {
    return brighter(color, -0.05f);
  }

  public static Color brighter(Color color, float factor)
  {
    return setBrightness(color, factor);
  }

  public static Color lighter(Color color, float factor)
  {
    return setSaturation(color, factor);
  }

  public static Color setSaturation(Color color, float saturation)
  {
    return color.deriveColor(1.0, saturation, 1.0, 1.0);
  }

  public static Color setBrightness(Color color, float brightness)
  {
    return color.deriveColor(1.0, 1.0, brightness, 1.0);
  }
}
