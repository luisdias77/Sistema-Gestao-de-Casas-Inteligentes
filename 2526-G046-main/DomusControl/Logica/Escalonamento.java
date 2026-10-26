package DomusControl.Logica;

import DomusControl.Entidades.Dispositivos.ColunaSom;
import DomusControl.Entidades.Dispositivos.Cortina;
import DomusControl.Entidades.Dispositivos.Dispositivo;
import DomusControl.Entidades.Dispositivos.Lampada;
import DomusControl.Entidades.Dispositivos.PortoesGaragem;

import java.io.Serializable;
import java.time.LocalTime;

public class Escalonamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricao; // Descrição da regra do escalonamento
    private LocalTime horaAExecutar; // Hora a executar a regra do escalonamento
    private Dispositivo dispositivo; // Dispositivo que vai executar o escalonamento
    private String acaoEscalonamento; // Ligar ou desligar um determinado dispositivo

    // Construtor Vazio
    public Escalonamento() {
        this.descricao = "";
        this.horaAExecutar = null;
        this.dispositivo = null;
        this.acaoEscalonamento = "";

    }

    // Construtor com Parametros
    public Escalonamento(String descricao, LocalTime horaAExecutar, Dispositivo dispositivo, String acaoEscalonamento) {
        this.descricao = descricao;
        this.horaAExecutar = horaAExecutar;
        this.dispositivo = dispositivo;
        this.acaoEscalonamento = acaoEscalonamento;
    }

    // Construtor de Cópia
    public Escalonamento(Escalonamento outroEscalonamento) {
        this.descricao = outroEscalonamento.descricao;
        this.horaAExecutar = outroEscalonamento.horaAExecutar;
        this.dispositivo = outroEscalonamento.dispositivo;
        this.acaoEscalonamento = outroEscalonamento.acaoEscalonamento;
    }

    // Getters
    public String getDescricao() {
        return this.descricao;
    }

    public LocalTime getHoraAExecutar() {
        return this.horaAExecutar;
    }

    public Dispositivo getDispositivo() {
        return this.dispositivo;
    }

    public String getAcaoEscalonamento() {
        return this.acaoEscalonamento;
    }

    // Setters
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setHoraAExecutar(LocalTime horaAExecutar) {
        this.horaAExecutar = horaAExecutar;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public void setAcaoEscalonamento(String acaoEscalonamento) {
        this.acaoEscalonamento = acaoEscalonamento;
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
        Escalonamento escalonamento = (Escalonamento) obj;
        return this.descricao.equals(escalonamento.descricao) &&
                this.horaAExecutar.equals(escalonamento.horaAExecutar) &&
                this.dispositivo.equals(escalonamento.dispositivo) &&
                this.acaoEscalonamento.equals(escalonamento.acaoEscalonamento);
    }

    // Método toString
    @Override
    public String toString() {
        // Limpa a ação para ficar bem(ex: "abertura:100" passa a "abertura: 100")
        String acaoLimpa = this.acaoEscalonamento.replace(":", ": ");

        String dispInfo = this.dispositivo.getClass().getSimpleName() + " [" + this.dispositivo.getId() + "]";
        
        return String.format(" %02d:%02d | %s -> %s | Ação: %s",
                this.horaAExecutar.getHour(), 
                this.horaAExecutar.getMinute(),
                this.descricao, 
                dispInfo, 
                acaoLimpa);
    }   
    

    // Método clone
    @Override
    public Escalonamento clone() {
        return new Escalonamento(this);
    }

    // Método para executar um escalonamento
    public void executarEscalonamento(LocalTime horaMomento) {

        // Se a horaMomento for igual à horaAExecutar
        if (horaMomento.getHour() == horaAExecutar.getHour() && horaMomento.getMinute() == horaAExecutar.getMinute()) {

            // Divide a ação pelo ":" (se existir)
            // Exemplo: "brilho:50" -> partes[0]="brilho", partes[1]="50"
            String[] partes = acaoEscalonamento.split(":");
            String comandoBase = partes[0].trim().toLowerCase();

            // Executa Comandos Simples
            if (comandoBase.equals("ligar")) {
                dispositivo.ligar();
            } 
            else if (comandoBase.equals("desligar")) {
                dispositivo.desligar();
            } 
            // Executa Comandos Específicos com Valores (Garante que liga primeiro)
            else if (partes.length > 1) {
                dispositivo.ligar(); 
                int valor = Integer.parseInt(partes[1].trim());

                if (comandoBase.equals("volume") && dispositivo instanceof ColunaSom) {
                    ((ColunaSom) dispositivo).setVolume(valor);
                } 
                else if (comandoBase.equals("abertura")) {
                    if (dispositivo instanceof PortoesGaragem) {
                        ((PortoesGaragem) dispositivo).definirAbertura(valor);
                    } else if (dispositivo instanceof Cortina) {
                        ((Cortina) dispositivo).definirAbertura(valor);
                    }
                } 
                else if (comandoBase.equals("brilho") && dispositivo instanceof Lampada) {
                    ((Lampada) dispositivo).setBrilho(valor);
                } 
                else if (comandoBase.equals("cor") && dispositivo instanceof Lampada) {
                    ((Lampada) dispositivo).setTempCor(valor);
                }
            }

            System.out.println(" [Escalonamento Executado] " + descricao + " -> Ação: " + acaoEscalonamento);
        }
    }
}
