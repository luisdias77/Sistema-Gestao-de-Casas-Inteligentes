package DomusControl.Entidades.Dispositivos;

/* O Relé trata-se do dispositivo mais simples, na medida em que ele é apenas um 
dispositivo que apenas permite desligar ou ligar, ou seja, permitir que passe ou não
a corrente elétrica. Ele não tem atributos específicos além dos que já estão definidos na classe Dispositivo.
*/

public class Rele extends Dispositivo {

    // Construtor Vazio
    public Rele() {
        super(); // aqui chamamos o construtor da classe pai (Dispositivo) para inicializar os
                 // atributos comuns a todos os dispositivos

    }

    // Construtor com os Parametros
    public Rele(String id, String marca, String modelo, double consumoHora, boolean ligado) { // numeroAtivaco tempoTotalUso são inicializados a 0 pois o dispositivo é novo ou seja nao são dados como parametros
        super(id, marca, modelo, consumoHora, ligado); // Chamamos o construtor da classe pai (Dispositivo) para inicializar os atributos comuns a todos os dispositivos
    }

    // Construtor de Cópia
    public Rele(Rele umRele) {
        super(umRele); 
    }

    // Não construímos getters e setters para esta classe, pois o Relé não tem
    // atributos específicos além dos que já estão definidos na classe Dispositivo.


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
        return super.equals(obj); // Chama o método equals da classe pai (Dispositivo)
    }

    // Método toString
    @Override
    public String toString() {
        return super.toString(); // Chama o método toString da classe pai (Dispositivo), identificando de que tipo de dispositivo se trata, um Relé
    }

    // Método clone
    @Override
    public Rele clone() {
        return new Rele(this); // Retorna um novo objeto Rele, utilizando o construtor de cópia
    }

}
