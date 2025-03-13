package util;

import ui.custom.frame.MainFrame;
import ui.custom.panel.MainPanel;
import ui.custom.screen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UIMain {
    public static void main(String[] args) {

       // var dimension = new Dimension(600,600);
        //JPanel jPanel = new MainPanel(dimension);
        //JFrame mainFrame = new MainFrame(dimension, jPanel);
        //mainFrame.revalidate();
        //mainFrame.repaint();

        final Map<String, String> gameConfig= Stream.of(args)
                .filter(arg -> arg.contains(";")) // Filtra strings que contêm ";"
                .collect(Collectors.toMap(
                        k -> k.split(";")[0], // Chave: primeira parte da string
                        v -> v.split(";")[1] // Valor: segunda parte da string
                ));
        var option = - 1;
        var mainScreen = new MainScreen(gameConfig);
        mainScreen.buildMainScreen();
    }
}
