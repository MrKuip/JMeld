package org.jmeld.util.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;
import org.jmeld.JMeldException;
import org.jmeld.diff.JMDiff;
import org.jmeld.diff.JMRevision;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.ui.StatusBar;
import org.jmeld.ui.text.BufferDocumentIF;
import org.jmeld.util.Ignore;
import org.jmeld.util.file.CompareUtil;
import org.jmeld.util.file.cmd.AbstractCmd;
import org.jmeld.util.file.cmd.CopyFileCmd;
import org.jmeld.util.file.cmd.RemoveFileCmd;

public class JMDiffNode
    implements TreeNode
{
  public enum Compare
  {
    Equal,
    NotEqual,
    RightMissing,
    LeftMissing,
    BothMissing;
  }

  private String text;
  private String name;
  private String id;
  private String shortName;
  private String parentName;
  private JMDiffNode parent;
  private List<JMDiffNode> children;
  private EnumMap<Location, BufferNode> bufferNodeByLocationMap = new EnumMap<>(Location.class);
  private boolean leaf;
  private Compare compareState;
  private JMDiff diff;
  private JMRevision revision;
  private Ignore ignore;

  public enum Location
  {
    LEFT,
    RIGHT;
  }

  public JMDiffNode(String name, boolean leaf)
  {
    this.name = name;
    this.shortName = name;
    this.leaf = leaf;

    ignore = JMeldSettingsFx.getInstance().getEditor().getIgnore();

    children = new ArrayList<>();
    calculateNames();
  }

  public String getId()
  {
    return id;
  }

  private void initId()
  {
    id = (getBufferNodeLeft() != null ? getBufferNodeLeft().getName() : "x")
        + (getBufferNodeRight() != null ? getBufferNodeRight().getName() : "x");
  }

  public String getName()
  {
    return name;
  }

  public String getShortName()
  {
    return shortName;
  }

  public Ignore getIgnore()
  {
    return ignore;
  }

  public String getParentName()
  {
    return parentName;
  }

  public void addChild(JMDiffNode child)
  {
    children.add(child);
    child.setParent(this);
  }

  private void setParent(JMDiffNode parent)
  {
    this.parent = parent;
  }

  public void setBufferNodeLeft(BufferNode bufferNode)
  {
    bufferNodeByLocationMap.put(Location.LEFT, bufferNode);
    initId();
  }

  public BufferNode getBufferNodeLeft()
  {
    return bufferNodeByLocationMap.get(Location.LEFT);
  }

  public void setBufferNodeRight(BufferNode bufferNode)
  {
    bufferNodeByLocationMap.put(Location.RIGHT, bufferNode);
    initId();
  }

  public BufferNode getBufferNodeRight()
  {
    return bufferNodeByLocationMap.get(Location.RIGHT);
  }

  public List<JMDiffNode> getChildren()
  {
    return children;
  }

  @Override
  public Enumeration<JMDiffNode> children()
  {
    return Collections.enumeration(children);
  }

  @Override
  public boolean getAllowsChildren()
  {
    return isLeaf();
  }

  @Override
  public JMDiffNode getChildAt(int childIndex)
  {
    return children.get(childIndex);
  }

  @Override
  public int getChildCount()
  {
    return children.size();
  }

  @Override
  public int getIndex(TreeNode node)
  {
    return children.indexOf(node);
  }

  @Override
  public JMDiffNode getParent()
  {
    return parent;
  }

  @Override
  public boolean isLeaf()
  {
    return leaf;
  }

  private void calculateNames()
  {
    int index;

    index = name.lastIndexOf(File.separator);
    if (index == -1)
    {
      parentName = null;
      return;
    }

    parentName = name.substring(0, index);
    shortName = name.substring(index + 1);
  }

  public AbstractCmd getCopyToRightCmd() throws Exception
  {
    // TODO: This is NOT OO!
    if (getBufferNodeLeft().exists() && getBufferNodeLeft() instanceof FileNode
        && getBufferNodeRight() instanceof FileNode)
    {
      return new CopyFileCmd(this, (FileNode) getBufferNodeLeft(), (FileNode) getBufferNodeRight());
    }

    return null;
  }

  public AbstractCmd getCopyToLeftCmd() throws Exception
  {
    // TODO: This is NOT OO!
    if (getBufferNodeRight().exists() && getBufferNodeLeft() instanceof FileNode
        && getBufferNodeRight() instanceof FileNode)
    {
      return new CopyFileCmd(this, (FileNode) getBufferNodeRight(), (FileNode) getBufferNodeLeft());
    }

    return null;
  }

  public AbstractCmd getRemoveLeftCmd() throws Exception
  {
    // TODO: This is NOT OO!
    if (getBufferNodeLeft() instanceof FileNode)
    {
      return new RemoveFileCmd(this, (FileNode) getBufferNodeLeft());
    }

    return null;
  }

  public AbstractCmd getRemoveRightCmd() throws Exception
  {
    // TODO: This is NOT OO!
    if (getBufferNodeRight() instanceof FileNode)
    {
      return new RemoveFileCmd(this, (FileNode) getBufferNodeRight());
    }

    return null;
  }

  public void compareContents()
  {
    boolean equals;

    if (!getBufferNodeLeft().exists() && !getBufferNodeRight().exists())
    {
      setCompareState(Compare.BothMissing);
      return;
    }

    if (getBufferNodeLeft().exists() && !getBufferNodeRight().exists())
    {
      setCompareState(Compare.RightMissing);
      return;
    }

    if (!getBufferNodeLeft().exists() && getBufferNodeRight().exists())
    {
      setCompareState(Compare.LeftMissing);
      return;
    }

    if (!isLeaf())
    {
      setCompareState(Compare.Equal);
      return;
    }

    if (getBufferNodeLeft().isSameNode(getBufferNodeRight()))
    {
      setCompareState(Compare.Equal);
      return;
    }

    equals = CompareUtil.contentEquals(getBufferNodeLeft(), getBufferNodeRight(), ignore);
    setCompareState(equals ? Compare.Equal : Compare.NotEqual);
  }

  public void diff() throws JMeldException
  {
    BufferDocumentIF documentLeft;
    BufferDocumentIF documentRight;
    Object[] left, right;

    StatusBar.getInstance().start();

    documentLeft = null;
    documentRight = null;

    if (getBufferNodeLeft() != null)
    {
      documentLeft = getBufferNodeLeft().getDocument();
      StatusBar.getInstance().setState("Reading left : %s", getBufferNodeLeft().getName());
      if (documentLeft != null)
      {
        documentLeft.read();
      }
    }

    if (getBufferNodeRight() != null)
    {
      documentRight = getBufferNodeRight().getDocument();
      StatusBar.getInstance().setState("Reading right: %s", getBufferNodeRight().getName());
      if (documentRight != null)
      {
        documentRight.read();
      }
    }

    StatusBar.getInstance().setState("Calculating differences");
    diff = new JMDiff();
    left = documentLeft == null ? null : documentLeft.getLines();
    right = documentRight == null ? null : documentRight.getLines();

    revision = diff.diff(left, right, ignore);
    StatusBar.getInstance().setState("Ready calculating differences");
    StatusBar.getInstance().stop();
  }

  public JMDiff getDiff()
  {
    return diff;
  }

  public JMRevision getRevision()
  {
    return revision;
  }

  public void setCompareState(Compare state)
  {
    compareState = state;
  }

  public boolean isCompareEqual(Compare state)
  {
    return compareState == state;
  }

  public void print(String indent)
  {
    System.out.println(indent + shortName + " (" + compareState + ")");
    indent += "  ";
    for (JMDiffNode node : children)
    {
      node.print(indent);
    }
  }

  @Override
  public String toString()
  {
    String pn;

    if (text == null)
    {
      text = name;
      if (parent != null)
      {
        pn = parent.getName();
        if (name.startsWith(pn))
        {
          text = name.substring(pn.length() + 1);
        }
      }
    }

    return text;
  }
}
