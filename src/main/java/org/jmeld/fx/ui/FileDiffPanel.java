package org.jmeld.fx.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import org.fxmisc.flowless.Virtualized;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.SimpleEditableStyledDocument;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.jmeld.diff.JMChunk;
import org.jmeld.diff.JMDelta;
import org.jmeld.diff.JMRevision;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxFontUtil;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.ui.fx.DiffLabel;
import org.jmeld.util.node.JMDiffNode;
import org.reactfx.collection.ListModification;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
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
    fileContentLeftCodeArea.setId("diffPanel");

    // fileContentLeftCodeArea.setWrapText(true);
    fileContentLeftCodeArea.replace(m_diffNode.getBufferNodeLeft().getDocument().getRichDocument());
    fileContentLeftScrollPane = new MyScrollPane<>(fileContentLeftCodeArea);
    // fileContentLeftScrollPane.onScrollProperty().addListener(new
    // MyScrollListener());
    fileContentLeftScrollPane.estimatedScrollXProperty().addListener(new MyScrollListener());
    fileContentLeftScrollPane.estimatedScrollYProperty().addListener(new MyScrollListener());
    fileContentLeftCodeArea.paragraphGraphicFactoryProperty()
        .bind(Bindings.when(JMeldSettingsFx.getInstance().getEditor().showLineNumbersProperty)
            .then(new LineNumberFactory(fileContentLeftCodeArea)).otherwise((LineNumberFactory) null));
    fileContentLeftCodeArea.styleProperty().bind(JMeldSettingsFx.getInstance().getEditor().styleProperty);

    saveRightButton = new Button();
    saveRightButton.setGraphic(new ImageView(FxIcon.SAVE.getSmallImage()));

    fileNameRightLabel = new DiffLabel();
    fileNameRightLabel.setText(rightText, leftText);

    fileContentRightCodeArea = new CodeArea();
    fileContentRightCodeArea.setId("diffPanel");
    // fileContentRightCodeArea.setWrapText(true);
    fileContentRightCodeArea.replace(m_diffNode.getBufferNodeRight().getDocument().getRichDocument());
    fileContentRightScrollPane = new VirtualizedScrollPane<>(fileContentRightCodeArea);
    fileContentRightCodeArea.paragraphGraphicFactoryProperty()
        .bind(Bindings.when(JMeldSettingsFx.getInstance().getEditor().showLineNumbersProperty)
            .then(new LineNumberFactory(fileContentRightCodeArea)).otherwise((LineNumberFactory) null));
    fileContentRightCodeArea.styleProperty().bind(JMeldSettingsFx.getInstance().getEditor().styleProperty);

    fileContentLeftScrollPane.estimatedScrollXProperty()
        .addListener((a, b, c) -> fileContentRightScrollPane.scrollXToPixel(c));
    fileContentRightScrollPane.estimatedScrollXProperty()
        .addListener((a, b, c) -> fileContentLeftScrollPane.scrollXToPixel(c));
    fileContentLeftScrollPane.estimatedScrollYProperty()
        .addListener((a, b, c) -> fileContentRightScrollPane.scrollYToPixel(c));
    fileContentRightScrollPane.estimatedScrollYProperty()
        .addListener((a, b, c) -> fileContentLeftScrollPane.scrollYToPixel(c));

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

    paintRevisionHighlights(fileContentLeftCodeArea, (JMDelta delta) -> delta.getOriginal());
    paintRevisionHighlights(fileContentRightCodeArea, (JMDelta delta) -> delta.getRevised());
  }

  class MyScrollListener
      implements ChangeListener
  {
    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue)
    {
      // System.out.println(oldValue + " -> " + newValue);
    }
  }

  /**
   * Hack to place the scrollbar on the left side
   * 
   * @author 'Kees Kuip'
   *
   * @param <V>
   */
  private class MyScrollPane<V extends Node & Virtualized>
    extends VirtualizedScrollPane<V>
  {
    public MyScrollPane(V content)
    {
      super(content);
    }

    @Override
    protected void layoutChildren()
    {
      ScrollBar vbar;

      super.layoutChildren();

      vbar = getVerticalBar();
      if (vbar.isVisible())
      {
        ScrollBar hbar;

        hbar = getHorizontalBar();

        vbar.relocate(0, 0);
        getContent().relocate(vbar.getWidth(), 0);
        hbar.relocate(vbar.getWidth(), hbar.getLayoutY());
      }
    }

    private ScrollBar getVerticalBar()
    {
      return (ScrollBar) getChildren().get(2);
    }

    private ScrollBar getHorizontalBar()
    {
      return (ScrollBar) getChildren().get(1);
    }
  }

  private void paintRevisionHighlights(CodeArea codeArea, Function<JMDelta, JMChunk> chunkFunction)
  {
    JMRevision revision;

    revision = m_diffNode.getRevision();
    for (JMDelta delta : revision.getDeltas())
    {
      JMChunk chunk;
      List<String> style;

      chunk = chunkFunction.apply(delta);
      style = Arrays.asList(delta.getType().getStyle());

      SimpleEditableStyledDocument lala = (SimpleEditableStyledDocument) codeArea.getDocument();

      IntStream.range(chunk.getAnchor(), chunk.getAnchor() + chunk.getSize()).forEach(index -> {
        SimpleEditableStyledDocument doc;

        doc = (SimpleEditableStyledDocument) codeArea.getDocument();
        doc.setParagraphStyle(index, style);

      });
    }

    /*
     * codeArea.getVisibleParagraphs() .addModificationObserver(new
     * VisibleParagraphStyler<>(codeArea, this::computeHighlighting));
     */

  }

  private StyleSpans<Collection<String>> computeHighlighting(String text)
  {
    StyleSpansBuilder<Collection<String>> spansBuilder;
    int index;

    spansBuilder = new StyleSpansBuilder<>();
    index = text.indexOf("bb");
    if (index != -1)
    {
      spansBuilder.add(Collections.singleton("delta-change"), text.length());
    }
    else
    {
      spansBuilder.add(Collections.singleton("delta-delete"), text.length());
    }

    return spansBuilder.create();
  }

  private class VisibleParagraphStyler<PS, SEG, S>
      implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>>
  {
    private final GenericStyledArea<PS, SEG, S> area;
    private final Function<String, StyleSpans<S>> computeStyles;
    private int prevParagraph, prevTextLength;

    public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, Function<String, StyleSpans<S>> computeStyles)
    {
      this.computeStyles = computeStyles;
      this.area = area;
    }

    @Override
    public void accept(ListModification<? extends Paragraph<PS, SEG, S>> lm)
    {
      if (lm.getAddedSize() > 0)
      {
        int paragraph = Math.min(area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size() - 1);
        String text = area.getText(paragraph, 0, paragraph, area.getParagraphLength(paragraph));

        System.out.println("paragraph=" + paragraph + ", added=" + lm.getAddedSize());
        if (paragraph != prevParagraph || text.length() != prevTextLength)
        {
          int startPos = area.getAbsolutePosition(paragraph, 0);
          // Platform.runLater(() -> {
          {
            List<String> style;

            style = Arrays.asList("delta-change");
            // area.setStyleSpans(startPos, computeStyles.apply(text));
            area.setParagraphStyle(paragraph, (PS) style);
          }
          // );
          prevTextLength = text.length();
          prevParagraph = paragraph;
        }
      }
    }
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
      codeArea.getParagraph(value).getParagraphStyle().forEach(style -> {
        if (style.startsWith("delta"))
        {
          label.getStyleClass().add(style);
        }
      });

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
