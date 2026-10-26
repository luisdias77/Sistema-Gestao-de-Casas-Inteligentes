package DomusControl.Logica;

import DomusControl.Entidades.*;
import DomusControl.Entidades.Dispositivos.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TextUI {
    // o modelo
    private DomusManager model;
    // scanner
    private Scanner sc;

    /**
     * Construtor que cria os menus e o model
     */

    public TextUI() {
        this.model = new DomusManager();
        this.sc = new Scanner(System.in);
    }

    /**
     * O MOTOR DO PROGRAMA: Gere o estado entre Login, Seleção de Casa e Operação.
     */
    public void run() {
        while (true) {
            if (model.getUtilizadorLogado() == null) {
                // FASE 1: Menu Inicial (Login / Carregar Dados)
                if (!menuInicialUI())
                    break;
            } else if (model.getCasaAtual() == null) {
                // FASE 2: Selecionar Casa
                menuSelecaoCasaUI();
            } else {
                // FASE 3: Menu Principal (Diferencia Admin de Utilizador)
                menuPrincipalUI();
            }
        }
        System.out.println("DomusControl encerrado. Até à próxima!");
    }

    /* ========================================================== */
    /* = GESTÃO DE ACESSO INICIAL = */
    /* ========================================================== */

    private boolean menuInicialUI() {
        System.out.println("\n ********************************** ");
        System.out.println(" *** BEM-VINDO AO DOMUS CONTROL *** ");
        System.out.println(" ********************************** ");
        System.out.println("1 - Carregar Estado do Sistema");
        System.out.println("2 - Gravar Estado do Sistema");
        System.out.println("3 - Entrar (Login)");
        System.out.println("4 - Criar Novo Utilizador");
        System.out.println("5 - Ver Estado Geral do Sistema");
        System.out.println("6 - Ver Estatísticas do Sistema");
        System.out.println("0 - Sair do Programa");
        System.out.print("Opção: ");

        String op = sc.nextLine();

        switch (op) {
            case "1":
                System.out.print("Nome do ficheiro para carregar: ");
                String ficheiroLeitura = sc.nextLine();

                try {

                    DomusManager carregado = DomusManager.carrega(ficheiroLeitura);
                    if (carregado != null) {
                        // Limpa a memória
                        carregado.logout();
                        carregado.sairDaCasa();
                        this.model = carregado;
                        System.out.println("Sistema carregado com sucesso!");
                    }
                } catch (Exception e) {
                    System.out.println("ERRO: Verifique o formato do ficheiro e os dados inseridos." + e.getMessage());
                }
                break;

            case "2":
                System.out.print("Nome do ficheiro para guardar: ");
                String ficheiroEscrita = sc.nextLine();

                try {
                    // Chama o método de instância
                    this.model.grava(ficheiroEscrita);
                    System.out.println("Sistema guardado com sucesso!");

                } catch (Exception e) {
                    System.out.println("ERRO: Verifique o formato do ficheiro e os dados inseridos." + e.getMessage());
                }
                break;

            case "3":
                if (model.getUtilizadores().isEmpty()) {
                    System.out.println("Erro: O sistema está vazio. Carregue um ficheiro ou registe-se primeiro.");
                } else {
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    if (model.login(email)) {
                        System.out.println("Bem-vindo, " + model.getUtilizadorLogado().getNome() + "!");
                    } else {
                        System.out.println("Erro: Email não encontrado.");
                    }
                }
                break;
            case "4":
                registarUtilizadorUI();
                break;
            case "5":
                listarSistemaUI();
                break;
            case "6":
                menuEstatisticasGlobaisUI();
                break;
            case "0":
                return false; // Quebra o ciclo do run() e fecha o programa
            default:
                System.out.println("Opção inválida.");
        }

        return true;
    }

    /* ========================================================== */
    /* = GESTÃO DE ACESSO INICIAL = */
    /* ========================================================== */

    private void listarSistemaUI() {
        String tituloSistema = " ESTADO GERAL DO SISTEMA ";
        String linhaSistema = "=".repeat(60);

        System.out.println("\n" + linhaSistema);
        System.out.println(tituloSistema);
        System.out.println(linhaSistema);

        if (model.getUtilizadores().isEmpty()) {
            System.out.println("\n O sistema está completamente vazio neste momento.");
            System.out.println("\n" + linhaSistema);
            return;
        }

        System.out.println(" UTILIZADORES REGISTADOS:");
        for (Utilizador u : model.getUtilizadores().values()) {
            System.out.println("  • " + u.getNome() + " -> Email: " + u.getEmail());
        }

        System.out.println("\n CASAS REGISTADAS:");
        // Nota: Se não tiveres um método getCasas(), podes remover esta parte das casas
        // ou criar um getCasas() no DomusManager que retorne o Map de casas.
        if (model.getCasas().isEmpty()) {
            System.out.println("  Nenhuma casa registada ainda.");
        } else {
            for (Casa c : model.getCasas().values()) {
                int numMoradores = c.getUtilizadoresIds().size() + 1; // +1 por causa do Admin

                System.out.println("  • " + c.getNome() + " (Morada: " + c.getMorada() + ")");
                System.out.println("      -> " + numMoradores + " morador(es) registado(s).");
            }
        }
        System.out.println("\n" + linhaSistema + "\n");
    }

    /* ========================================================== */
    /* = SELEÇÃO DE CONTEXTO (QUAL CASA?) = */
    /* ========================================================== */

    /**
     * SELEÇÃO DE CASA: Lista as casas ou permite criar uma nova se o sistema
     * estiver vazio.
     */
    private void menuSelecaoCasaUI() {
        List<Casa> acessiveis = model.getCasasAcessiveis();

        System.out.println("\n--- Selecione a Casa para Operar ---");

        if (acessiveis.isEmpty()) {
            System.out.println("Ainda não tem casas associadas ao seu email.");
        } else {
            for (int i = 0; i < acessiveis.size(); i++) {
                String papel = acessiveis.get(i).isAdmin(model.getUtilizadorLogado().getEmail()) ? "[ADMIN]" : "[USER]";
                System.out.println((i + 1) + " - " + acessiveis.get(i).getNome() + " " + papel);
            }
        }

        // A opção de criar casa fica sempre no final da lista
        int opcaoCriar = acessiveis.size() + 1;
        System.out.println(opcaoCriar + " - Criar Nova Casa");
        System.out.println("0 - Logout");

        System.out.print("Opção: ");
        try {
            int op = Integer.parseInt(sc.nextLine());
            if (op == 0) {
                model.logout();
            } else if (op == opcaoCriar) {
                registarCasaUI();
            } else if (op > 0 && op <= acessiveis.size()) {
                model.selecionarCasa(acessiveis.get(op - 1).getNome());
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (Exception e) {
            System.out.println("Opção inválida.");
        }
    }

    /* ========================================================== */
    /* = MENU PRINCIPAL (ADMIN/USER) = */
    /* ========================================================== */

    public void menuPrincipalUI() {
        // O ciclo mantém o menu a funcionar até o utilizador decidir sair
        while (true) {

            // Se a casa sumiu (ex: falhou o load ou carregou um ficheiro novo),
            // sai do menu automaticamente para não dar NullPointerException!
            if (model.getCasaAtual() == null) {
                return;
            }

            boolean isAdmin = model.isUserAdminDaCasaAtual();

            String tituloMenu = "*** MENU DA CASA: " + model.getCasaAtual().getNome().toUpperCase() + " ***";
            String linhaAsteriscos = "*".repeat(tituloMenu.length());
            System.out.println("\n " + linhaAsteriscos);
            System.out.println(" " + tituloMenu);
            System.out.println(" " + linhaAsteriscos);


            if (isAdmin) {
                System.out.println("1 - Gestão de Casas e Utilizadores");
                System.out.println("2 - Gestão de Dispositivos");
                System.out.println("3 - Automações e Escalonamentos");
                System.out.println("4 - Cenários");
                System.out.println("5 - Ver Estado da Casa");
                System.out.println("6 - Ver Estatísticas da Casa");
                System.out.println("7 - Gravar Estado Atual");
                System.out.println("8 - Trocar de Casa (Voltar atrás)");
            } else {
                System.out.println("1 - Gestão de Dispositivos");
                System.out.println("2 - Automações e Escalonamentos");
                System.out.println("3 - Cenários");
                System.out.println("4 - Ver Estado da Casa");
                System.out.println("5 - Ver Estatísticas da Casa");
                System.out.println("6 - Gravar Estado Atual");
                System.out.println("7 - Trocar de Casa (Voltar atrás)");
            }
            System.out.println("0 - Sair do Programa");
            System.out.print("Opção: ");

            String op = sc.nextLine();

            if (isAdmin) {
                switch (op) {
                    case "1":
                        menuUtilizadoresCasas();
                        break;
                    case "2":
                        menuDispositivos();
                        break;
                    case "3":
                        menuInteligente();
                        break;
                    case "4":
                        menuCenarios();
                        break;
                    case "5":
                        verEstadoCasaUI();
                        break;
                    case "6":
                        menuEstatisticasLocaisUI();
                        break;
                    case "7":
                        System.out.print("Nome do ficheiro para guardar: ");
                        String ficheiroEscrita = sc.nextLine();

                        try {
                            // Usamos o método de instância
                            model.grava(ficheiroEscrita);
                            System.out.println("Sistema guardado com sucesso!");
                        } catch (Exception e) {
                            System.out.println(
                                    "ERRO: Verifique o formato do ficheiro e os dados inseridos." + e.getMessage());
                        }
                        break;

                    case "8":
                        model.sairDaCasa(); // Limpa a casa atual em segurança
                        return; // O return faz com que saia deste menu e volte para trás
                    case "0":
                        System.out.println("A encerrar o DomusControl...");
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida.");
                }
            } else {
                switch (op) {
                    case "1":
                        menuDispositivos();
                        break;
                    case "2":
                        menuInteligente();
                        break;
                    case "3":
                        menuCenarios();
                        break;
                    case "4":
                        verEstadoCasaUI();
                        break;
                    case "5":
                        menuEstatisticasLocaisUI();
                    case "6":
                        System.out.print("Nome do ficheiro para guardar: ");
                        String ficheiroEscrita = sc.nextLine();

                        try {
                            // Usamos o método de instância
                            model.grava(ficheiroEscrita);
                            System.out.println("Sistema guardado com sucesso!");

                        } catch (Exception e) {

                            System.out.println(
                                    "ERRO: Verifique o formato do ficheiro e os dados inseridos." + e.getMessage());
                        }
                        break;

                    case "7":
                        model.sairDaCasa(); // Limpa a casa atual em segurança
                        return; // O return faz com que saia deste menu e volte para trás
                    case "0":
                        System.out.println("A encerrar o DomusControl...");
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        }
    }

    /* ========================================================== */
    /* = SUB-MENUS DE GESTÃO E OPERAÇÃO = */
    /* ========================================================== */

    private void menuUtilizadoresCasas() {
        NewMenu menu = new NewMenu(new String[] {
                "Adicionar Divisão",
                "Adicionar Morador (Utilizador Normal)",
                "Registar Novo Utilizador no Sistema"
        });
        menu.setHandler(1, () -> registarDivisaoUI());
        menu.setHandler(2, () -> adicionarMoradorCasaAtualUI());
        menu.setHandler(3, () -> registarUtilizadorUI());
        menu.run();
    }

    private void menuDispositivos() {
        boolean isAdmin = model.isUserAdminDaCasaAtual();
        NewMenu menu = new NewMenu(new String[] {
                "Controlar Dispositivo",
                isAdmin ? "Instalar Novo Dispositivo (ADMIN)" : "---"
        });
        menu.setHandler(1, () -> controlarDispositivoUI());
        if (isAdmin)
            menu.setHandler(2, () -> criarDispositivoUI());
        menu.run();
    }

    private void menuInteligente() {
        NewMenu menu = new NewMenu(new String[] {
                "Criar Automação",
                "Listar Automações",
                "Processar Automações",
                "Eliminar Automações\n--------------------------",
                "Criar Escalonamento",
                "Listar Escalonamentos",
                "Processar Escalonamentos",
                "Eliminar Escalonamentos\n--------------------------",
                "Ver Sugestões Inteligentes"
        });
        menu.setHandler(1, () -> criarAutomacaoUI());
        menu.setHandler(2, () -> listarAutomacoesUI());
        menu.setHandler(3, () -> {
            model.processarAutomacoes();
            System.out.println("Processadas.");
        });
        menu.setHandler(4, () -> eliminarAutomacaoUI());
        menu.setHandler(5, () -> criarEscalonamentoUI());
        menu.setHandler(6, () -> listarEscalonamentosUI());
        menu.setHandler(7, () -> processarEscalonamentosUI());
        menu.setHandler(8, () -> eliminarEscalonamentoUI());
        menu.setHandler(9, () -> {
            System.out.println("Verificando sugestões...");
            model.verificarSugestoes(this.sc);
            System.out.println("Sugestões verificadas.");
        });
        menu.run();
    }

    private void menuCenarios() {
        NewMenu menu = new NewMenu(new String[] {
                "Criar Novo Cenário",
                "Ativar Cenário Existente",
                "Listar Meus Cenários",
                "Remover Cenários"
        });
        menu.setHandler(1, () -> criarCenarioUI());
        menu.setHandler(2, () -> ativarCenarioUI());
        menu.setHandler(3, () -> listarCenariosUI());
        menu.setHandler(4, () -> eliminarCenarioUI());
        menu.run();
    }

    /* ==================================== */
    /* = Para o Menu Utilizadores e Casas = */
    /* ==================================== */

    private void registarUtilizadorUI() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        if (model.registarUtilizador(nome, email, pass))
            System.out.println("Utilizador registado com sucesso!");
        else
            System.out.println("Erro: Utilizador já existe.");
    }

    private void registarCasaUI() {
        System.out.print("Nome da Casa: ");
        String nome = sc.nextLine();
        System.out.print("Morada: ");
        String morada = sc.nextLine();
        System.out.print("Email do Dono: ");
        String emailDono = sc.nextLine();
        if (model.registarCasa(nome, morada, emailDono))
            System.out.println("Casa criada com sucesso!");
        else
            System.out.println("Erro: Verifique os dados.");
    }

    private void registarDivisaoUI() {
        String casa = model.getCasaAtual().getNome();
        System.out.print("Divisão: ");
        String divisao = sc.nextLine();
        if (model.registarDivisão(casa, divisao))
            System.out.println("Divisão adicionada com sucesso!");
        else
            System.out.println("Erro: Casa não encontrada.");
    }

    private void adicionarMoradorCasaAtualUI() {
        System.out.println("\n--- Adicionar Utilizador à Casa: " + model.getCasaAtual().getNome() + " ---");
        System.out.print("Qual é o email da pessoa que quer adicionar? ");
        String email = sc.nextLine();

        if (model.adicionarMoradorCasaAtual(email)) {
            System.out.println("Sucesso! O utilizador '" + email + "' agora tem acesso a esta casa.");
        } else {
            System.out.println(
                    "Erro: Esse email não está registado no sistema. Use a opção '3 - Registar Novo Utilizador no Sistema' neste mesmo menu para criar a conta primeiro.");
            ;
        }
    }

    /* ============================ */
    /* = Para o Menu Dispositivos = */
    /* ============================ */

    private void criarDispositivoUI() {
        // Criamos um sub-menu para os tipos
        NewMenu menuTipos = new NewMenu(new String[] {
                "Lâmpada",
                "Tomada",
                "Relé",
                "Cortina",
                "Coluna de Som",
                "Portão de Garagem"
        });

        // Associamos cada opção ao seu método específico
        menuTipos.setHandler(1, () -> addLampadaUI());
        menuTipos.setHandler(2, () -> addTomadaUI());
        menuTipos.setHandler(3, () -> addReleUI());
        menuTipos.setHandler(4, () -> addCortinaUI());
        menuTipos.setHandler(5, () -> addColunaUI());
        menuTipos.setHandler(6, () -> addPortaoUI());

        menuTipos.run();
    }

    // Pede os dados que todos os dispositivos têm em comum
    private String[] pedirDadosBase() {
        System.out.println("\n--- Dados do Dispositivo ---");
        String casa = model.getCasaAtual().getNome();
        System.out.print("Nome da Divisão: ");
        String div = sc.nextLine();
        System.out.print("ID Único: ");
        String id = sc.nextLine();
        System.out.print("Marca: ");
        String marca = sc.nextLine();
        System.out.print("Modelo: ");
        String mod = sc.nextLine();
        System.out.print("Consumo Base (Wh): ");
        String cons = sc.nextLine();

        return new String[] { casa, div, id, marca, mod, cons };
    }

    // Faz a ponte final com o DomusManager
    private void finalizarInstalacao(String casa, String div, Dispositivo d) {
        if (model.addDispositivoDivisao(casa, div, d)) {
            System.out.println("Dispositivo '" + d.getId() + "' instalado com sucesso!");
        } else {
            System.out.println("ERRO: Não foi possível instalar. Verifique se a casa e divisão existem.");
        }
    }

    /** Métodos específicos para cada tipo de dispositivo */

    // Método para adicionar uma Lâmpada
    private void addLampadaUI() {
        String[] dados = pedirDadosBase();

        System.out.print("Intensidade (0-100): ");
        int intensidade = Integer.parseInt(sc.nextLine());
        System.out.print("Temperatura de Cor (2700K-4000K): ");
        int temp = Integer.parseInt(sc.nextLine());

        // Criamos a lâmpada
        Lampada l = new Lampada(dados[2], dados[3], dados[4], Double.parseDouble(dados[5]),
                false, 0, 0.0, intensidade, temp);

        finalizarInstalacao(dados[0], dados[1], l);
    }

    // Método para adicionar uma Tomada
    private void addTomadaUI() {
        String[] dados = pedirDadosBase();

        System.out.print("Número de Entradas: ");
        int numEntradas = Integer.parseInt(sc.nextLine());

        Tomada t = new Tomada(dados[2], dados[3], dados[4], Double.parseDouble(dados[5]), false, numEntradas);

        finalizarInstalacao(dados[0], dados[1], t);
    }

    // Método para adicionar um Relé
    private void addReleUI() {
        String[] dados = pedirDadosBase();

        Rele r = new Rele(dados[2], dados[3], dados[4], Double.parseDouble(dados[5]), false);

        finalizarInstalacao(dados[0], dados[1], r);
    }

    // Método para adicionar uma Cortina
    private void addCortinaUI() {
        String[] dados = pedirDadosBase();

        System.out.print("Abertura Inicial (0-100): ");
        int abert = Integer.parseInt(sc.nextLine());

        Cortina c = new Cortina(dados[2], dados[3], dados[4], Double.parseDouble(dados[5]), false, abert);

        finalizarInstalacao(dados[0], dados[1], c);
    }

    // Método para adicionar uma Coluna de Som
    private void addColunaUI() {
        String[] dados = pedirDadosBase();

        System.out.print("Volume Inicial (0-100): ");
        int vol = Integer.parseInt(sc.nextLine());

        ColunaSom cs = new ColunaSom(dados[2], dados[3], dados[4], Double.parseDouble(dados[5]), false, 0, 0.0, vol);

        finalizarInstalacao(dados[0], dados[1], cs);
    }

    // Método para adicionar um Portao de uma Garagem
    private void addPortaoUI() {
        String[] dados = pedirDadosBase();

        System.out.print("Abertura Inicial (0-100): ");
        int abert = Integer.parseInt(sc.nextLine());

        PortoesGaragem pg = new PortoesGaragem(dados[2], dados[3], dados[4], Double.parseDouble(dados[5]), false,
                abert);

        finalizarInstalacao(dados[0], dados[1], pg);
    }

    // Método para ligar/desligar um dispositivo
    private void controlarDispositivoUI() {
        System.out.print("Qual é o ID do Dispositivo que quer controlar? ");
        String id = sc.nextLine();

        Dispositivo d = model.procurarDispositivo(model.getCasaAtual().getNome(), id);

        if (d != null) {
            System.out.println("\n--- Controlar: " + d.getClass().getSimpleName() + " (" + id + ") ---");
            System.out.println("1 - Ligar");
            System.out.println("2 - Desligar");

            // Estas opções extras só aparecem para os tipos que têm atributos específicos
            // para alterar
            if (d instanceof ColunaSom) {
                System.out.println("3 - Mudar Volume");
            } else if (d instanceof PortoesGaragem) {
                System.out.println("3 - Alterar % de Abertura");
            } else if (d instanceof Lampada) {
                System.out.println("3 - Alterar Brilho");
                System.out.println("4 - Alterar Cor (Quente/Fria)");
            } else if (d instanceof Cortina) {
                System.out.println("3 - Alterar % de Abertura");
            }

            System.out.print("Opção: ");
            String op = sc.nextLine();

            switch (op) {
                case "1":
                    d.ligar();
                    // registar no historico, para contar o numero de vezes que o dispositivo foi
                    // ligado
                    model.registarNoHistorico(id, "Ligar");
                    System.out.println("Dispositivo ligado.");
                    break;
                case "2":
                    d.desligar();
                    // registar no historico, para contar o numero de vezes que o dispositivo foi
                    // desligado
                    model.registarNoHistorico(id, "Desligar");
                    System.out.println("Dispositivo desligado.");
                    break;
                case "3":
                    d.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos
                               // específicos

                    // Para cada tipo de dispositivo, pede o atributo específico e chama o método
                    // correspondente
                    if (d instanceof ColunaSom) {
                        System.out.print("Novo volume (0-100): ");
                        int vol = Integer.parseInt(sc.nextLine());
                        ((ColunaSom) d).setVolume(vol);
                        System.out.println("Volume alterado para " + vol + "%.");

                    } else if (d instanceof PortoesGaragem) {
                        System.out.print("Nova abertura (0-100): ");
                        int ab = Integer.parseInt(sc.nextLine());
                        ((PortoesGaragem) d).definirAbertura(ab);
                        System.out.println("Portão movido para " + ab + "% de abertura.");

                    } else if (d instanceof Lampada) {
                        System.out.print("Novo brilho (0-100): ");
                        int brilho = Integer.parseInt(sc.nextLine());
                        ((Lampada) d).setBrilho(brilho);
                        System.out.println("Brilho alterado para " + brilho + ".");

                    } else if (d instanceof Cortina) {
                        System.out.print("Nova abertura (0-100): ");
                        int ab = Integer.parseInt(sc.nextLine());
                        ((Cortina) d).definirAbertura(ab);
                        System.out.println("Cortina movida para " + ab + "% de abertura.");
                    }
                    break;
                case "4":
                    d.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos
                               // específicos

                    if (d instanceof Lampada) {
                        System.out.print("Nova Temperatura de Cor (K): ");
                        int cor = Integer.parseInt(sc.nextLine());
                        ((Lampada) d).setTempCor(cor);
                        System.out.println("Cor alterada.");
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } else {
            System.out.println("Erro: Dispositivo '" + id + "' não encontrado nesta casa.");
        }
    }

    // Método para ver o estado de uma casa
    private void verEstadoCasaUI() {
        // Imprime o toString da casa que está selecionada neste momento
        System.out.println(model.getCasaAtual().toString());

        System.out.println("\n==============================");
        System.out.println("Pressione ENTER para voltar...");
        sc.nextLine();
    }

    /* =============================================== */
    /* = Para o Menu de Automações e Escalonamentos) = */
    /* =============================================== */

    // Método para criar uma automação
    private void criarAutomacaoUI() {
        System.out.println("\n--- Criar Nova Automação ---");

        String cNome = model.getCasaAtual().getNome(); // Para não pedir a casa, já que só podemos criar automações na
                                                       // casa onde estamos

        System.out.print("Descrição da regra: ");
        String desc = sc.nextLine();

        System.out.print("ID do dispositivo sensor (Origem): ");
        String idOrigem = sc.nextLine();

        System.out.print("ID do dispositivo que reage (Destino): ");
        String idDestino = sc.nextLine();

        // Procurar os objetos reais no sistema
        Dispositivo dOrig = model.procurarDispositivo(cNome, idOrigem);
        Dispositivo dDest = model.procurarDispositivo(cNome, idDestino);

        if (dOrig != null && dDest != null) {
            // Pergunta a Condição da Origem
            System.out.print("Quando o sensor [" + dOrig.getClass().getSimpleName() + "] estiver (Ex: ligado/desligado): ");
            String cond = sc.nextLine().toLowerCase().trim();

            // Apresenta o Menu Inteligente para a Ação do Destino
            System.out.println("\n--- Que ação quer aplicar no Destino: " + dDest.getClass().getSimpleName() + " (" + idDestino + ") ---");
            System.out.println("1 - Ligar");
            System.out.println("2 - Desligar");

            if (dDest instanceof ColunaSom) {
                System.out.println("3 - Mudar Volume");
            } else if (dDest instanceof PortoesGaragem || dDest instanceof Cortina) {
                System.out.println("3 - Alterar % de Abertura");
            } else if (dDest instanceof Lampada) {
                System.out.println("3 - Alterar Brilho");
                System.out.println("4 - Alterar Cor (Quente/Fria)");
            }

            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            String acaoFinal = "";

            switch (op) {
                case "1": 
                    acaoFinal = "ligar"; 
                    break;
                case "2": 
                    acaoFinal = "desligar"; 
                    break;
                case "3":
                    if (dDest instanceof ColunaSom) {
                        System.out.print("Volume pretendido (0-100): ");
                        acaoFinal = "volume:" + sc.nextLine().trim();
                    } else if (dDest instanceof PortoesGaragem || dDest instanceof Cortina) {
                        System.out.print("Abertura pretendida (0-100): ");
                        acaoFinal = "abertura:" + sc.nextLine().trim();
                    } else if (dDest instanceof Lampada) {
                        System.out.print("Brilho pretendido (0-100): ");
                        acaoFinal = "brilho:" + sc.nextLine().trim();
                    } else {
                        System.out.println("Opção inválida.");
                        return; 
                    }
                    break;
                case "4":
                    if (dDest instanceof Lampada) {
                        System.out.print("Temperatura de Cor pretendida (2700K-4000K): ");
                        acaoFinal = "cor:" + sc.nextLine().trim();
                    } else {
                        System.out.println("Opção inválida.");
                        return; 
                    }
                    break;
                default:
                    System.out.println("Opção inválida. Operação cancelada.");
                    return; 
            }

            // 3. Cria e adiciona a automação com a String formatada (ex: "brilho:50")
            Automacao novaAuto = new Automacao(desc, dOrig, cond, dDest, acaoFinal);
            model.adicionarAutomacao(novaAuto);
            System.out.println("\nAutomação registada com sucesso!");

        } else {
            System.out.println("ERRO: Dispositivo(s) não encontrado(s) na casa " + cNome);
        }
    }

    private void listarAutomacoesUI() {
        System.out.println("\n==========================");
        System.out.println(" MINHAS AUTOMAÇÕES ATIVAS ");
        System.out.println("==========================");

        List<Automacao> automacoes = model.getAutomacoes(); 

        if (automacoes == null || automacoes.isEmpty()) {
            System.out.println("\n  Ainda não existem automações configuradas.");
        } else {
            System.out.println();
            for (Automacao a : automacoes) {
                System.out.println(a.toString());
            }
        }
        System.out.println("\n==========================");
    }

    private void eliminarAutomacaoUI() {
        System.out.println("\n--- Eliminar Automação ---");
        
        List<Automacao> lista = model.getAutomacoes(); // Vai buscar a lista
        
        if (lista.isEmpty()) {
            System.out.println("Não existem automações para eliminar.");
            return;
        }

        // 1. Listar com índices para o utilizador escolher
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + " -> " + lista.get(i).toString());
        }

        System.out.print("\nNúmero da automação a eliminar (0 para cancelar): ");
        try {
            int escolha = Integer.parseInt(sc.nextLine());
            if (escolha > 0 && escolha <= lista.size()) {
                model.removerAutomacao(escolha - 1); // -1 porque a lista começa em 0
                System.out.println("Automação eliminada!");
            }
        } catch (Exception e) {
            System.out.println("Opção inválida.");
        }
    }



    // Método para criar um escalonamento (agendamento)
    private void criarEscalonamentoUI() {
        System.out.println("\n--- Criar Escalonamento (Agendamento) ---");

        String casaEsc = model.getCasaAtual().getNome(); // Para não pedir a casa, já que só podemos criar
                                                         // escalonamentos na casa onde estamos

        System.out.print("Descrição: ");
        String descEsc = sc.nextLine();

        System.out.print("Hora da execução (HH:mm, ex: 18:30): ");
        String horaEsc = sc.nextLine();

        System.out.print("ID do dispositivo a controlar: ");
        String idDispEsc = sc.nextLine();

        // Procura o dispositivo para saber o tipo
        Dispositivo d = model.procurarDispositivo(model.getCasaAtual().getNome(), idDispEsc);

        if (d == null) {
            System.out.println("Erro: O dispositivo '" + idDispEsc + "' não existe nesta casa!");
            return; // Termina a criação porque o ID é inválido
        }

        // Apresentar o Menu Dinâmico
        System.out.println("\n--- Que ação quer aplicar em: " + d.getClass().getSimpleName() + " (" + idDispEsc + ") ---");
        System.out.println("1 - Ligar");
        System.out.println("2 - Desligar");

        if (d instanceof ColunaSom) {
            System.out.println("3 - Mudar Volume");
        } else if (d instanceof PortoesGaragem || d instanceof Cortina) {
            System.out.println("3 - Alterar % de Abertura");
        } else if (d instanceof Lampada) {
            System.out.println("3 - Alterar Brilho");
            System.out.println("4 - Alterar Cor (Quente/Fria)");
        }

        System.out.print("Opção: ");
        String op = sc.nextLine().trim();
        String acaoEsc = "";

        switch (op) {
            case "1": 
                acaoEsc = "ligar"; 
                break;
            case "2": 
                acaoEsc = "desligar"; 
                break;
            case "3":
                if (d instanceof ColunaSom) {
                    System.out.print("Volume pretendido (0-100): ");
                    acaoEsc = "volume:" + sc.nextLine().trim();
                } else if (d instanceof PortoesGaragem || d instanceof Cortina) {
                    System.out.print("Abertura pretendida (0-100): ");
                    acaoEsc = "abertura:" + sc.nextLine().trim();
                } else if (d instanceof Lampada) {
                    System.out.print("Brilho pretendido (0-100): ");
                    acaoEsc = "brilho:" + sc.nextLine().trim();
                } else {
                    System.out.println("Opção inválida.");
                    return;
                }
                break;
            case "4":
                if (d instanceof Lampada) {
                    System.out.print("Temperatura de Cor pretendida (2700K-4000K): ");
                    acaoEsc = "cor:" + sc.nextLine().trim();
                } else {
                    System.out.println("Opção inválida.");
                    return;
                }
                break;
            default:
                System.out.println("Opção inválida. Operação cancelada.");
                return;
        }

        try {
            // O gestor trata da procura e criação
            model.adicionarEscalonamento(descEsc, horaEsc, idDispEsc, casaEsc, acaoEsc);
            System.out.println("Escalonamento agendado para as " + horaEsc + "!");
        } catch (Exception e) {
            System.out.println("ERRO: Verifique o formato da hora e os dados inseridos.");
        }
    }

    private void listarEscalonamentosUI() {
        System.out.println("\n==============================");
        System.out.println(" MEUS ESCALONAMENTOS AGENDADOS ");
        System.out.println("===============================");

        List<Escalonamento> escalonamentos = model.getEscalonamentos(); 

        if (escalonamentos == null || escalonamentos.isEmpty()) {
            System.out.println("\n  Ainda não existem escalonamentos agendados.");
        } else {
            System.out.println();
            for (Escalonamento e : escalonamentos) {
                System.out.println(e.toString());
            }
        }
        System.out.println("\n=========================");
    }

    // Método para processar os escalonamentos (pode ser chamado manualmente ou usar
    // a hora do sistema)
    private void processarEscalonamentosUI() {
        System.out.println("\n--- Processar Escalonamentos ---");
        System.out.println("1. Usar a hora real do sistema");
        System.out.println("2. Simular uma hora específica");
        System.out.print("Escolha: ");

        String escolha = sc.nextLine();

        if (escolha.equals("1")) {
            model.processarEscalonamentosHoraSistema(); // O gestor obtém a hora atual e processa os escalonamentos
            System.out.println("Verificação concluída com a hora atual.");
        } else if (escolha.equals("2")) {
            System.out.print("Digite a hora para simulação (HH:mm): ");
            String hora = sc.nextLine();
            model.processarEscalonamentos(hora); // O gestor processa os escalonamentos usando a hora simulada
            System.out.println("Simulação concluída para as " + hora + ".");
        } else {
            System.out.println("Opção inválida.");
        }
    }

    private void eliminarEscalonamentoUI() {
        System.out.println("\n--- Eliminar Escalonamento ---");
        
        List<Escalonamento> lista = model.getEscalonamentos(); 
        
        if (lista.isEmpty()) {
            System.out.println("Não existem escalonamentos para eliminar.");
            return;
        }

        // Listar com índices para o utilizador escolher
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + " -> " + lista.get(i).toString());
        }

        System.out.print("\nNúmero do escalonamento a eliminar (0 para cancelar): ");
        try {
            int escolha = Integer.parseInt(sc.nextLine());
            if (escolha > 0 && escolha <= lista.size()) {
                model.removerEscalonamento(escolha - 1); // -1 porque a lista começa em 0
                System.out.println("Escalonamento eliminado!");
            }
        } catch (Exception e) {
            System.out.println("Opção inválida.");
        }
    }

    /* ============================ */
    /* === Para o Menu Cenários === */
    /* ============================ */

    private void criarCenarioUI() {
        Utilizador atualUser = model.getUtilizadorLogado();
        if (atualUser == null) {
            System.out.println("ERRO: Nenhum utilizador logado.");
            return;
        }

        System.out.println("\n--- Criar Novo Cenário ---");
        System.out.print("Nome do novo cenário (ex: Cinema, Fora de Casa):  ");
        String nomeCenario = sc.nextLine().trim();

        Cenario novo = new Cenario();
        novo.setNome(nomeCenario);

        // Criamos um contador para saber se adicionou algo, se ficar a 0, nao cria
        // cenario nenhum
        int dispositivosAdicionados = 0;

        while (true) {
            System.out.print("\nID do dispositivo a controlar (ou 'cancelar' para terminar): ");
            String id = sc.nextLine().trim();
            if (id.equalsIgnoreCase("cancelar")) break;

            // Procurar o dispositivo real na casa para saber o tipo dele
            Dispositivo d = model.procurarDispositivo(model.getCasaAtual().getNome(), id);

            if (d == null) {
                System.out.println("Erro: O dispositivo '" + id + "' não existe nesta casa!");
                continue;
            }

            System.out.println("\n--- Que ação quer aplicar em: " + d.getClass().getSimpleName() + " (" + id + ") ---");
            System.out.println("1 - Ligar");
            System.out.println("2 - Desligar");

            if (d instanceof ColunaSom) {
                System.out.println("3 - Mudar Volume");
            } else if (d instanceof PortoesGaragem) {
                System.out.println("3 - Alterar % de Abertura");
            } else if (d instanceof Lampada) {
                System.out.println("3 - Alterar Brilho");
                System.out.println("4 - Alterar Cor (Quente/Fria)");
            } else if (d instanceof Cortina) {
                System.out.println("3 - Alterar % de Abertura");
            }

            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            String acaoEsc = "";

            switch (op) {
                case "1": 
                    acaoEsc = "ligar"; 
                    break;
                case "2": 
                    acaoEsc = "desligar"; 
                    break;
                case "3":
                    if (d instanceof ColunaSom) {
                        System.out.print("Volume pretendido para o cenário (0-100): ");
                        acaoEsc = "volume:" + sc.nextLine().trim();
                    } else if (d instanceof PortoesGaragem || d instanceof Cortina) {
                        System.out.print("Abertura pretendida para o cenário (0-100): ");
                        acaoEsc = "abertura:" + sc.nextLine().trim();
                    } else if (d instanceof Lampada) {
                        System.out.print("Brilho pretendido para o cenário (0-100): ");
                        acaoEsc = "brilho:" + sc.nextLine().trim();
                    } else {
                        System.out.println("Opção inválida para este dispositivo.");
                        continue;
                    }
                    break;
                case "4":
                    if (d instanceof Lampada) {
                        System.out.print("Temperatura de Cor pretendida (2700K-4000K): ");
                        acaoEsc = "cor:" + sc.nextLine().trim();
                    } else {
                        System.out.println("Opção inválida.");
                        continue;
                    }
                    break;
                default:
                    System.out.println("Opção inválida. Ação não adicionada.");
                    continue;
            }

            // Guarda a ação no cenário (ex: "brilho:50" ou "ligar")
            novo.addAcao(id, acaoEsc);
            dispositivosAdicionados++;
            System.out.println("Ação definida com sucesso!");
        }

        // Se o utilizador não adicionou nenhum dispositivo, não faz sentido guardar um
        // cenário vazio, por isso só guarda se tiver pelo menos 1 ação associada
        if (dispositivosAdicionados == 0) {
            System.out
                    .println("Criação cancelada: O cenário '" + nomeCenario + "' não foi guardado porque ficou vazio.");
        } else {
            // Só guarda se tiver pelo menos 1 dispositivo
            atualUser.addCenario(novo);
            System.out.println("Cenário '" + nomeCenario + "' guardado com sucesso na sua conta com "
                    + dispositivosAdicionados + " ações.");
        }
    }

    // Método para ativar um cenário existente
    private void ativarCenarioUI() {
        Utilizador atual = model.getUtilizadorLogado();
        if (atual == null)
            return;

        System.out.println("\n--- Ativar Cenário ---");
        System.out.print("Qual o nome do cenário que deseja ativar? ");
        String nomeCen = sc.nextLine().trim();

        String nomeCasa = model.getCasaAtual().getNome(); // Para não pedir a casa, já que só podemos criar cenarios na
                                                          // casa onde estamos

        // O gestor trata de procurar o cenário e executar as ações associadas
        boolean sucesso = model.ativarCenarioUtilizador(atual.getEmail(), nomeCen, nomeCasa);

        if (sucesso) {
            System.out.println("Cenário '" + nomeCen + "' executado com sucesso na " + nomeCasa + "!");
        } else {
            System.out.println("Erro: Verifique se o cenário existe na sua conta.");
        }
    }

    // Método para listar os cenários do utilizador logado
    private void listarCenariosUI() {
        Utilizador atual = model.getUtilizadorLogado();
        if (atual == null)
            return;

        System.out.println("\n--- Meus Cenários Disponíveis ---");
        Map<String, Cenario> cenarios = atual.getMeusCenarios();

        if (cenarios.isEmpty()) {
            System.out.println("Ainda não criou nenhum cenário.");
        } else {
            for (Cenario c : cenarios.values()) {
                // Mostra o nome e a lista de ações
                System.out.println(c.toString());
            }
        }
    }

    // Método para remover Cenários
    private void eliminarCenarioUI() {
        System.out.println("\n--- Eliminar Cenário ---");
        
        // Vai buscar o utilizador logado no momento
        Utilizador uLogado = model.getUtilizadorLogado();
        Map<String, Cenario> mapaCenarios = uLogado.getMeusCenarios();
        
        if (mapaCenarios.isEmpty()) {
            System.out.println("Não existem cenários configurados.");
            return;
        }

        // Passa os cenários do Map para uma Lista temporária para podermos usar números (1, 2, 3...)
        List<Cenario> listaTemp = new java.util.ArrayList<>(mapaCenarios.values());

        // 1. Listar cenários com números
        for (int i = 0; i < listaTemp.size(); i++) {
            System.out.println((i + 1) + " -> " + listaTemp.get(i).getNome());
        }

        System.out.print("\nNúmero do cenário a eliminar (0 para cancelar): ");
        try {
            int escolha = Integer.parseInt(sc.nextLine());
            if (escolha > 0 && escolha <= listaTemp.size()) {
                String nomeRemovido = listaTemp.get(escolha - 1).getNome();
                
                uLogado.removerCenario(nomeRemovido); 
                
                System.out.println("Cenário '" + nomeRemovido + "' eliminado com sucesso!");
            } else if (escolha != 0) {
                System.out.println("Número fora dos limites.");
            }
        } catch (Exception e) {
            System.out.println("Opção inválida.");
        }
    }


    /* ========================================================== */
    /* = ESTATÍSTICAS DO SISTEMA = */
    /* ========================================================== */

    private void menuEstatisticasGlobaisUI() {
        String tituloGlobal = " ESTATÍSTICAS GLOBAIS DO SISTEMA ";
        String linhaGlobal = "=".repeat(tituloGlobal.length());

        System.out.println("\n " + linhaGlobal);
        System.out.println(" " + tituloGlobal);
        System.out.println(" " + linhaGlobal);

        System.out.println("\n CONSUMO ENERGÉTICO:");
        System.out.println("  " + model.casaMaisConsome());

        System.out.println("\n TOP 3 DIVISÕES COM MAIS DISPOSITIVOS:");
        List<String> divisoes = model.tresDivMaisDispositivos();
        for (String div : divisoes) {
            System.out.println("  " + div);
        }
        
        System.out.println("\n " + linhaGlobal);
        System.out.println(" Pressione ENTER para voltar...");
        sc.nextLine();
    }
    
    private void menuEstatisticasLocaisUI() {
        Casa casaAtual = model.getCasaAtual();
        
        String tituloLocal = " ESTATÍSTICAS DA CASA: " + casaAtual.getNome().toUpperCase() + " ";
        String linhaLocal = "=".repeat(tituloLocal.length());
        
        System.out.println("\n " + linhaLocal);
        System.out.println(" " + tituloLocal);
        System.out.println(" " + linhaLocal);

        System.out.println("\n TOP 3 DISPOSITIVOS MAIS ATIVADOS:");
        List<Dispositivo> ativados = model.tresDispMaisAtivados(casaAtual);
        if (ativados == null || ativados.isEmpty()) {
            System.out.println("  Ainda não há dados suficientes.");
        } else {
            for (int i = 0; i < ativados.size(); i++) {
                System.out.println("  " + (i + 1) + "º Lugar: [" + ativados.get(i).getId() + "] " 
                    + ativados.get(i).getClass().getSimpleName());
            }
        }

        System.out.println("\n TOP 3 DISPOSITIVOS COM MAIS TEMPO DE USO:");
        List<Dispositivo> tempoUso = model.tresDispMaisTempoUso(casaAtual);
        if (tempoUso == null || tempoUso.isEmpty()) {
            System.out.println("  Ainda não há dados suficientes.");
        } else {
            for (int i = 0; i < tempoUso.size(); i++) {
                System.out.println("  " + (i + 1) + "º Lugar: [" + tempoUso.get(i).getId() + "] " 
                    + tempoUso.get(i).getClass().getSimpleName());
            }
        }
        
        System.out.println("\n " + linhaLocal);
        System.out.println(" Pressione ENTER para voltar...");
        sc.nextLine();
    }
}
