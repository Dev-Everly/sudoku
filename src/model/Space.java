package model;

public class Space {

    //atributos
    private Integer atual;
    private final int esperado;
    private final boolean fixo;

    public Space(int esperado, boolean fixo) {
        this.esperado = esperado;
        this.fixo = fixo;

        if(fixo) {
            atual = esperado;
        }
    }

    public Integer getAtual() {
        return atual;
    }

    public void setAtual(Integer atual) {
        this.atual = atual;
        if(fixo)  return;


    }

    // limpar space
    public void limparSpace() {
        setAtual(null);
    }

    public int getEsperado() {
        return esperado;
    }

    public boolean isFixo() {
        return fixo;
    }
}
