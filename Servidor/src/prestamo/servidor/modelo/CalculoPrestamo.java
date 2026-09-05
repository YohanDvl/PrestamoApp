package prestamo.servidor.modelo;

public class CalculoPrestamo {
    private double capital;
    private double tasa;
    private double tiempo;

    public static class Resultado {
        public double interes;
        public double total;
        public String mensaje;
    }

    public CalculoPrestamo() { }

    public CalculoPrestamo(double capital, double tasa, double tiempo) {
        this.capital = capital;
        this.tasa = tasa;
        this.tiempo = tiempo;
    }

    public Resultado calcular() {
        Resultado res = new Resultado();
        if (capital <= 0 || tasa <= 0 || tiempo <= 0) {
            res.mensaje = "ERROR: El capital, tasa y tiempo deben ser mayores a 0.";
            return res;
        }
        
        res.interes = capital * (tasa / 100) * tiempo;
        res.total = capital + res.interes;
        res.mensaje = "Calculo exitoso";
        return res;
    }
}
