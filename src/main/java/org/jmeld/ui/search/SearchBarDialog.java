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
package org.jmeld.ui.search;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jmeld.ui.AbstractBarDialog;
import org.jmeld.ui.JMeldPanel;
import org.jmeld.ui.util.Icons;
import org.jmeld.util.StringUtil;

public class SearchBarDialog
    extends AbstractBarDialog
{
  // class variables:
  private static final String CP_FOREGROUND = "JMeld.foreground";
  private static final String CP_BACKGROUND = "JMeld.background";

  // Instance variables:
  private JTextField searchField;
  private JLabel searchResult;
  private Timer timer;

  public SearchBarDialog(JMeldPanel meldPanel)
  {
    super(meldPanel);
  }

  @Override
  protected void init()
  {
    JButton closeButton;
    JButton previousButton;
    JButton nextButton;

    setLayout(new FlowLayout(FlowLayout.LEADING));

    // Close the search dialog:
    closeButton = new JButton(Icons.CLOSE.getSmallIcon());
    closeButton.addActionListener((e) -> {
      getMeldPanel().doStopSearch(null);
    });
    initButton(closeButton);
    closeButton.setBorder(null);

    // Incremental search:
    searchField = new JTextField(15);
    searchField.getDocument().addDocumentListener(getSearchAction());
    searchField.addKeyListener(getSearchKeyAction());

    // Find previous match:
    previousButton = new JButton("Previous",
                                 Icons.SEARCH_PREVIOUS.getSmallIcon());
    previousButton.addActionListener(getPreviousAction());
    initButton(previousButton);

    // Find next match:
    nextButton = new JButton("Next",
                             Icons.SEARCH_NEXT.getSmallIcon());
    nextButton.addActionListener(getNextAction());
    initButton(nextButton);

    searchResult = new JLabel();

    initButton(previousButton);
    add(closeButton);
    add(Box.createHorizontalStrut(5));
    add(new JLabel("Find:"));
    add(searchField);
    add(previousButton);
    add(nextButton);
    add(Box.createHorizontalStrut(10));
    add(searchResult);

    timer = new Timer(500,
                      executeSearch());
    timer.setRepeats(false);
  }

  private void initButton(AbstractButton button)
  {
    button.setFocusable(false);
    button.setBorderPainted(false);
    button.setBorder(new EmptyBorder(0,
                                     5,
                                     0,
                                     5));
  }

  public SearchCommand getCommand()
  {
    return new SearchCommand(searchField.getText(),
                             false);
  }

  public void setSearchText(String searchText)
  {
    if (StringUtil.isEmpty(searchText))
    {
      return;
    }

    // You can start a search by selecting some text and then
    //   hit CTRL-F. But if you have selected more than x 
    //   characters you probably don't want to search for that.
    //   So I choose to ignore those texts.
    if (searchText.length() > 50)
    {
      return;
    }

    searchField.setText(searchText);
  }

  @Override
  public void _activate()
  {
    searchField.requestFocus();
    searchField.selectAll();

    if (!StringUtil.isEmpty(searchField.getText()))
    {
      timer.restart();
    }
  }

  @Override
  public void _deactivate()
  {
  }

  private DocumentListener getSearchAction()
  {
    return new DocumentListener()
    {
      @Override
      public void changedUpdate(DocumentEvent e)
      {
        timer.restart();
      }

      @Override
      public void insertUpdate(DocumentEvent e)
      {
        timer.restart();
      }

      @Override
      public void removeUpdate(DocumentEvent e)
      {
        timer.restart();
      }
    };
  }

  private ActionListener executeSearch()
  {
    return (e) ->
    {
      boolean notFound;
      Color color;
      String searchText;
      SearchHits searchHits;

      searchText = searchField.getText();

      searchHits = getMeldPanel().doSearch(null);
      notFound = (searchHits == null || searchHits.getSearchHits().size() == 0);

      if (notFound)
      {
        // I would love to set the background to red and foreground
        //   to white but the jdk won't let me set the background if
        //   GTK look&feel is chosen.
        if (searchField.getForeground() != Color.red)
        {
          // Remember the original colors:
          searchField.putClientProperty(CP_FOREGROUND,
                                        searchField.getForeground());

          // Set the new colors:
          searchField.setForeground(Color.red);
        }

        searchResult.setIcon(Icons.ALERT.getSmallIcon());
        searchResult.setText("Phrase not found");
      }
      else
      {
        // Set the original colors:
        color = (Color) searchField.getClientProperty(CP_FOREGROUND);
        if (color != null)
        {
          searchField.setForeground(color);
          searchField.putClientProperty(CP_FOREGROUND,
                                        null);
        }

        if (!StringUtil.isEmpty(searchResult.getText()))
        {
          searchResult.setIcon(null);
          searchResult.setText("");
        }
      }
    };
  }

  private KeyListener getSearchKeyAction()
  {
    return new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
          getMeldPanel().doNextSearch(null);
        }
      }
    };
  }

  private ActionListener getCloseAction()
  {
    return (e) ->
    {
      getMeldPanel().doStopSearch(null);
    };
  }

  private ActionListener getPreviousAction()
  {
    return (e) ->
    {
      getMeldPanel().doPreviousSearch(null);
    };
  }

  private ActionListener getNextAction()
  {
    return (e) ->
    {
      getMeldPanel().doNextSearch(null);
    };
  }
}
