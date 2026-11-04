import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Esta clase es la única que habla con la base de datos (usa JDBC)
public class BaseDatos {

    // SQLite guarda todo en un solo archivo, no necesita servidor
    private static final String URL = "jdbc:sqlite:badprofesors.db";

    private Connection conectar() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL);
        conexion.createStatement().execute("PRAGMA foreign_keys = ON");
        return conexion;
    }

    // Crea las tablas y, si están vacías, carga los datos de ejemplo
    public void inicializar() throws Exception {
        ejecutarScript("sql/01_crear_tablas.sql");

        int cantidadCursos;
        try (Connection conexion = conectar();
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery("SELECT COUNT(*) FROM cursos")) {
            cantidadCursos = resultado.getInt(1);
        }   // acá se cierra la conexión, antes de volver a escribir en la base

        if (cantidadCursos == 0) {
            ejecutarScript("sql/02_datos_ejemplo.sql");
        }
    }

    // Lee un archivo .sql y ejecuta cada sentencia (separadas por ;)
    private void ejecutarScript(String ruta) throws Exception {
        String contenido = "";
        for (String linea : Files.readAllLines(Path.of(ruta))) {
            if (!linea.trim().startsWith("--")) contenido = contenido + linea + "\n";
        }
        try (Connection conexion = conectar(); Statement consulta = conexion.createStatement()) {
            for (String sentencia : contenido.split(";")) {
                if (!sentencia.trim().isEmpty()) consulta.execute(sentencia);
            }
        }
    }

    // SELECT: convierte cada fila en un objeto Docente
    public List<Docente> listarDocentes() throws SQLException {
        List<Docente> docentes = new ArrayList<>();
        try (Connection conexion = conectar();
             Statement consulta = conexion.createStatement();
             ResultSet filas = consulta.executeQuery("SELECT id, nombre, materia FROM docentes ORDER BY id")) {
            while (filas.next()) {
                docentes.add(new Docente(filas.getInt("id"), filas.getString("nombre"), filas.getString("materia")));
            }
        }
        return docentes;
    }

    // SELECT con JOIN: trae también el nombre del curso de cada alumno
    public List<Alumno> listarAlumnos() throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT a.id, a.nombre, a.email, c.nombre AS curso "
                   + "FROM alumnos a JOIN cursos c ON a.id_curso = c.id ORDER BY a.id";
        try (Connection conexion = conectar();
             Statement consulta = conexion.createStatement();
             ResultSet filas = consulta.executeQuery(sql)) {
            while (filas.next()) {
                alumnos.add(new Alumno(filas.getInt("id"), filas.getString("nombre"),
                                       filas.getString("email"), filas.getString("curso")));
            }
        }
        return alumnos;
    }

    // SELECT: lista los nombres de los cursos
    public List<String> listarCursos() throws SQLException {
        List<String> cursos = new ArrayList<>();
        try (Connection conexion = conectar();
             Statement consulta = conexion.createStatement();
             ResultSet filas = consulta.executeQuery("SELECT nombre FROM cursos ORDER BY id")) {
            while (filas.next()) cursos.add(filas.getString("nombre"));
        }
        return cursos;
    }

    // INSERT: crea la cuenta de un alumno (el email no se puede repetir, la tabla lo impide con UNIQUE)
    // El (SELECT ...) busca el id del curso a partir de su nombre
    public void guardarAlumno(Alumno alumno) throws SQLException {
        String sql = "INSERT INTO alumnos (nombre, email, id_curso) "
                   + "VALUES (?, ?, (SELECT id FROM cursos WHERE nombre = ?))";
        try (Connection conexion = conectar(); PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, alumno.getNombre());
            consulta.setString(2, alumno.getEmail());
            consulta.setString(3, alumno.getCurso());
            consulta.executeUpdate();
        }
    }

    // INSERT: guarda una encuesta (los ? se completan con los datos del objeto)
    public void guardarEncuesta(Encuesta encuesta) throws SQLException {
        String sql = "INSERT INTO encuestas (id_alumno, id_docente, comunicacion, cant_mejoras, comentarios, puntaje) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conectar(); PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, encuesta.getAlumno().getId());
            consulta.setInt(2, encuesta.getDocente().getId());
            consulta.setString(3, encuesta.getComunicacion());
            consulta.setInt(4, encuesta.getMejoras());
            consulta.setString(5, encuesta.getComentarios());
            consulta.setInt(6, encuesta.getPuntaje());
            consulta.executeUpdate();
        }
    }

    // SELECT con JOIN, GROUP BY y CASE: cuenta malos, regulares y buenos por curso
    public void mostrarEstadisticas() throws SQLException {
        String sql = "SELECT c.nombre AS curso, COUNT(*) AS total, "
                   + "SUM(CASE WHEN e.puntaje >= 50 THEN 1 ELSE 0 END) AS malos, "
                   + "SUM(CASE WHEN e.puntaje >= 30 AND e.puntaje < 50 THEN 1 ELSE 0 END) AS regulares, "
                   + "SUM(CASE WHEN e.puntaje < 30 THEN 1 ELSE 0 END) AS buenos "
                   + "FROM encuestas e JOIN alumnos a ON e.id_alumno = a.id JOIN cursos c ON a.id_curso = c.id "
                   + "GROUP BY c.nombre ORDER BY c.nombre";
        try (Connection conexion = conectar();
             Statement consulta = conexion.createStatement();
             ResultSet filas = consulta.executeQuery(sql)) {
            boolean hayDatos = false;
            while (filas.next()) {
                hayDatos = true;
                int total = filas.getInt("total");
                System.out.println(filas.getString("curso") + " (" + total + " encuestas)");
                System.out.println("  Malo:    " + filas.getInt("malos") * 100 / total + "%");
                System.out.println("  Regular: " + filas.getInt("regulares") * 100 / total + "%");
                System.out.println("  Bueno:   " + filas.getInt("buenos") * 100 / total + "%");
            }
            if (!hayDatos) System.out.println("Todavía no hay encuestas.");
        }
    }
}
