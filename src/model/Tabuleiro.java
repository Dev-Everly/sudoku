package model;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Tabuleiro {


    private final List<List<Space>> spaces;

    public Tabuleiro(List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus() {
        if (spaces.stream()
                .flatMap(Collection::stream)
                .noneMatch(s -> !s.isFixo() && nonNull(s.getAtual()))) {
            return GameStatusEnum.NON_STARTED;

        }
        return spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> isNull(s.getAtual())) ? GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETE;

    }
        public boolean hasErrors() {
            if(getStatus() == GameStatusEnum.NON_STARTED) {
                return  false;
            }
            return  spaces.stream()
                    .flatMap(Collection::stream)
                    .anyMatch(s -> nonNull(s.getAtual()) && s.getAtual().equals(s.getEsperado()));
        }

        public boolean mudarValor(final int col, final int row,Integer value) {
           var space = spaces.get(col).get(row);

           if (space.isFixo()) {
               return false;
           }
           space.setAtual(value);
           return  true;
        }

    public boolean clearValue(final int col, final int row) {
        Space space = spaces.get(col).get(row);
        if(space.isFixo()) {
             return false;

         }
         space.limparSpace();
        return  true;
    }

    public void reset() {
        spaces.forEach(c -> c.forEach(Space::limparSpace));
    }

    public boolean gameFinish() {
        return !hasErrors() && getStatus() == GameStatusEnum.COMPLETE;
    }

}