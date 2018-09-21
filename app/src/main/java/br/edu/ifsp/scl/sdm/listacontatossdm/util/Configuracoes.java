package br.edu.ifsp.scl.sdm.listacontatossdm.util;

public class Configuracoes {
    public static final int ARMAZENAMENTO_INTERNO = 0;
    public static final int ARMAZENAMENTO_EXTERNO = 1; //android >= 23 precisa de permissão em tempo de execução
    public static final int BANCO_DE_DADOS        = 2;

    private int tipoArmazenamento;

    private static final Configuracoes ourInstance = new Configuracoes();

    public static Configuracoes getInstance() {
        return ourInstance;
    }


    private Configuracoes() {
        //
        tipoArmazenamento = ARMAZENAMENTO_INTERNO;

    }

    public int getTipoArmazenamento() {
        return tipoArmazenamento;
    }

    public void setTipoArmazenamento(int tipoArmazenamento) {
        this.tipoArmazenamento = tipoArmazenamento;
    }


}
