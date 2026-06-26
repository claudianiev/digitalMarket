-- ========================================
-- CREACIÓN DE TABLAS PARA USUARIOS, ROLES Y PERMISOS
-- ========================================

CREATE TABLE usuarios (
    usuario_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    rol_id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE permisos (
    permiso_id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

-- Tabla intermedia Usuario <-> Rol (N:M)
CREATE TABLE usuario_rol (
    usuario_id INT NOT NULL,
    rol_id INT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(rol_id) ON DELETE CASCADE
);

-- Tabla intermedia Rol <-> Permiso (N:M)
CREATE TABLE rol_permiso (
    rol_id INT NOT NULL,
    permiso_id INT NOT NULL,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES roles(rol_id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES permisos(permiso_id) ON DELETE CASCADE
);

-- ========================================
-- CREACIÓN DE TABLAS PARA ESTUDIANTES Y CURSOS
-- ========================================

CREATE TABLE estudiantes (
    estudiante_id SERIAL PRIMARY KEY,
    usuario_id INT UNIQUE NOT NULL,
    matricula VARCHAR(50) UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id) ON DELETE CASCADE
);

CREATE TABLE cursos (
    curso_id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    duracion_horas INT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inscripciones (Estudiante <-> Curso)
CREATE TABLE inscripciones (
    inscripcion_id SERIAL PRIMARY KEY,
    estudiante_id INT NOT NULL,
    curso_id INT NOT NULL,
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) CHECK (estado IN ('En progreso','Finalizado','Aprobado','Reprobado')) DEFAULT 'En progreso',
    calificacion DECIMAL(5,2),
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(estudiante_id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(curso_id) ON DELETE CASCADE,
    UNIQUE (estudiante_id, curso_id) -- evita inscripciones duplicadas
);

-- ========================================
-- CERTIFICADOS
-- ========================================

CREATE TABLE certificados (
    certificado_id SERIAL PRIMARY KEY,
    inscripcion_id INT UNIQUE NOT NULL,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    codigo_verificacion VARCHAR(100) UNIQUE NOT NULL,
    url_pdf TEXT,
    FOREIGN KEY (inscripcion_id) REFERENCES inscripciones(inscripcion_id) ON DELETE CASCADE
);

--DROP SEQUENCE public.certificados_certificado_id_seq;

CREATE SEQUENCE public.certificados_certificado_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.cursos_curso_id_seq;

CREATE SEQUENCE public.cursos_curso_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.estudiantes_estudiante_id_seq;

CREATE SEQUENCE public.estudiantes_estudiante_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.inscripciones_inscripcion_id_seq;

CREATE SEQUENCE public.inscripciones_inscripcion_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.permisos_permiso_id_seq;

CREATE SEQUENCE public.permisos_permiso_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.roles_rol_id_seq;

CREATE SEQUENCE public.roles_rol_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.usuarios_usuario_id_seq;

CREATE SEQUENCE public.usuarios_usuario_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;