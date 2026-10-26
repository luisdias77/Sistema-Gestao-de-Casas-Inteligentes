package DomusControl.Entidades.Dispositivos;

/* A Tomada, tal como o Relé, trata-se de um dispositivo simples,
que apenas permite a passagem ou não da corrente elétrica, ou seja, ligar ou desligar. 
No entanto, a Tomada tem um atributo específico, que é a quantidade de tomadas que ela possui, 
ou seja, quantos dispositivos podem ser ligados a ela ao mesmo tempo. 
Por exemplo, uma tomada pode ter 2 entradas, permitindo que dois dispositivos sejam ligados a ela ao mesmo tempo, ou 4 entradas, etc.
Assim como o Relé, a Tomada irá herdar os atributos comuns a todos os dispositivos, que estão definidos na classe Dispositivo, 
e terá um atributo específico, que é a quantidade de tomadas.
*/

public class Tomada extends Dispositivo {

    // Atributo específico da Tomada
    private int numEntradas;

    // Construtor Vazio
    public Tomada() {
        super(); // Chamamos o construtor da classe pai (Dispositivo) para inicializar os
                 // atributos comuns a todos os dispositivos
        this.numEntradas = 1; // Inicializamos com 1, ou seja, uma entrada(tomada comum)

    }

    // Construtor com os Parametros
    public Tomada(String id, String marca, String modelo, double consumoHora, boolean ligado, int numEntradas) {
                                                                                                                 
        super(id, marca, modelo, consumoHora, ligado); // Chamamos o construtor da classe pai (Dispositivo) para
                                                       // inicializar os atributos comuns a todos os dispositivos
        this.numEntradas = numEntradas; // Inicializamos o atributo específico da Tomada
    }

    // Construtor de Cópia
    public Tomada(Tomada umaTomada) {
        super(umaTomada);
        this.numEntradas = umaTomada.numEntradas; 
    }

    // Getter
    public int getNumEntradas() {
        return numEntradas;
    }

    // Setter
    public void setNumEntradas(int numEntradas) {
        this.numEntradas = numEntradas;
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
        Tomada outraTomada = (Tomada) obj;
        return this.numEntradas == outraTomada.numEntradas; // Compara o atributo específico da Tomada
    }

    // Método toString
    @Override
    public String toString() {
        return super.toString() + String.format(" | Número de Entradas: %d", this.numEntradas);
    }

    // Método clone
    @Override
    public Tomada clone() {
        return new Tomada(this); // Retorna um novo objeto Tomada
    }

}