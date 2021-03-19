package org.jmeld.fx.ui;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import net.miginfocom.layout.CC;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.util.node.JMDiffNode;
import org.jmeld.util.node.JMDiffNodeFactory;
import org.tbee.javafx.scene.layout.MigPane;

public class FileDiffPanel
    extends MigPane
{
  private JMDiffNode m_diffNode;
  private SimpleStringProperty m_fileNameLeft = new SimpleStringProperty("/usr/local/kees/projecten/Standard/branches/7_3/bin/backupDb");
  private SimpleStringProperty m_fileNameRight = new SimpleStringProperty("/usr/local/kees/projecten/Standard/branches/7_4/bin/backupDb");

  public FileDiffPanel()
  {
    super("fill",
          "[p][p][fill, grow]0[p]0[p][fill, grow][p]",
          "[p][fill, grow][p]");

    init();
    test();
  }

  private void test()
  {
    File leftFile;
    File rightFile;

    leftFile = new File("/usr/local/kees/projecten/Standard/branches/7_3/bin/backupDb");
    rightFile = new File("/usr/local/kees/projecten/Standard/branches/7_4/bin/backupDb");

    setDiffNode(JMDiffNodeFactory.create(leftFile.getName(), leftFile, rightFile.getName(), rightFile));

    m_fileNameLeft.set(leftFile.getAbsolutePath());
    m_fileNameRight.set(rightFile.getAbsolutePath());
  }

  public void setDiffNode(JMDiffNode diffNode)
  {
    this.m_diffNode = diffNode;
  }

  private void init()
  {
    Button saveLeftButton;
    Label fileNameLeftLabel;
    CodeArea fileContentLeftCodeArea;
    VirtualizedScrollPane<CodeArea> fileContentLeftScrollPane;

    Button saveRightButton;
    Label fileNameRightLabel;
    CodeArea fileContentRightCodeArea;
    VirtualizedScrollPane<CodeArea> fileContentRightScrollPane;

    saveLeftButton = new Button();
    saveLeftButton.setGraphic(new ImageView(FxIcon.SAVE.getSmallImage()));

    fileNameLeftLabel = new Label();
    fileNameLeftLabel.textProperty().bind(m_fileNameLeft);

    fileContentLeftCodeArea = new CodeArea(Arrays.stream(m_diffNode.getBufferNodeLeft().getDocument().getLines()).map(
        line -> line.toString()).collect(Collectors.joining()));
    fileContentLeftScrollPane = new VirtualizedScrollPane(fileContentLeftCodeArea);

    saveRightButton = new Button();
    saveRightButton.setGraphic(new ImageView(FxIcon.SAVE.getSmallImage()));

    fileNameRightLabel = new Label();
    fileNameRightLabel.textProperty().bind(m_fileNameRight);

    fileContentRightCodeArea = new CodeArea(Arrays.stream(m_diffNode.getBufferNodeRight().getDocument().getLines())
        .map(line -> line.toString()).collect(Collectors.joining()));
    fileContentRightScrollPane = new VirtualizedScrollPane(fileContentRightCodeArea);

    add(saveLeftButton, new CC());
    add(fileNameLeftLabel, new CC().spanX(2).grow());
    add(fileNameRightLabel, new CC().skip().spanX(2).grow());
    add(saveRightButton, new CC().wrap());
    add(new Button("e"), new CC().growX());
    add(fileContentLeftScrollPane, new CC().spanX(2).grow());
    add(new Button("g"), new CC());
    add(fileContentRightScrollPane, new CC().spanX(2).grow());
    add(new Button("i"), new CC().wrap().growX());
    add(new Button("j"), new CC().skip());
    add(new Button("k"), new CC());
    add(new Button("l"), new CC().skip());
    add(new Button("m"), new CC());
  }

  private void init2()
  {
    add(new Button("aaaa"), new CC());
    add(new Button("b"), new CC().spanX(2).grow());
    add(new Button("c"), new CC().skip().spanX(2).grow());
    add(new Button("dddd"), new CC().wrap());
    add(new Button("e"), new CC().growX());
    add(new Button("f"), new CC().spanX(2).grow());
    add(new Button("g"), new CC());
    add(new Button("h"), new CC().spanX(2).grow());
    add(new Button("i"), new CC().wrap().growX());
    add(new Button("j"), new CC().skip());
    add(new Button("k"), new CC());
    add(new Button("l"), new CC().skip());
    add(new Button("m"), new CC());
  }
}
