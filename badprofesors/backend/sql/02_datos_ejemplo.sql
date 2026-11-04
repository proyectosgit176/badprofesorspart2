-- BadProfesors: datos de ejemplo (todos inventados)

INSERT INTO cursos (nombre) VALUES
    ('Quinto primera'), ('Quinto segunda'), ('Quinto tercera'), ('Quinto cuarta'),
    ('Quinto quinta'), ('Quinto sexta'), ('Quinto séptima'), ('Quinto octava');

INSERT INTO docentes (nombre, materia) VALUES
    ('Prof. A', 'Matemática'),
    ('Prof. B', 'Lengua'),
    ('Prof. C', 'Historia'),
    ('Prof. D', 'Física'),
    ('Prof. E', 'Inglés'),
    ('Prof. F', 'Geografía');

INSERT INTO alumnos (nombre, email, id_curso) VALUES
    ('Alumno 1', 'alumno1@mail.com', 1),
    ('Alumno 2', 'alumno2@mail.com', 1),
    ('Alumno 3', 'alumno3@mail.com', 2),
    ('Alumno 4', 'alumno4@mail.com', 3),
    ('Alumno 5', 'alumno5@mail.com', 3),
    ('Alumno 6', 'alumno6@mail.com', 4);

INSERT INTO encuestas (fecha, id_alumno, id_docente, comunicacion, cant_mejoras, comentarios, puntaje) VALUES
    ('2026-09-18', 1, 1, 'buena',     1, 'Explica claro',           12),
    ('2026-09-20', 2, 1, 'regular',   2, 'A veces aburrido',        76),
    ('2026-09-25', 3, 2, 'excelente', 0, 'Muy buena clase',          0),
    ('2026-10-02', 4, 2, 'buena',     1, '',                         30),
    ('2026-10-05', 4, 3, 'mala',      3, 'No explica nada',         100),
    ('2026-10-06', 5, 3, 'regular',   2, '',                         68),
    ('2026-10-08', 5, 4, 'regular',   2, '',                         68),
    ('2026-10-09', 6, 1, 'buena',     0, 'Fácil de entender',        12),
    ('2026-10-10', 6, 5, 'excelente', 0, '',                          2),
    ('2026-10-11', 1, 4, 'mala',      2, 'Nunca responde',          100);
