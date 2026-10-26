package DomusControl.Logica;

import DomusControl.Entidades.Dispositivos.ColunaSom;
import DomusControl.Entidades.Dispositivos.Cortina;
import DomusControl.Entidades.Dispositivos.Dispositivo;
import DomusControl.Entidades.Dispositivos.Lampada;
import DomusControl.Entidades.Dispositivos.PortoesGaragem;

import java.io.Serializable;

public class Automacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricao;

    // Dispositivo que manda, ou seja, o sensor, por exemplo
    private Dispositivo dispositivoOrigem;

    // Condição, que pode ser ligado ou desligado
    private String condicao;

    // Dispositivo que recebe a ação, e vai reagir
    private Dispositivo dispositivoDestino;

    // Atividade, que pode ser ligar ou desligar, true = liga e false = desliga
    private String acaoDestino;

    // Construtor Vazio
    public Automacao() {
        this.descricao = "";
        this.dispositivoOrigem = null;
        this.condicao = "";
        this.dispositivoDestino = null;
        this.acaoDestino = "";
    }

    // Construtor com os Parametros
    public Automacao(String descricao, Dispositivo dispositivoOrigem, String condicao, Dispositivo dispositivoDestino, String acaoDestino) {
        this.descricao = descricao;
        this.dispositivoOrigem = dispositivoOrigem;
        this.condicao = condicao;
        this.dispositivoDestino = dispositivoDestino;
        this.acaoDestino = acaoDestino;
    }

    // Construtor de Cópia
    public Automacao(Automacao outraAutomacao) {
        this.descricao = outraAutomacao.descricao;
        this.dispositivoOrigem = outraAutomacao.dispositivoOrigem;
        this.condicao = outraAutomacao.condicao;
        this.dispositivoDestino = outraAutomacao.dispositivoDestino;
        this.acaoDestino = outraAutomacao.acaoDestino;
    }

    // Getters
    public String getDescricao() {
        return this.descricao;
    }

    public Dispositivo getDispositivoOrigem() {
        return this.dispositivoOrigem;
    }

    public String getCondicao() {
        return this.condicao;
    }

    public Dispositivo getDispositivoDestino() {
        return this.dispositivoDestino;
    }

    public String getacaoDestino() {
        return this.acaoDestino;
    }

    // Setters
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDispositivoOrigem(Dispositivo dispositivoOrigem) {
        this.dispositivoOrigem = dispositivoOrigem;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public void setDispositivoDestino(Dispositivo dispositivoDestino) {
        this.dispositivoDestino = dispositivoDestino;
    }

    public void setacaoDestino(String acaoDestino) {
        this.acaoDestino = acaoDestino;
    }

    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */

    // Método equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Automacao automacao = (Automacao) obj;
        return this.descricao.equals(automacao.descricao) &&
                this.dispositivoOrigem.equals(automacao.dispositivoOrigem) &&
                this.condicao.equals(automacao.condicao) &&
                this.dispositivoDestino.equals(automacao.dispositivoDestino) &&
                this.acaoDestino.equals(automacao.acaoDestino);
    }

    // Método toString
    @Override
    public String toString() {
        String acaoLimpa = this.acaoDestino.replace(":", ": ");
        
        // Junta o Nome da Classe com o ID. Se for nulo, escreve "Casa"
        String origemInfo = (this.dispositivoOrigem != null) 
                ? this.dispositivoOrigem.getClass().getSimpleName() + " [" + this.dispositivoOrigem.getId() + "]" 
                : "Casa [Geral]";
                
        // Junta o Nome da Classe com o ID para o destino
        String destinoInfo = this.dispositivoDestino.getClass().getSimpleName() + " [" + this.dispositivoDestino.getId() + "]";

        return String.format(" %s | Se %s %s -> Atuar em %s com Ação: %s",
                this.descricao, 
                origemInfo, 
                this.condicao, 
                destinoInfo, 
                acaoLimpa);
    }

    // Método clone
    @Override
    public Automacao clone() {
        return new Automacao(this);
    }


    
    // Método para ligar o dispositivo de destino
    public void ligarDispositivoDestino() {
        if (this.dispositivoDestino != null) {
            this.dispositivoDestino.ligar();
        }
    }

    // Método para desligar o dispositivo de destino
    public void desligarDispositivoDestino() {
        if (this.dispositivoDestino != null) {
            this.dispositivoDestino.desligar();
        }
    }

    // Método para verificar se a automação deve ser executada
    public boolean verificarAutomacao() {
        if (this.dispositivoOrigem != null && this.dispositivoDestino != null) {
            // Limpar espaços e meter em minúsculas para evitar erros de digitação
            String condicaoLimpa = this.condicao.trim().toLowerCase(); 
            
            if (condicaoLimpa.equals("ligado")) {
                return this.dispositivoOrigem.isLigado();
            } else if (condicaoLimpa.equals("desligado")) {
                return !this.dispositivoOrigem.isLigado();
            }
        }
        return false;
    }

    // Método para executar a automação
    public void executarAutomacao() {
        // Verifica se a condição do sensor bate certo
        if (verificarAutomacao()) {
            
            String[] partes = this.acaoDestino.split(":");
            String comandoBase = partes[0].trim().toLowerCase();

            if (comandoBase.equals("ligar")) {
                this.dispositivoDestino.ligar();
            } 
            else if (comandoBase.equals("desligar")) {
                this.dispositivoDestino.desligar();
            } 
            else if (partes.length > 1) {
                this.dispositivoDestino.ligar(); // Se vai alterar um valor, tem de ligar o dispositivo primeiro
                int valor = Integer.parseInt(partes[1].trim());

                if (comandoBase.equals("volume") && this.dispositivoDestino instanceof ColunaSom) {
                    ((ColunaSom) this.dispositivoDestino).setVolume(valor);
                } 
                else if (comandoBase.equals("abertura")) {
                    if (this.dispositivoDestino instanceof PortoesGaragem) {
                        ((PortoesGaragem) this.dispositivoDestino).definirAbertura(valor);
                    } else if (this.dispositivoDestino instanceof Cortina) {
                        ((Cortina) this.dispositivoDestino).definirAbertura(valor);
                    }
                } 
                else if (comandoBase.equals("brilho") && this.dispositivoDestino instanceof Lampada) {
                    ((Lampada) this.dispositivoDestino).setBrilho(valor);
                } 
                else if (comandoBase.equals("cor") && this.dispositivoDestino instanceof Lampada) {
                    ((Lampada) this.dispositivoDestino).setTempCor(valor);
                }
            }

            System.out.println(" [Automação Efetuada] " + this.descricao + " -> Ação executada: " + this.acaoDestino);
        }
    }
}
