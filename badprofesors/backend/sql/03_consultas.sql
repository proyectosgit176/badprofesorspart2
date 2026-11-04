-- BadProfesors: consultas de ejemplo (se pueden probar en cualquier programa de SQLite)

-- 1. Ver todos los docentes
SELECT * FROM docentes;

-- 2. Encuestas de un docente (JOIN une varias tablas)
SELECT e.fecha, c.nombre AS curso, e.comunicacion, e.puntaje
FROM encuestas e
JOIN alumnos a ON e.id_alumno = a.id
JOIN cursos c  ON a.id_curso = c.id
WHERE e.id_docente = 1;

-- 3. Cantidad de encuestas por curso (GROUP BY agrupa, COUNT cuenta)
SELECT c.nombre AS curso, COUNT(*) AS total
FROM encuestas e
JOIN alumnos a ON e.id_alumno = a.id
JOIN cursos c  ON a.id_curso = c.id
GROUP BY c.nombre
ORDER BY total DESC;

-- 4. Puntaje promedio de cada docente (AVG saca el promedio)
SELECT d.nombre, d.materia, ROUND(AVG(e.puntaje), 1) AS promedio
FROM docentes d
JOIN encuestas e ON e.id_docente = d.id
GROUP BY d.id
ORDER BY promedio DESC;

-- 5. Malo / Regular / Bueno por curso (lo que muestra estadisticas.html)
SELECT c.nombre AS curso,
       COUNT(*) AS total,
       SUM(CASE WHEN e.puntaje >= 50 THEN 1 ELSE 0 END) AS malos,
       SUM(CASE WHEN e.puntaje >= 30 AND e.puntaje < 50 THEN 1 ELSE 0 END) AS regulares,
       SUM(CASE WHEN e.puntaje < 30 THEN 1 ELSE 0 END) AS buenos
FROM encuestas e
JOIN alumnos a ON e.id_alumno = a.id
JOIN cursos c  ON a.id_curso = c.id
GROUP BY c.nombre;

-- 6. Docentes que todavía no tienen encuestas (LEFT JOIN)
SELECT d.nombre, d.materia
FROM docentes d
LEFT JOIN encuestas e ON e.id_docente = d.id
WHERE e.id IS NULL;

-- 7. Modificar y borrar datos
UPDATE docentes SET materia = 'Matemática II' WHERE id = 1;
DELETE FROM encuestas WHERE id = 10;
