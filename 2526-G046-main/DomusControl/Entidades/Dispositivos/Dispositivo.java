package DomusControl.Entidades.Dispositivos;

import java.util.Objects;
import java.io.Serializable;

public abstract class Dispositivo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // Identificador único do dispositivo
    private String marca; // Marca do dispositivo
    private String modelo; // Modelo do dispositivo
    private double consumoHora; // Consumo em Wh
    private boolean ligado; // Estado: true para ligado, false para desligado
    private int numeroAtivacoes; // Número de vezes que o dispositivo foi ligado
    private double tempoTotalUso; // Tempo total de uso em horas

    // Construtor vazio
    public Dispositivo() {
        this.id = "";
        this.marca = "";
        this.modelo = "";
        this.consumoHora = 0.0;
        this.ligado = false;
        this.numeroAtivacoes = 0;
        this.tempoTotalUso = 0.0;
    }

    // Construtor com parâmetros
    public Dispositivo(String id, String marca, String modelo, double consumoHora, boolean ligado) { // O número de ativações e o tempo total de uso começam em 0
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.consumoHora = consumoHora;
        this.ligado = ligado;
        this.numeroAtivacoes = 0;
        this.tempoTotalUso = 0;
    }

    // Construtor de cópia
    public Dispositivo(Dispositivo umDispositivo) {
        this.id = umDispositivo.getId();
        this.marca = umDispositivo.getMarca();
        this.modelo = umDispositivo.getModelo();
        this.consumoHora = umDispositivo.getConsumoHora();
        this.ligado = umDispositivo.isLigado();
        this.numeroAtivacoes = umDispositivo.getNumeroAtivacoes();
        this.tempoTotalUso = umDispositivo.getTempoTotalUso();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getConsumoHora() {
        return consumoHora;
    }

    public void setConsumoHora(double consumoHora) {
        this.consumoHora = consumoHora;
    }

    public boolean isLigado() {
        return ligado;
    }

    public void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    public int getNumeroAtivacoes() {
        return numeroAtivacoes;
    }

    public void setNumeroAtivacoes(int numeroAtivacoes) {
        this.numeroAtivacoes = numeroAtivacoes;
    }

    public double getTempoTotalUso() {
        return tempoTotalUso;
    }

    public void setTempoTotalUso(double tempoTotalUso) {
        this.tempoTotalUso = tempoTotalUso;
    }

    // --- Métodos comuns a todos os dispositivos ----

    // Método para ligar o dispositivo
    public void ligar() {
        this.ligado = true;
        this.registarAtivacao(); // Incrementa o número de ativações ao ligar
    }

    // Método para desligar o dispositivo
    public void desligar() {
        this.ligado = false;
    }

    // Método para registrar uma ativação do dispositivo (quando é ligado),
    // incrementando o número de ativações
    public void registarAtivacao() {
        this.numeroAtivacoes++;
    }

    // Metodo para comparar dois numeros de ativaçoes
    public int comparatorNumAtivacoes(Dispositivo d1, Dispositivo d2) {
        int nrAtivaçoes1 = d1.getNumeroAtivacoes();
        int nrAtivaçoes2 = d2.getNumeroAtivacoes();

        if (nrAtivaçoes1 == nrAtivaçoes2)
            return 0; // numeros de ativaçoes IGUAIS RETORNA 0
        if (nrAtivaçoes1 > nrAtivaçoes2)
            return 1; // maior numero de ativaçoes vem PRIMEIRO RETORNA 1
        return -1; // menor numero de ativaçoes vem DEPOIS RETORNA -1

    }

    // Metodo para comparar dois tempos de uso
    public int comparatorTempoUso(Dispositivo d1, Dispositivo d2) {
        double tempoUso1 = d1.getTempoTotalUso();
        double tempoUso2 = d2.getTempoTotalUso();

        if (tempoUso1 == tempoUso2)
            return 0; // tempos de uso IGUAIS RETORNA 0
        if (tempoUso1 > tempoUso2)
            return 1; // maior tempo de uso vem PRIMEIRO RETORNA 1
        return -1; // menor tempo de uso vem DEPOIS RETORNA -1

    }

    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */ 

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Dispositivo that = (Dispositivo) o;
        return Double.compare(that.consumoHora, consumoHora) == 0 && ligado == that.ligado
                && numeroAtivacoes == that.numeroAtivacoes && Double.compare(that.tempoTotalUso, tempoTotalUso) == 0
                && Objects.equals(id, that.id) && Objects.equals(marca, that.marca)
                && Objects.equals(modelo, that.modelo);
    }

    @Override
    public String toString() {

        String estado = this.ligado ? "ON " : "OFF";

        // Criamos a string do tipo com parênteses, ex: "[Lampada]"
        // getClass().getSimpleName() vai buscar automaticamente "Lampada", "Rele", etc.
        String tipoComParenteses = "[" + this.getClass().getSimpleName() + "]";

        // %-18s  -> Reserva 18 espaços para o Tipo (alinhado à esquerda)
        // %-6s  -> Reserva 6 espaços para o ID
        // %3s    -> Reserva 3 espaços para o Estado
        // %5.0f  -> Reserva 5 espaços para o Consumo (sem decimais)
        return String.format(" %-18s ID: %-6s | Estado: %s | Consumo: %.0fWh", 
                            tipoComParenteses, this.id, estado, this.consumoHora);
    }

    @Override
    public abstract Dispositivo clone(); // Cada subclasse de Dispositivo deve implementar o seu próprio método clone

}
