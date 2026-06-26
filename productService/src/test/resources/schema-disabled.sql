-- ====================================================
-- CREACIÓN DE TABLAS PARA USUARIOS, ROLES Y PERMISOS
-- ====================================================

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

-- =========================
-- PRODUCTOS
-- =========================
CREATE TABLE productos (
   producto_id BIGSERIAL PRIMARY KEY,
   nombre VARCHAR(150) NOT NULL,
   descripcion TEXT,
   precio NUMERIC(12,2) NOT NULL CHECK (precio >= 0),
   tipo_producto VARCHAR(30) NOT NULL,
   activo BOOLEAN DEFAULT TRUE,
   fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   usuario_id INT NOT NULL,
   CONSTRAINT fk_productos_usuario
       FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id) ON DELETE CASCADE
);

CREATE INDEX idx_productos_activo ON productos(activo);


-- =========================
-- ORDENES
-- =========================
CREATE TABLE ordenes (
     orden_id BIGSERIAL PRIMARY KEY,
     usuario_id BIGINT NOT NULL,
     estado VARCHAR(20) NOT NULL CHECK (estado IN ('CREADA','PAGADA','CANCELADA','FALLIDA')),
     total NUMERIC(12,2) NOT NULL CHECK (total >= 0),
     fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_orden_usuario
         FOREIGN KEY (usuario_id)
             REFERENCES usuarios(usuario_id)
             ON DELETE RESTRICT
);

CREATE INDEX idx_orden_usuario ON ordenes(usuario_id);
CREATE INDEX idx_orden_estado ON ordenes(estado);


-- =========================
-- ORDEN DETALLE
-- =========================
CREATE TABLE orden_detalle (
   orden_detalle_id BIGSERIAL PRIMARY KEY,
   orden_id BIGINT NOT NULL,
   producto_id BIGINT NOT NULL,
   precio_unitario NUMERIC(12,2) NOT NULL CHECK (precio_unitario >= 0),
   cantidad INT NOT NULL CHECK (cantidad > 0),

   CONSTRAINT fk_detalle_orden
       FOREIGN KEY (orden_id)
           REFERENCES ordenes(orden_id)
           ON DELETE CASCADE,

   CONSTRAINT fk_detalle_producto
       FOREIGN KEY (producto_id)
           REFERENCES productos(producto_id)
           ON DELETE RESTRICT
);

CREATE INDEX idx_detalle_orden ON orden_detalle(orden_id);
CREATE INDEX idx_detalle_producto ON orden_detalle(producto_id);


-- =========================
-- PAGOS
-- =========================
CREATE TABLE pagos (
   pago_id BIGSERIAL PRIMARY KEY,
   orden_id BIGINT NOT NULL,
   metodo_pago VARCHAR(50) NOT NULL,
   estado VARCHAR(20) NOT NULL CHECK (estado IN ('PENDIENTE','APROBADO','RECHAZADO')),
   referencia_externa VARCHAR(150),
   monto NUMERIC(12,2) NOT NULL CHECK (monto >= 0),
   fecha_pago TIMESTAMP,

   CONSTRAINT fk_pago_orden
       FOREIGN KEY (orden_id)
           REFERENCES ordenes(orden_id)
           ON DELETE CASCADE
);

CREATE INDEX idx_pago_orden ON pagos(orden_id);
CREATE INDEX idx_pago_estado ON pagos(estado);


-- =========================
-- ENTREGAS DIGITALES
-- =========================
CREATE TABLE entregas_digitales (
    entrega_id BIGSERIAL PRIMARY KEY,
    orden_detalle_id BIGINT NOT NULL UNIQUE,
    url_descarga TEXT NOT NULL,
    fecha_expiracion TIMESTAMP,
    limite_descargas INT DEFAULT 5 CHECK (limite_descargas >= 0),
    veces_descargado INT DEFAULT 0 CHECK (veces_descargado >= 0),

    CONSTRAINT fk_entrega_detalle
        FOREIGN KEY (orden_detalle_id)
            REFERENCES orden_detalle(orden_detalle_id)
            ON DELETE CASCADE
);

--DROP SEQUENCE public.usuarios_usuario_id_seq;

CREATE SEQUENCE public.usuarios_usuario_id_seq
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

--DROP SEQUENCE public.permisos_permiso_id_seq;

CREATE SEQUENCE public.permisos_permiso_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.productos_producto_id_seq;

CREATE SEQUENCE public.productos_producto_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.ordenes_orden_id_seq;

CREATE SEQUENCE public.ordenes_orden_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.orden_detalle_orden_detalle_id_seq;

CREATE SEQUENCE public.orden_detalle_orden_detalle_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.pagos_pago_id_seq;

CREATE SEQUENCE public.pagos_pago_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

--DROP SEQUENCE public.entregas_digitales_entrega_id_seq;

CREATE SEQUENCE public.entregas_digitales_entrega_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;