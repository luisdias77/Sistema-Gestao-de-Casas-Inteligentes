package DomusControl.Entidades.Dispositivos;

public class ColunaSom extends Dispositivo {

    // atributos
    private int volume; // 0-100%

    // construtor vazio
    public ColunaSom() {
        super();
        this.volume = 0;
    }

    // construtor com parametros
    public ColunaSom(String id, String Marca, String Modelo, Double consumoHora, boolean ligado, int numeroAtivacoes,
            double tempoTotalUso, int volume) {
        super(id, Marca, Modelo, consumoHora, ligado); // numeroAtivacoes e tempoTotalUso são inicializados a 0 pois o
                                                       // dispositivo é novo
        this.setVolume(volume);
    }

    // construtor de copia
    public ColunaSom(ColunaSom c) {
        super(c);
        this.setVolume(c.getVolume());
    }

    // getters
    public int getVolume() {
        return this.volume;
    }

    // setters
    public void setVolume(int volume) {
        if (volume < 0) {
            this.volume = 0;
        } else if (volume > 100) {
            this.volume = 100;
        } else {
            this.volume = volume;
        }
    }

    // --- Métodos específicos da coluna de som ---

    // Método para aumentar o volume em 10 unidades, sem ultrapassar o máximo de 100
    public void aumentarVolume() {
        if (this.volume < 100) {
            this.volume += 10;
            if (this.volume > 100) {
                this.volume = 100;
            }
        }
    }

    // Método para diminuir o volume em 10 unidades, sem ultrapassar o mínimo de 0
    public void diminuirVolume() {
        if (this.volume > 0) {
            this.volume -= 10;
            if (this.volume < 0) {
                this.volume = 0;
            }
        }
    }

    // Método para silenciar a coluna de som, definindo o volume para 0
    public void silenciar() {
        this.volume = 0;
    }


    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */ 

    public ColunaSom clone() {
        return new ColunaSom(this);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ColunaSom colunaSom = (ColunaSom) o;
        return volume == colunaSom.volume && super.equals(o);
    }

    public String toString() {
        return super.toString() + String.format(" | Volume: %d%%", this.volume);
    }

}
