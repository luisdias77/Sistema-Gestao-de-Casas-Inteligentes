package DomusControl.Logica;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import DomusControl.Entidades.Dispositivos.ColunaSom;
import DomusControl.Entidades.Dispositivos.Cortina;
import DomusControl.Entidades.Dispositivos.Dispositivo;
import DomusControl.Entidades.Dispositivos.Lampada;
import DomusControl.Entidades.Dispositivos.PortoesGaragem;

public class Cenario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome; // Ex: "Fora de Casa", "Ver Cinema"

    // Guarda o ID do Dispositivo e a Ação que queremos ("Ligar" ou "Desligar")
    private Map<String, String> acoesDosDispositivos;

    // Construtor Vazio
    public Cenario() {
        this.nome = "";
        this.acoesDosDispositivos = new HashMap<>();
    }

    // Construtor com Parametros
    public Cenario(String nome, Map<String, String> acoesDosDispositivos) {
        this.nome = nome;
        this.acoesDosDispositivos = new HashMap<>(acoesDosDispositivos);
    }

    // Construtor de Cópia
    public Cenario(Cenario outroCenario) {
        this.nome = outroCenario.nome;
        this.acoesDosDispositivos = new HashMap<>(outroCenario.acoesDosDispositivos);
    }

    // Getters
    public String getNome() {
        return this.nome;
    }

    public Map<String, String> getAcoesDosDispositivos() {
        return this.acoesDosDispositivos;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAcoesDosDispositivos(Map<String, String> acoesDosDispositivos) {
        this.acoesDosDispositivos = new HashMap<>(acoesDosDispositivos);
    }

    public void addAcao(String idDispositivo, String acao) {
        if (this.acoesDosDispositivos.containsKey(idDispositivo)) {
            // Se já tem uma ação, junta a nova usando um ";"
            String acaoAntiga = this.acoesDosDispositivos.get(idDispositivo);
            this.acoesDosDispositivos.put(idDispositivo, acaoAntiga + "; " + acao);
        } else {
            // Se for a primeira ação para este dispositivo, guarda normal
            this.acoesDosDispositivos.put(idDispositivo, acao);
        }
    }

    public void aplicarCenario(Map<String, Dispositivo> dispositivosDaCasa) {
        for (Map.Entry<String, String> acao : this.acoesDosDispositivos.entrySet()) {
            String id = acao.getKey();
            String comandosJuntos = acao.getValue();
            
            Dispositivo d = dispositivosDaCasa.get(id);
            if (d != null) {
                // Separa os comandos pelo ";"
                String[] listaComandos = comandosJuntos.split(";");
                
                // Executa um a um
                for (String comandoCompleto : listaComandos) {
                    String[] partes = comandoCompleto.split(":");
                    String comandoBase = partes[0].trim().toLowerCase();

                    if (comandoBase.equals("ligar")) {
                        d.ligar();
                    } else if (comandoBase.equals("desligar")) {
                        d.desligar();
                    } else if (partes.length > 1) {
                        d.ligar(); // Liga antes de alterar os valores
                        int valor = Integer.parseInt(partes[1].trim());

                        if (comandoBase.equals("volume") && d instanceof ColunaSom) {
                            ((ColunaSom) d).setVolume(valor);
                        } else if (comandoBase.equals("abertura")) {
                            if (d instanceof PortoesGaragem) ((PortoesGaragem) d).definirAbertura(valor);
                            else if (d instanceof Cortina) ((Cortina) d).definirAbertura(valor);
                        } else if (comandoBase.equals("brilho") && d instanceof Lampada) {
                            ((Lampada) d).setBrilho(valor);
                        } else if (comandoBase.equals("cor") && d instanceof Lampada) {
                            ((Lampada) d).setTempCor(valor);
                        }
                    }
                }
            }
        }
    }

    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cenario cenario = (Cenario) obj;
        return this.nome.equals(cenario.getNome()) &&
                this.acoesDosDispositivos.equals(cenario.getAcoesDosDispositivos());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cenário: '").append(this.nome).append("'\n");
        
        if (this.acoesDosDispositivos.isEmpty()) {
            sb.append("    └─ (Nenhuma ação definida)\n");
        } else {
            for (Map.Entry<String, String> acao : this.acoesDosDispositivos.entrySet()) {
                String id = acao.getKey();
                
                // Troca o ";" por " | " e mete um espaço a seguir ao ":"
                String comandosLimpos = acao.getValue().replace(";", "  |  ").replace(":", ": ");
                
                sb.append("    └─ Dispositivo [").append(id).append("] -> Ação: ").append(comandosLimpos).append("\n");
            }
        }
        return sb.toString(); 
    }

    @Override
    public Cenario clone() {
        return new Cenario(this);
    }
}