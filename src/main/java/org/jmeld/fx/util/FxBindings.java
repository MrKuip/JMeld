package org.jmeld.fx.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jmeld.settings.JMeldSettings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FxBindings
{
  public static Color toFxColor(java.awt.Color awtColor)
  {
    if (awtColor == null)
    {
      return null;
    }
    return new Color(awtColor.getRed() / 255.0, awtColor.getGreen() / 255.0, awtColor.getBlue() / 255.0,
        awtColor.getAlpha() / 255.0);
  }

  public static java.awt.Color toAwtColor(javafx.scene.paint.Color fxColor)
  {
    if (fxColor == null)
    {
      return null;
    }

    return new java.awt.Color((int) Math.round(fxColor.getRed() * 255), (int) Math.round(fxColor.getGreen() * 255),
        (int) Math.round(fxColor.getBlue() * 255), (int) Math.round(fxColor.getOpacity() * 255));
  }

  public static Font toFxFont(java.awt.Font awtFont)
  {
    String family;
    double size;
    boolean bold;
    boolean italic;
    FontWeight weight;
    FontPosture posture;

    if (awtFont == null)
    {
      return null;
    }

    family = awtFont.getFamily();
    size = awtFont.getSize2D();
    bold = awtFont.isBold();
    italic = awtFont.isItalic();
    weight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
    posture = italic ? FontPosture.ITALIC : FontPosture.REGULAR;

    return Font.font(family, weight, posture, size);
  }

  public static java.awt.Font toAwtFont(Font fxFont)
  {
    String family;
    String style;
    int awtStyle;

    if (fxFont == null)
    {
      return null;
    }

    family = fxFont.getFamily();
    style = fxFont.getStyle().toLowerCase();

    awtStyle = java.awt.Font.PLAIN;
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

  public static class FxPropertyBooleanAdapter
    extends SimpleBooleanProperty
  {
    private final Supplier<Boolean> mi_getter;
    private final Consumer<Boolean> mi_setter;
    private boolean updating = false;

    private FxPropertyBooleanAdapter(Supplier<Boolean> getter, Consumer<Boolean> setter)
    {
      mi_getter = getter;
      mi_setter = setter;

      JMeldSettings settings = JMeldSettings.getInstance();

      init(settings::addConfigurationListener);

      JMeldSettings.getInstance().addConfigurationListener(() -> { refreshFromBean(); });

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

    private <T> void init(Consumer<T> object)
    {
      object.accept((T) new Object());
    }

    @Override
    public void set(boolean newValue)
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

  public static <T> SimpleObjectProperty<T> objectProperty(Supplier<T> getter, Consumer<T> setter)
  {
    return new FxPropertyAdapter<T>(getter, setter);
  }

  public static SimpleBooleanProperty booleanProperty(Supplier<Boolean> getter, Consumer<Boolean> setter)
  {
    return new FxPropertyBooleanAdapter(getter, setter);
  }

  public static <T> void bindBidirectional(Property<T> property, Supplier<T> getter, Consumer<T> setter)
  {
    property.bindBidirectional(objectProperty(getter, setter));
  }

  public static <T, R> void bindBidirectional(Property<T> property, Supplier<R> getter, Consumer<R> setter,
      Function<T, R> convertFrom, Function<R, T> convertTo)
  {
    property.bindBidirectional(
        new FxPropertyAdapter<T>(convertGetter(getter, convertTo), convertSetter(setter, convertFrom)));
  }

  private static <T, R> Supplier<T> convertGetter(Supplier<R> getter, Function<R, T> convert)
  {
    return () -> convert.apply(getter.get());
  }

  private static <T, R> Consumer<T> convertSetter(Consumer<R> setter, Function<T, R> convert)
  {
    return (v) -> setter.accept(convert.apply(v));
  }
}
