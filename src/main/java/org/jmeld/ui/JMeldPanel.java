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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideTabbedPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.help.HelpSet;
import javax.help.JHelpContentViewer;
import javax.help.JHelpNavigator;
import javax.help.NavigatorView;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingworker.SwingWorker;
import org.jmeld.Version;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.settings.util.Filter;
import org.jmeld.ui.action.ActionHandler;
import org.jmeld.ui.action.Actions;
import org.jmeld.ui.action.MeldAction;
import org.jmeld.ui.bar.LineNumberBarDialog;
import org.jmeld.ui.search.SearchBarDialog;
import org.jmeld.ui.search.SearchCommand;
import org.jmeld.ui.search.SearchHits;
import org.jmeld.ui.settings.SettingsPanel;
import org.jmeld.ui.util.Icons;
import org.jmeld.ui.util.ImageUtil;
import org.jmeld.ui.util.SwingUtil;
import org.jmeld.ui.util.TabIcon;
import org.jmeld.ui.util.ToolBarBuilder;
import org.jmeld.ui.util.WidgetFactory;
import org.jmeld.util.ObjectUtil;
import org.jmeld.util.StringUtil;
import org.jmeld.util.conf.ConfigurationListenerIF;
import org.jmeld.util.file.DirectoryDiff;
import org.jmeld.util.file.VersionControlDiff;
import org.jmeld.util.node.JMDiffNode;
import org.jmeld.util.node.JMDiffNodeFactory;
import org.jmeld.vc.VersionControlUtil;

public class JMeldPanel
    extends JPanel
    implements ConfigurationListenerIF
{
  // class variables:
  // All actions:
  public final Actions actions = new Actions();

  // Options (enable/disable before adding this component to its container)
  public final Option SHOW_TOOLBAR_OPTION = new Option(true);
  public final Option SHOW_STATUSBAR_OPTION = new Option(true);
  public final Option SHOW_TABBEDPANE_OPTION = new Option(true);
  public final Option SHOW_FILE_TOOLBAR_OPTION = new Option(true);
  public final Option SHOW_FILE_STATUSBAR_OPTION = new Option(true);
  public final Option STANDALONE_INSTALLKEY_OPTION = new Option(false);

  // instance variables:
  private ActionHandler actionHandler;
  private JideTabbedPane tabbedPane;
  private JPanel barContainer;
  private AbstractBarDialog currentBarDialog;
  private SearchBarDialog searchBarDialog;
  private JComponent toolBar;
  private boolean mergeMode;
  private boolean started;

  public JMeldPanel()
  {
    setFocusable(true);

    addAncestorListener(getAncestorListener());
  }

  private void start()
  {
    if (started)
    {
      return;
    }

    started = true;

    tabbedPane = new JideTabbedPane();
    tabbedPane.setFocusable(false);
    tabbedPane.setShowCloseButtonOnTab(true);
    tabbedPane.setShowCloseButtonOnSelectedTab(true);

    if (!SHOW_TABBEDPANE_OPTION.isEnabled())
    {
      tabbedPane.setShowTabArea(false);
    }

    // Pin the tabshape because the defaults do not look good
    // on lookandfeels other than JGoodies Plastic.
    tabbedPane.setTabShape(JideTabbedPane.SHAPE_OFFICE2003);

    // Watch out: initActions uses 'tabbedPane' so this statement should be
    // after the instantiation of tabbedPane.
    initActions();

    if (SHOW_TABBEDPANE_OPTION.isEnabled())
    {
      // Watch out: actionHandler gets initialized in 'initActions' so this
      // statement should be AFTER initActions();
      tabbedPane.setCloseAction(getAction(actions.EXIT));
    }

    setLayout(new BorderLayout());
    addToolBar();
    add(tabbedPane,
        BorderLayout.CENTER);
    add(getBar(),
        BorderLayout.PAGE_END);

    tabbedPane.getModel().addChangeListener(getChangeListener());

    JMeldSettings.getInstance().addConfigurationListener(this);

    // setTransferHandler(getDragAndDropHandler());
  }

  public void openComparison(List<String> fileNameList)
  {
    String fileName1;
    String fileName2;

    if (fileNameList.size() <= 0)
    {
      return;
    }

    // Possibilities;
    // 1. <fileName>, <fileName>
    // 2. <fileName>, <fileName>, <fileName>, <fileName>
    // 3. <directory>, <directory>
    // 4. <directory>, <fileName>, <fileName> ...
    // 5. <directory (version controlled)>
    // ad 2:
    // I always assume filepairs!
    // for instance: <file1> <file2> <file3> <file4>
    // will open 2 filediffs "file1-file2" and "file3-file4".
    // ad 4:
    // The fileNames are relative and are also available in
    // the <directory>. So this enables you to do:
    // jmeld ../branches/branch1 src/lala.java src/haha.java
    // This results in 2 compares:
    // 1. ../branches/branch1/src/lala.java with ./src/lala.java
    // 2. ../branches/branch1/src/haha.java with ./src/haha.java
    if (fileNameList.size() > 1)
    {
      if (new File(fileNameList.get(0)).isDirectory())
      {
        fileName1 = fileNameList.get(0);
        for (int i = 1; i < fileNameList.size(); i++)
        {
          fileName2 = fileNameList.get(i);
          openComparison(fileName1,
                         fileName2);
        }
      }
      else
      {
        for (int i = 0; i < fileNameList.size(); i += 2)
        {
          fileName1 = fileNameList.get(i);
          if (i + 1 >= fileNameList.size())
          {
            continue;
          }

          fileName2 = fileNameList.get(i + 1);
          openComparison(fileName1,
                         fileName2);
        }
      }
    }
    else
    {
      openComparison(fileNameList.get(0),
                     null);
    }
  }

  public void openComparison(String leftName,
      String rightName)
  {
    File leftFile;
    File rightFile;
    File file;

    if (!StringUtil.isEmpty(leftName) && !StringUtil.isEmpty(rightName))
    {
      leftFile = new File(leftName);
      rightFile = new File(rightName);
      if (leftFile.isDirectory())
      {
        if (rightFile.isDirectory())
        {
          openDirectoryComparison(leftFile,
                                  rightFile,
                                  JMeldSettings.getInstance().getFilter().getFilter("default"));
        }
        else
        {
          openFileComparison(new File(leftFile,
                                      rightName),
                             rightFile,
                             false);
        }
      }
      else
      {
        openFileComparison(leftFile,
                           rightFile,
                           false);
      }
    }
    else
    {
      if (!StringUtil.isEmpty(leftName))
      {
        file = new File(leftName);
        if (file.exists() && VersionControlUtil.isVersionControlled(file))
        {
          openVersionControlComparison(file);
        }
      }
    }
  }

  public void openFileComparison(File leftFile,
      File rightFile,
      boolean openInBackground)
  {
    new NewFileComparisonPanel(leftFile,
                               rightFile,
                               openInBackground).execute();
  }

  public void openFileComparison(JMDiffNode diffNode,
      boolean openInBackground)
  {
    new NewFileComparisonPanel(diffNode,
                               openInBackground).execute();
  }

  public void openDirectoryComparison(File leftFile,
      File rightFile,
      Filter filter)
  {
    new NewDirectoryComparisonPanel(leftFile,
                                    rightFile,
                                    filter).execute();
  }

  public void openVersionControlComparison(File file)
  {
    new NewVersionControlComparisonPanel(file).execute();
  }

  public MeldAction getAction(Actions.Action action)
  {
    return getActionHandler().get(action);
  }

  public void addToolBar()
  {
    if (SHOW_TOOLBAR_OPTION.isEnabled())
    {
      if (toolBar != null)
      {
        remove(toolBar);
      }

      toolBar = getToolBar();
      add(toolBar,
          BorderLayout.PAGE_START);

      revalidate();
    }
  }

  private JComponent getToolBar()
  {
    JButton button;
    JToolBar tb;
    ToolBarBuilder builder;

    tb = new JToolBar();
    tb.setFloatable(false);
    tb.setRollover(true);

    builder = new ToolBarBuilder(tb);

    button = WidgetFactory.getToolBarButton(getAction(actions.NEW));
    builder.addButton(button);
    button = WidgetFactory.getToolBarButton(getAction(actions.SAVE));
    builder.addButton(button);

    builder.addSeparator();

    button = WidgetFactory.getToolBarButton(getAction(actions.UNDO));
    builder.addButton(button);
    button = WidgetFactory.getToolBarButton(getAction(actions.REDO));
    builder.addButton(button);

    builder.addSpring();

    button = WidgetFactory.getToolBarButton(getAction(actions.SETTINGS));
    builder.addButton(button);

    button = WidgetFactory.getToolBarButton(getAction(actions.HELP));
    builder.addButton(button);

    button = WidgetFactory.getToolBarButton(getAction(actions.ABOUT));
    builder.addButton(button);

    return tb;
  }

  private JComponent getBar()
  {
    CellConstraints cc;

    cc = new CellConstraints();

    barContainer = new JPanel(new FormLayout("0:grow",
                                             "pref, pref, pref"));
    barContainer.add(new JSeparator(),
                     cc.xy(1,
                           2));
    if (SHOW_STATUSBAR_OPTION.isEnabled())
    {
      barContainer.add(StatusBar.getInstance(),
                       cc.xy(1,
                             3));
    }

    return barContainer;
  }

  private SearchBarDialog getSearchBarDialog()
  {
    if (searchBarDialog == null)
    {
      searchBarDialog = new SearchBarDialog(this);
    }

    return searchBarDialog;
  }

  public void initActions()
  {
    MeldAction action;

    actionHandler = new ActionHandler();

    action = actionHandler.createAction(actions.NEW,
                                        this::doNew);
    action.setIcon(Icons.NEW);
    action.setToolTip("Merge 2 new files");

    action = actionHandler.createAction(actions.SAVE,
                                        this::doSave,
                                        this::isSaveEnabled);
    action.setIcon(Icons.SAVE);
    action.setToolTip("Save the changed files");
    if (!STANDALONE_INSTALLKEY_OPTION.isEnabled())
    {
      installKey("ctrl S",
                 action);
    }

    action = actionHandler.createAction(actions.UNDO,
                                        this::doUndo,
                                        this::isUndoEnabled);
    action.setIcon(Icons.UNDO);
    action.setToolTip("Undo the latest change");
    installKey("control Z",
               action);
    installKey("control Y",
               action);

    action = actionHandler.createAction(actions.REDO,
                                        this::doRedo,
                                        this::isRedoEnabled);
    action.setIcon(Icons.REDO);
    action.setToolTip("Redo the latest change");
    installKey("control R",
               action);

    action = actionHandler.createAction(actions.LEFT,
                                        this::doLeft);
    installKey("LEFT",
               action);
    installKey("alt LEFT",
               action);
    installKey("alt KP_LEFT",
               action);

    action = actionHandler.createAction(actions.RIGHT,
                                        this::doRight);
    installKey("RIGHT",
               action);
    installKey("alt RIGHT",
               action);
    installKey("alt KP_RIGHT",
               action);

    action = actionHandler.createAction(actions.UP,
                                        this::doUp);
    installKey("UP",
               action);
    installKey("alt UP",
               action);
    installKey("alt KP_UP",
               action);
    installKey("F7",
               action);

    action = actionHandler.createAction(actions.DOWN,
                                        this::doDown);
    installKey("DOWN",
               action);
    installKey("alt DOWN",
               action);
    installKey("alt KP_DOWN",
               action);
    installKey("F8",
               action);

    action = actionHandler.createAction(actions.ZOOM_PLUS,
                                        this::doZoomPlus);
    installKey("alt EQUALS",
               action);
    installKey("shift alt EQUALS",
               action);
    installKey("alt ADD",
               action);

    action = actionHandler.createAction(actions.ZOOM_MIN,
                                        this::doZoomMin);
    installKey("alt MINUS",
               action);
    installKey("shift alt MINUS",
               action);
    installKey("alt SUBTRACT",
               action);

    action = actionHandler.createAction(actions.GOTO_SELECTED,
                                        this::doGoToSelected);
    installKey("alt ENTER",
               action);

    action = actionHandler.createAction(actions.GOTO_FIRST,
                                        this::doGoToLast);
    installKey("alt HOME",
               action);

    action = actionHandler.createAction(actions.GOTO_LAST,
                                        this::doGoToLast);
    installKey("alt END",
               action);

    action = actionHandler.createAction(actions.GOTO_LINE,
                                        this::doGoToLine);
    installKey("ctrl L",
               action);

    action = actionHandler.createAction(actions.START_SEARCH,
                                        this::doStartSearch);
    installKey("ctrl F",
               action);

    action = actionHandler.createAction(actions.NEXT_SEARCH,
                                        this::doNextSearch);
    installKey("F3",
               action);
    installKey("ctrl G",
               action);

    action = actionHandler.createAction(actions.PREVIOUS_SEARCH,
                                        this::doPreviousSearch);
    installKey("shift F3",
               action);

    action = actionHandler.createAction(actions.REFRESH,
                                        this::doRefresh);
    installKey("F5",
               action);

    action = actionHandler.createAction(actions.MERGEMODE,
                                        this::doMergeMode);
    installKey("F9",
               action);

    if (!STANDALONE_INSTALLKEY_OPTION.isEnabled())
    {
      action = actionHandler.createAction(actions.HELP,
                                          this::doHelp);
      action.setIcon(Icons.HELP);
      installKey("F1",
                 action);

      action = actionHandler.createAction(actions.ABOUT,
                                          this::doAbout);
      action.setIcon(Icons.ABOUT);

      action = actionHandler.createAction(actions.SETTINGS,
                                          this::doSettings);
      action.setIcon(Icons.SETTINGS);
      action.setToolTip("Settings");

      action = actionHandler.createAction(actions.EXIT,
                                          this::doExit);
      installKey("ESCAPE",
                 action);
    }
  }

  public ActionHandler getActionHandler()
  {
    return actionHandler;
  }

  public void checkActions()
  {
    if (actionHandler != null)
    {
      actionHandler.checkActions();
    }
  }

  public void doNew(ActionEvent ae)
  {
    NewPanelDialog dialog;

    dialog = new NewPanelDialog(this);
    dialog.show();

    if (dialog.getFunction() == NewPanelDialog.Function.FILE_COMPARISON)
    {
      openFileComparison(new File(dialog.getLeftFileName()),
                         new File(dialog.getRightFileName()),
                         false);
    }
    else if (dialog.getFunction() == NewPanelDialog.Function.DIRECTORY_COMPARISON)
    {
      openDirectoryComparison(new File(dialog.getLeftDirectoryName()),
                              new File(dialog.getRightDirectoryName()),
                              dialog.getFilter());
    }
    else if (dialog.getFunction() == NewPanelDialog.Function.VERSION_CONTROL)
    {
      openVersionControlComparison(new File(dialog.getVersionControlDirectoryName()));
    }
  }

  public void doSave(ActionEvent ae)
  {
    getCurrentContentPanel().doSave();
  }

  public boolean isSaveEnabled()
  {
    JMeldContentPanelIF panel;

    panel = getCurrentContentPanel();
    if (panel == null)
    {
      return false;
    }

    return panel.isSaveEnabled();
  }

  public void doUndo(ActionEvent ae)
  {
    getCurrentContentPanel().doUndo();
  }

  public boolean isUndoEnabled()
  {
    JMeldContentPanelIF panel;

    panel = getCurrentContentPanel();
    if (panel == null)
    {
      return false;
    }

    return panel.isUndoEnabled();
  }

  public void doRedo(ActionEvent ae)
  {
    getCurrentContentPanel().doRedo();
  }

  public boolean isRedoEnabled()
  {
    JMeldContentPanelIF panel;

    panel = getCurrentContentPanel();
    if (panel == null)
    {
      return false;
    }

    return panel.isRedoEnabled();
  }

  public void doLeft(ActionEvent ae)
  {
    getCurrentContentPanel().doLeft();
    repaint();
  }

  public void doRight(ActionEvent ae)
  {
    getCurrentContentPanel().doRight();
    repaint();
  }

  public void doUp(ActionEvent ae)
  {
    getCurrentContentPanel().doUp();
    repaint();
  }

  public void doDown(ActionEvent ae)
  {
    getCurrentContentPanel().doDown();
    repaint();
  }

  public void doZoomPlus(ActionEvent ae)
  {
    getCurrentContentPanel().doZoom(true);
    repaint();
  }

  public void doZoomMin(ActionEvent ae)
  {
    getCurrentContentPanel().doZoom(false);
    repaint();
  }

  public void doGoToSelected(ActionEvent ae)
  {
    getCurrentContentPanel().doGoToSelected();
    repaint();
  }

  public void doGoToFirst(ActionEvent ae)
  {
    getCurrentContentPanel().doGoToFirst();
    repaint();
  }

  public void doGoToLast(ActionEvent ae)
  {
    getCurrentContentPanel().doGoToLast();
    repaint();
  }

  public void doGoToLine(ActionEvent ae)
  {
    activateBarDialog(new LineNumberBarDialog(this));
  }

  public void doGoToLine(int line)
  {
    getCurrentContentPanel().doGoToLine(line);
    repaint();
    deactivateBarDialog();
  }

  public void doStartSearch(ActionEvent ae)
  {
    SearchBarDialog sbd;

    sbd = getSearchBarDialog();
    sbd.setSearchText(getSelectedSearchText());

    activateBarDialog(sbd);
  }

  public void doStopSearch(ActionEvent ae)
  {
    deactivateBarDialog();

    for (JMeldContentPanelIF cp : getContentPanelList())
    {
      cp.doStopSearch();
    }
  }

  public SearchHits doSearch(ActionEvent ae)
  {
    return getCurrentContentPanel().doSearch();
  }

  SearchCommand getSearchCommand()
  {
    if (currentBarDialog != getSearchBarDialog())
    {
      return null;
    }

    return getSearchBarDialog().getCommand();
  }

  public void doNextSearch(ActionEvent ae)
  {
    if (currentBarDialog != getSearchBarDialog())
    {
      return;
    }

    getCurrentContentPanel().doNextSearch();
  }

  public void doPreviousSearch(ActionEvent ae)
  {
    if (currentBarDialog != getSearchBarDialog())
    {
      return;
    }

    getCurrentContentPanel().doPreviousSearch();
  }

  private String getSelectedSearchText()
  {
    return getCurrentContentPanel().getSelectedText();
  }

  public void doRefresh(ActionEvent ae)
  {
    getCurrentContentPanel().doRefresh();
  }

  public void doMergeMode(ActionEvent ae)
  {
    MeldAction action;

    mergeMode = !mergeMode;

    action = getAction(actions.LEFT);
    installKey(mergeMode,
               "LEFT",
               action);

    action = getAction(actions.RIGHT);
    installKey(mergeMode,
               "RIGHT",
               action);

    action = getAction(actions.UP);
    installKey(mergeMode,
               "UP",
               action);

    action = getAction(actions.DOWN);
    installKey(mergeMode,
               "DOWN",
               action);

    getCurrentContentPanel().doMergeMode(mergeMode);
    requestFocus();

    if (mergeMode)
    {
      StatusBar.getInstance().setNotification(actions.MERGEMODE.getName(),
                                              ImageUtil.getSmallIcon("jmeld_mergemode-on"));
    }
    else
    {
      StatusBar.getInstance().removeNotification(actions.MERGEMODE.getName());
    }
  }

  public void doHelp(ActionEvent ae)
  {
    try
    {
      AbstractContentPanel content;
      URL url;
      HelpSet helpSet;
      JHelpContentViewer viewer;
      JHelpNavigator navigator;
      NavigatorView navigatorView;
      JSplitPane splitPane;
      String contentId;

      contentId = "HelpPanel";
      if (checkAlreadyOpen(contentId))
      {
        return;
      }

      url = HelpSet.findHelpSet(getClass().getClassLoader(),
                                "help/jmeld/jmeld.hs");
      helpSet = new HelpSet(getClass().getClassLoader(),
                            url);
      viewer = new JHelpContentViewer(helpSet);
      navigatorView = helpSet.getNavigatorView("TOC");
      navigator = (JHelpNavigator) navigatorView.createNavigator(viewer.getModel());
      splitPane = new JSplitPane();
      splitPane.setLeftComponent(navigator);
      splitPane.setRightComponent(viewer);
      content = new AbstractContentPanel();
      content.setId(contentId);
      content.setLayout(new BorderLayout());
      content.add(splitPane,
                  BorderLayout.CENTER);

      /*
       * content = new HelpPanel(this);
       */
      tabbedPane.addTab("Help",
                        Icons.HELP.getSmallIcon(),
                        content);
      tabbedPane.setSelectedComponent(content);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void doAbout(ActionEvent ae)
  {
    AbstractContentPanel content;
    String contentId;

    contentId = "AboutPanel";
    if (checkAlreadyOpen(contentId))
    {
      return;
    }

    content = new AbstractContentPanel();
    content.setId(contentId);
    content.setLayout(new BorderLayout());
    content.add(new JButton("JMeld version: " + Version.getVersion()),
                BorderLayout.CENTER);

    tabbedPane.addTab("About",
                      Icons.ABOUT.getSmallIcon(),
                      content);
    tabbedPane.setSelectedComponent(content);
  }

  public void doExit(ActionEvent ae)
  {
    JMeldContentPanelIF cp;

    // Stop the searchBarDialog if it is showing.
    if (currentBarDialog == getSearchBarDialog())
    {
      doStopSearch(ae);
      return;
    }

    if (currentBarDialog != null)
    {
      deactivateBarDialog();
      return;
    }

    cp = getCurrentContentPanel();
    if (cp == null)
    {
      return;
    }

    // Detect if this close is due to pressing ESC.
    if (ae.getSource() == this)
    {
      if (!cp.checkExit())
      {
        return;
      }
    }

    // Exit a tab!
    doExitTab((Component) getCurrentContentPanel());
  }

  public void doSettings(ActionEvent ae)
  {
    AbstractContentPanel content;
    String contentId;

    contentId = "SettingsPanel";
    if (checkAlreadyOpen(contentId))
    {
      return;
    }

    content = new SettingsPanel(this);
    content.setId(contentId);
    tabbedPane.addTab("Settings",
                      Icons.SETTINGS.getSmallIcon(),
                      content);
    tabbedPane.setSelectedComponent(content);
  }

  private boolean checkAlreadyOpen(String contentId)
  {
    AbstractContentPanel contentPanel;

    contentPanel = getAlreadyOpen(contentId);
    if (contentPanel != null)
    {
      tabbedPane.setSelectedComponent(contentPanel);
      return true;
    }

    return false;
  }

  private AbstractContentPanel getAlreadyOpen(String contentId)
  {
    for (AbstractContentPanel contentPanel : getContentPanelList())
    {
      if (ObjectUtil.equals(contentPanel.getId(),
                            contentId))
      {
        System.out.println("already open: " + contentId);
        return contentPanel;
      }
    }

    return null;
  }

  private ChangeListener getChangeListener()
  {
    return new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        checkActions();
      }
    };
  }

  public WindowListener getWindowListener()
  {
    return new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent we)
      {
        for (JMeldContentPanelIF contentPanel : getContentPanelList())
        {
          if (!contentPanel.checkSave())
          {
            return;
          }
        }

        System.exit(1);
      }
    };
  }

  private AbstractContentPanel getCurrentContentPanel()
  {
    return (AbstractContentPanel) tabbedPane.getSelectedComponent();
  }

  private List<AbstractContentPanel> getContentPanelList()
  {
    List<AbstractContentPanel> result;

    result = new ArrayList<AbstractContentPanel>();

    if (tabbedPane != null)
    {
      for (int i = 0; i < tabbedPane.getTabCount(); i++)
      {
        result.add((AbstractContentPanel) tabbedPane.getComponentAt(i));
      }
    }

    return result;
  }

  class NewFileComparisonPanel
      extends SwingWorker<String, Object>
  {
    private JMDiffNode diffNode;
    private File leftFile;
    private File rightFile;
    private boolean openInBackground;
    private BufferDiffPanel panel;
    private AbstractContentPanel contentPanel;
    private String contentId;

    NewFileComparisonPanel(JMDiffNode diffNode,
        boolean openInBackground)
    {
      this.diffNode = diffNode;
      this.openInBackground = openInBackground;
    }

    NewFileComparisonPanel(File leftFile,
        File rightFile,
        boolean openInBackground)
    {
      this.leftFile = leftFile;
      this.rightFile = rightFile;
      this.openInBackground = openInBackground;
    }

    @Override
    public String doInBackground()
    {
      try
      {
        if (diffNode == null)
        {
          if (StringUtil.isEmpty(leftFile.getName()))
          {
            return "left filename is empty";
          }

          if (!leftFile.exists())
          {
            return "left filename(" + leftFile.getAbsolutePath() + ") doesn't exist";
          }

          if (StringUtil.isEmpty(rightFile.getName()))
          {
            return "right filename is empty";
          }

          if (!rightFile.exists())
          {
            return "right filename(" + rightFile.getAbsolutePath() + ") doesn't exist";
          }

          diffNode = JMDiffNodeFactory.create(leftFile.getName(),
                                              leftFile,
                                              rightFile.getName(),
                                              rightFile);
        }

        contentId = "BufferDiffPanel:" + diffNode.getId();
        contentPanel = getAlreadyOpen(contentId);
        if (contentPanel == null)
        {
          diffNode.diff();
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();

        return ex.getMessage();
      }

      return null;
    }

    @Override
    protected void done()
    {
      try
      {
        String result;

        result = get();

        if (result != null)
        {
          JOptionPane.showMessageDialog(JMeldPanel.this,
                                        result,
                                        "Error opening file",
                                        JOptionPane.ERROR_MESSAGE);
        }
        else
        {
          if (contentPanel != null)
          {
            // Already opened!
            tabbedPane.setSelectedComponent(contentPanel);
          }
          else
          {
            panel = new BufferDiffPanel(JMeldPanel.this);
            panel.setId(contentId);
            panel.setDiffNode(diffNode);
            tabbedPane.addTab(panel.getTitle(),
                              Icons.NEW.getSmallIcon(),
                              panel);
            if (!openInBackground)
            {
              tabbedPane.setSelectedComponent(panel);
            }

            panel.doGoToFirst();
            panel.repaint();

            // Goto the first delta:
            // This should be invoked after the panel is displayed!
            SwingUtilities.invokeLater(getDoGoToFirst());
          }
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }

    private Runnable getDoGoToFirst()
    {
      return new Runnable()
      {
        @Override
        public void run()
        {
          panel.doGoToFirst();
          panel.repaint();
        }
      };
    }
  }

  class NewDirectoryComparisonPanel
      extends SwingWorker<String, Object>
  {
    private File leftFile;
    private File rightFile;
    private Filter filter;
    private DirectoryDiff diff;
    private AbstractContentPanel contentPanel;
    private String contentId;

    NewDirectoryComparisonPanel(File leftFile,
        File rightFile,
        Filter filter)
    {
      this.leftFile = leftFile;
      this.rightFile = rightFile;
      this.filter = filter;
    }

    @Override
    public String doInBackground()
    {
      if (StringUtil.isEmpty(leftFile.getName()))
      {
        return "left directoryName is empty";
      }

      if (!leftFile.exists())
      {
        return "left directoryName(" + leftFile.getAbsolutePath() + ") doesn't exist";
      }

      if (!leftFile.isDirectory())
      {
        return "left directoryName(" + leftFile.getName() + ") is not a directory";
      }

      if (StringUtil.isEmpty(rightFile.getName()))
      {
        return "right directoryName is empty";
      }

      if (!rightFile.exists())
      {
        return "right directoryName(" + rightFile.getAbsolutePath() + ") doesn't exist";
      }

      if (!rightFile.isDirectory())
      {
        return "right directoryName(" + rightFile.getName() + ") is not a directory";
      }

      contentId = "FolderDiffPanel:" + leftFile.getName() + "-" + rightFile.getName();
      contentPanel = getAlreadyOpen(contentId);
      if (contentPanel == null)
      {
        diff = new DirectoryDiff(leftFile,
                                 rightFile,
                                 filter,
                                 DirectoryDiff.Mode.TWO_WAY);
        diff.diff();
      }

      return null;
    }

    @Override
    protected void done()
    {
      try
      {
        String result;
        FolderDiffPanel panel;

        result = get();

        if (result != null)
        {
          JOptionPane.showMessageDialog(JMeldPanel.this,
                                        result,
                                        "Error opening file",
                                        JOptionPane.ERROR_MESSAGE);
        }
        else
        {
          if (contentPanel != null)
          {
            // Already opened!
            tabbedPane.setSelectedComponent(contentPanel);
          }
          else
          {
            panel = new FolderDiffPanel(JMeldPanel.this,
                                        diff);
            panel.setId(contentId);

            tabbedPane.addTab(panel.getTitle(),
                              Icons.FOLDER.getSmallIcon(),
                              panel);
            tabbedPane.setSelectedComponent(panel);
          }
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }

  class NewVersionControlComparisonPanel
      extends SwingWorker<String, Object>
  {
    private File file;
    private VersionControlDiff diff;
    private AbstractContentPanel contentPanel;
    private String contentId;

    NewVersionControlComparisonPanel(File file)
    {
      this.file = file;
    }

    @Override
    public String doInBackground()
    {
      if (StringUtil.isEmpty(file.getName()))
      {
        return "file is empty";
      }

      if (!file.exists())
      {
        return "file(" + file.getAbsolutePath() + ") doesn't exist";
      }

      /*
       * if (!directory.isDirectory()) { return "directoryName(" + directory.getName()
       * + ") is not a directory"; }
       */

      contentId = "VersionControlDiffPanel:" + file.getName();
      contentPanel = getAlreadyOpen(contentId);
      if (contentPanel == null)
      {
        diff = new VersionControlDiff(file,
                                      DirectoryDiff.Mode.TWO_WAY);
        diff.diff();
      }

      return null;
    }

    @Override
    protected void done()
    {
      try
      {
        String result;
        VersionControlPanel panel;

        result = get();

        if (result != null)
        {
          JOptionPane.showMessageDialog(JMeldPanel.this,
                                        result,
                                        "Error opening file",
                                        JOptionPane.ERROR_MESSAGE);
        }
        else
        {
          if (contentPanel != null)
          {
            // Already opened!
            tabbedPane.setSelectedComponent(contentPanel);
          }
          else
          {
            // panel = new FolderDiffPanel(JMeldPanel.this, diff);
            panel = new VersionControlPanel(JMeldPanel.this,
                                            diff);
            panel.setId(contentId);

            tabbedPane.addTab("TODO: Think of title!",
                              Icons.FOLDER.getSmallIcon(),
                              panel);
            tabbedPane.setSelectedComponent(panel);
          }
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }

  private void installKey(boolean enabled,
      String key,
      MeldAction action)
  {
    if (!enabled)
    {
      deInstallKey(key,
                   action);
    }
    else
    {
      installKey(key,
                 action);
    }
  }

  private void installKey(String key,
      MeldAction action)
  {
    SwingUtil.installKey(this,
                         key,
                         action);
  }

  private void deInstallKey(String key,
      MeldAction action)
  {
    SwingUtil.deInstallKey(this,
                           key,
                           action);
  }

  /*
   * private TabIcon getTabIcon( String iconName, String text) { TabIcon icon;
   * icon = new TabIcon( ImageUtil.getSmallImageIcon(iconName), text);
   * icon.addExitListener(getTabExitListener()); return icon; } private
   * TabExitListenerIF getTabExitListener() { return new TabExitListenerIF() {
   * public boolean doExit(TabExitEvent te) { int tabIndex; tabIndex =
   * te.getTabIndex(); if (tabIndex == -1) { return false; } return
   * doExitTab(tabbedPane.getComponentAt(tabIndex)); } }; }
   */
  private boolean doExitTab(Component component)
  {
    AbstractContentPanel content;
    Icon icon;
    int index;

    if (component == null)
    {
      return false;
    }

    index = tabbedPane.indexOfComponent(component);
    if (index == -1)
    {
      return false;
    }

    if (component instanceof AbstractContentPanel)
    {
      content = (AbstractContentPanel) component;
      if (!content.checkSave())
      {
        return false;
      }
    }

    icon = tabbedPane.getIconAt(index);
    if (icon != null && icon instanceof TabIcon)
    {
      ((TabIcon) icon).exit();
    }

    tabbedPane.remove(component);

    return true;
  }

  public void activateBarDialog(AbstractBarDialog bar)
  {
    CellConstraints cc;

    deactivateBarDialog();

    cc = new CellConstraints();
    barContainer.add(bar,
                     cc.xy(1,
                           1));
    bar.activate();
    currentBarDialog = bar;
    barContainer.revalidate();
  }

  public void deactivateBarDialog()
  {
    if (currentBarDialog != null)
    {
      barContainer.remove(currentBarDialog);
      barContainer.revalidate();
      currentBarDialog.deactivate();
      currentBarDialog = null;
    }
  }

  @Override
  public void configurationChanged()
  {
    checkActions();
  }

  /*
   * == JDK6! private TransferHandler getDragAndDropHandler() { return new
   * TransferHandler() { public boolean canImport(TransferHandler.TransferSupport
   * support) { System.out.println(support); return false; } public boolean
   * importData(TransferHandler.TransferSupport support) { return true; } }; }
   */

  public class Option
  {
    private boolean enabled;

    Option(boolean enabled)
    {
      this.enabled = enabled;
    }

    public boolean isEnabled()
    {
      return enabled;
    }

    public boolean isDisabled()
    {
      return !isEnabled();
    }

    public void enable()
    {
      if (started)
      {
        System.out.println("Cannot change an option after start!");
        return;
      }

      this.enabled = true;
    }

    public void disable()
    {
      if (started)
      {
        System.out.println("Cannot change an option after start!");
        return;
      }

      this.enabled = false;
    }
  }

  private AncestorListener getAncestorListener()
  {
    return new AncestorListener()
    {
      @Override
      public void ancestorAdded(AncestorEvent event)
      {
        start();
      }

      @Override
      public void ancestorMoved(AncestorEvent event)
      {
      }

      @Override
      public void ancestorRemoved(AncestorEvent event)
      {
      }
    };
  }
}
