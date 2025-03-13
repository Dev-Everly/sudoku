package util;

import ui.custom.frame.MainFrame;
import ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;

public class UIMain {
    public static void main(String[] args) {

        var dimension = new Dimension(600,600);
        JPanel jPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, jPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
