# PrestamoApp - Sistemas Distribuidos

Repositorio de practicas de Sistemas Distribuidos (Profesor John Carlos Arrieta Arrieta).

## Contenido del Repositorio

1. **Corte 1 - TCP/IP Sockets**:
   - `Cliente`: Aplicacion cliente en Java Swing con sockets TCP.
   - `Servidor`: Servidor multi-hilo en Java con sockets TCP.
   - `Presentacion_PrestamoTCP.pdf`: Diapositivas de sustentacion.

2. **RMI con LipeRMI (Calculo de IMC)**:
   - `EjemploLipeRmiLibImc`: Proyecto tipo libreria (JAR) con la interfaz remota `IRemotaCalculoImc` y la clase de transporte `DatosImc`.
   - `EjemploLipeRmiServidorImc`: Proyecto del servidor RMI que implementa el calculo de IMC y publica el servicio en el puerto 9007 usando LipeRMI.
   - `EjemploLipeRmiClienteImc`: Aplicacion cliente con interfaz grafica Swing (NetBeans) que se conecta al servidor y realiza el calculo remoto.

## Como Ejecutar los proyectos de LipeRMI

### Opcion 1: Desde NetBeans IDE
1. Abrir NetBeans IDE.
2. Ir a Archivo -> Abrir Proyecto.
3. Abrir los 3 proyectos: `EjemploLipeRmiLibImc`, `EjemploLipeRmiServidorImc` y `EjemploLipeRmiClienteImc`.
4. Ejecutar primero `EjemploLipeRmiServidorImc` (Click derecho -> Run).
5. Ejecutar luego `EjemploLipeRmiClienteImc` (Click derecho -> Run).

### Opcion 2: Ejecucion directa por consola o scripts (.bat)
- Ejecutar `ejecutar_servidor.bat` para iniciar el servidor en el puerto 9007.
- Ejecutar `ejecutar_cliente.bat` para abrir la interfaz grafica del cliente.