package DomusControl.Entidades.Dispositivos;

public class Cortina extends Dispositivo {

    private double grauAbertura; // 0-100%

    // Construtor vazio
    public Cortina() {
        super();
        this.grauAbertura = 0.0;
    }

    // Construtor com parâmetros
    public Cortina(String id, String marca, String modelo, double consumoHora, boolean ligado, double grauAbertura) {
        super(id, marca, modelo, consumoHora, ligado); // numeroAtivacoes e tempoTotalUso são inicializados a 0 pois o
                                                       // dispositivo é novo
        this.setGrauAbertura(grauAbertura);
    }

    // Construtor de cópia
    public Cortina(Cortina c) {
        super(c);
        this.setGrauAbertura(c.getGrauAbertura());
    }

    // Getters e Setters
    public double getGrauAbertura() {
        return grauAbertura;
    }

    public void setGrauAbertura(double grauAbertura) {
        if (grauAbertura < 0.0) {
            this.grauAbertura = 0.0;
        } else if (grauAbertura > 100.0) {
            this.grauAbertura = 100.0;
        } else {
            this.grauAbertura = grauAbertura;
        }
    }

    // --- Métodos específicos da cortina ---
    // Método para abrir totalmente a cortina, ou seja, definir o grau de abertura
    // para 100%
    public void abrirTotalmente() {
        this.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos específicos
        this.setGrauAbertura(100.0);
    }

    // Método para fechar totalmente a cortina, ou seja, definir o grau de abertura
    // para 0%
    public void fecharTotalmente() {
        this.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos específicos
        this.setGrauAbertura(0.0);
    }

    // Método para definir a abertura da cortina com base em uma percentagem (0-100%)
    public void definirAbertura(double percentagem) {
        this.ligar(); // Para garantir que o dispositivo está ligado antes de alterar os atributos específicos
        this.setGrauAbertura(percentagem);
    }

    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */ 

    public Cortina clone() {
        return new Cortina(this);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Cortina cortina = (Cortina) o;
        return Double.compare(cortina.grauAbertura, grauAbertura) == 0;
    }

    public String toString() {
        return super.toString() + String.format(" | Abertura: %.0f%%", this.grauAbertura);
    }
}