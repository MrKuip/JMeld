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
package org.jmeld.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.jmeld.ui.util.Icons;
import org.jmeld.util.ResourceLoader;
import net.miginfocom.swing.MigLayout;

public class HelpViewer
  extends JPanel
{
  private Browser m_tocPane;
  private Browser m_contentPane;

  public HelpViewer()
  {
    init();
  }

  public void init()
  {
    m_tocPane = createBrowser();
    m_contentPane = createBrowser();

    setLayout(new MigLayout("fill", "[grow 0, fill][grow, fill]", "[][grow, fill]"));

    add(m_tocPane.getHistoryControlPane(m_contentPane), "cell 0 0");
    add(m_tocPane.getPane(), "cell 0 1");
    add(m_contentPane.getPane(), "cell 1 0, spany 2");

  }

  private static class Browser
  {
    private JEditorPane mi_editorPane;
    private List<HistoryItem> mi_historyList;
    private int mi_historyIndex;

    private Browser()
    {
      HTMLEditorKit kit;
      StyleSheet styleSheet;

      kit = new HTMLEditorKit();

      styleSheet = kit.getStyleSheet();
      styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");

      mi_editorPane = new JEditorPane();
      mi_editorPane.setEditable(false);
      mi_editorPane.setEditorKit(kit);

      mi_historyList = new Stack<>();
    }

    JComponent getPane()
    {
      return mi_editorPane;
    }

    JComponent getHistoryControlPane(Browser browser)
    {
      JPanel panel;
      JButton button;

      panel = new JPanel(new MigLayout());

      button = new JButton(Icons.HOME.getSmallerIcon());
      button.addActionListener((ae) -> { browser.show(browser.getHistoryList().get(0)); });
      panel.add(button);

      button = new JButton(Icons.BACK.getSmallerIcon());
      button.addActionListener((ae) -> { browser.show(browser.incrementHistoryAndGet(-1)); });
      panel.add(button);

      button = new JButton(Icons.FORWARD.getSmallerIcon());
      button.addActionListener((ae) -> { browser.show(browser.incrementHistoryAndGet(1)); });
      panel.add(button);

      button = new JButton(Icons.RELOAD.getSmallerIcon());
      button.addActionListener((ae) -> { browser.show(browser.incrementHistoryAndGet(0)); });
      panel.add(button);

      return panel;
    }

    private List<HistoryItem> getHistoryList()
    {
      return mi_historyList;
    }

    private HistoryItem incrementHistoryAndGet(int increment)
    {
      return getHistoryList().get(incrementHistory(increment));
    }

    private int incrementHistory(int increment)
    {
      mi_historyIndex += increment;
      if (mi_historyIndex < 0)
      {
        mi_historyIndex = 0;
      }
      if (mi_historyIndex >= mi_historyList.size())
      {
        mi_historyIndex = mi_historyList.size() - 1;
      }

      return mi_historyIndex;
    }

    public void addHyperlinkListener(HyperlinkListener listener)
    {
      mi_editorPane.addHyperlinkListener(listener);
    }

    public void show(String url)
    {
      show(url, true);
    }

    public void show(HistoryItem historyItem)
    {
      show(historyItem.getURL(), false);
    }

    private final void show(String url, boolean appendToHistory)
    {
      System.out.println("show: " + url);
      try
      {
        mi_editorPane.setPage(ResourceLoader.getResource(url));
        if (appendToHistory)
        {
          mi_historyList.add(new HistoryItem(url));
          mi_historyIndex = mi_historyList.size() - 1;
        }
      }
      catch (IOException e)
      {
        mi_editorPane.setText("<html><body>Resource '" + url + "' not found!</body></html>");
      }
    }

    static private class HistoryItem
    {
      private final String mii_url;

      HistoryItem(String url)
      {
        mii_url = url;
      }

      public String getURL()
      {
        return mii_url;
      }
    }
  }

  private Browser createBrowser()
  {
    Browser browser;

    browser = new Browser();
    browser.addHyperlinkListener((a) -> {
      if (a.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
      {
        showContent(a.getDescription());
      }
    });

    return browser;
  }

  public void showContent(String url)
  {
    m_contentPane.show(url);
  }

  public void showTOC(String url)
  {
    m_tocPane.show(url);
  }

  public static void main(String[] args)
  {
    JFrame frame;
    HelpViewer helpViewer;

    helpViewer = new HelpViewer();
    helpViewer.showTOC("help/TOC.html");
    helpViewer.showContent("help/Licenses.html");

    frame = new JFrame("HtmlEditorKit Test");
    frame.getContentPane().add(new JScrollPane(helpViewer), BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(300, 200));
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}