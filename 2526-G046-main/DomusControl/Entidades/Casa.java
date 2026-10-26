package DomusControl.Entidades;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.io.Serializable;

import DomusControl.Entidades.Dispositivos.Dispositivo;

// A classe Casa representa uma casa inteligente, contendo informações sobre a casa, seus administradores, utilizadores e as divisões com os dispositivos. Serializable é necessário para que possamos gravar o estado da casa em ficheiros binários usando a classe GestorFicheiros.
public class Casa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String morada;

    // Atributos para gestão de utilizadores e administradores da casa
    private Set<String> adminsIds; // Guarda os emails de quem é Administrador nesta casa
    private Set<String> utilizadoresIds; // Guarda os emails de quem é Utilizador Normal nesta casa

    // Mapa onde a chave é o nome da divisão e o valor é o objeto Divisao
    private Map<String, Divisao> divisoes;

    // Construtor vazio
    public Casa() {
        this.nome = "";
        this.morada = "";
        this.adminsIds = new HashSet<>();
        this.utilizadoresIds = new HashSet<>();
        this.divisoes = new HashMap<>();
    }

    // Construtor com parametros
    public Casa(String nome, String morada) {
        this.nome = nome;
        this.morada = morada;
        this.adminsIds = new HashSet<>();
        this.utilizadoresIds = new HashSet<>();
        this.divisoes = new HashMap<>();
    }

    // Construtor de cópia
    public Casa(Casa umaCasa) {
        this.nome = umaCasa.getNome();
        this.morada = umaCasa.getMorada();
        this.adminsIds = new HashSet<>(umaCasa.adminsIds);
        this.utilizadoresIds = new HashSet<>(umaCasa.utilizadoresIds);
        this.divisoes = new HashMap<>();
        for (Divisao d : umaCasa.divisoes.values()) {
            this.divisoes.put(d.getNome(), d.clone());
        }
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public Set<String> getAdminsIds() {
        return new HashSet<>(this.adminsIds);
    }

    public void setAdminsIds(Set<String> adminsIds) {
        this.adminsIds = new HashSet<>(adminsIds);
    }

    public Set<String> getUtilizadoresIds() {
        return new HashSet<>(this.utilizadoresIds);
    }

    public void setUtilizadoresIds(Set<String> utilizadoresIds) {
        this.utilizadoresIds = new HashSet<>(utilizadoresIds);
    }

    public Map<String, Divisao> getDivisoes() {
        return this.divisoes.values().stream()
                .collect(Collectors.toMap(Divisao::getNome, Divisao::clone));
    }

    // Método para obter as divisões originais, sem clones
    public Map<String, Divisao> getDivisoesSemClone() {
        return this.divisoes;
    }

    public void setDivisoes(Map<String, Divisao> divisoes) {
        this.divisoes = new HashMap<>();
        for (Divisao d : divisoes.values()) {
            this.divisoes.put(d.getNome(), d.clone());
        }
    }

    // Métodos de gestão de divisões
    public void addDivisao(Divisao divisao) {
        this.divisoes.put(divisao.getNome(), divisao.clone());
    }

    public void addDispositivoDivisao(String nomeDivisao, Dispositivo dispositivo) {
        // Percorremos as chaves que existem no Map
        for (String chave : this.divisoes.keySet()) {
            // Comparamos ignorando maiúsculas/minúsculas e cortando espaços extra
            if (chave.equalsIgnoreCase(nomeDivisao.trim())) {
                this.divisoes.get(chave).addDispositivo(dispositivo);
                return; // Encontrou e instalou, não precisa de procurar mais
            }
        }
    }
    

    // Métodos de gestão de utilizadores e administradores
    public void addAdmin(String email) {
        this.adminsIds.add(email);
        this.utilizadoresIds.remove(email); // Se era utilizador normal, sobe de cargo
    }

    public void addUtilizador(String email) {
        if (!this.adminsIds.contains(email)) { // Só adiciona como utilizador normal se não for admin
            this.utilizadoresIds.add(email);
        }
    }

    public boolean isAdmin(String email) {
        return this.adminsIds.contains(email);
    }

    // Método para calcular o consumo total dos dispositivos LIGADOS
    public double getConsumoTotalCasa() {
        return this.divisoes.values().stream()
                .mapToDouble(Divisao::getConsumoDivisao)
                .sum();
    }

    // Percorre todas as divisões da casa e agrupa todos os dispositivos num único
    // mapa. Retorna um Map com todos os dispositivos da casa (Chave: ID, Valor: Objeto Dispositivo)
    public Map<String, Dispositivo> getTodosDispositivos() {
        Map<String, Dispositivo> todos = new HashMap<>();

        // Percorremos as divisões
        for (Divisao d : this.divisoes.values()) {
            // Adicionamos os dispositivos desta divisão ao mapa global
            todos.putAll(d.getDispositivosReais());
        }

        return todos;
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
        Casa casa = (Casa) o;
        return nome.equals(casa.nome) &&
                morada.equals(casa.morada) &&
                adminsIds.equals(casa.adminsIds) &&
                utilizadoresIds.equals(casa.utilizadoresIds) &&
                divisoes.equals(casa.divisoes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Cabeçalho
        String tituloCasa = String.format(" CASA: %s | MORADA: %s ", this.nome.toUpperCase(), this.morada);
        String linhaCasa = "=".repeat(tituloCasa.length());

        sb.append("\n").append(linhaCasa).append("\n");
        sb.append(tituloCasa).append("\n");
        sb.append(linhaCasa).append("\n\n");

        // Percorre as divisões 
        for (Divisao div : this.divisoes.values()) {
            sb.append(div.toString()).append("\n");
        }

        // Rodapé 
        double consumoTotalWh = this.getConsumoTotalCasa();
        double consumoKwh = consumoTotalWh / 1000.0;

        String textoConsumo = String.format(" CONSUMO TOTAL ATUAL: %.3f kWh ", consumoKwh);
        String linhaConsumo = "=".repeat(textoConsumo.length());

        sb.append(linhaConsumo).append("\n");
        sb.append(textoConsumo).append("\n");
        sb.append(linhaConsumo).append("\n");

        return sb.toString();
    }

    @Override
    public Casa clone() {
        return new Casa(this);
    }
}
