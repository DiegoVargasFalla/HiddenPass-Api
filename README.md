# 🔐 HiddenPass – Gestor Seguro de Contraseñas

**HiddenPass** es una aplicación robusta y escalable para la gestión de contraseñas personales y notas privadas, con enfoque en la seguridad avanzada, cifrado de conocimiento cero y buenas prácticas de arquitectura backend moderna.  
Desarrollado con **Java + Spring Boot +  Spring Security + JWT + Gradle**, integra seguridad de cifrado AES y clave derivada con vector de inicialización y salto para una persistencia segura los datos sensibles e implementando el principio de conocimiento cero.

---

## 🚀 Funcionalidades Principales

- **CRUD completo de contraseñas**
    - Crear, ver, editar y eliminar contraseñas
    - Cada contraseña puede tener: usuario, URL, contraseña, y nota opcional

- **Gestión de notas privadas cifradas**
    - Almacenamiento seguro de notas con cifrado AES y clave derivada.

- **Generador de contraseñas seguras**
    - Función integrada para generar contraseñas aleatorias, robustas y personalizables

- **Cifrado AES con clave derivadaa**
    - Envío de datos cifrados desde el frontend con **AES** a partir de clave derivada.
    - El backend almacena los datos cifrados enviados desde el cliente sin decifrar ningun tipo de datos.

- **Persistencia segura con clave derivada**
    - Los datos se almacenan usando un esquema de **clave derivada única por usuario**
    - Esto asegura una confidencialidad cumpliendo con el principio de conocimiento cero.

- **Autenticación y autorización con JWT**
    - Inicio de sesión seguro mediante tokens JWT
    - Gestión de roles y permisos por endpoint (`ROLE_USER`, `ROLE_ADMIN`) etc.

---

## 🧱 Tecnologías y Arquitectura

- Java 17+
- Spring Boot + Spring Security + Spring Data
- Gradle como sistema de construcción
- JWT para autenticación
-  MySQL como base de datos relacional
- AES-256
- Arquitectura por capas (Controller, Service, Repository, DAO, Model)
- Manejo centralizado de errores y respuestas
- Claves derivadas por usuario para persistencia cifrada
- Acceso a todas las contraseñas mediante clave maestra unica, derivandola con vector de inicializacion y salto.

---
