-- BadProfesors: creación de las tablas

CREATE TABLE IF NOT EXISTS cursos (
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS alumnos (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre   TEXT NOT NULL,
    email    TEXT NOT NULL UNIQUE,
    id_curso INTEGER NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES cursos(id)
);

CREATE TABLE IF NOT EXISTS docentes (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre  TEXT NOT NULL,
    materia TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS encuestas (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha        TEXT NOT NULL DEFAULT CURRENT_DATE,
    id_alumno    INTEGER NOT NULL,
    id_docente   INTEGER NOT NULL,
    comunicacion TEXT NOT NULL,
    cant_mejoras INTEGER NOT NULL DEFAULT 0,
    comentarios  TEXT,
    puntaje      INTEGER NOT NULL CHECK (puntaje BETWEEN 0 AND 100),
    FOREIGN KEY (id_alumno)  REFERENCES alumnos(id),
    FOREIGN KEY (id_docente) REFERENCES docentes(id)
);
