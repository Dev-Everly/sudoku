package util;

import model.Space;
import model.Tabuleiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static model.TabuTemplate.BOARD_TEMPLATE;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    static Tabuleiro tabuleiro;

    public final static  int TABULEIRO_LIMIT = 9 ;

    public static void main(String[] args) {
        final Map<String, String> positions = Stream.of(args)
                .filter(arg -> arg.contains(";")) // Filtra strings que contêm ";"
                .collect(Collectors.toMap(
                        k -> k.split(";")[0], // Chave: primeira parte da string
                        v -> v.split(";")[1] // Valor: segunda parte da string
                ));

        var option = - 1;

        while (true) {
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();


            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }
        }
    }

    private static void finishGame() {
        if(isNull(tabuleiro)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        if(tabuleiro.gameFinish()){
            System.out.println("Parabéns você conseguiu concluir ");
            showCurrentGame();
            tabuleiro = null;
        } else if (tabuleiro.hasErrors()) {
            System.out.println("Seu jogo contém erros , verifique o tabuleiro");
        } else {
            System.out.println("Você ainda não completou o jogo, tem espaço vazio");
        }
    }

    private static void clearGame() {
        if(isNull(tabuleiro)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Deseja continuar ? Caso sim perdera o progresso do jogo");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
            System.out.println("Por favor digite sim ou não");
            confirm = scanner.next();
        }

        if(confirm.equalsIgnoreCase("sim")){
            tabuleiro.reset();
        }
    }

    private static void showGameStatus() {
        if(isNull(tabuleiro)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.printf("Status de jogo %s\n",tabuleiro.getStatus().getLabel() );
        if(tabuleiro.hasErrors()){
            System.out.println("O jogo contém erros");
        }else{
            System.out.println("O jogo não contém erros");
        }
    }

    private static void showCurrentGame() {

        if(isNull(tabuleiro)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        var args = new Object[81];
        var argsPos = 0;


        for(int i = 0; i <TABULEIRO_LIMIT; i++) {
            for(var col : tabuleiro.getSpaces()){
                args[argsPos ++] = " " + (isNull(col.get(i).getAtual()) ? " " : col.get(i).getAtual());
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "\n",   args);
    }


    private static void removeNumber() {
        if (isNull(tabuleiro)){
            System.out.println("O jogo ainda não foi iniciado iniciado");
            return;
        }

        System.out.println("Informe a coluna que em que o número será inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o número será inserido");
        var row = runUntilGetValidNumber(0, 8);
        if (!tabuleiro.clearValue(col, row)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }
    }

    private static void inputNumber() {
        if(isNull(tabuleiro)) {
            System.out.println("O jogo não foi iniciado");
            return;

        }

        System.out.println("Insira a coluna que o número será inserido: ");
        int col = runUntilGetValidNumber(0,8);
        System.out.println("Insira a linha que o número será inserido: ");
        int row = runUntilGetValidNumber(0,8);
        System.out.printf("Informe o número que vai entrar na posição %s %s \n", col,row);
        int value = runUntilGetValidNumber(1,9);

        if(!tabuleiro.mudarValor(col, row, value)) {
            System.out.printf("Posição %s %s tem um valor fixo\n", col,row);
        }

    }

    private static void startGame(Map<String, String> positions) {
        if(nonNull(tabuleiro)) {
            System.out.println("O jogo já foi iniciado");
            return;

        }




        List<List<Space>> spaces = new ArrayList<>();
        for(int i = 0 ; i < TABULEIRO_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0 ; j < TABULEIRO_LIMIT; j++) {

                var positionConfig = positions.get("%s%s".formatted(i,j));

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
        tabuleiro = new Tabuleiro(spaces);
        System.out.println("O jogo vai começar ");

    }

    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }
}
