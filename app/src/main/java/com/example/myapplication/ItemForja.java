package com.example.myapplication;

public class ItemForja {

    private String nome;
    private int custo, tempo, valorVenda;

    public ItemForja(String nome, int custo, int tempo, int valorVenda) {
        this.nome = nome;
        this.custo = custo;
        this.tempo = tempo;
        this.valorVenda = valorVenda;
    }

    public String getNome() {
        return nome;
    }

    public int getCusto() {
        return custo;
    }

    public int getTempo() {
        return tempo;
    }

    public int getValorVenda() {
        return valorVenda;
    }

    @Override
    public String toString() {
        return nome + " | " + custo + "g | " + tempo + "s";
    }
}
