package org.jmeld.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import org.jmeld.util.ObjectUtil;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class JFontChooser
{
  private Font m_initialFont;
  private Font m_newFont;
  private JTextArea m_exampleField;
  private JLabel m_currentFamilyLabel;

  public static final Integer[] DEFAULT_FONT_SIZES =
  {
      6, 8, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 32, 40, 48, 56, 64, 72
  };

  public void setSelectedFont(Font initialFont)
  {
    m_initialFont = initialFont;
  }

  public Font showFontDialog(JPanel parent)
  {
    JOptionPane pane;
    JDialog dialog;

    pane = new JOptionPane(getChooseFontPanel());
    pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);

    dialog = pane.createDialog(parent, "Choose files");
    dialog.setSize(new Dimension(400, 400));
    dialog.setResizable(true);
    try
    {
      dialog.setVisible(true);

      if (ObjectUtil.equals(pane.getValue(), JOptionPane.OK_OPTION))
      {
        return getFont();
      }
    }
    finally
    {
      // Always dispose a dialog -> otherwise there is a memory leak
      dialog.dispose();
    }

    return null;
  }

  private JPanel getChooseFontPanel()
  {
    JPanel fontPanel;
    String[] fontFamilyArray;
    JList<String> familyList;
    JList<Integer> sizeList;
    JCheckBox boldCheckBox;
    JCheckBox italicCheckBox;

    fontPanel = new JPanel(new MigLayout("fill", "[grow, fill][fill]", "[fill][fill][fill][fill][grow, fill][fill]"));

    fontFamilyArray = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    Arrays.sort(fontFamilyArray);

    m_exampleField = new JTextArea("abcABC123");
    m_exampleField.setBorder(new LineBorder(Color.lightGray));
    m_exampleField.setMinimumSize(new Dimension(40, 40));

    m_currentFamilyLabel = new JLabel();
    m_currentFamilyLabel.setBorder(new LineBorder(Color.lightGray));

    setFont(m_initialFont);

    familyList = new JList<>(fontFamilyArray);
    familyList.setVisibleRowCount(10);
    familyList.setSelectedValue(m_initialFont.getName(), true);
    familyList.addListSelectionListener((a) -> {
      setFont(new Font(familyList.getSelectedValue(), m_initialFont.getStyle(), m_initialFont.getSize()));
    });

    boldCheckBox = new JCheckBox("Bold");
    boldCheckBox.setSelected(getFont().isBold());
    italicCheckBox = new JCheckBox("Italic");
    boldCheckBox.setSelected(getFont().isItalic());

    boldCheckBox.addActionListener((a) -> {
      setFont(new Font(getFont().getFamily(), getStyle(boldCheckBox.isSelected(), italicCheckBox.isSelected()),
          getFont().getSize()));
    });
    italicCheckBox.addActionListener((a) -> {
      setFont(new Font(getFont().getFamily(), getStyle(boldCheckBox.isSelected(), italicCheckBox.isSelected()),
          getFont().getSize()));
    });

    sizeList = new JList<>(DEFAULT_FONT_SIZES);
    sizeList.setSelectedValue(m_initialFont.getSize(), true);
    sizeList.addListSelectionListener((a) -> {
      setFont(new Font(getFont().getName(), getFont().getStyle(), sizeList.getSelectedValue()));
    });

    fontPanel.add(new JLabel("Font:"));
    fontPanel.add(new JLabel("Font Style:"), new CC().wrap());
    fontPanel.add(m_currentFamilyLabel, new CC().growY());
    fontPanel.add(boldCheckBox, new CC().wrap());
    fontPanel.add(new JScrollPane(familyList), new CC().grow().spanY(3));
    fontPanel.add(italicCheckBox, new CC().wrap());
    fontPanel.add(new JLabel("Size:"), new CC().wrap());
    fontPanel.add(new JScrollPane(sizeList), new CC().wrap().grow());
    fontPanel.add(m_exampleField, new CC().spanX(2).growX());

    return fontPanel;
  }

  private Font getFont()
  {
    return m_newFont;
  }

  private void setFont(Font newFont)
  {
    m_newFont = newFont;
    m_exampleField.setFont(m_newFont);
    m_currentFamilyLabel.setText(m_newFont.getFamily());
  }

  private int getStyle(boolean bold, boolean italic)
  {
    return (bold ? Font.BOLD : 0) | (italic ? Font.ITALIC : 0);
  }

  static public void main(String args[])
  {
    JFontChooser fontChooser;

    fontChooser = new JFontChooser();
    fontChooser.setSelectedFont(new JTextField().getFont());
    System.out.println("font=" + fontChooser.showFontDialog(null));
  }
}
