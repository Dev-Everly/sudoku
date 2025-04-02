package ui.custom.screen;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import model.Space;
import service.EventEnum;
import service.NotifierService;
import service.TabuleiroService;
import ui.custom.button.ButtonCheckGameStatus;
import ui.custom.button.ButtonReset;
import ui.custom.button.FinishGameButton;
import ui.custom.frame.MainFrame;
import ui.custom.input.NumberText;
import ui.custom.panel.MainPanel;
import ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;

import java.util.Map;


public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private final TabuleiroService tabuleiroService;

    public MainScreen (final Map<String,String> gameConfig) {
        this.tabuleiroService = new TabuleiroService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension,mainPanel);
        // Para cada setor 3x3 do Sudoku
        for (int sectorRow = 0; sectorRow < 3; sectorRow++) {
            for (int sectorCol = 0; sectorCol < 3; sectorCol++) {
                // Calcula os limites do setor
                int initRow = sectorRow * 3;
                int endRow = initRow + 3;
                int initCol = sectorCol * 3;
                int endCol = initCol + 3;

                // Obtém os espaços do setor atual
                List<Space> sectorSpaces = getSpaces(tabuleiroService.getSpaces(), initCol, endCol, initRow, endRow);

                // Adiciona o setor ao painel principal
                mainPanel.add(generateSection(sectorSpaces));
            }
        }

        addResetButton(mainPanel);
        addShowGameStatus(mainPanel);
        addFinishGame(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpaces(List<List<Space>> spaces,
                                  final int initCol, final int endCol,
                                  final int initRow, final int endRow) {
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r < endRow; r++) {
            for (int c = initCol; c < endCol; c++) {
                spaceSector.add(spaces.get(r).get(c)); // Acesso correto: linha primeiro
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces) {
        if (spaces == null) {
            throw new IllegalArgumentException("Lista de espaços não pode ser nula");
        }

        List<NumberText> campos = spaces.stream()
                .map(NumberText::new)
                .collect(Collectors.toList());
                campos.forEach(t -> notifierService.subEscrever(EventEnum.CLEAR_SPACE,t));
        return new SudokuSector(campos);
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
                notifierService.notify(EventEnum.CLEAR_SPACE);
            }
        });
        mainPanel.add(buttonReset);
    }

    private final NotifierService notifierService;
    private JButton finishGameButton;
    private JButton buttonCheckGameStatus;
    private JButton buttonReset;


}
