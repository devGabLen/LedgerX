-- LedgerX database schema
-- Run this script against the ledgerx database after creating it.

CREATE TABLE transacciones (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('INGRESO', 'GASTO')),
    monto NUMERIC(12, 2) NOT NULL CHECK (monto > 0),
    categoria VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('INGRESO', 'GASTO'))
);

INSERT INTO categorias (nombre, tipo) VALUES
    ('Salario', 'INGRESO'),
    ('Freelance', 'INGRESO'),
    ('Inversiones', 'INGRESO'),
    ('Otros ingresos', 'INGRESO'),
    ('Comida', 'GASTO'),
    ('Transporte', 'GASTO'),
    ('Vivienda', 'GASTO'),
    ('Entretenimiento', 'GASTO'),
    ('Salud', 'GASTO'),
    ('Educacion', 'GASTO'),
    ('Otros gastos', 'GASTO');
