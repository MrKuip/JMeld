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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.undo.CompoundEdit;
import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.jmeld.settings.EditorSettings;
import org.jmeld.settings.FolderSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.action.ActionHandler;
import org.jmeld.ui.action.MeldAction;
import org.jmeld.ui.swing.table.JMTreeTableModel;
import org.jmeld.ui.util.Colors;
import org.jmeld.ui.util.Icons;
import org.jmeld.ui.util.ImageUtil;
import org.jmeld.ui.util.SwingUtil;
import org.jmeld.util.conf.ConfigurationListenerIF;
import org.jmeld.util.file.FolderDiff;
import org.jmeld.util.file.cmd.AbstractCmd;
import org.jmeld.util.node.JMDiffNode;

public class FolderDiffPanel
    extends FolderDiffForm
    implements ConfigurationListenerIF
{
  private JMeldPanel mainPanel;
  private FolderDiff diff;
  private ActionHandler actionHandler;
  private JMTreeTableModel treeTableModel;

  FolderDiffPanel(JMeldPanel mainPanel,
      FolderDiff diff)
  {
    this.mainPanel = mainPanel;
    this.diff = diff;

    init();
  }

  private void init()
  {
    actionHandler = new ActionHandler();

    hierarchyComboBox.setModel(new DefaultComboBoxModel(FolderSettings.FolderView.values()));
    hierarchyComboBox.setSelectedItem(getFolderSettings().getView());
    hierarchyComboBox.setFocusable(false);

    initActions();

    onlyRightButton.setText(null);
    onlyRightButton.setIcon(Icons.ONLY_RIGHT);
    onlyRightButton.setFocusable(false);
    onlyRightButton.setSelected(getFolderSettings().getOnlyRight());

    leftRightChangedButton.setText(null);
    leftRightChangedButton.setIcon(Icons.LEFT_RIGHT_CHANGED);
    leftRightChangedButton.setFocusable(false);
    leftRightChangedButton.setSelected(getFolderSettings().getLeftRightChanged());

    onlyLeftButton.setText(null);
    onlyLeftButton.setIcon(Icons.ONLY_LEFT);
    onlyLeftButton.setFocusable(false);
    onlyLeftButton.setSelected(getFolderSettings().getOnlyLeft());

    leftRightUnChangedButton.setText(null);
    leftRightUnChangedButton.setIcon(Icons.LEFT_RIGHT_UNCHANGED);
    leftRightUnChangedButton.setFocusable(false);
    leftRightUnChangedButton.setSelected(getFolderSettings().getLeftRightUnChanged());

    expandAllButton.setBorder(BorderFactory.createEmptyBorder(2,
                                                              2,
                                                              2,
                                                              2));
    expandAllButton.setContentAreaFilled(false);
    expandAllButton.setText(null);
    expandAllButton.setIcon(Icons.EXPAND_ALL.getSmallIcon());
    expandAllButton.setPressedIcon(ImageUtil.createDarkerIcon(expandAllButton.getIcon()));
    expandAllButton.setFocusable(false);

    collapseAllButton.setBorder(BorderFactory.createEmptyBorder(2,
                                                                2,
                                                                2,
                                                                2));
    collapseAllButton.setContentAreaFilled(false);
    collapseAllButton.setText(null);
    collapseAllButton.setIcon(Icons.COLLAPSE_ALL.getSmallIcon());
    collapseAllButton.setPressedIcon(ImageUtil.createDarkerIcon(collapseAllButton.getIcon()));
    collapseAllButton.setFocusable(false);

    folder1Label.init();
    folder1Label.setText(diff.getLeftFolderName(),
                         diff.getRightFolderName());

    folder2Label.init();
    folder2Label.setText(diff.getRightFolderName(),
                         diff.getLeftFolderName());

    folderTreeTable.setTreeTableModel(getTreeTableModel());

    folderTreeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    folderTreeTable.setToggleClickCount(1);
    folderTreeTable.setTerminateEditOnFocusLost(false);
    folderTreeTable.setRowSelectionAllowed(true);
    folderTreeTable.addMouseListener(getMouseListener());
    folderTreeTable.expandAll();

    folderTreeTable.addHighlighter(new ColorHighlighter(HighlightPredicate.EVEN,
                                                        Color.white,
                                                        Color.black));
    folderTreeTable.addHighlighter(new ColorHighlighter(HighlightPredicate.ODD,
                                                        Colors.getTableRowHighLighterColor(),
                                                        Color.black));
    // folderTreeTable.setHighlighters(new AlternateRowHighlighter(Color.white,
    // Colors.getTableRowHighLighterColor(), Color.black));

    JMeldSettings.getInstance().addConfigurationListener(this);
  }

  private void initActions()
  {
    MeldAction action;

    action = actionHandler.createAction(mainPanel.actions.FOLDER_SELECT_NEXT_ROW,
                                        (ae) -> doSelectNextRow(ae));
    installKey("DOWN",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_SELECT_PREVIOUS_ROW,
                                        (ae) -> doSelectPreviousRow(ae));
    installKey("UP",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_NEXT_NODE,
                                        (ae) -> doNextNode(ae));
    installKey("RIGHT",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_PREVIOUS_NODE,
                                        (ae) -> doPreviousNode(ae));
    installKey("LEFT",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_OPEN_FILE_COMPARISON,
                                        (ae) -> doOpenFileComparison(ae));
    action.setIcon(Icons.COMPARE);
    compareButton.setAction(action);
    compareButton.setText(null);
    compareButton.setFocusable(false);
    compareButton.setIcon(action.getSmallIcon());
    compareButton.setDisabledIcon(action.getTransparentSmallIcon());
    installKey("ENTER",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_OPEN_FILE_COMPARISON_BACKGROUND,
                                        (ae) -> doOpenFileComparisonBackground(ae));
    action.setIcon(Icons.COMPARE);
    installKey("alt ENTER",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_EXPAND_ALL,
                                        (ae) -> doExpandAll(ae));
    expandAllButton.setAction(action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_COLLAPSE_ALL,
                                        (ae) -> doCollapseAll(ae));
    collapseAllButton.setAction(action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_REFRESH,
                                        (ae) -> doRefresh(ae));
    action.setIcon(Icons.REFRESH);
    refreshButton.setAction(action);
    refreshButton.setText(null);
    refreshButton.setFocusable(false);
    refreshButton.setIcon(action.getSmallIcon());
    refreshButton.setDisabledIcon(action.getTransparentSmallIcon());

    action = actionHandler.createAction(mainPanel.actions.FOLDER_REMOVE_RIGHT,
                                        (ae) -> doRemoveRight(ae));
    action.setIcon(Icons.DELETE);
    deleteRightButton.setAction(action);
    deleteRightButton.setText(null);
    deleteRightButton.setFocusable(false);
    deleteRightButton.setIcon(action.getSmallIcon());
    deleteRightButton.setDisabledIcon(action.getTransparentSmallIcon());
    installKey("ctrl alt RIGHT",
               action);
    installKey("ctrl alt KP_RIGHT",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_REMOVE_LEFT,
                                        (ae) -> doRemoveLeft(ae));
    action.setIcon(Icons.DELETE);
    deleteLeftButton.setAction(action);
    deleteLeftButton.setText(null);
    deleteLeftButton.setFocusable(false);
    deleteLeftButton.setIcon(action.getSmallIcon());
    deleteLeftButton.setDisabledIcon(action.getTransparentSmallIcon());
    installKey("ctrl alt LEFT",
               action);
    installKey("ctrl alt KP_LEFT",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_COPY_TO_LEFT,
                                        (ae) -> doCopyToLeft(ae));
    action.setIcon(Icons.LEFT);
    copyToLeftButton.setAction(action);
    copyToLeftButton.setText(null);
    copyToLeftButton.setFocusable(false);
    copyToLeftButton.setIcon(action.getSmallIcon());
    copyToLeftButton.setDisabledIcon(action.getTransparentSmallIcon());
    installKey("alt LEFT",
               action);
    installKey("alt KP_LEFT",
               action);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_COPY_TO_RIGHT,
                                        (ae) -> doCopyToRight(ae));
    action.setIcon(Icons.RIGHT);
    copyToRightButton.setAction(action);
    copyToRightButton.setText(null);
    copyToRightButton.setFocusable(false);
    copyToRightButton.setIcon(action.getSmallIcon());
    copyToRightButton.setDisabledIcon(action.getTransparentSmallIcon());
    installKey("alt RIGHT",
               action);
    installKey("alt KP_RIGHT",
               action);

    // deleteRightButton.setVisible(false);
    // copyToRightButton.setVisible(false);
    // copyToLeftButton.setVisible(false);
    // deleteLeftButton.setVisible(false);

    action = actionHandler.createAction(mainPanel.actions.FOLDER_FILTER,
                                        (ae) -> doFilter(ae));
    onlyRightButton.setAction(action);
    leftRightChangedButton.setAction(action);
    onlyLeftButton.setAction(action);
    leftRightUnChangedButton.setAction(action);
    hierarchyComboBox.setAction(action);
  }

  private void installKey(String key,
      MeldAction action)
  {
    SwingUtil.installKey(folderTreeTable,
                         key,
                         action);
  }

  public String getTitle()
  {
    return diff.getLeftFolderShortName() + " - " + diff.getRightFolderShortName();
  }

  private TreeTableNode getRootNode()
  {
    return filter(diff.getRootNode());
  }

  private TreeTableNode filter(JMDiffNode diffNode)
  {
    List<JMDiffNode> nodes;
    UINode uiParentNode;
    UINode uiNode;
    UINode rootNode;
    JMDiffNode parent;
    Object hierarchy;

    // Filter the nodes:
    nodes = new ArrayList<>();
    for (JMDiffNode node : diff.getNodes())
    {
      if (!node.isLeaf())
      {
        continue;
      }

      if (node.isCompareEqual(JMDiffNode.Compare.Equal))
      {
        if (leftRightUnChangedButton.isSelected())
        {
          nodes.add(node);
        }
      }
      else if (node.isCompareEqual(JMDiffNode.Compare.NotEqual))
      {
        if (leftRightChangedButton.isSelected())
        {
          nodes.add(node);
        }
      }
      else if (node.isCompareEqual(JMDiffNode.Compare.RightMissing))
      {
        if (onlyLeftButton.isSelected())
        {
          nodes.add(node);
        }
      }
      else if (node.isCompareEqual(JMDiffNode.Compare.LeftMissing))
      {
        if (onlyRightButton.isSelected())
        {
          nodes.add(node);
        }
      }
    }

    rootNode = new UINode(getTreeTableModel(),
                          "<root>",
                          false);
    hierarchy = hierarchyComboBox.getSelectedItem();

    // Build the hierarchy:
    if (hierarchy == FolderSettings.FolderView.packageView)
    {
      for (JMDiffNode node : nodes)
      {
        parent = node.getParent();
        uiNode = new UINode(getTreeTableModel(),
                            node);

        if (parent != null)
        {
          uiParentNode = new UINode(getTreeTableModel(),
                                    parent);
          uiParentNode = rootNode.addChild(uiParentNode);
          uiParentNode.addChild(uiNode);
        }
        else
        {
          rootNode.addChild(uiNode);
        }
      }
    }
    else if (hierarchy == FolderSettings.FolderView.fileView)
    {
      for (JMDiffNode node : nodes)
      {
        rootNode.addChild(new UINode(getTreeTableModel(),
                                     node));
      }
    }
    else if (hierarchy == FolderSettings.FolderView.directoryView)
    {
      for (JMDiffNode node : nodes)
      {
        addDirectoryViewNode(rootNode,
                             node);
      }
    }

    return rootNode;
  }

  private void addDirectoryViewNode(UINode rootNode,
      JMDiffNode node)
  {
    UINode parent;
    JMDiffNode uiNode;
    List<JMDiffNode> uiNodes;

    uiNodes = new ArrayList<JMDiffNode>();
    do
    {
      uiNodes.add(node);
    }
    while ((node = node.getParent()) != null);

    Collections.reverse(uiNodes);

    parent = rootNode;
    for (int i = 1; i < uiNodes.size(); i++)
    {
      uiNode = uiNodes.get(i);
      parent = parent.addChild(new UINode(getTreeTableModel(),
                                          uiNode));
    }
  }

  public void doSelectPreviousRow(ActionEvent ae)
  {
    int row;

    row = folderTreeTable.getSelectedRow() - 1;
    row = row < 0 ? (folderTreeTable.getRowCount() - 1) : row;
    folderTreeTable.setRowSelectionInterval(row,
                                            row);
    folderTreeTable.scrollRowToVisible(row);
  }

  public void doSelectNextRow(ActionEvent ae)
  {
    int row;

    row = folderTreeTable.getSelectedRow() + 1;
    row = row >= folderTreeTable.getRowCount() ? 0 : row;
    folderTreeTable.setRowSelectionInterval(row,
                                            row);
    folderTreeTable.scrollRowToVisible(row);
  }

  public void doNextNode(ActionEvent ae)
  {
    int row;

    row = folderTreeTable.getSelectedRow();
    if (row == -1)
    {
      return;
    }

    if (folderTreeTable.isCollapsed(row))
    {
      folderTreeTable.expandRow(row);
    }

    doSelectNextRow(ae);
  }

  public void doPreviousNode(ActionEvent ae)
  {
    int row;

    row = folderTreeTable.getSelectedRow();
    if (row == -1)
    {
      return;
    }

    if (folderTreeTable.isExpanded(row))
    {
      folderTreeTable.collapseRow(row);
    }

    doSelectPreviousRow(ae);
  }

  public void doOpenFileComparisonBackground(ActionEvent ae)
  {
    doOpenFileComparison(ae,
                         true);
  }

  public void doOpenFileComparison(ActionEvent ae)
  {
    doOpenFileComparison(ae,
                         false);
  }

  private void doOpenFileComparison(ActionEvent ae,
      boolean background)
  {
    for (UINode uiNode : getSelectedUINodes())
    {
      mainPanel.openFileComparison(uiNode.getDiffNode(),
                                   background);
    }
  }

  @Override
  public boolean checkExit()
  {
    return false;
  }

  public void doExpandAll(ActionEvent ae)
  {
    folderTreeTable.expandAll();
  }

  public void doCollapseAll(ActionEvent ae)
  {
    folderTreeTable.collapseAll();
  }

  public boolean isCopyToLeftEnabled()
  {
    return !getEditorSettings().getLeftsideReadonly();
  }

  public void doCopyToLeft(ActionEvent ae)
  {
    CompoundCommand cc;

    cc = new CompoundCommand();
    for (UINode uiNode : getSelectedUINodes())
    {
      try
      {
        cc.add(uiNode,
               uiNode.getDiffNode().getCopyToLeftCmd());
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    cc.execute();
  }

  public boolean isCopyToRightEnabled()
  {
    return !getEditorSettings().getRightsideReadonly();
  }

  public void doCopyToRight(ActionEvent ae)
  {
    CompoundCommand cc;

    cc = new CompoundCommand();
    for (UINode uiNode : getSelectedUINodes())
    {
      try
      {
        cc.add(uiNode,
               uiNode.getDiffNode().getCopyToRightCmd());
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    cc.execute();
  }

  class CompoundCommand
      extends CompoundEdit
  {
    List<AbstractCmd> cmds;
    Map<AbstractCmd, UINode> uiNodeMap;

    CompoundCommand()
    {
      uiNodeMap = new HashMap<AbstractCmd, UINode>();
      cmds = new ArrayList<AbstractCmd>();
    }

    void add(UINode uiNode,
        AbstractCmd cmd)
    {
      if (cmd == null)
      {
        return;
      }

      uiNodeMap.put(cmd,
                    uiNode);
      cmds.add(cmd);
    }

    void execute()
    {
      try
      {
        for (AbstractCmd cmd : cmds)
        {
          cmd.execute();
          addEdit(cmd);
        }
        end();

        getUndoHandler().add(this);
        compareContents();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }

      check();
    }

    @Override
    public void redo()
    {
      super.redo();
      compareContents();
      check();
    }

    @Override
    public void undo()
    {
      super.undo();
      compareContents();
      check();
    }

    private void check()
    {
      mainPanel.checkActions();
      repaint();
    }

    private void compareContents()
    {
      for (AbstractCmd cmd : cmds)
      {
        uiNodeMap.get(cmd).getDiffNode().compareContents();
      }
    }
  }

  public void doRefresh(ActionEvent ae)
  {
    new RefreshAction().execute();
  }

  class RefreshAction
      extends SwingWorker<String, Object>
  {
    RefreshAction()
    {
    }

    @Override
    public String doInBackground()
    {
      diff.refresh();

      return null;
    }

    @Override
    protected void done()
    {
      treeTableModel = null;
      folderTreeTable.setTreeTableModel(getTreeTableModel());
      folderTreeTable.expandAll();
    }
  }

  public boolean isRemoveRightEnabled()
  {
    return !getEditorSettings().getRightsideReadonly();
  }

  public void doRemoveRight(ActionEvent ae)
  {
    CompoundCommand cc;

    cc = new CompoundCommand();
    for (UINode uiNode : getSelectedUINodes())
    {
      try
      {
        cc.add(uiNode,
               uiNode.getDiffNode().getRemoveRightCmd());
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    cc.execute();
  }

  public boolean isRemoveLeftEnabled()
  {
    return !getEditorSettings().getLeftsideReadonly();
  }

  public void doRemoveLeft(ActionEvent ae)
  {
    CompoundCommand cc;

    cc = new CompoundCommand();
    for (UINode uiNode : getSelectedUINodes())
    {
      try
      {
        cc.add(uiNode,
               uiNode.getDiffNode().getRemoveLeftCmd());
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    cc.execute();
  }

  public void doFilter(ActionEvent ae)
  {
    ((JMTreeTableModel) folderTreeTable.getTreeTableModel()).setRoot(getRootNode());
    folderTreeTable.expandAll();
  }

  private EditorSettings getEditorSettings()
  {
    return JMeldSettings.getInstance().getEditor();
  }

  private FolderSettings getFolderSettings()
  {
    return JMeldSettings.getInstance().getFolder();
  }

  private Set<UINode> getSelectedUINodes()
  {
    Set<UINode> result;
    TreePath path;
    UINode uiNode;

    result = new HashSet<UINode>();
    for (int row : folderTreeTable.getSelectedRows())
    {
      path = folderTreeTable.getPathForRow(row);
      if (path == null)
      {
        continue;
      }

      uiNode = (UINode) path.getLastPathComponent();
      if (uiNode == null)
      {
        continue;
      }

      buildResult(result,
                  uiNode);
    }

    return result;
  }

  private void buildResult(Set<UINode> result,
      UINode uiNode)
  {
    if (uiNode.isLeaf() && uiNode.getDiffNode() != null)
    {
      result.add(uiNode);
      return;
    }

    for (UINode node : uiNode.getChildren())
    {
      buildResult(result,
                  node);
    }
  }

  private MouseListener getMouseListener()
  {
    return new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent me)
      {
        UINode uiNode;
        TreePath path;
        int row;
        boolean open;
        boolean background;
        List<JMDiffNode> diffNodeList;
        int openCount;

        background = me.getClickCount() == 1 && me.getButton() == MouseEvent.BUTTON2;
        open = me.getClickCount() == 2 || background;

        if (open)
        {
          row = ((JTable) me.getSource()).rowAtPoint(me.getPoint());

          path = folderTreeTable.getPathForRow(row);
          if (path == null)
          {
            return;
          }

          uiNode = (UINode) path.getLastPathComponent();
          if (uiNode == null)
          {
            return;
          }

          diffNodeList = getDiffNodeList(uiNode);
          if (diffNodeList.isEmpty())
          {
            return;
          }

          openCount = 0;
          for (JMDiffNode diffNode : diffNodeList)
          {
            if (openCount++ > 20)
            {
              break;
            }

            mainPanel.openFileComparison(diffNode,
                                         background);
          }

          // Hack to make it possible to select with the MIDDLE
          // button of a mouse.
          if (folderTreeTable.getSelectedRow() != row)
          {
            folderTreeTable.setRowSelectionInterval(row,
                                                    row);
          }

          // Make sure that UP and DOWN keys work the way I want.
          folderTreeTable.requestFocus();
        }
      }
    };
  }

  private List<JMDiffNode> getDiffNodeList(UINode uiNode)
  {
    return new CollectDiffNodeLeaf(uiNode).getResult();
  }

  class CollectDiffNodeLeaf
  {
    private Set<JMDiffNode> diffNodeSet;

    CollectDiffNodeLeaf(UINode uiNode)
    {
      diffNodeSet = new HashSet<JMDiffNode>();

      collectDiffNode(uiNode);
    }

    private void collectDiffNode(UINode uiNode)
    {
      JMDiffNode diffNode;

      if (!uiNode.isLeaf())
      {
        for (UINode childUINode : uiNode.getChildren())
        {
          collectDiffNode(childUINode);
        }
      }
      else
      {
        diffNode = uiNode.getDiffNode();
        if (diffNode != null)
        {
          diffNodeSet.add(diffNode);
        }
      }
    }

    public List<JMDiffNode> getResult()
    {
      return new ArrayList<>(diffNodeSet);
    }
  }

  private JMTreeTableModel getTreeTableModel()
  {
    if (treeTableModel == null)
    {
      treeTableModel = createTreeTableModel();
      treeTableModel.setRoot(getRootNode());
    }

    return treeTableModel;
  }

  protected JMTreeTableModel createTreeTableModel()
  {
    return new FolderDiffTreeTableModel();
  }

  public void configurationChanged()
  {
    actionHandler.checkActions();
  }
}
