# Virtus Fitness - Sistema de Gestión de Gimnasio

## 👥 Miembros del Equipo
| Nombre y Apellidos | Correo URJC | Usuario GitHub |
|:--- |:--- |:--- |
| Jorge Rodríguez Lázaro | [correo]@alumnos.urjc.es | [User_Jorge] |
| Miguel Rey Carballo | [correo]@alumnos.urjc.es | [User_Miguel] |
| Pablo Valdés Colomo | [correo]@alumnos.urjc.es | [User_Pablo] |

---

## 🎭 **Preparación: Definición del Proyecto**

### **Descripción del Tema**
Virtus Fitness es una plataforma de gestión integral para centros deportivos y gimnasios de alto rendimiento. Pertenece al sector de la salud y el bienestar (Wellness). El valor principal que aporta al usuario es la capacidad de autogestionar su entrenamiento mediante la reserva de clases dirigidas y el acceso a planes de entrenamiento personalizados, facilitando la organización tanto del cliente como del administrador del centro.

### **Entidades**
Indicar las entidades principales que gestionará la aplicación y las relaciones entre ellas:

1. **Usuario**: Perfil de la persona que accede al sistema (Anónimo, Registrado o Administrador).
2. **Clase**: Sesiones deportivas programadas (ej: CrossFit, Yoga, HIIT).
3. **Reserva**: Registro de la asistencia de un usuario a una clase específica.
4. **Plan de Entrenamiento**: Rutina de ejercicios técnica y personalizada.

**Relaciones entre entidades:**
- **Usuario - Reserva**: Un usuario puede realizar múltiples reservas para diferentes clases (1:N).
- **Clase - Reserva**: Una clase puede tener múltiples reservas de distintos usuarios hasta completar aforo (1:N).
- **Usuario - Plan de Entrenamiento**: Un usuario registrado puede tener asignado un plan de entrenamiento específico, y un plan pertenece a un único usuario (1:1).
- **Administrador - Clase/Plan**: El administrador gestiona (crea, edita, elimina) todas las Clases y Planes de entrenamiento.

### **Permisos de los Usuarios**
Describir los permisos de cada tipo de usuario e indicar de qué entidades es dueño:

* **Usuario Anónimo**: 
  - Permisos: Visualización del catálogo de clases, consulta de horarios, acceso a la página de "Sobre nosotros" y registro en la plataforma.
  - No es dueño de ninguna entidad.

* **Usuario Registrado**: 
  - Permisos: Gestión de su perfil personal, realización y cancelación de reservas de clases, y visualización de su plan de entrenamiento asignado.
  - Es dueño de: Sus propias **Reservas** y su **Perfil de Usuario**.

* **Administrador**: 
  - Permisos: Gestión completa (CRUD) de Clases, Usuarios y Planes de Entrenamiento. Supervisión de todas las reservas y acceso a estadísticas globales.
  - Es dueño de: Las **Clases** y los **Planes de Entrenamiento** del sistema.

### **Imágenes**
Indicar qué entidades tendrán asociadas una o varias imágenes:

- **Usuario**: Una imagen de perfil (avatar) por usuario.
- **Clase**: Imagen representativa de la actividad deportiva (ej: foto de la sala de Yoga).
- **Plan de Entrenamiento**: Imágenes o iconos ilustrativos para los diferentes tipos de rutinas.

### **Gráficos**
Indicar qué información se mostrará usando gráficos y de qué tipo serán:

- **Gráfico 1**: Nivel de actividad mensual - Gráfico de barras (Número de clases asistidas por el usuario al mes).
- **Gráfico 2**: Ocupación por tipo de clase - Gráfico de tarta (Distribución de reservas entre Yoga, CrossFit, etc.).
- **Gráfico 3**: Evolución de nuevas altas - Gráfico de líneas (Para uso del administrador).

### **Tecnología Complementaria**
Indicar qué tecnología complementaria se empleará:

- **Generación de PDFs**: Se utilizará la librería **iText** para generar automáticamente un comprobante de reserva en PDF cada vez que un usuario se inscriba en una clase.

### **Algoritmo o Consulta Avanzada**
Indicar cuál será el algoritmo o consulta avanzada que se implementará:

- **Algoritmo/Consulta**: Sistema de Gestión de Aforo Dinámico y Prioridad de Lista de Espera.
- **Descripción**: El algoritmo valida en tiempo real si queda cupo en una Clase. Si el aforo está completo, gestiona una lista de espera que prioriza a los usuarios en función de su antigüedad en el gimnasio y su tasa de asistencia previa (penalizando a quienes reservan y no asisten).

---

## 🛠 **Práctica 1: Maquetación de páginas web con HTML y CSS**

### **Diagrama de Navegación**
![Diagrama de Navegación](images/navigation-diagram.png)

> El usuario puede acceder desde la página principal a las clases disponibles. Los usuarios anónimos son redirigidos al login si intentan reservar. Una vez autenticado, el usuario tiene acceso a su panel personal y sus planes.

### **Capturas de Pantalla y Descripción de Páginas**

#### **1. Página Principal / Home**
![Página Principal](images/home-page.png)
> Landing page con banner motivacional, acceso rápido a las clases destacadas y botones de registro/login.

#### **2. Catálogo de Clases**
> Listado de todas las actividades con filtros por tipo e intensidad.

#### **3. Perfil de Usuario**
> Espacio personal donde se muestra el avatar, el gráfico de actividad y las reservas activas.

#### **4. Panel de Administración**
> Interfaz para que el administrador gestione los horarios y los planes de entrenamiento.

---

### **Participación de Miembros en la Práctica 1**

#### **Alumno 1 - Jorge Rodríguez Lázaro**
Responsable de la definición del modelo de datos y la maquetación de la página principal y el catálogo de clases.

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Initial README setup](URL)  | [README.md](URL)   |

---

#### **Alumno 2 - Miguel Rey Carballo**
Responsable del diseño de los perfiles de usuario y la lógica de navegación entre roles.

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [User profile layout](URL)  | [profile.html](URL)   |

---

#### **Alumno 3 - Pablo Valdés Colomo**
Responsable de la maquetación de los paneles de administración y el diseño del sistema de gráficos.

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Admin dashboard mockup](URL)  | [admin.html](URL)   |

## 🛠 **Práctica 2: Web con HTML generado en servidor**

### **Navegación y Capturas de Pantalla**

#### **Diagrama de Navegación**

Solo si ha cambiado.

#### **Capturas de Pantalla Actualizadas**

Solo si han cambiado.

### **Instrucciones de Ejecución**

#### **Requisitos Previos**
- **Java**: versión 21 o superior
- **Maven**: versión 3.8 o superior
- **MySQL**: versión 8.0 o superior
- **Git**: para clonar el repositorio

#### **Pasos para ejecutar la aplicación**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/[usuario]/[nombre-repositorio].git
   cd [nombre-repositorio]
   ```

2. **AQUÍ INDICAR LO SIGUIENTES PASOS**

#### **Credenciales de prueba**
- **Usuario Admin**: usuario: `admin`, contraseña: `admin`
- **Usuario Registrado**: usuario: `user`, contraseña: `user`

### **Diagrama de Entidades de Base de Datos**

Diagrama mostrando las entidades, sus campos y relaciones:

![Diagrama Entidad-Relación](images/database-diagram.png)

> [Descripción opcional: Ej: "El diagrama muestra las 4 entidades principales: Usuario, Producto, Pedido y Categoría, con sus respectivos atributos y relaciones 1:N y N:M."]

### **Diagrama de Clases y Templates**

Diagrama de clases de la aplicación con diferenciación por colores o secciones:

![Diagrama de Clases](images/classes-diagram.png)

> [Descripción opcional del diagrama y relaciones principales]

### **Participación de Miembros en la Práctica 2**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 3: API REST, docker y despliegue**

### **Documentación de la API REST**

#### **Especificación OpenAPI**
📄 **[Especificación OpenAPI (YAML)](/api-docs/api-docs.yaml)**

#### **Documentación HTML**
📖 **[Documentación API REST (HTML)](https://raw.githack.com/[usuario]/[repositorio]/main/api-docs/api-docs.html)**

> La documentación de la API REST se encuentra en la carpeta `/api-docs` del repositorio. Se ha generado automáticamente con SpringDoc a partir de las anotaciones en el código Java.

### **Diagrama de Clases y Templates Actualizado**

Diagrama actualizado incluyendo los @RestController y su relación con los @Service compartidos:

![Diagrama de Clases Actualizado](images/complete-classes-diagram.png)

### **Instrucciones de Ejecución con Docker**

#### **Requisitos previos:**
- Docker instalado (versión 20.10 o superior)
- Docker Compose instalado (versión 2.0 o superior)

#### **Pasos para ejecutar con docker-compose:**

1. **Clonar el repositorio** (si no lo has hecho ya):
   ```bash
   git clone https://github.com/[usuario]/[repositorio].git
   cd [repositorio]
   ```

2. **AQUÍ LOS SIGUIENTES PASOS**:

### **Construcción de la Imagen Docker**

#### **Requisitos:**
- Docker instalado en el sistema

#### **Pasos para construir y publicar la imagen:**

1. **Navegar al directorio de Docker**:
   ```bash
   cd docker
   ```

2. **AQUÍ LOS SIGUIENTES PASOS**

### **Despliegue en Máquina Virtual**

#### **Requisitos:**
- Acceso a la máquina virtual (SSH)
- Clave privada para autenticación
- Conexión a la red correspondiente o VPN configurada

#### **Pasos para desplegar:**

1. **Conectar a la máquina virtual**:
   ```bash
   ssh -i [ruta/a/clave.key] [usuario]@[IP-o-dominio-VM]
   ```
   
   Ejemplo:
   ```bash
   ssh -i ssh-keys/app.key vmuser@10.100.139.XXX
   ```

2. **AQUÍ LOS SIGUIENTES PASOS**:

### **URL de la Aplicación Desplegada**

🌐 **URL de acceso**: `https://[nombre-app].etsii.urjc.es:8443`

#### **Credenciales de Usuarios de Ejemplo**

| Rol | Usuario | Contraseña |
|:---|:---|:---|
| Administrador | admin | admin123 |
| Usuario Registrado | user1 | user123 |
| Usuario Registrado | user2 | user123 |

### **OTRA DOCUMENTACIÓN ADICIONAL REQUERIDA EN LA PRÁCTICA**

### **Participación de Miembros en la Práctica 3**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---
