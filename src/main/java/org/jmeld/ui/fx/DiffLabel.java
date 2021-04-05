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
package org.jmeld.ui.fx;

import java.util.List;
import java.util.stream.IntStream;
import javafx.scene.layout.Background;
import org.fxmisc.richtext.CodeArea;
import org.jmeld.diff.JMChunk;
import org.jmeld.diff.JMDelta;
import org.jmeld.diff.JMDiff;
import org.jmeld.diff.JMRevision;
import org.jmeld.util.Ignore;
import org.jmeld.util.TokenizerFactory;
import org.jmeld.util.WordTokenizer;

public class DiffLabel
    extends CodeArea
{
  public DiffLabel()
  {
    init();
  }

  public void init()
  {
    setEditable(false);
    setBackground(Background.EMPTY);
    setWrapText(true);
    setPrefHeight(5);
    setStyle("diffLabel");
  }

  /**
   * Set the text on this label. Some parts of the text will be displayed in bold-style. These parts are the differences
   * between text and otherText.
   */
  public void setText(String text, String otherText)
  {
    WordTokenizer wt;
    List<String> textList;
    List<String> otherTextList;
    JMRevision revision;
    JMChunk chunk;
    int previousAnchor;
    int previousIndex;

    clear();
    appendText(text);
    setStyleClass(0, text.length(), "diffLabel");

    try
    {
      wt = TokenizerFactory.getFileNameTokenizer();
      textList = wt.getTokens(text);
      otherTextList = wt.getTokens(otherText);

      if (otherTextList.size() != 0)
      {
        revision = new JMDiff().diff(textList, otherTextList, Ignore.NULL_IGNORE);

        previousIndex = 0;
        previousAnchor = 0;
        for (JMDelta delta : revision.getDeltas())
        {
          int index;

          chunk = delta.getOriginal();

          index = previousIndex + IntStream.range(previousAnchor, chunk.getAnchor()).mapToObj(i -> textList.get(i))
              .mapToInt(String::length).sum();
          setStyleClass(index, index + textList.get(chunk.getAnchor()).length(), "diffLabel-bold");
          previousAnchor = chunk.getAnchor();
          previousIndex = index;
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
