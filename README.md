# Nova · Aplicación móvil para el control de gastos personales

**Proyecto final – Desarrollo Web y Móvil**
**Autora:** Karen Rivera

## Descripción

Nova es una aplicación Android nativa que permite registrar y gestionar gastos personales de forma sencilla. Cada usuario crea su cuenta, inicia sesión y administra sus propios gastos, indicando el concepto, el monto, la categoría, la fecha y el método de pago. La aplicación muestra además el total gastado en el mes actual, para tener una visión rápida del dinero utilizado.

El proyecto se desarrolló con el objetivo de aplicar los conceptos vistos en el curso: autenticación local, operaciones CRUD sobre una entidad de dominio, persistencia con Room, navegación entre pantallas y una arquitectura con separación de responsabilidades (MVVM).

Escogí el dominio de gastos personales porque es un problema cotidiano, fácil de entender para cualquier usuario y que permite trabajar con distintos tipos de datos: texto, valores numéricos, fechas y listas de opciones.

## Funcionalidades principales

- **Registro e inicio de sesión local**, con validación de formularios: correo con formato válido, contraseña de mínimo 6 caracteres, confirmación de contraseña y verificación de que el correo no esté registrado.
- **Contraseñas protegidas**: se almacenan como hash SHA-256, nunca en texto plano.
- **Sesión persistente**: la aplicación recuerda al usuario autenticado aunque se cierre.
- **Listado de gastos** con el total del mes y un estado vacío cuando no hay registros.
- **CRUD completo**: crear, consultar, editar y eliminar gastos, con confirmación antes de eliminar.
- **Compartir un gasto** con otras aplicaciones del dispositivo.
- **Datos independientes por usuario**: cada persona solo ve sus propios gastos.

## Tecnologías y versiones

| Elemento | Versión |
|---|---|
| Android Studio | Rabbit 1 (2026.2.1) |
| Lenguaje | Kotlin |
| JDK | 25 (JetBrains Runtime 25.0.3, incluido con Android Studio) |
| Compatibilidad del código | Java 11 |
| Android Gradle Plugin | 9.4.1 |
| compileSdk / targetSdk | 37 |
| minSdk | 24 (Android 7.0) |
| Base de datos | Room 2.8.5 |
| Navegación | Navigation Component 2.10.2 |
| Ciclo de vida | ViewModel y LiveData 2.11.0 |
| Interfaz | Material Components y ViewBinding |
| Tipografía | Poppins (Google Fonts) |

## Instalación y ejecución

1. Clonar el repositorio:

   ```
   git clone https://github.com/KarenRiver4/Nova.git
   ```

   También es posible descargarlo como ZIP desde GitHub (botón **Code** → **Download ZIP**) y descomprimirlo.

2. Abrir Android Studio, seleccionar **Open** y elegir la carpeta `Nova`.

3. Esperar a que finalice la