# 🏦 Sistema de Cobros de Cartera "CrediYa"

**CrediYa S.A.S.** es un sistema de consola desarrollado en Java para la gestión digitalizada de préstamos personales, cobros de cartera, control de empleados y clientes. El sistema implementa persistencia de datos mediante **MySQL (JDBC)** y generación de reportes automáticos en archivos planos (`.txt`).

## 📋 Descripción del Proyecto

El objetivo principal es reemplazar el manejo manual de hojas de cálculo por un software modular y robusto que garantice la integridad de los datos financieros. El sistema permite:

* Registrar y administrar **Empleados** y **Clientes**.
* Crear **Préstamos** con cálculo automático de cuotas e intereses (Amortización).
* Gestionar **Pagos** y abonos a la deuda, actualizando saldos en tiempo real.
* Generar **Reportes** operativos y financieros (Cartera vencida, préstamos activos).

## 🛠️ Tecnologías y Arquitectura

El proyecto fue construido siguiendo principios de **Programación Orientada a Objetos (POO)** y buenas prácticas de diseño:

* **Lenguaje:** Java (JDK 17+ recomendado).
* **Base de Datos:** MySQL.
* **Conectividad:** JDBC (Java Database Connectivity).
* **Patrón de Diseño:** Arquitectura en Capas (N-Tier):
    * **View:** Interfaz de usuario en consola (`MenuPrincipal`).
    * **Service:** Lógica de negocio y validaciones (`PrestamoService`, `PagoService`).
    * **Domain:** Modelos de datos y Entidades (`Cliente`, `Prestamo`, `Pago`).
    * **Data/Repository:** Acceso a datos y persistencia (`Repositories`).
* **Manejo de Errores:** Excepciones personalizadas y mensajes de error amigables en español.

## 📂 Estructura del Proyecto

El código fuente está organizado bajo la estructura de paquetes `src/main/java/com/crediya/`:

com.crediya │ ├── connection/ # Configuración de la conexión a MySQL (Singleton) ├── data/ # Implementación de Repositorios y Mappers (DAO) ├── domain/ # Modelos (POJOs), Interfaces de Repositorio y Enums de Error 
├── resources/ # Scripts SQL para creación de la BD 
├── service/ # Lógica de negocio, cálculos financieros y generación de reportes 
├── util/ # Utilidades (Scanner blindado, Gestor de Archivos) 
├── view/ # Menús y entrada/salida de datos por consola 
└── Main.java # Punto de entrada de la aplicación

## ⚙️ Configuración e Instalación

### 1. Despliegue de Base de Datos con Docker 🐳
Para levantar la base de datos sin instalaciones complejas, ejecute el siguiente comando en su terminal. Esto creará el contenedor en el puerto **3305**:

```bash
# Crear y ejecutar el contenedor
docker run --name proyecto_java -e MYSQL_ROOT_PASSWORD=admin -p 3305:3306 -d mysql:8.4.3
```
### 2.Una vez el contenedor esté corriendo, acceda a él para ejecutar el script SQL:

```Bash

# Acceder a la consola de MySQL dentro del contenedor
docker exec -it proyecto_java mysql -h localhost -u root -p
# (Ingrese la contraseña: admin)
```
Dentro de la consola SQL, copie y pegue el contenido del archivo src/main/java/com/crediya/resources/script.sql para crear las tablas.

### 2. Conexión JDBC
Verifique el archivo com/crediya/connection/Conexion.java. Asegúrese de que el puerto, usuario y contraseña coincidan con su instalación local:

Java
```
private static final String URL = "jdbc:mysql://localhost:3306/crediya_db"; // Verifique puerto (3306 o 3305)
private static final String USER = "root";
private static final String PASSWORD = "su_contraseña"; // Ajuste según su configur 
```

### 3. Ejecución
   Compile el proyecto y ejecute la clase principal com.crediya.Main.

## 🚀 Funcionalidades Principales
### 1. Gestión de Clientes y Empleados
 Registro: Validación de documentos únicos y formatos correctos (Regex).

 Consultas: Listado general y búsqueda por documento.

 Integridad: No permite eliminar registros si tienen préstamos activos asociados.

### 2. Motor de Préstamos
 Cálculo automático de la Cuota Mensual y Monto Total (Capital + Interés) al momento de la solicitud.

 Lógica de Negocio: El saldo de la deuda incluye los intereses desde el inicio.

 Edición Segura: Si se modifican las condiciones del préstamo, el sistema recalcula automáticamente los valores financieros.

### 3. Gestión de Pagos
 Registro de abonos con validación de monto (no permite pagar más de lo debido ni valores negativos).

Actualización automática del estado del préstamo a PAGADO cuando el saldo llega a cero.

Historial detallado de pagos por préstamo.

### 4. Reportes
   El sistema genera reportes en pantalla y archivos físicos .txt en la carpeta reportes_crediya/ (se crea automáticamente en la raíz del proyecto):

📄 Reporte Clientes/Empleados: Listado de personas.

📄 Cartera Vencida: Reporte de clientes morosos (filtrado con Java Stream API).

📄 Préstamos Activos: Estado actual de la cartera pendiente.