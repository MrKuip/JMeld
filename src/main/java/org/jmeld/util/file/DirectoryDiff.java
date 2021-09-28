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
package org.jmeld.util.file;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.jmeld.tools.ant.DirectoryScanner;
import org.jmeld.settings.util.Filter;
import org.jmeld.ui.StatusBar;
import org.jmeld.util.StopWatch;
import org.jmeld.util.StringUtil;
import org.jmeld.util.node.FileNode;
import org.jmeld.util.node.JMDiffNode;

public class DirectoryDiff
  extends FolderDiff
{
  private File rightDirectory;
  private File leftDirectory;
  private JMDiffNode rootNode;
  private Map<String, JMDiffNode> nodes;
  private Filter filter;

  public DirectoryDiff(File leftDirectory, File rightDirectory, Filter filter, Mode mode)
  {
    super(mode);

    this.leftDirectory = leftDirectory;
    this.rightDirectory = rightDirectory;
    this.filter = filter;

    try
    {
      setLeftFolderShortName(leftDirectory.getName());
      setRightFolderShortName(rightDirectory.getName());
      setLeftFolderName(leftDirectory.getCanonicalPath());
      setRightFolderName(rightDirectory.getCanonicalPath());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public JMDiffNode getRootNode()
  {
    return rootNode;
  }

  public Collection<JMDiffNode> getNodes()
  {
    return nodes.values();
  }

  public void diff()
  {
    DirectoryScanner ds;
    JMDiffNode node;
    StopWatch stopWatch;
    int numberOfNodes;
    int currentNumber;
    FileNode fn;

    stopWatch = new StopWatch();
    stopWatch.start();

    StatusBar.getInstance().start();
    StatusBar.getInstance().setState("Start scanning directories...");

    rootNode = new JMDiffNode("<root>", false);
    nodes = new HashMap<String, JMDiffNode>();

    ds = new DirectoryScanner();
    ds.setShowStateOn(true);
    ds.setBasedir(leftDirectory);
    if (filter != null)
    {
      // ds.setIncludes(filter.getIncludes());
      // ds.setExcludes(filter.getExcludes());
    }
    ds.setCaseSensitive(true);

    System.out.println("Scan: " + leftDirectory);
    ds.scan();
    System.out.println("End Scan in " + stopWatch.getElapsedTime());

    for (FileNode fileNode : ds.getIncludedFiles())
    {
      node = addNode(fileNode.getName());
      node.setBufferNodeLeft(fileNode);
    }

    ds = new DirectoryScanner();
    ds.setShowStateOn(true);
    ds.setBasedir(rightDirectory);
    if (filter != null)
    {
      // ds.setIncludes(filter.getIncludes());
      // ds.setExcludes(filter.getExcludes());
    }
    ds.setCaseSensitive(true);
    ds.scan();

    for (FileNode fileNode : ds.getIncludedFiles())
    {
      node = addNode(fileNode.getName());
      node.setBufferNodeRight(fileNode);
    }

    StatusBar.getInstance().setState("Comparing nodes...");
    numberOfNodes = nodes.size();
    currentNumber = 0;
    for (JMDiffNode n : nodes.values())
    {
      // Make sure that each node has it's opposite.
      // This makes the following copying actions possible :
      // - copy 'left' to 'not existing'
      // - copy 'right' to 'not existing'
      if (n.getBufferNodeRight() == null || n.getBufferNodeLeft() == null)
      {
        if (n.getBufferNodeRight() == null)
        {
          fn = (FileNode) n.getBufferNodeLeft();
          fn = new FileNode(fn.getName(), new File(rightDirectory, fn.getName()));
          n.setBufferNodeRight(fn);
        }
        else
        {
          fn = (FileNode) n.getBufferNodeRight();
          fn = new FileNode(fn.getName(), new File(leftDirectory, fn.getName()));
          n.setBufferNodeLeft(fn);
        }
      }

      n.compareContents();

      StatusBar.getInstance().setProgress(++currentNumber, numberOfNodes);
    }

    StatusBar.getInstance()
        .setState("Ready comparing directories (took " + (stopWatch.getElapsedTime() / 1000) + " seconds)");
    StatusBar.getInstance().stop();
  }

  private JMDiffNode addNode(String name)
  {
    JMDiffNode node;

    node = nodes.get(name);
    if (node == null)
    {
      node = addNode(new JMDiffNode(name, true));
    }

    return node;
  }

  private JMDiffNode addNode(JMDiffNode node)
  {
    String parentName;
    JMDiffNode parent;
    File file;

    nodes.put(node.getName(), node);

    parentName = node.getParentName();
    if (StringUtil.isEmpty(parentName))
    {
      parent = rootNode;
    }
    else
    {
      parent = nodes.get(parentName);
      if (parent == null)
      {
        parent = addNode(new JMDiffNode(parentName, false));
        parent.setBufferNodeRight(new FileNode(parentName, new File(rightDirectory, parentName)));
        parent.setBufferNodeLeft(new FileNode(parentName, new File(leftDirectory, parentName)));
      }
    }

    parent.addChild(node);
    return node;
  }

  public void print()
  {
    rootNode.print("");
  }

  public static void test()
  {
    DirectoryDiff dd;

    dd = new DirectoryDiff(new File("/media/kees/CubeBackup/backup/snapshot_001_2021_06_16__18_00_01/projecten/"),
        new File("/media/kees/CubeBackup/backup/snapshot_008_2021_06_11__12_00_01/projecten/"), null, Mode.TWO_WAY);
    dd.diff();
  }

  public static void test2()
  {
    DirectoryScanner ds;

    ds = new DirectoryScanner();
    ds.setShowStateOn(false);
    ds.setBasedir("/media/kees/CubeBackup/backup/snapshot_001_2021_06_16__18_00_01/projecten/");
    ds.setCaseSensitive(true);
    ds.scan();
  }

  public static void main(String[] args)
  {
    StopWatch stopWatch;

    stopWatch = new StopWatch();
    stopWatch.start();

    System.out.println("Start Scan");

    test();

    System.out.println("End Scan in " + stopWatch.getElapsedTime());
  }
}
