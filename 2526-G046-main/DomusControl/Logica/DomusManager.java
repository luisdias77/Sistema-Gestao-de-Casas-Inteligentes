package DomusControl.Logica;

import DomusControl.Entidades.*;
import DomusControl.Entidades.Dispositivos.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.*;
import java.time.LocalTime;

public class DomusManager implements Serializable {

    // Esta constante serve para garantir que a versão do ficheiro é a mesma
    // quando o programa é atualizado, caso contrário, o programa não consegue
    // carregar o ficheiro, pois a versão do ficheiro não coincide com a versão do
    // programa, pois o ID é alterado. Assim, sempre que o programa é atualizado,
    // é necessário alterar esta constante.

    private static final long serialVersionUID = 1L;

    // chave: email do utilizador, valor: objeto utilizador
    private Map<String, Utilizador> utilizadores;
    // chave: nome da casa, valor: objeto casa
    private Map<String, Casa> casas;
    // Atributo para guardar o utilizador que está logado no sistema, para facilitar
    // a gestão de permissões e ações relacionadas ao utilizador
    private Utilizador utilizadorLogado;
    // Atributo para guardar a casa que está a ser gerida no momento, para facilitar
    // a gestão de dispositivos, automações e cenários relacionados à casa
    private Casa casaAtual;
    // Lista de automações do sistema
    private List<Automacao> automacoes = new ArrayList<>();
    // Lista de escalonamentos do sistema
    private List<Escalonamento> escalonamentos = new ArrayList<>();

    // Construtor
    public DomusManager() {
        this.utilizadores = new HashMap<>();
        this.casas = new HashMap<>();
        this.utilizadorLogado = null;
        this.casaAtual = null;
        this.automacoes = new ArrayList<>();
        this.escalonamentos = new ArrayList<>();
    }

    public DomusManager(Map<String, Utilizador> utilizadores, Map<String, Casa> casas, Utilizador utilizadorLogado,
            Casa casaAtual, List<Automacao> automacoes, List<Escalonamento> escalonamentos) {
        this.utilizadores = new HashMap<>(utilizadores);
        this.casas = new HashMap<>(casas);
        this.utilizadorLogado = utilizadorLogado;
        this.casaAtual = casaAtual;
        this.automacoes = new ArrayList<>(automacoes);
        this.escalonamentos = new ArrayList<>(escalonamentos);
    }

    public DomusManager(DomusManager umDomusManager) {
        this.utilizadores = new HashMap<>(umDomusManager.utilizadores);
        this.casas = new HashMap<>(umDomusManager.casas);
        this.utilizadorLogado = umDomusManager.utilizadorLogado;
        this.casaAtual = umDomusManager.casaAtual;
        this.automacoes = new ArrayList<>(umDomusManager.automacoes);
        this.escalonamentos = new ArrayList<>(umDomusManager.escalonamentos);
    }

    // Get lista de utilizadores
    public Map<String, Utilizador> getUtilizadores() {
        return this.utilizadores;
    }

    // Get lista de casas
    public Map<String, Casa> getCasas() {
        return this.casas;
    }

    // Get utilizador logado
    public Utilizador getUtilizadorLogado() {
        return this.utilizadorLogado;
    }

    // Get casa atual
    public Casa getCasaAtual() {
        return this.casaAtual;
    }

    // Get lista de automações
    public List<Automacao> getAutomacoes() {
        return this.automacoes;
    }

    // Get lista de escalonamentos
    public List<Escalonamento> getEscalonamentos() {
        return this.escalonamentos;
    }

    // Set lista de utilizadores
    public void setUtilizadores(Map<String, Utilizador> utilizadores) {
        this.utilizadores = new HashMap<>(utilizadores);
    }

    // Set lista de casas
    public void setCasas(Map<String, Casa> casas) {
        this.casas = new HashMap<>(casas);
    }

    // Set utilizador logado
    public void setUtilizadorLogado(Utilizador utilizadorLogado) {
        this.utilizadorLogado = utilizadorLogado;
    }

    // Set casa atual
    public void setCasaAtual(Casa casaAtual) {
        this.casaAtual = casaAtual;
    }

    // Set lista de automações
    public void setAutomacoes(List<Automacao> automacoes) {
        this.automacoes = new ArrayList<>(automacoes);
    }

    // Set lista de escalonamentos
    public void setEscalonamentos(List<Escalonamento> escalonamentos) {
        this.escalonamentos = new ArrayList<>(escalonamentos);
    }

    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DomusManager domusManager = (DomusManager) o;
        return this.utilizadores.equals(domusManager.utilizadores) &&
                this.casas.equals(domusManager.casas) &&
                this.utilizadorLogado.equals(domusManager.utilizadorLogado) &&
                this.casaAtual.equals(domusManager.casaAtual) &&
                this.automacoes.equals(domusManager.automacoes) &&
                this.escalonamentos.equals(domusManager.escalonamentos);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Estado do DomusManager ---\n");
        sb.append("Utilizadores: ").append(utilizadores.size()).append("\n");
        sb.append("Casas: ").append(casas.size()).append("\n");
        sb.append("Automações Ativas: ").append(automacoes.size()).append("\n");
        sb.append("Escalonamentos Agendados: ").append(escalonamentos.size()).append("\n");
        sb.append("----------------------------");
        return sb.toString();
    }

    public DomusManager clone() {
        return new DomusManager(this);
    }

    /* ------------------------ */
    /* -- Métodos De Gestão --- */
    /* ------------------------ */

    // Adiciona um utilizador
    public void addUtilizador(Utilizador u) {
        this.utilizadores.put(u.getEmail(), u.clone());
    }

    // Verifica se um utilizador existe
    public boolean existUtilizador(String email) {
        return this.utilizadores.containsKey(email);
    }

    // Regista um utilizador, verificando se já existe um utilizador com o mesmo
    // email
    public boolean registarUtilizador(String nome, String email, String password) {
        if (this.existUtilizador(email)) {
            return false;
        }
        Utilizador u = new Utilizador(nome, email, password);
        this.addUtilizador(u);
        return true;
    }

    // Adiciona uma casa
    public void addCasa(Casa c) {
        this.casas.put(c.getNome(), c.clone());
    }

    // Verifica se uma casa existe, recebendo como parametro o nome da casa
    public boolean existCasa(String nomeCasa) {
        return this.casas.containsKey(nomeCasa);
    }

    // Verifica se uma casa existe, recebendo como parametro um objeto Casa
    public boolean existCasa2(Casa nomeCasa) {
        return this.casas.containsKey(nomeCasa.getNome());
    }

    /*
     * // Registar uma casa, verificando se já existe uma casa com o mesmo nome e se
     * existe um utilizador com o email dado
     * public boolean registarCasa(String nome, String morada, String
     * emailUtilizador) {
     * if (this.existCasa(nome)) {
     * return false; // já existe uma casa com este nome
     * }
     * if (!this.existUtilizador(emailUtilizador)) {
     * return false; // não existe um utilizador com este email
     * }
     * 
     * Casa c = new Casa(nome, morada, emailUtilizador);
     * this.addCasa(c);
     * return true;
     * }
     */

    public boolean registarCasa(String nome, String morada, String emailDono) {
        // Verifica se a casa já existe
        if (this.casas.containsKey(nome)) {
            return false;
        }

        // Cria a nova casa
        Casa novaCasa = new Casa(nome, morada);

        // Define quem é o Administrador
        novaCasa.addAdmin(emailDono);

        // Guarda a casa no sistema
        this.casas.put(nome, novaCasa);

        return true;
    }

    // Registar uma divisão, verificando se já existe uma divisão com o mesmo nome
    // na casa dada e se a casa existe
    public boolean registarDivisão(String nomeCasa, String nomeDivisão) {
        // verificar se a casa existe
        if (!this.existCasa(nomeCasa)) {
            return false; // já existe uma casa com este nome
        }

        // obter a casa
        Casa casa = this.casas.get(nomeCasa);
        // criar a divisão
        Divisao d = new Divisao(nomeDivisão);
        // adicionar a divisão à casa
        casa.addDivisao(d);
        // atualizar a casa no map
        this.casas.put(nomeCasa, casa);
        return true;
    }

    // Obtem os dispositivos de uma casa, percorrendo as divisões dessa casa e
    // guardando os dispositivos numa lista para depois retornar essa lista
    public List<Dispositivo> getDispositivosFromCasa(Casa nomeCasa) {
        List<Dispositivo> list = new ArrayList<>();

        Casa casa = this.casas.get(nomeCasa.getNome());

        if (casa != null) {
            // Percorre as divisões dessa casa e guarda os dispositivos
            for (Divisao d : casa.getDivisoes().values()) {
                list.addAll(d.getDispositivos().values());
            }
        }
        return list;
    }

    // Método para fazer "login"
    public boolean login(String email) {
        if (this.utilizadores.containsKey(email)) {
            this.utilizadorLogado = this.utilizadores.get(email);
            return true;
        }
        return false;
    }

    public void logout() {
        this.utilizadorLogado = null;
    }

    // MÉtodo para selecionar a casa a ser gerida no momento
    public void selecionarCasa(String nomeCasa) {
        if (this.casas.containsKey(nomeCasa)) {
            this.casaAtual = this.casas.get(nomeCasa);
        }
    }

    // Método que retorna apenas as casas a que o user logado tem acesso
    public List<Casa> getCasasAcessiveis() {
        if (this.utilizadorLogado == null) {
            return new ArrayList<>(); // Nenhuma casa acessível se não houver um utilizador logado
        }

        String email = this.utilizadorLogado.getEmail();

        return this.casas.values().stream()
                .filter(casa -> casa.isAdmin(email) || casa.getUtilizadoresIds().contains(email))
                .collect(Collectors.toList());
    }

    // Método para verificar se o utilizador logado tem permissoes de administrador
    // numa casa
    public boolean isUserAdminDaCasaAtual() {
        if (this.casaAtual == null || this.utilizadorLogado == null) {
            return false;
        }
        return this.casaAtual.isAdmin(this.utilizadorLogado.getEmail());
    }

    // Método para sair da casa atual
    public void sairDaCasa() {
        this.casaAtual = null;
    }

    // Método para adicionar um morador à casa atual, verificando se o email do
    // morador existe no sistema e se há uma casa selecionada
    public boolean adicionarMoradorCasaAtual(String emailMorador) {
        // Verifica se a pessoa existe no sistema
        if (!this.utilizadores.containsKey(emailMorador)) {
            return false;
        }

        if (this.casaAtual != null) {
            // Vai buscar a casa real, sem clone (Map)
            Casa casaReal = this.casas.get(this.casaAtual.getNome());

            // Adiciona o morador à casa
            if (casaReal != null) {
                casaReal.addUtilizador(emailMorador);
            }

            // Adiciona também à cópia que está a ser usada agora
            this.casaAtual.addUtilizador(emailMorador);

            return true;
        }
        return false;
    }

    /* ------------------------ */
    /* - Métodos Estatísticos - */
    /* ------------------------ */

    // Saber qual é a casa que mais consome
    public String casaMaisConsome() {
        if (casas.isEmpty())
            return "Nenhuma casa registada";

        String nomeCasaMaisConsome = "";
        double maxConsumo = -1;

        for (Casa c : casas.values()) {
            double consumoCasa = c.getConsumoTotalCasa();
            if (consumoCasa > maxConsumo) {
                maxConsumo = consumoCasa;
                nomeCasaMaisConsome = c.getNome();
            }
        }

        // Converte de Wh para kWh
        double maxConsumoKwh = maxConsumo / 1000.0;

        return String.format("A casa que mais consome é: %s com um consumo de %.3f kWh", nomeCasaMaisConsome, maxConsumoKwh);
    }

    // Saber quais são os 3 dispositivos que mais foram ativados
    public List<Dispositivo> tresDispMaisAtivados(Casa nomeCasa) {

        if (this.existCasa2(nomeCasa)) {
            List<Dispositivo> list = new ArrayList<>(getDispositivosFromCasa(nomeCasa));
            if (list.isEmpty()) {
                return null;
            }

            // ordenar a lista de dispositivos de forma decrescenete pelo numero de
            // ativações
            list.sort((d1, d2) -> d1.comparatorNumAtivacoes(d1, d2)); // formato crescente
            Collections.reverse(list); // inverte para formato decrescente

            List<Dispositivo> top3 = new ArrayList<>();

            for (int i = 0; i < 3 && i < list.size(); i++) {
                top3.add(list.get(i));
            }
            return top3;
        }
        return null;
    }

    // Saber quais são os 3 dispositivos com mais tempo de uso
    public List<Dispositivo> tresDispMaisTempoUso(Casa nomeCasa) {

        if (this.existCasa2(nomeCasa)) {
            List<Dispositivo> list = new ArrayList<>(getDispositivosFromCasa(nomeCasa));

            // ordenar a lista de dispositivos de forma decrescente pelo tempo de uso
            list.sort((d1, d2) -> d1.comparatorTempoUso(d1, d2)); // formato crescente
            Collections.reverse(list); // inverte para formato decrescente

            List<Dispositivo> top3 = new ArrayList<>();

            for (int i = 0; i < 3 && i < list.size(); i++) {
                top3.add(list.get(i));
            }
            return top3;
        }
        return null;
    }

    // As 3 divisões com mais dispositivos e respetiva casa
    public List<String> tresDivMaisDispositivos() {

        // chave: identificação em texto casa divisão e o valor: numero de dispositivos
        Map<String, Integer> mapDivisoes = new HashMap<>();

        for (Casa casa : this.casas.values()) {
            for (Divisao divisao : casa.getDivisoes().values()) {
                String identificador = "Casa: " + casa.getNome() + "Divisão: " + divisao.getNome();
                int qtdDisp = divisao.getDispositivos().size();
                // guardar este par em mapDivisoes
                mapDivisoes.put(identificador, qtdDisp);
            }
        }

        // Variáveis para guardar os tres dispositivos (Chave e Valor)
        Map.Entry<String, Integer> primeiro = null;
        Map.Entry<String, Integer> segundo = null;
        Map.Entry<String, Integer> terceiro = null;

        // Percorrer a hashmap mapaDivisoes
        for (Map.Entry<String, Integer> entrada : mapDivisoes.entrySet()) {
            int total = entrada.getValue();

            // Se é maior que o 1º, todos descem um lugar
            if (primeiro == null || total > primeiro.getValue()) {
                terceiro = segundo;
                segundo = primeiro;
                primeiro = entrada;
            }
            // Se é menor que o 1º, mas é maior que o 2º, o 2º desce para 3º
            else if (segundo == null || total > segundo.getValue()) {
                terceiro = segundo;
                segundo = entrada;
            }
            // Se é menor que o 2º, mas é maior que o 3º, substitui o 3º
            else if (terceiro == null || total > terceiro.getValue()) {
                terceiro = entrada;
            }
        }

        // Construir o resultado final
        List<String> resultado = new ArrayList<>();

        if (primeiro != null)
            resultado.add("1º Lugar: " + primeiro.getKey() + " -> " + primeiro.getValue() + " dispositivos.");
        if (segundo != null)
            resultado.add("2º Lugar: " + segundo.getKey() + " -> " + segundo.getValue() + " dispositivos.");
        if (terceiro != null)
            resultado.add("3º Lugar: " + terceiro.getKey() + " -> " + terceiro.getValue() + " dispositivos.");

        if (resultado.isEmpty()) {
            resultado.add("Ainda não existem divisões registadas.");
        }
        return resultado;
    }


    /* ------------------------ */
    /* - Métodos de Automação - */
    /* ------------------------ */

    // Método para adicionar uma automação
    public void adicionarAutomacao(Automacao a) {
        this.automacoes.add(a.clone());
    }

    // Método que remove uma automação
    public void removerAutomacao(int index) {
        if (index >= 0 && index < this.automacoes.size()) {
            this.automacoes.remove(index);
        }
    }
    // Método para verificar se uma automação existe
    public boolean existeAutomacao(Automacao a) {
        return this.automacoes.contains(a.clone());
    }

    // Método para processar as automações
    public void processarAutomacoes() {

        for (int i = 0; i < this.automacoes.size(); i++) {

            // Procuramos a automação na posição i
            Automacao a = this.automacoes.get(i);

            // Executamos a automação
            a.executarAutomacao();
        }
    }

    // Método que procura um dispositivo numa casa, percorrendo as divisões
    // existentes
    public Dispositivo procurarDispositivo(String nomeCasa, String idDispositivo) {
        Casa c = this.casas.get(nomeCasa);
        if (c == null) {
            System.out.println("Casa '" + nomeCasa + "' não existe no Map");
            return null;
        }

        for (Divisao d : c.getDivisoesSemClone().values()) {
            Dispositivo encontrado = d.getDispositivo(idDispositivo);
            if (encontrado != null)
                return encontrado;
        }

        System.out.println("Dispositivo '" + idDispositivo + "' não encontrado nas divisões");
        return null;
    }

    // Método para adicionar um dispositivo a uma divisão de uma casa
    public boolean addDispositivoDivisao(String nomeCasa, String nomeDivisao, Dispositivo d) {
        // Vai buscar a casa correta
        Casa c = this.casas.get(nomeCasa); 
        
        if (c != null) {
            // Procura nas divisões da casa o nome real ignorando as maiúsculas
            for (String nomeRealDivisao : c.getDivisoes().keySet()) {
                if (nomeRealDivisao.equalsIgnoreCase(nomeDivisao.trim())) {
                    
                    c.addDispositivoDivisao(nomeRealDivisao, d); 
                    
                    return true; 
                }
            }
        }
        return false;
    }

    /* ---------------------------- */
    /* - Métodos De Escalonamento - */
    /* ---------------------------- */

    // Método para adicionar um escalonamento
    public void adicionarEscalonamento(String desc, String hora, String idDispositivo, String casa, String acao) {

        Dispositivo disp = procurarDispositivo(casa, idDispositivo);

        if (disp != null) {

            Escalonamento e = new Escalonamento(desc, LocalTime.parse(hora), disp, acao);
            this.escalonamentos.add(e);
        }
    }

    // Método para processar os escalonamentos, que recebe a hora dada pelo
    // utilizador, e compara essa hora com a hora
    // a que tem de executar o escalonamento e verifica se já é para executar ou
    // não.
    public void processarEscalonamentos(String horaDadaUtilizador) {

        LocalTime atual = LocalTime.parse(horaDadaUtilizador);

        for (int i = 0; i < this.escalonamentos.size(); i++) {

            Escalonamento e = this.escalonamentos.get(i);
            e.executarEscalonamento(atual);
        }
    }

    // Método para processar os escalonamentos, que recebe a hora atual do sistema
    public void processarEscalonamentosHoraSistema() {

        LocalTime agora = LocalTime.now();

        System.out.println("A verificar se existem escalonamentos para a hora atual: " + agora.getHour() + ":"
                + agora.getMinute());

        for (int i = 0; i < this.escalonamentos.size(); i++) {

            Escalonamento e = this.escalonamentos.get(i);
            e.executarEscalonamento(agora);
        }
    }

    // Remove um escalonento
    public void removerEscalonamento(int index) {
        if (index >= 0 && index < this.escalonamentos.size()) {
            this.escalonamentos.remove(index);
        }
    }



    /* ---------------------------- */
    /* --- Métodos De Cenários ---- */
    /* ---------------------------- */

    // Método para ativar um cenário, recebendo o email do utilizador, o nome do
    // cenário e o nome da casa onde o cenário se vai aplicar
    public boolean ativarCenarioUtilizador(String emailUser, String nomeCenario, String nomeCasa) {
        Utilizador u = this.utilizadores.get(emailUser);
        Casa c = this.casas.get(nomeCasa);

        if (u != null && c != null) {
            // Vamos buscar o cenário ao utilizador
            Cenario cenario = u.procurarCenarioSeguro(nomeCenario);

            if (cenario != null) {
                // Pedimos à casa todos os seus dispositivos
                Map<String, Dispositivo> dispositivosDaCasa = c.getTodosDispositivos();

                // 3. Mandamos o cenário aplicar-se a esses dispositivos
                cenario.aplicarCenario(dispositivosDaCasa);

                // Processamos as automações logo a seguir para o caso de
                // ligar um dispositivo ter ativado uma regra
                this.processarAutomacoes();

                return true;
            }
        }
        return false;
    }

    // Gravar e carregar a instância de DomusManager num (e de um) ficheiro de
    // objectos.

    // Método para gravar o estado da aplicação em um ficheiro
    public void grava(String nomeFicheiro) throws IOException {
        FileOutputStream fos = new FileOutputStream(nomeFicheiro);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this); // Grava a própria instância (this)
        oos.flush();
        oos.close();
    }

    // Método para carregar o estado da aplicação de um ficheiro
    public static DomusManager carrega(String nomeFicheiro) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(nomeFicheiro);
        ObjectInputStream ois = new ObjectInputStream(fis);
        DomusManager dm = (DomusManager) ois.readObject();
        ois.close();
        return dm;
    }

    /* ========================================================== */
    // SUGESTÃO DE ESCALONAMENTOS
    /* ========================================================== */

    // Basicamente, é sugerido ao utilizador um escalonamento, quando o sistema
    // deteta que o utilizador fez uma ação num dispositivo pelo menos três vezes à
    // mesma hora em diferentes dias.

    // Aqui guardamos o histórico de ações dos dispositivos
    private Map<String, Integer> historico = new HashMap<>();

    public void registarNoHistorico(String id, String acao) {

        int hora = java.time.LocalTime.now().getHour();

        // aqui criamos uma chave com o id do dispositivo, a ação e a hora(exemplo:
        // lampada_l1:ligar:17)
        String chave = id + ":" + acao + ":" + hora;

        // vai buscar o valor atual da chave e soma 1
        int contagem = historico.getOrDefault(chave, 0);
        historico.put(chave, contagem + 1);
    }

    public void verificarSugestoes(Scanner sc) {

        for (String chave : historico.keySet()) {
            int vezes = historico.get(chave);

            // Se aconteceu 3 ou mais vezes, assume que é algo habitual, que é feito
            // constantemente
            if (vezes >= 3) {
                String[] dados = chave.split(":");
                String id = dados[0];
                String acao = dados[1];
                String hora = dados[2] + ":00";

                System.out.println("\n💡 SUGESTÃO INTELIGENTE:");
                System.out.println(
                        "O sistema reparou que costuma " + acao + " o dispositivo [" + id + "] por volta das " + hora
                                + ".");
                System.out.print("Quer criar um agendamento automático? (sim/não): ");

                String res = sc.nextLine();

                if (res.equalsIgnoreCase("sim")) {
                    // usa a função adicionarEscalonamento
                    this.adicionarEscalonamento("Sugestão Automática", hora, id, this.casaAtual.getNome(), acao);

                    // Limpamos o histórico desta chave para não sugerir a mesma coisa repetidamente
                    historico.put(chave, 0);
                    System.out.println("✔ Agendamento criado!");
                }
            }
        }

    }

}
