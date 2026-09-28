package johnarrieta.seminario.imc.rmi;

import johnarrieta.seminario.imc.rmi.net.Servidor;

/**
 *
 * @author JOHN CARLOS ARRIETA ARRIETA
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Servidor servicio = new Servidor();
        try {
            servicio.iniciar();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}