package nhom50.CLIENT.GUI.custom;


import nhom50.CLIENT.helpers.Helper;

import javax.swing.*;
import java.awt.*;

public class FileChooser extends JFrame {

    private javax.swing.JFileChooser jFileChooser1;

    public FileChooser() {
        initComponents();
        this.setTitle("Choose File");
        this.setIconImage(new ImageIcon("images/attach_24px.png").getImage());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Helper.changeLNF("Windows");
                new FileChooser().setVisible(true);
            }
        });
    }

    private void initComponents() {

        jFileChooser1 = new JFileChooser();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jFileChooser1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jFileChooser1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }
}
