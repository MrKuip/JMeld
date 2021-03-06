/*
 * NewPanelDialogForm.java
 *
 * Created on August 28, 2007, 9:10 PM
 */

package org.jmeld.ui;

/**
 *
 * @author kees
 */
public class NewPanelDialogForm
    extends javax.swing.JPanel
{

  /** Creates new form NewPanelDialogForm */
  public NewPanelDialogForm()
  {
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    fileLeftLabel = new javax.swing.JLabel();
    fileRightLabel = new javax.swing.JLabel();
    fileBrowseLeftButton = new javax.swing.JButton();
    fileBrowseRightButton = new javax.swing.JButton();
    fileLeftComboBox = new javax.swing.JComboBox();
    fileRightComboBox = new javax.swing.JComboBox();
    fileFilterComboBox = new javax.swing.JComboBox();
    fileLeftReadonlyCheckBox = new javax.swing.JCheckBox();
    fileRightReadonlyCheckBox = new javax.swing.JCheckBox();
    filterLabel = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jComboBox3 = new javax.swing.JComboBox();
    jComboBox4 = new javax.swing.JComboBox();
    jButton5 = new javax.swing.JButton();
    jButton6 = new javax.swing.JButton();
    jCheckBox1 = new javax.swing.JCheckBox();
    jCheckBox2 = new javax.swing.JCheckBox();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();

    fileLeftLabel.setFont(new java.awt.Font("Dialog",
                                            1,
                                            11));
    fileLeftLabel.setText("Left");

    fileRightLabel.setFont(new java.awt.Font("Dialog",
                                             1,
                                             11));
    fileRightLabel.setText("Right");

    fileBrowseLeftButton.setText("Browse");

    fileBrowseRightButton.setText("Browse");

    fileLeftComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]
    {
        "Item 1", "Item 2", "Item 3", "Item 4"
    }));

    fileRightComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]
    {
        "Item 1", "Item 2", "Item 3", "Item 4"
    }));

    fileFilterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]
    {
        "Item 1", "Item 2", "Item 3", "Item 4"
    }));

    fileLeftReadonlyCheckBox.setText("Left readonly");

    fileRightReadonlyCheckBox.setText("Right readonly");

    filterLabel.setFont(new java.awt.Font("Dialog",
                                          1,
                                          11));
    filterLabel.setText("Filter");

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout
        .setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup().addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(fileRightLabel).add(fileLeftLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(fileRightComboBox,
                                 0,
                                 261,
                                 Short.MAX_VALUE)
                            .add(fileLeftComboBox,
                                 0,
                                 261,
                                 Short.MAX_VALUE)))
                    .add(jPanel1Layout.createSequentialGroup().add(filterLabel).add(11,
                                                                                    11,
                                                                                    11)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(fileLeftReadonlyCheckBox).add(fileFilterComboBox,
                                                               0,
                                                               263,
                                                               Short.MAX_VALUE)
                            .add(fileRightReadonlyCheckBox))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(fileBrowseRightButton).add(fileBrowseLeftButton))
                .addContainerGap()));
    jPanel1Layout.setVerticalGroup(
                                   jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                       .add(jPanel1Layout.createSequentialGroup().addContainerGap()
                                           .add(jPanel1Layout
                                               .createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                               .add(fileLeftLabel).add(fileLeftComboBox,
                                                                       org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                                       org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                                       org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                               .add(fileBrowseLeftButton))
                                           .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                           .add(jPanel1Layout
                                               .createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                               .add(fileRightComboBox,
                                                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                               .add(fileRightLabel).add(fileBrowseRightButton))
                                           .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                           .add(jPanel1Layout
                                               .createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                               .add(fileFilterComboBox,
                                                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                               .add(filterLabel))
                                           .add(18,
                                                18,
                                                18)
                                           .add(fileLeftReadonlyCheckBox)
                                           .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                           .add(fileRightReadonlyCheckBox).addContainerGap(57,
                                                                                           Short.MAX_VALUE)));

    jPanel1Layout.linkSize(new java.awt.Component[]
    {
        fileFilterComboBox, fileLeftComboBox, fileRightComboBox
    },
                           org.jdesktop.layout.GroupLayout.VERTICAL);

    jTabbedPane1.addTab("File comparison",
                        jPanel1);

    jLabel3.setText("Left");

    jLabel4.setText("Right");

    jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[]
    {
        "Item 1", "Item 2", "Item 3", "Item 4"
    }));

    jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[]
    {
        "Item 1", "Item 2", "Item 3", "Item 4"
    }));

    jButton5.setText("Browse");

    jButton6.setText("Browse");

    jCheckBox1.setText("Left readonly");

    jCheckBox2.setText("Right readonly");

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel2Layout.createSequentialGroup().addContainerGap()
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(jLabel4))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jCheckBox2)
                .add(jCheckBox1)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING,
                             jComboBox4,
                             0,
                             263,
                             Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING,
                             jComboBox3,
                             0,
                             263,
                             Short.MAX_VALUE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING,
                             jButton5)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING,
                             jButton6))))
            .addContainerGap()));
    jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel2Layout.createSequentialGroup().addContainerGap()
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(jButton5)
                .add(jComboBox3,
                     org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                     org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                     org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(jButton6)
                .add(jComboBox4,
                     org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                     org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                     org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(18,
                 18,
                 18)
            .add(jCheckBox1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox2)
            .addContainerGap(86,
                             Short.MAX_VALUE)));

    jTabbedPane1.addTab("Directory comparison",
                        jPanel2);

    jButton1.setText("Cancel");

    jButton2.setText("OK");

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout
        .createSequentialGroup()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup()
            .add(124,
                 124,
                 124)
            .add(jButton2,
                 org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                 70,
                 org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1,
                                                                          org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                                          70,
                                                                          org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup().addContainerGap().add(jTabbedPane1)))
        .addContainerGap()));
    layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING,
             layout.createSequentialGroup().addContainerGap().add(jTabbedPane1,
                                                                  org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                                  240,
                                                                  Short.MAX_VALUE)
                 .add(12,
                      12,
                      12)
                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton1).add(jButton2))
                 .addContainerGap()));
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  protected javax.swing.JButton fileBrowseLeftButton;
  protected javax.swing.JButton fileBrowseRightButton;
  protected javax.swing.JComboBox fileFilterComboBox;
  protected javax.swing.JComboBox fileLeftComboBox;
  protected javax.swing.JLabel fileLeftLabel;
  protected javax.swing.JCheckBox fileLeftReadonlyCheckBox;
  protected javax.swing.JComboBox fileRightComboBox;
  protected javax.swing.JLabel fileRightLabel;
  protected javax.swing.JCheckBox fileRightReadonlyCheckBox;
  protected javax.swing.JLabel filterLabel;
  protected javax.swing.JButton jButton1;
  protected javax.swing.JButton jButton2;
  protected javax.swing.JButton jButton5;
  protected javax.swing.JButton jButton6;
  protected javax.swing.JCheckBox jCheckBox1;
  protected javax.swing.JCheckBox jCheckBox2;
  protected javax.swing.JComboBox jComboBox3;
  protected javax.swing.JComboBox jComboBox4;
  protected javax.swing.JLabel jLabel3;
  protected javax.swing.JLabel jLabel4;
  protected javax.swing.JPanel jPanel1;
  protected javax.swing.JPanel jPanel2;
  protected javax.swing.JTabbedPane jTabbedPane1;
  // End of variables declaration//GEN-END:variables

}
