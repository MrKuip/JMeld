package org.jmeld.fx.util;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FxAwtUtil
{
  public static Color toFxColor(java.awt.Color awtColor)
  {
    return javafx.scene.paint.Color.rgb(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(),
        awtColor.getAlpha() / 255.0);
  }

  public static Font toFxFont(java.awt.Font awtFont)
  {
    String family = awtFont.getFamily();
    double size = awtFont.getSize2D();

    boolean bold = awtFont.isBold();
    boolean italic = awtFont.isItalic();

    FontWeight weight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
    FontPosture posture = italic ? FontPosture.ITALIC : FontPosture.REGULAR;

    return Font.font(family, weight, posture, size);
  }

  public static java.awt.Font toAwtFont(Font fxFont)
  {
    String family = fxFont.getFamily();
    String style = fxFont.getStyle().toLowerCase();

    int awtStyle = java.awt.Font.PLAIN;
    if (style.contains("bold"))
    {
      awtStyle |= java.awt.Font.BOLD;
    }
    if (style.contains("italic"))
    {
      awtStyle |= java.awt.Font.ITALIC;
    }

    return new java.awt.Font(family, awtStyle, (int) Math.round(fxFont.getSize()));
  }

  public static <T> SimpleObjectProperty<T> property(Supplier<T> getter, Consumer<T> setter)
  {
    return new FxPropertyAdapter<T>(getter, setter);
  }

  public static class FxPropertyAdapter<T>
    extends SimpleObjectProperty<T>
  {
    private final Supplier<T> mi_getter;
    private final Consumer<T> mi_setter;
    private boolean updating = false;

    private FxPropertyAdapter(Supplier<T> getter, Consumer<T> setter)
    {
      mi_getter = getter;
      mi_setter = setter;

      setValue(getter.get());

      // Whenever JavaFX property changes, update the bean
      addListener((obs, oldVal, newVal) -> {
        if (!updating)
        {
          try
          {
            updating = true;
            setter.accept(newVal);
          }
          finally
          {
            updating = false;
          }
        }
      });
    }

    @Override
    public void set(T newValue)
    {
      super.set(newValue);
      if (!updating)
      {
        try
        {
          updating = true;
          mi_setter.accept(newValue);
        }
        finally
        {
          updating = false;
        }
      }
    }

    /** Syncs from bean → property (useful if bean changes externally). */
    public void refreshFromBean()
    {
      setValue(mi_getter.get());
    }
  }
}
