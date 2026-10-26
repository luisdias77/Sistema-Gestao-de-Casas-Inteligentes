package DomusControl.Entidades.Dispositivos;

/* O PortoesGaragem é um tipo de dispositivo que tem um comportamento específico, que é o de abrir e fechar o portão da garagem.
Terá um atributo específico, que é o grau de abertura do portão, que pode variar entre 0% (portão fechado) e 100% (portão totalmente aberto).
Assim como o Relé e a Tomada, o PortoesGaragem irá herdar os atributos comuns a todos os dispositivos, que estão definidos na classe Dispositivo, e terá um atributo específico, que é o grau de abertura do portão.
*/

public class PortoesGaragem extends Dispositivo {

    private double grauAbertura;

    // Construtor Vazio
    public PortoesGaragem() {
        super();
        this.grauAbertura = 0.0; // Inicializamos com 0, ou seja, portão fechado

    }

    // Construtor com os Parametros
    public PortoesGaragem(String id, String marca, String modelo, double consumoHora, boolean ligado,
            double grauAbertura) {
        super(id, marca, modelo, consumoHora, ligado);
        this.setGrauAbertura(grauAbertura);
    }

    // Construtor de Cópia
    public PortoesGaragem(PortoesGaragem umPortao) {
        super(umPortao);
        this.setGrauAbertura(umPortao.getGrauAbertura());
    }

    // Getter
    public double getGrauAbertura() {
        return this.grauAbertura;
    }

    // Setter
    public void setGrauAbertura(double grauAbertura) {
        if (grauAbertura < 0.0) {
            this.grauAbertura = 0.0;
        } else if (grauAbertura > 100.0) {
            this.grauAbertura = 100.0;
        } else {
            this.grauAbertura = grauAbertura;
        }
    }

    // Métodos específicos do PortoesGaragem
    // Método para abrir totalmente o portão, ou seja, definir o grau de abertura
    // para 100%
    public void abrirTotalmente() {
        this.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos específicos
        this.setGrauAbertura(100.0);
    }

    // Método para fechar totalmente o portão, ou seja, definir o grau de abertura
    // para 0%
    public void fecharTotalmente() {
        this.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos específicos
        this.setGrauAbertura(0.0);
    }

    // Método para definir a abertura do portão com base em uma percentagem (0-100%)
    public void definirAbertura(double percentagem) {
        this.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos específicos
        this.setGrauAbertura(percentagem);
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
            return false; // Se o objeto é nulo ou de classes diferentes, não são iguais
        }
        if (!super.equals(obj)) {
            return false; // Chama o método equals da classe pai (Dispositivo) para comparar
        }
        PortoesGaragem p = (PortoesGaragem) obj;
        return this.grauAbertura == p.getGrauAbertura(); // Compara o atributo específico do PortoesGaragem
    }

    // Método toString
    @Override
    public String toString() {
        return super.toString() + String.format(" | Grau de Abertura: %.0f%%", this.grauAbertura);
    }

    // Método clone
    @Override
    public PortoesGaragem clone() {
        return new PortoesGaragem(this); // Retorna um novo objeto PortoesGaragem
    }

}