package prestamo.cliente.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VentanaPrincipal extends JFrame {
    private JButton btnConectar;
    private JButton btnCalcular;
    private JTextField campoIPServidor;
    private JTextField campoPuertoServidor;
    private JLabel txtEstado;
    
    private JTextField campoCapital;
    private JTextField campoTasa;
    private JTextField campoTiempo;
    private JLabel txtInteres;
    private JLabel txtTotal;
    private JLabel txtMensaje;

    private Socket servidor;
    private DataOutputStream out;
    private DataInputStream in;

    public VentanaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cliente Prestamo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelConexion = new JPanel(new GridLayout(4, 2, 10, 10));
        panelConexion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelConexion.add(new JLabel("DIRECCION IP:"));
        campoIPServidor = new JTextField("localhost");
        panelConexion.add(campoIPServidor);

        panelConexion.add(new JLabel("PUERTO DE RED:"));
        campoPuertoServidor = new JTextField("9007");
        panelConexion.add(campoPuertoServidor);

        panelConexion.add(new JLabel("ESTADO:"));
        txtEstado = new JLabel("Desconectado");
        txtEstado.setForeground(Color.RED);
        panelConexion.add(txtEstado);

        btnConectar = new JButton("Conectar");
        btnConectar.setForeground(new Color(0, 153, 51));
        panelConexion.add(btnConectar);

        tabbedPane.addTab("CONEXION", panelConexion);

        JPanel panelCalculo = new JPanel(new GridLayout(7, 2, 10, 10));
        panelCalculo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelCalculo.add(new JLabel("CAPITAL:"));
        campoCapital = new JTextField();
        panelCalculo.add(campoCapital);

        panelCalculo.add(new JLabel("TASA (%):"));
        campoTasa = new JTextField();
        panelCalculo.add(campoTasa);
        
        panelCalculo.add(new JLabel("TIEMPO:"));
        campoTiempo = new JTextField();
        panelCalculo.add(campoTiempo);
        
        panelCalculo.add(new JLabel("")); // Espacio
        btnCalcular = new JButton("CALCULAR");
        btnCalcular.setForeground(new Color(0, 153, 51));
        panelCalculo.add(btnCalcular);

        panelCalculo.add(new JLabel("INTERES:"));
        txtInteres = new JLabel("0.0");
        txtInteres.setForeground(Color.RED);
        panelCalculo.add(txtInteres);
        
        panelCalculo.add(new JLabel("TOTAL A PAGAR:"));
        txtTotal = new JLabel("0.0");
        txtTotal.setForeground(Color.RED);
        panelCalculo.add(txtTotal);

        panelCalculo.add(new JLabel("MENSAJE:"));
        txtMensaje = new JLabel("");
        panelCalculo.add(txtMensaje);

        tabbedPane.addTab("CALCULAR PRESTAMO", panelCalculo);

        add(new JLabel("CLIENTE PRESTAMO", SwingConstants.CENTER), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        btnConectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnConectar.getText().equalsIgnoreCase("Conectar")) {
                    try {
                        String ip = campoIPServidor.getText();
                        int puerto = Integer.parseInt(campoPuertoServidor.getText());
                        servidor = new Socket(ip, puerto);
                        out = new DataOutputStream(servidor.getOutputStream());
                        in = new DataInputStream(servidor.getInputStream());
                        
                        btnConectar.setText("Desconectar");
                        btnConectar.setForeground(Color.RED);
                        txtEstado.setText("Conectado");
                        txtEstado.setForeground(Color.GREEN);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(VentanaPrincipal.this, "ERROR AL CONECTAR:\n" + ex.getMessage());
                    }
                } else {
                    try {
                        if (servidor != null && !servidor.isClosed()) {
                            servidor.close();
                        }
                    } catch (IOException ex) {}
                    btnConectar.setText("Conectar");
                    btnConectar.setForeground(new Color(0, 153, 51));
                    txtEstado.setText("Desconectado");
                    txtEstado.setForeground(Color.RED);
                }
            }
        });

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (servidor == null || servidor.isClosed()) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Cliente OffLine, Conecte con el Servidor");
                    return;
                }
                try {
                    double capital = Double.parseDouble(campoCapital.getText());
                    double tasa = Double.parseDouble(campoTasa.getText());
                    double tiempo = Double.parseDouble(campoTiempo.getText());
                    
                    Thread hilo = new Thread() {
                        @Override
                        public void run() {
                            try {
                                out.writeDouble(capital);
                                out.writeDouble(tasa);
                                out.writeDouble(tiempo);
                                out.flush();
                                
                                double interes = in.readDouble();
                                double total = in.readDouble();
                                String msg = in.readUTF();
                                
                                SwingUtilities.invokeLater(() -> {
                                    txtInteres.setText(String.format("%.2f", interes));
                                    txtTotal.setText(String.format("%.2f", total));
                                    txtMensaje.setText(msg);
                                });
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(VentanaPrincipal.this, "ERROR con el cliente: " + ex.getMessage());
                            }
                        }
                    };
                    hilo.start();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Por favor ingrese valores numéricos validos.");
                }
            }
        });
    }
}
