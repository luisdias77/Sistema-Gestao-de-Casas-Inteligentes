package DomusControl.Entidades;

/* A classe Utilizador representa um utilizador do sistema DomusControl.
Este utilizador pode ser um utilizador comum ou um administrador.
*/

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import DomusControl.Logica.Cenario;

public class Utilizador implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String password;

    // Usamos Map para facilitar a procura pelo nome do cenário (Ex: "Ver Cinema")
    private Map<String, Cenario> meusCenarios;

    // Construtor Vazio
    public Utilizador() {
        this.nome = "";
        this.email = "";
        this.password = "";
        this.meusCenarios = new HashMap<>();
    }

    // Construtor com os Parametros, para o registo inicial do utilizador
    public Utilizador(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.meusCenarios = new HashMap<>();
    }

    // Construtor com todos os parameteros
    public Utilizador(String nome, String email, String password, Map<String, Cenario> meusCenarios) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        // Fazemos uma cópia profunda dos cenários para manter o encapsulamento
        this.meusCenarios = meusCenarios.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    // Construtor de Cópia
    public Utilizador(Utilizador umUtilizador) {
        this.nome = umUtilizador.getNome();
        this.email = umUtilizador.getEmail();
        this.password = umUtilizador.getPassword();
        this.meusCenarios = umUtilizador.getMeusCenarios();
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Cenario> getMeusCenarios() {
        // Devolve clones para ninguém mexer nos dados originais por fora
        return this.meusCenarios.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMeusCenarios(Map<String, Cenario> meusCenarios) {
        this.meusCenarios = meusCenarios;
    }

    // Método para adicionar um cenário ao utilizador
    public void addCenario(Cenario c) {
        this.meusCenarios.put(c.getNome(), c.clone());
    }

    // Procura um cenario ignorando maiusculas, minusculas e espaços
    public Cenario procurarCenarioSeguro(String nomeCenarioPesquisa) {
        for (Cenario c : this.meusCenarios.values()) {
            // Limpa espaços dos dois lados e ignora as maiúsculas para comparar
            if (c.getNome().trim().equalsIgnoreCase(nomeCenarioPesquisa.trim())) {
                return c; 
            }
        }
        return null;
    }

    // Método para eliminar um cenário do utilizador
    public boolean removerCenario(String nomeCenarioPesquisa) {
        Cenario c = procurarCenarioSeguro(nomeCenarioPesquisa);
        if (c != null) {
            this.meusCenarios.remove(c.getNome()); // Remove do Map usando a chave correta
            return true; 
        }
        return false; 
    }

    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */

    // Método equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Se os objetos são o mesmo, são iguais
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Se o objeto é null ou de uma classe diferente, não são iguais
        }
        Utilizador ut = (Utilizador) obj;
        return this.nome.equals(ut.getNome()) &&
                this.email.equals(ut.getEmail()) &&
                this.password.equals(ut.getPassword()) &&
                this.meusCenarios.equals(ut.getMeusCenarios());

    }

    // Método toString
    @Override
    public String toString() {
        return "Utilizador: nome=" + nome + ", email=" + email + ", meusCenarios=" + meusCenarios; // Sem password, pois
                                                                                                   // não é algo a
                                                                                                   // mostrar
    }

    // Método clone
    @Override
    public Utilizador clone() {
        return new Utilizador(this); // Retorna um novo objeto Utilizador, utilizando o construtor de cópia
    }

}