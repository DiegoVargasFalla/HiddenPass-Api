# 🔐 HiddenPass – Gestor Seguro de Contraseñas

**HiddenPass** es una aplicación robusta y escalable para la gestión de contraseñas personales y notas privadas, con enfoque en la seguridad avanzada, cifrado híbrido y buenas prácticas de arquitectura backend moderna.  
Desarrollado con **Java + Spring Boot +  Spring Security + JWT + Gradle**, integra seguridad de extremo a extremo y persistencia cifrada con un enfoque de múltiples capas para proteger los datos sensibles.

---

## 🚀 Funcionalidades Principales

- **CRUD completo de contraseñas**
    - Crear, ver, editar y eliminar contraseñas
    - Cada contraseña puede tener: usuario, URL, valor cifrado, y nota opcional

- **Gestión de notas privadas cifradas**
    - Almacenamiento seguro de notas con cifrado híbrido de extremo a extremo

- **Generador de contraseñas seguras**
    - Función integrada para generar contraseñas aleatorias, robustas y personalizables

- **Cifrado híbrido de extremo a extremo (E2EE)**
    - Envío de datos cifrados desde el frontend con **AES + RSA**
    - El backend desencripta con clave privada

- **Persistencia segura con clave derivada**
    - Los datos se almacenan en la base de datos cifrados nuevamente, usando un esquema de **clave derivada única por usuario**
    - Esto asegura una separación entre el cifrado de transmisión y el cifrado de almacenamiento

- **Autenticación y autorización con JWT**
    - Inicio de sesión seguro mediante tokens JWT
    - Gestión de roles y permisos por endpoint (`ROLE_USER`, `ROLE_ADMIN`) etc.

---

## 🧱 Tecnologías y Arquitectura

- Java 17+
- Spring Boot + Spring Security
- Gradle como sistema de construcción
- JWT para autenticación
-  MySQL como base de datos relacional
- AES-256 + RSA-2048 para cifrado
- Arquitectura por capas (Controller, Service, Repository, DAO, Model)
- Manejo centralizado de errores y respuestas
-   Separación entre cifrado en tránsito y en reposo
- Claves derivadas por usuario para persistencia cifrada

---
