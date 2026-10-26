package DomusControl.Entidades.Dispositivos;

public class Lampada extends Dispositivo { // herda de Dispositivo

    // Atributos
    private int brilho; // 1-100%
    private int tempCor; // 2700K-4000K

    // Cosntrutor

    // parametros
    public Lampada(String id, String Marca, String Modelo, Double consumoHora, boolean ligado, int numeroAtivacoes,
            double tempoTotalUso, int brilho, int tempCor) {
        super(id, Marca, Modelo, consumoHora, ligado); // numeroAtivacoes e tempoTotalUso são inicializados a 0 pois o
                                                       // dispositivo é novo
        this.brilho = brilho;
        this.tempCor = tempCor;
    }

    // copia
    public Lampada(Lampada l) {
        super(l);
        this.brilho = l.brilho;
        this.tempCor = l.tempCor;
    }

    // Construtor Vazio
    public Lampada() {
        super();
        this.brilho = 0;
        this.tempCor = 0;
    }

    // Getters
    public int getBrilho() {
        return this.brilho;
    }

    public int getTempCor() {
        return this.tempCor;
    }

    // Setters
    public void setBrilho(int brilho) {
        if (brilho < 0) {
            this.brilho = 0;
        } else if (brilho > 100) {
            this.brilho = 100;
        } else {
            this.brilho = brilho;
        }
    }

    public void setTempCor(int tempCor) {
        if (tempCor < 2700) {
            this.tempCor = 2700;
        } else if (tempCor > 4000) {
            this.tempCor = 4000;
        } else {
            this.tempCor = tempCor;
        }
    }

    // --- Metodos especificos da lampada ---

    // Método para aumentar o brilho em 10 unidades, sem ultrapassar o máximo de 100
    public void aumentarIntensidade() {
        if (this.brilho < 100) {
            this.brilho += 10;
            if (this.brilho > 100) {
                this.brilho = 100;
            }
        }
    }

    // Método para diminuir o brilho em 10 unidades, sem ultrapassar o mínimo de 0
    public void diminuirIntensidade() {
        if (this.brilho > 0) {
            this.brilho -= 10;
            if (this.brilho < 0) {
                this.brilho = 0;
            }
        }
    }

    // Método para aumentar a temperatura de cor em 500K, sem ultrapassar o máximo
    // de 4000K
    public void aumentarTempCor() {
        if (this.tempCor < 4000) {
            this.tempCor += 500;
            if (this.tempCor > 4000) {
                this.tempCor = 4000;
            }
        }
    }

    // Método para diminuir a temperatura de cor em 500K, sem ultrapassar o mínimo
    // de 2700K
    public void diminuirTempCor() {
        if (this.tempCor > 2700) {
            this.tempCor -= 500;
            if (this.tempCor < 2700) {
                this.tempCor = 2700;
            }
        }
    }

    /* ------------------------ */
    /* - Métodos Obrigatórios - */
    /* ------------------------ */ 

    @Override
    public Lampada clone() {
        return new Lampada(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Lampada lampada = (Lampada) o;
        return brilho == lampada.brilho && tempCor == lampada.tempCor && super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Brilho: %d | TempCor: %dK", this.brilho, this.tempCor);
    }

}
