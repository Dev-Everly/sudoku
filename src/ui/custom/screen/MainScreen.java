package ui.custom.screen;

import model.GameStatusEnum;
import service.TabuleiroService;
import ui.custom.button.ButtonCheckGameStatus;
import ui.custom.button.ButtonReset;
import ui.custom.button.FinishGameButton;
import ui.custom.frame.MainFrame;
import ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static model.GameStatusEnum.NON_STARTED;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private final TabuleiroService tabuleiroService;

    public MainScreen (final Map<String,String> gameConfig) {
        this.tabuleiroService = new TabuleiroService(gameConfig);
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension,mainPanel);
        addResetButton(mainPanel);
        addShowGameStatus(mainPanel);
        addFinishGame(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addFinishGame(JPanel mainPanel) {
          finishGameButton = new FinishGameButton(e -> {
             if(tabuleiroService.gameIsFinished()) {
                 JOptionPane.showConfirmDialog(null,"Jogo concluído Parabéns");
                this.buttonReset.setEnabled(false);
                this.buttonCheckGameStatus.setEnabled(false);
                this.finishGameButton.setEnabled(false);
             } else {
                 JOptionPane.showConfirmDialog(null,"Seu jogo contém algum erro");
             }
         });



        mainPanel.add(finishGameButton);
    }

    private void addShowGameStatus(JPanel mainPanel) {

        buttonCheckGameStatus= new ButtonCheckGameStatus(e -> {
            var hasErrors = tabuleiroService.hasErrors();
            var gameStatus = tabuleiroService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "Jogo não iniciado ";
                case INCOMPLETE ->  "Jogo incompleto " ;
                case COMPLETE -> "Jogo completo";
            };

            message += hasErrors ? " e contém erros " : " não contém erros";
            JOptionPane.showConfirmDialog(null,message);
        });
        mainPanel.add(buttonCheckGameStatus);

    }

    private void addResetButton(JPanel mainPanel) {
        buttonReset = new ButtonReset(e ->{
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja reiniciar o jogo ? ",
                    "Limpar jogo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if(dialogResult == 0) {
                tabuleiroService.reset();
            }
        });
        mainPanel.add(buttonReset);
    }

    private JButton finishGameButton;
    private JButton buttonCheckGameStatus;
    private JButton buttonReset;


}
