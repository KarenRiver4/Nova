# Nova · Aplicación móvil para el control de gastos personales

**Proyecto final – Desarrollo Web y Móvil**
**Autora:** Karen Dayanna Rivera Gómez

## Descripción

Nova es una aplicación Android nativa que permite registrar y gestionar gastos personales de forma sencilla. Cada usuario crea su cuenta, inicia sesión y administra sus propios gastos, indicando el concepto, el monto, la categoría, la fecha y el método de pago. La aplicación muestra además el total gastado en el mes actual, para tener una visión rápida del dinero utilizado.

El proyecto se desarrolló con el objetivo de aplicar los conceptos vistos en el curso: autenticación local, operaciones CRUD sobre una entidad de dominio, persistencia con Room, navegación entre pantallas y una arquitectura con separación de responsabilidades (MVVM).

Escogí el dominio de gastos personales porque es un problema cotidiano, fácil de entender para cualquier usuario y que permite trabajar con distintos tipos de datos: texto, valores numéricos, fechas y listas de opciones.

## Funcionalidades principales

- **Pantalla de bienvenida** que explica el propósito de la aplicación la primera vez que se abre.
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

3. Esperar a que finalice la sincronización de Gradle. La primera vez puede tardar varios minutos, ya que se descargan las dependencias, por lo que se requiere conexión a internet.

4. Desde el **Device Manager**, iniciar un emulador con Android 7.0 (API 24) o superior. El proyecto fue probado en un emulador *Medium Phone* con API 37.

5. Ejecutar la aplicación con el botón **Run** ▶.

### Consideraciones

- La base de datos es local, por lo que en una instalación nueva no existen usuarios. Es necesario registrarse antes de iniciar sesión.
- La pantalla de bienvenida solo aparece la primera vez que se abre la aplicación. Para verla de nuevo, se debe desinstalar la app o borrar sus datos desde los ajustes del dispositivo.
- El cálculo del **total del mes** utiliza la fecha del dispositivo. Si el emulador está configurado en la zona horaria UTC, los gastos registrados en la noche pueden quedar en un mes distinto al esperado. Se recomienda configurar la zona horaria de Bogotá en *Settings → System → Date & time* del emulador.
- Si el emulador deja de responder, se puede apagar desde el **Device Manager** e iniciarlo nuevamente con la opción **Cold Boot Now**.

## Uso de la aplicación

1. La primera vez que se abre la aplicación aparece la **pantalla de bienvenida**. Al tocar **Comenzar** se pasa al inicio de sesión.
2. Si el usuario no tiene cuenta, debe seleccionar **"¿No tienes cuenta? Regístrate"**.
3. Después de registrarse, se regresa a la pantalla de inicio de sesión para ingresar con el correo y la contraseña creados.
4. En la pantalla **Mis gastos**, el botón **+** permite agregar un gasto. Los campos obligatorios son concepto, monto, categoría, fecha y método de pago; las notas son opcionales.
5. Al seleccionar un gasto se abre su detalle, desde donde es posible **editarlo, compartirlo o eliminarlo**.
6. Para cerrar sesión se utiliza la opción **Salir** de la barra superior.

## Arquitectura

La aplicación sigue el patrón **MVVM (Model – View – ViewModel)**, que separa las responsabilidades de cada capa:

- **View** (Fragments y layouts XML): muestran la información y capturan las acciones del usuario.
- **ViewModel**: contiene la lógica de cada pantalla y las validaciones, y comunica los cambios a la vista mediante LiveData.
- **Repositorio**: actúa como intermediario entre los ViewModels y la base de datos.
- **Model / Room**: entidades, DAOs y la clase de la base de datos, encargados de la persistencia local.

La navegación se implementó con **Navigation Component** bajo un esquema de una sola Activity (`MainActivity`) que aloja los distintos Fragments.

### Estructura de paquetes

```
com.karen.nova
  datos/local         Entidades (Usuario, Gasto), DAOs y BaseDatosNova
  datos/repositorio   RepositorioUsuario y RepositorioGasto
  ui/bienvenida       Pantalla informativa de bienvenida
  ui/autenticacion    Inicio de sesión y registro
  ui/lista            Lista de gastos y su adaptador
  ui/detalle          Detalle de un gasto
  ui/formulario       Formulario para crear y editar gastos
  utilidades          GestorSesion, Seguridad, Formato y Opciones
  MainActivity        Contenedor de la navegación
```

## Modelo de datos

La base de datos cuenta con dos tablas:

- **usuarios**: id, nombre, correo (único), contrasena (hash SHA-256) y fechaCreacion.
- **gastos**: id, usuarioId, concepto, monto, categoria, fecha, metodoPago, notas (opcional) y fechaCreacion.

La relación entre ambas es de **uno a muchos**: un usuario puede tener varios gastos. La columna `usuarioId` es una llave foránea hacia la tabla `usuarios`, configurada con eliminación en cascada.

Algunas decisiones de diseño:

- El **monto** se almacena como número entero (`Long`), ya que en pesos colombianos no se manejan centavos y así se evitan errores de precisión propios de los decimales.
- Las **fechas** se guardan en milisegundos, lo que facilita ordenarlas y filtrarlas.
- Las **contraseñas** se guardan como hash SHA-256, de modo que no pueden leerse aunque se acceda a la base de datos.
- La **sesión** y el registro de que la bienvenida ya se mostró se guardan en SharedPreferences.

## Posibles mejoras

- Agregar un salt aleatorio por usuario para fortalecer el hash de las contraseñas.
- Agregar filtros de gastos por categoría y por mes.
- Mostrar un gráfico con la distribución de los gastos por categoría.