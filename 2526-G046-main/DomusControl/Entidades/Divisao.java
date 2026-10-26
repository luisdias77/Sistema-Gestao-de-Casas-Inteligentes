package DomusControl.Entidades;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.Serializable;

import DomusControl.Entidades.Dispositivos.Dispositivo;

public class Divisao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private Map<String, Dispositivo> dispositivos; // Usamos um Map em que a chave (String) é o Id do dispositivo e o
                                                   // valor é o Dispositivo em si

    // Construtor vazio
    public Divisao() {
        this.nome = "";
        this.dispositivos = new HashMap<>();
    }

    // Construtor com parametros
    public Divisao(String nome) {
        this.nome = nome;
        this.dispositivos = new HashMap<>();
    }

    // Construtor com cópia
    public Divisao(Divisao outraDivisao) {
        this.nome = outraDivisao.getNome();
        this.dispositivos = new HashMap<>();
        // Copiamos os dispositivos da outra divisão usando o método clone()
        for (Dispositivo d : outraDivisao.dispositivos.values()) {
            this.dispositivos.put(d.getId(), d.clone());
        }
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String, Dispositivo> getDispositivos() {
        return this.dispositivos.values().stream()
                .collect(Collectors.toMap(Dispositivo::getId, Dispositivo::clone));
    }

    public void setDispositivos(Map<String, Dispositivo> dispositivos) {
        this.dispositivos = dispositivos.values().stream()
                .collect(Collectors.toMap(Dispositivo::getId, Dispositivo::clone));
    }

    // Métodos de gestão de dispositivos

    public void addDispositivo(Dispositivo dispositivo) {
        this.dispositivos.put(dispositivo.getId(), dispositivo.clone());
    }

    public boolean existeDispositivo(String id) {
        return this.dispositivos.containsKey(id);
    }

    public Dispositivo getDispositivo(String id) {
        return this.dispositivos.get(id);
    }

    // Devolve os dispositivos verdadeiros, sem clones, (usado pelos Cenários)
    public Map<String, Dispositivo> getDispositivosReais() {
        return this.dispositivos;
    }

    // Método para calcular o consumo total da divisão, considerando apenas os
    // dispositivos que estão ligados
    public double getConsumoDivisao() {
        return this.dispositivos.values().stream()
                .filter(Dispositivo::isLigado)
                .mapToDouble(Dispositivo::getConsumoHora)
                .sum();
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
        Divisao divisao = (Divisao) o;
        return nome.equals(divisao.nome) && dispositivos.equals(divisao.dispositivos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Conta os dispositivos e cria o título com "s" no plural se necessário
        int numDisp = this.dispositivos.size();
        String plural = (numDisp != 1) ? "s" : "";
        String tituloDivisao = String.format(" Divisão: %s (%d dispositivo%s) ", this.nome.toUpperCase(), numDisp, plural);
        
        // Cria a linha com os traços dinâmicos baseados no tamanho do título
        String linhaDivisao = "-".repeat(tituloDivisao.length());
        
        sb.append(" ").append(linhaDivisao).append("\n");
        sb.append(tituloDivisao).append("\n");
        sb.append(" ").append(linhaDivisao).append("\n");
        
        // Imprime os dispositivos dessa divisão
        for (Dispositivo d : this.dispositivos.values()) {
            sb.append(" ").append(d.toString()).append("\n");
        }
        
        return sb.toString();
    }

    @Override
    public Divisao clone() {
        return new Divisao(this);
    }
}