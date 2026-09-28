package johnarrieta.seminario.imc.rmi.lib;

import java.io.Serializable;

/**
 *
 * @author JOHN CARLOS ARRIETA ARRIETA
 */
public class DatosImc implements Serializable{
    
    private float peso;
    private float altura;
    private float resultado;
    private String interpretacion;

    public DatosImc() {
    }

    public DatosImc(float peso, float altura) {
        this.peso = peso;
        this.altura = altura;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getResultado() {
        return resultado;
    }

    public void setResultado(float resultado) {
        this.resultado = resultado;
    }

    public String getInterpretacion() {
        return interpretacion;
    }

    public void setInterpretacion(String interpretacion) {
        this.interpretacion = interpretacion;
    }
}