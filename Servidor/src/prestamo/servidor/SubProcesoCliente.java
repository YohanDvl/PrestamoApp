package prestamo.servidor;

import prestamo.servidor.modelo.CalculoPrestamo;
import prestamo.servidor.vistas.VentanaPrincipal;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubProcesoCliente extends Thread {
    private Socket cliente;
    private String ip;
    private VentanaPrincipal ventana;

    public SubProcesoCliente(Socket cliente, VentanaPrincipal v) {
        this.cliente = cliente;
        ip = cliente.getInetAddress().getHostAddress();
        ventana = v;
    }

    @Override
    public void run() {
        try {
            CalculoPrestamo.Resultado res = calcularPrestamo();
            enviarRespuesta(res);
        } catch (Exception ex) {
            System.out.println(log() + ex.getMessage());
            ventana.getCajaLog().append(log() + ex.getMessage() + "\n");
            try {
                cliente.close();
            } catch (IOException ex1) {
            } finally {
                ServidorTcp.listaDeClientes.remove(ip);
            }
        }
    }

    public CalculoPrestamo.Resultado calcularPrestamo() throws Exception {
        DataInputStream input = null;
        try {
            input = new DataInputStream(cliente.getInputStream());
            
            String msg = "Esperando Capital: ";
            ventana.getCajaLog().append(log() + msg + "\n");
            double capital = input.readDouble();
            ventana.getCajaLog().append(log() + "CAPITAL: " + capital + "\n");
            
            msg = "Esperando Tasa: ";
            ventana.getCajaLog().append(log() + msg + "\n");
            double tasa = input.readDouble();
            ventana.getCajaLog().append(log() + "TASA: " + tasa + "\n");

            msg = "Esperando Tiempo: ";
            ventana.getCajaLog().append(log() + msg + "\n");
            double tiempo = input.readDouble();
            ventana.getCajaLog().append(log() + "TIEMPO: " + tiempo + "\n");
            
            CalculoPrestamo calculo = new CalculoPrestamo(capital, tasa, tiempo);
            CalculoPrestamo.Resultado res = calculo.calcular();
            
            ventana.getCajaLog().append(log() + "INTERES: " + res.interes + "\n");
            ventana.getCajaLog().append(log() + "TOTAL: " + res.total + "\n");
            
            return res;
        } catch (IOException ex) {
            ventana.getCajaLog().append(log() + "Error al capturar datos del cliente " + ip + "\n");
            throw new Exception("Error al capturar datos del cliente " + ip);
        }
    }

    public void enviarRespuesta(CalculoPrestamo.Resultado res) {
        Thread hiloResponde = new Thread() {
            @Override
            public void run() {
                DataOutputStream output = null;
                try {
                    output = new DataOutputStream(cliente.getOutputStream());
                    output.writeDouble(res.interes);
                    output.writeDouble(res.total);
                    output.writeUTF(res.mensaje);
                    
                    ventana.getCajaLog().append(log() + "Enviado Interes: " + res.interes + "\n");
                    ventana.getCajaLog().append(log() + "Enviado Total: " + res.total + "\n");
                    output.flush();
                    
                    enviarRespuesta(calcularPrestamo());
                    
                } catch (IOException ex) {
                    ventana.getCajaLog().append(log() + "Error al enviar datos al cliente " + ip + "\n");
                    ServidorTcp.listaDeClientes.remove(ip);
                } catch (Exception ex) {
                    ventana.getCajaLog().append(log() + "Error al leer datos del cliente " + ip + "\n");
                    try {
                        cliente.close();
                    } catch (IOException ex1) {
                    } finally {
                        ServidorTcp.listaDeClientes.remove(ip);
                    }
                }
            }
        };
        hiloResponde.start();
    }

    public Socket getCliente() {
        return cliente;
    }

    public String log() {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        return ip + " -> " + f.format(new Date()) + " - ";
    }
}
