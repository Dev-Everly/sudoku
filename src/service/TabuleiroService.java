package service;

import model.GameStatusEnum;
import model.Space;
import model.Tabuleiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabuleiroService {

    private final static int TABULEIRO_LIMIT = 9;

    private final Tabuleiro tabuleiro;

    public TabuleiroService (final Map<String, String> gameConfig) {
        this.tabuleiro = new Tabuleiro(initTabuleiro(gameConfig));
    }

    public List<List<Space>> getSpaces() {
        return this.getSpaces();
    }
    public void reset () {
        this.tabuleiro.reset();
    }

    public boolean hasErrors() {
        return this.tabuleiro.hasErrors();
    }

    public GameStatusEnum getStatus() {
        return  this.tabuleiro.getStatus();
    }

    public boolean gameIsFinished() {
        return this.tabuleiro.gameFinish();
    }

    private List<List<Space>> initTabuleiro (final Map<String, String> gameConfig) {

        List<List<Space>> spaces = new ArrayList<>();
        for(int i = 0 ; i < TABULEIRO_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0 ; j < TABULEIRO_LIMIT; j++) {

                var positionConfig = gameConfig.get("%s%s".formatted(i,j));

                if(positionConfig == null) {
                    throw new IllegalArgumentException("Configuração da posição %s%s não encontrada"
                            .formatted(i,j));
                }

                String [] configParts = positionConfig.split(",");

                var expected = Integer.parseInt(configParts[0]);
                var fixed = Boolean.parseBoolean(configParts[1]);
                var currentSpace = new Space(expected,fixed);
                spaces.get(i).add(currentSpace);

            }
        }
        return spaces;
    }


}
