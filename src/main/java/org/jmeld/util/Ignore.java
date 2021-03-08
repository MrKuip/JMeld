package org.jmeld.util;

import javafx.beans.property.SimpleBooleanProperty;
import org.jmeld.util.conf.AbstractConfiguration;
import org.jmeld.util.conf.AbstractConfigurationElement;

public class Ignore
    extends AbstractConfigurationElement
{
  // Class variables:
  static public final Ignore NULL_IGNORE = new Ignore();

  // Instance variables:
  public SimpleBooleanProperty ignoreWhitespaceAtBegin = new SimpleBooleanProperty(false);
  public SimpleBooleanProperty ignoreWhitespaceInBetween = new SimpleBooleanProperty(false);
  public SimpleBooleanProperty ignoreWhitespaceAtEnd = new SimpleBooleanProperty(false);
  public SimpleBooleanProperty ignoreEOL = new SimpleBooleanProperty(false);
  public SimpleBooleanProperty ignoreBlankLines = new SimpleBooleanProperty(false);
  public SimpleBooleanProperty ignoreCase = new SimpleBooleanProperty(false);

  public Ignore(Ignore ignore)
  {
    this(ignore.ignoreWhitespaceAtBegin.get(),
         ignore.ignoreWhitespaceInBetween.get(),
         ignore.ignoreWhitespaceAtEnd.get(),
         ignore.ignoreEOL.get(),
         ignore.ignoreBlankLines.get(),
         ignore.ignoreCase.get());
  }

  public Ignore()
  {
    this(false,
         false,
         false);
  }

  public Ignore(boolean ignoreWhitespace,
      boolean ignoreEOL,
      boolean ignoreBlankLines)
  {
    this(ignoreWhitespace,
         ignoreWhitespace,
         ignoreWhitespace,
         ignoreEOL,
         ignoreBlankLines,
         false);
  }

  public Ignore(boolean ignoreWhitespaceAtBegin,
      boolean ignoreWhitespaceInBetween,
      boolean ignoreWhitespaceAtEnd,
      boolean ignoreEOL,
      boolean ignoreBlankLines,
      boolean ignoreCase)
  {
    this.ignoreWhitespaceAtBegin.set(ignoreWhitespaceAtBegin);
    this.ignoreWhitespaceInBetween.set(ignoreWhitespaceInBetween);
    this.ignoreWhitespaceAtEnd.set(ignoreWhitespaceAtEnd);
    this.ignoreEOL.set(ignoreEOL);
    this.ignoreBlankLines.set(ignoreBlankLines);
    this.ignoreCase.set(ignoreCase);

    System.out.println("new ignoreWhitespaceAtBegin = " + System.identityHashCode(this.ignoreWhitespaceAtBegin));
    this.ignoreWhitespaceAtBegin.addListener((observable, oldValue, newValue) -> System.out
        .println("Changed " + oldValue + " -> " + newValue));
    this.ignoreBlankLines.addListener((observable, oldValue, newValue) -> System.out
        .println("Changed " + oldValue + " -> " + newValue));
    this.ignoreWhitespaceInBetween.addListener((observable, oldValue, newValue) -> fireChanged());
    this.ignoreWhitespaceAtEnd.addListener((observable, oldValue, newValue) -> fireChanged());
    this.ignoreEOL.addListener((observable, oldValue, newValue) -> fireChanged());
    this.ignoreCase.addListener((observable, oldValue, newValue) -> fireChanged());

    init();
  }

  @Override
  public void init(AbstractConfiguration configuration)
  {
    super.init(configuration);
    init();
  }

  private void init()
  {
  }

  public boolean getIgnoreWhitespaceAtBegin()
  {
    return ignoreWhitespaceAtBegin.get();
  }

  public void setIgnoreWhitespaceAtBegin(boolean ignoreWhitespaceAtBegin)
  {
    System.out.println("setIgnoreWhitespaceAtBegin = " + System.identityHashCode(this.ignoreWhitespaceAtBegin));
    this.ignoreWhitespaceAtBegin.set(ignoreWhitespaceAtBegin);
  }

  public boolean getIgnoreWhitespaceInBetween()
  {
    return ignoreWhitespaceInBetween.get();
  }

  public void setIgnoreWhitespaceInBetween(boolean ignoreWhitespaceInBetween)
  {
    this.ignoreWhitespaceInBetween.set(ignoreWhitespaceInBetween);
  }

  public boolean getIgnoreWhitespaceAtEnd()
  {
    return ignoreWhitespaceAtEnd.get();
  }

  public void setIgnoreWhitespaceAtEnd(boolean ignoreWhitespaceAtEnd)
  {
    this.ignoreWhitespaceAtEnd.set(ignoreWhitespaceAtEnd);
  }

  public boolean getIgnoreEOL()
  {
    return ignoreEOL.get();
  }

  public void setIgnoreEOL(boolean ignoreEOL)
  {
    this.ignoreEOL.set(ignoreEOL);
  }

  public boolean getIgnoreBlankLines()
  {
    return ignoreBlankLines.get();
  }

  public void setIgnoreBlankLines(boolean ignoreBlankLines)
  {
    this.ignoreBlankLines.set(ignoreBlankLines);
  }

  public boolean getIgnoreCase()
  {
    return ignoreCase.get();
  }

  public void setIgnoreCase(boolean ignoreCase)
  {
    this.ignoreCase.set(ignoreCase);
  }

  public boolean getIgnore()
  {
    return ignoreWhitespaceAtBegin.get() || ignoreWhitespaceInBetween.get() || ignoreWhitespaceAtEnd.get()
        || ignoreEOL.get() || ignoreBlankLines.get() || ignoreCase.get();
  }

  public boolean getIgnoreWhitespace()
  {
    return ignoreWhitespaceAtBegin.get() || ignoreWhitespaceInBetween.get() || ignoreWhitespaceAtEnd.get();
  }

  @Override
  public String toString()
  {
    StringBuilder builder;

    builder = new StringBuilder("ignore:");
    builder.append(!getIgnore() ? "nothing" : "");
    builder.append(getIgnoreWhitespaceAtBegin() ? "whitespace[begin] " : "");
    builder.append(getIgnoreWhitespaceInBetween() ? "whitespace[in between] " : "");
    builder.append(getIgnoreWhitespaceAtEnd() ? "whitespace[end] " : "");
    builder.append(getIgnoreEOL() ? "eol " : "");
    builder.append(getIgnoreBlankLines() ? "blanklines " : "");
    builder.append(getIgnoreCase() ? "case " : "");

    return builder.toString();
  }
}
