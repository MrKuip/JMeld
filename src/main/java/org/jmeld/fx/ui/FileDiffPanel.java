package org.jmeld.fx.ui;

import java.util.stream.Stream;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.ui.fx.DiffLabel;
import org.jmeld.util.node.JMDiffNode;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import net.miginfocom.layout.CC;

public class FileDiffPanel
  extends MigPane
{
  private JMDiffNode m_diffNode;
  private SimpleStringProperty m_fileNameLeft = new SimpleStringProperty();
  private SimpleStringProperty m_fileNameRight = new SimpleStringProperty();

  public FileDiffPanel(JMDiffNode diffNode)
  {
    super("fill", "[p][p][fill, grow]0[p]0[p][fill, grow][p]", "[p][fill, grow][p]");

    setDiffNode(diffNode);

    init();
  }

  public void setDiffNode(JMDiffNode diffNode)
  {
    this.m_diffNode = diffNode;

    m_fileNameLeft.set(diffNode.getBufferNodeLeft().getName());
    m_fileNameRight.set(diffNode.getBufferNodeRight().getName());
  }

  private void init()
  {
    Button saveLeftButton;
    DiffLabel fileNameLeftLabel;
    CodeArea fileContentLeftCodeArea;
    VirtualizedScrollPane<CodeArea> fileContentLeftScrollPane;
    Button saveRightButton;
    DiffLabel fileNameRightLabel;
    CodeArea fileContentRightCodeArea;
    VirtualizedScrollPane<CodeArea> fileContentRightScrollPane;
    String leftText;
    String rightText;

    leftText = m_diffNode.getBufferNodeLeft().getName();
    rightText = m_diffNode.getBufferNodeRight().getName();

    saveLeftButton = new Button();
    saveLeftButton.setGraphic(new ImageView(FxIcon.SAVE.getSmallImage()));

    fileNameLeftLabel = new DiffLabel();
    fileNameLeftLabel.setText(leftText, rightText);

    // fileContentLeftCodeArea = new
    // CodeArea(Arrays.stream(m_diffNode.getBufferNodeLeft().getDocument().getLines()).map(
    // line -> line.toString()).collect(Collectors.joining()));
    fileContentLeftCodeArea = new CodeArea();

    Stream.of(m_diffNode.getBufferNodeLeft().getDocument().getLines())
        .forEach(line -> fileContentLeftCodeArea.appendText(line.toString()));

    fileContentLeftScrollPane = new VirtualizedScrollPane(fileContentLeftCodeArea);

    saveRightButton = new Button();
    saveRightButton.setGraphic(new ImageView(FxIcon.SAVE.getSmallImage()));

    fileNameRightLabel = new DiffLabel();
    // fileNameRightLabel.textProperty().bind(m_fileNameRight);
    fileNameRightLabel.setText(rightText, leftText);

    fileContentRightCodeArea = new CodeArea();

    Stream.of(m_diffNode.getBufferNodeRight().getDocument().getLines())
        .forEach(line -> fileContentRightCodeArea.appendText(line.toString()));

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
