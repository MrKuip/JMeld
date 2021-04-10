package org.jmeld.fx.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxFontUtil;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.ui.fx.DiffLabel;
import org.jmeld.util.node.JMDiffNode;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
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

    fileContentLeftCodeArea = new CodeArea();
    fileContentLeftCodeArea.setWrapText(true);
    fileContentLeftCodeArea.replace(m_diffNode.getBufferNodeLeft().getDocument().getRichDocument());
    fileContentLeftScrollPane = new VirtualizedScrollPane<>(fileContentLeftCodeArea);
    fileContentLeftCodeArea.paragraphGraphicFactoryProperty()
        .bind(Bindings.when(JMeldSettingsFx.getInstance().getEditor().showLineNumbersProperty)
            .then(new LineNumberFactory(fileContentLeftCodeArea)).otherwise((LineNumberFactory) null));

    saveRightButton = new Button();
    saveRightButton.setGraphic(new ImageView(FxIcon.SAVE.getSmallImage()));

    fileNameRightLabel = new DiffLabel();
    fileNameRightLabel.setText(rightText, leftText);

    fileContentRightCodeArea = new CodeArea();
    fileContentRightCodeArea.setWrapText(true);
    fileContentRightCodeArea.replace(m_diffNode.getBufferNodeRight().getDocument().getRichDocument());
    fileContentRightScrollPane = new VirtualizedScrollPane<>(fileContentRightCodeArea);
    fileContentRightCodeArea.paragraphGraphicFactoryProperty()
        .bind(Bindings.when(JMeldSettingsFx.getInstance().getEditor().showLineNumbersProperty)
            .then(new LineNumberFactory(fileContentRightCodeArea)).otherwise((LineNumberFactory) null));

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

  private class LineNumberFactory
      implements IntFunction<Node>
  {
    private CodeArea codeArea;
    private Map<Integer, Double> m_minSizeByLength = new HashMap<>();

    public LineNumberFactory(CodeArea codeArea)
    {
      this.codeArea = codeArea;
    }

    @Override
    public Node apply(int value)
    {
      Label label;

      label = new Label(String.valueOf(value));
      label.setAlignment(Pos.CENTER_RIGHT);
      label.setPadding(new Insets(0, 5, 0, 0));
      label.setPrefWidth(getMinSize(label, codeArea.getParagraphs().size()));
      label
          .setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), CornerRadii.EMPTY, Insets.EMPTY)));
      label.getStyleClass().add("lineno");

      return label;
    }

    private double getMinSize(Label label, int size)
    {
      return m_minSizeByLength.computeIfAbsent(size, (s) -> {
        return (double) FxFontUtil.getFontMetrics(label.getFont()).computeStringWidth("" + size)
            + label.getPadding().getRight();
      });
    }
  }
}
