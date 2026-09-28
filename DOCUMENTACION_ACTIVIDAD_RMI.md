# Documentacion de Actividad: Sistemas Distribuidos con RMI y LipeRMI

**Autor:** Yohan Alexander Maldonado Santana  
**Docente:** Ing. John Carlos Arrieta Arrieta  
**Materia:** Seminario de Sistemas Distribuidos  

---

## 1. Descripcion de los Proyectos Desarrollados con LipeRMI

Siguiendo la guia practica del docente, se crearon tres proyectos modulares en Java con NetBeans:

### 1.1 Proyecto Libreria (`EjemploLipeRmiLibImc`)
Contiene los artefactos compartidos entre el servidor y el cliente:
- `DatosImc`: Clase que encapsula las propiedades del calculo (peso, altura, resultado e interpretacion). Implementa `java.io.Serializable` para permitir su transferencia a traves de la red.
- `IRemotaCalculoImc`: Interfaz que define el contrato de invocacion remota:
  ```java
  public interface IRemotaCalculoImc {
      public DatosImc calcularImc(DatosImc datos);
  }
  ```
Este proyecto compila como un archivo JAR (`EjemploLipeRmiLibImc.jar`) que se incluye como dependencia tanto en el servidor como en el cliente.

### 1.2 Proyecto Servidor (`EjemploLipeRmiServidorImc`)
Contiene la logica de negocio y el servicio de escucha:
- `CalculoRmiImcImplem`: Implementa la interfaz remota `IRemotaCalculoImc` con la formula del IMC:
  $$\text{resultado} = \frac{\text{peso}}{\text{altura}^2}$$
  Ademas, clasifica el resultado segun los rangos establecidos (bajo peso, normal, sobrepeso, obesidad).
- `Servidor`: Inicializa el `CallHandler` de LipeRMI, registra la implementacion global de la interfaz y enlaza el servidor al puerto `9007`.
- `Principal`: Clase con el metodo `main` para instanciar y arrancar el servidor.

### 1.3 Proyecto Cliente (`EjemploLipeRmiClienteImc`)
Interfaz grafica de usuario construida en Swing:
- `VentanaPrincipal`: Formulario con pestañas (JTabbedPane):
  - **CONEXION**: Permite configurar la direccion IP (por defecto `localhost`) y el puerto (`9007`). Al presionar "Conectar", se inicializa el cliente LipeRMI (`Client`) y se obtiene el stub remoto mediante `cliente.getGlobal(IRemotaCalculoImc.class)`.
  - **CALCULAR IMC**: Formulario para ingresar peso y altura. Al hacer clic en "CALCULAR", se ejecuta un hilo (`Thread`) que realiza la invocacion al metodo remoto del servidor, recibe el objeto `DatosImc` procesado y actualiza los campos de resultado e interpretacion en la interfaz grafica.
- `Principal`: Inicializa la ventana y la hace visible centrada en pantalla.

---

## 2. Comparacion: LipeRMI vs RMI Estandar de Java

### 2.1 Similitudes
- Ambos enfoques permiten la Invocacion de Metodos Remotos (RMI), haciendo que una llamada a un metodo en un objeto remoto parezca una llamada local.
- Ambos requieren que los objetos intercambiados implementen `java.io.Serializable`.
- Ambos desacoplan la definicion del servicio (interfaz) de su implementacion concreta.

### 2.2 Diferencias
| Caracteristica | Java RMI Estandar (`java.rmi`) | LipeRMI (`net.sf.lipermi`) |
| :--- | :--- | :--- |
| **Registro de Nombres** | Requiere el `rmiregistry` (registro RMI externo o local via `LocateRegistry`). | No requiere `rmiregistry`; usa un manejador directo de llamadas (`CallHandler`). |
| **Herencia Requerida** | La clase de implementacion debe heredar de `UnicastRemoteObject` o exportarse explicitamente. | No requiere heredar de ninguna clase base especial; solo implementar la interfaz. |
| **Manejo de Excepciones** | Todos los metodos de la interfaz remota deben declarar `throws RemoteException`. | Las interfaces no estan obligadas a declarar `RemoteException`, reduciendo el acoplamiento a la API. |
| **Configuracion de Red** | Suele presentar complicaciones con cortafuegos, NAT y puertos dinamicos efimeros. | Utiliza una sola conexion socket TCP en un puerto unico bien definido. |

### 2.3 Ejemplo con RMI Estandar de Java (Sin LipeRMI)

**Interfaz:**
```java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICalculoImcEstandar extends Remote {
    DatosImc calcularImc(DatosImc datos) throws RemoteException;
}
```

**Implementacion:**
```java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculoImcEstandarImpl extends UnicastRemoteObject implements ICalculoImcEstandar {
    public CalculoImcEstandarImpl() throws RemoteException {
        super();
    }
    @Override
    public DatosImc calcularImc(DatosImc datos) throws RemoteException {
        float res = datos.getPeso() / (datos.getAltura() * datos.getAltura());
        datos.setResultado(res);
        return datos;
    }
}
```

**Servidor (Registro y Publicacion):**
```java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRmiEstandar {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServicioIMC", new CalculoImcEstandarImpl());
            System.out.println("Servidor RMI Estandar listo en puerto 1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## 3. Investigacion de Otras Librerias RPC / RMI

### 3.1 gRPC (Google Remote Procedure Call)
- **Lenguajes:** Java, C++, Python, Go, Node.js, C#, etc.
- **Protocolo de transporte:** HTTP/2 (soporta streaming bidireccional, multiplexacion y compresion).
- **Serializacion:** Protocol Buffers (Protobuf), binario y tipado estricto.
- **Ventajas:** Muy alto rendimiento, interoperabilidad total entre diferentes lenguajes.

### 3.2 Apache Thrift
- **Lenguajes:** Java, C++, Python, PHP, Ruby, etc.
- **Descripcion:** Desarrollado originalmente por Facebook, permite definir servicios RPC mediante un archivo IDL y compilar clientes y servidores en multiples lenguajes.

### 3.3 Pyro4 / Pyro5 (Python Remote Objects)
- **Lenguaje:** Python.
- **Descripcion:** Es el equivalente directo de RMI en Python. Permite invocar metodos en objetos de Python que residen en otras maquinas de red casi sin configuracion de bajo nivel.