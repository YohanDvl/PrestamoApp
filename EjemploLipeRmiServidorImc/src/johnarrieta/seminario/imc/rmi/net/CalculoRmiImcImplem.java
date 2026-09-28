package johnarrieta.seminario.imc.rmi.net;

import johnarrieta.seminario.imc.rmi.lib.DatosImc;
import johnarrieta.seminario.imc.rmi.lib.IRemotaCalculoImc;

/**
 *
 * @author JOHN CARLOS ARRIETA ARRIETA
 */
public class CalculoRmiImcImplem implements IRemotaCalculoImc {
    
    private DatosImc datos;

    public CalculoRmiImcImplem() {
    }
    
    /*
    public DatosImc calcularImc() {
    
    */
    @Override
    public DatosImc calcularImc(DatosImc datos) {
        
        float resultado = 0;
        if (datos.getPeso() <= 0 || datos.getAltura() <= 0) {
            datos.setInterpretacion("ERROR: El peso y la altura deben ser mayores que 0");
            return datos;
        } else {
            resultado = datos.getPeso() / (datos.getAltura() * datos.getAltura());
            datos.setResultado(resultado);
            if (resultado < 18.5) {
                datos.setInterpretacion("Debes consultar un Medico, tu peso es muy bajo");
            } else if (resultado >= 18.5 && resultado <= 24.9) {
                datos.setInterpretacion("Estas bien de peso");
            } else if (resultado > 24.9 && resultado <= 29.9) {
                datos.setInterpretacion("Debes bajar un poco de peso");
            } else {
                datos.setInterpretacion("Debes consultar un Medico, tu peso es muy alto");
            }
            return datos;
        }
    }
}