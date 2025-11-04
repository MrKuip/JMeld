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
package org.jmeld.ui.swing;

import java.awt.Color;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.jmeld.diff.JMChunk;
import org.jmeld.diff.JMDelta;
import org.jmeld.diff.JMDiff;
import org.jmeld.diff.JMRevision;
import org.jmeld.util.Ignore;
import org.jmeld.util.TokenizerFactory;
import org.jmeld.util.WordTokenizer;

public class DiffLabel
  extends JTextPane
{
  public DiffLabel()
  {
    init();
  }

  public void init()
  {
    setEditorKit(new HTMLEditorKit());
    setContentType("text/html");
    putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
    setEditable(false);
    setOpaque(false);

    // Bug in Nimbus L&F doesn't honour the opaqueness of a JLabel.
    // Setting a fully transparent color is a workaround:
    setBackground(new Color(0, 0, 0, 0));
    setBorder(null);
  }

  public void setText(String text, String otherText)
  {
    WordTokenizer wt;
    List<String> textList;
    List<String> otherTextList;
    boolean[] boldArray;
    StringBuffer styledText;

    styledText = new StringBuffer();

    try
    {
      wt = TokenizerFactory.getFileNameTokenizer();
      textList = wt.getTokens(text);
      otherTextList = wt.getTokens(otherText);
      boldArray = new boolean[textList.size()];

      if (otherTextList.size() != 0)
      {
        JMRevision revision;

        revision = new JMDiff().diff(textList, otherTextList, Ignore.NULL_IGNORE);
        for (JMDelta delta : revision.getDeltas())
        {
          JMChunk chunk;

          chunk = delta.getOriginal();
          for (int index = 0; index < chunk.getSize(); index++)
          {
            boldArray[chunk.getAnchor() + index] = true;
          }
        }
      }

      for (int index = 0; index < textList.size(); index++)
      {
        String t;
        boolean bold;

        bold = boldArray[index];
        t = textList.get(index);
        if (bold)
        {
          styledText.append("<b>");
        }
        styledText.append(t);
        if (t.equals("/"))
        {
          styledText.append("<wbr>");
        }
        if (bold)
        {
          styledText.append("</b>");
        }
      }

      setText(styledText.toString());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      // Make the best out of this situation. (Should never happen)
      setText(text);
    }
  }

  private static String escapeHtml(String s)
  {
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
        "&#39;");
  }

  /**
   * Set the text on this label. Some parts of the text will be displayed in bold-style. These parts are the differences
   * between text and otherText.
   */
  public void setText2(String text, String otherText)
  {
    WordTokenizer wt;
    List<String> textList;
    List<String> otherTextList;
    JMRevision revision;
    JTextPane fl;
    String[] styles;
    JMChunk chunk;
    String styleName;
    StyledDocument doc;

    text = text.replace("/", " ");
    otherText = otherText.replace("/", " ");

    try
    {
      wt = TokenizerFactory.getFileNameTokenizer();
      textList = wt.getTokens(text);
      otherTextList = wt.getTokens(otherText);

      styles = new String[textList.size()];

      if (otherTextList.size() != 0)
      {
        revision = new JMDiff().diff(textList, otherTextList, Ignore.NULL_IGNORE);

        for (JMDelta delta : revision.getDeltas())
        {
          chunk = delta.getOriginal();
          for (int i = 0; i < chunk.getSize(); i++)
          {
            styles[chunk.getAnchor() + i] = "bold";
          }
        }
      }

      doc = getStyledDocument();
      doc.remove(0, doc.getLength());

      for (int i = 0; i < textList.size(); i++)
      {
        doc.insertString(doc.getLength(), textList.get(i), (styles[i] != null ? doc.getStyle(styles[i]) : null));
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      // Make the best out of this situation. (Should never happen)
      setText(text);
    }
  }
}
