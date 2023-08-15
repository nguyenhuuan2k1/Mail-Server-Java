package nhom50.CLIENT.GUI.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Loading extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */


    public static void Sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {

        }
    }

    /**
     * Create the frame.
     */
    public Loading() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 659, 465);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel
                .setIcon(new ImageIcon("images/unnamed.gif"));
        lblNewLabel.setBounds(230, 73, 294, 272);
        contentPane.add(lblNewLabel);
        contentPane.setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));
    }

}
