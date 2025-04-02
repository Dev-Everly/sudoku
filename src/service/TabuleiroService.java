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
        return this.tabuleiro.getSpaces();
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

    private List<List<Space>> initTabuleiro(final Map<String, String> gameConfig) {
        List<List<Space>> spaces = new ArrayList<>();

        // Inicializa a matriz 9x9
        for(int i = 0; i < TABULEIRO_LIMIT; i++) {
            List<Space> linha = new ArrayList<>();
            for (int j = 0; j < TABULEIRO_LIMIT; j++) {
                linha.add(new Space(0, false)); // Células vazias por padrão
            }
            spaces.add(linha);
        }

        // Preenche com as configurações fornecidas
        for (Map.Entry<String, String> entry : gameConfig.entrySet()) {
            String[] coords = entry.getKey().split(",");
            if (coords.length != 2) {
                throw new IllegalArgumentException("Formato de coordenadas inválido: " + entry.getKey());
            }

            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            String[] valores = entry.getValue().split(",");
            if (valores.length != 2) {
                throw new IllegalArgumentException("Formato de valores inválido: " + entry.getValue());
            }

            Integer valor = valores[0].equals("null") ? null : Integer.parseInt(valores[0]);
            boolean fixo = Boolean.parseBoolean(valores[1]);

            spaces.get(x).set(y, new Space(valor, fixo));
        }

        return spaces;
    }


}
