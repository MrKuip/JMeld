package org.jmeld.ui.swing;

import org.jmeld.ui.util.*;

import javax.swing.text.*;

import java.awt.*;

public class JMHighlightPainter
       extends DefaultHighlighter.DefaultHighlightPainter
{
  public static final JMHighlightPainter ADDED;
  public static final JMHighlightPainter ADDED_LINE;
  public static final JMHighlightPainter CHANGED;
  public static final JMHighlightPainter CHANGED2;
  public static final JMHighlightPainter DELETED;
  public static final JMHighlightPainter DELETED_LINE;
  public static final JMHighlightPainter CURRENT_SEARCH;
  public static final JMHighlightPainter SEARCH;

  static
  {
    ADDED = new JMHighlightPainter(Colors.ADDED);
    ADDED_LINE = new JMHighlightPainter(Colors.ADDED, true);
    CHANGED = new JMHighlightPainter(Colors.CHANGED);
    CHANGED2 = new JMHighlightPainter(Colors.CHANGED2);
    DELETED = new JMHighlightPainter(Colors.DELETED);
    DELETED_LINE = new JMHighlightPainter(Colors.DELETED, true);
    SEARCH = new JMHighlightPainter(Color.yellow);
    CURRENT_SEARCH = new JMHighlightPainter(Color.yellow.darker());
  }

  private Color   color;
  private boolean line;

  private JMHighlightPainter(Color color)
  {
    this(color, false);
  }

  private JMHighlightPainter(
    Color   color,
    boolean line)
  {
    super(color);

    this.color = color;
    this.line = line;
  }

  public void paint(
    Graphics       g,
    int            p0,
    int            p1,
    Shape          shape,
    JTextComponent comp)
  {
    Rectangle b;
    Rectangle r1;
    Rectangle r2;
    int       x;
    int       y;
    int       width;
    int       count;

    b = shape.getBounds();

    try
    {
      r1 = comp.modelToView(p0);
      r2 = comp.modelToView(p1);

      g.setColor(color);
      if (line)
      {
        g.drawLine(0, r1.y, b.x + b.width, r1.y);
      }
      else
      {
        if (this == CHANGED2 || this == SEARCH || this == CURRENT_SEARCH)
        {
          if (r1.y == r2.y)
          {
            g.fillRect(r1.x, r1.y, r2.x - r1.x, r1.height);
          }
          else
          {
            count = ((r2.y - r1.y) / r1.height) + 1;
            y = r1.y;
            for (int i = 0; i < count; i++)
            {
              y += (i * r1.height);
              if (i == 0)
              {
                // firstline:
                x = r1.x;
                width = b.width - b.x;
              }
              else if (i == count - 1)
              {
                // lastline:
                x = b.x;
                width = r2.x;
              }
              else
              {
                // all lines in between the first and the lastline:
                x = b.x;
                width = b.width - b.x;
              }

              g.fillRect(x, y, width, r1.height);
            }
          }
        }
        else
        {
          g.fillRect(0, r1.y, b.x + b.width, r2.y - r1.y);
        }
      }
    }
    catch (BadLocationException ex)
    {
      ex.printStackTrace();
    }
  }
}
