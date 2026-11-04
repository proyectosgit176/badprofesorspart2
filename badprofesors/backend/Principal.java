import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) throws Exception {
        BaseDatos baseDatos = new BaseDatos();
        baseDatos.inicializar();
        Scanner teclado = new Scanner(System.in);

        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("=== BadProfesors ===");
            System.out.println("1. Ver docentes");
            System.out.println("2. Hacer una encuesta");
            System.out.println("3. Ver estadísticas por curso");
            System.out.println("4. Ver todas las personas");
            System.out.println("5. Crear cuenta");
            System.out.println("0. Salir");
            opcion = leerNumero(teclado, "Elegí una opción: ");

            if (opcion == 1) {
                for (Docente docente : baseDatos.listarDocentes()) {
                    System.out.println(docente.getId() + ". " + docente.descripcion());
                }
            } else if (opcion == 2) {
                hacerEncuesta(baseDatos, teclado);
            } else if (opcion == 3) {
                baseDatos.mostrarEstadisticas();
            } else if (opcion == 4) {
                verPersonas(baseDatos);
            } else if (opcion == 5) {
                crearCuenta(baseDatos, teclado);
            }
        }
        System.out.println("¡Hasta luego!");
    }

    static void hacerEncuesta(BaseDatos baseDatos, Scanner teclado) throws Exception {
        List<Alumno> alumnos = baseDatos.listarAlumnos();
        List<Docente> docentes = baseDatos.listarDocentes();

        for (Alumno alumno : alumnos) System.out.println(alumno.getId() + ". " + alumno.descripcion());
        Alumno alumno = alumnos.get(leerNumero(teclado, "Número de alumno: ") - 1);

        for (Docente docente : docentes) System.out.println(docente.getId() + ". " + docente.descripcion());
        Docente docente = docentes.get(leerNumero(teclado, "Número de docente: ") - 1);

        System.out.println("Comunicación: 1. Excelente  2. Buena  3. Regular  4. Mala");
        String[] opciones = {"excelente", "buena", "regular", "mala"};
        String comunicacion = opciones[leerNumero(teclado, "Elegí (1-4): ") - 1];

        int mejoras = leerNumero(teclado, "¿Cuántas mejoras querés pedir? (0-5): ");
        System.out.print("Comentarios (puede quedar vacío): ");
        String comentarios = teclado.nextLine();

        // Se crea el OBJETO Encuesta y después se guarda en la base de datos
        Encuesta encuesta = new Encuesta(alumno, docente, comunicacion, mejoras, comentarios);
        baseDatos.guardarEncuesta(encuesta);

        System.out.println();
        System.out.println("Resultado: " + encuesta.getMensaje());
        System.out.println("Malo    " + barra(encuesta.getMalo()) + " " + encuesta.getMalo() + "%");
        System.out.println("Regular " + barra(encuesta.getRegular()) + " " + encuesta.getRegular() + "%");
        System.out.println("Bueno   " + barra(encuesta.getBueno()) + " " + encuesta.getBueno() + "%");
    }

    static void crearCuenta(BaseDatos baseDatos, Scanner teclado) throws Exception {
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine().trim();
        System.out.print("Email: ");
        String email = teclado.nextLine().trim().toLowerCase();

        List<String> cursos = baseDatos.listarCursos();
        for (int i = 0; i < cursos.size(); i++) System.out.println((i + 1) + ". " + cursos.get(i));
        String curso = cursos.get(leerNumero(teclado, "Número de curso: ") - 1);

        // Se crea el OBJETO Alumno (id 0 porque todavía no lo tiene: lo asigna la base) y se guarda
        Alumno alumno = new Alumno(0, nombre, email, curso);
        try {
            baseDatos.guardarAlumno(alumno);
            System.out.println("Cuenta creada: " + alumno.descripcion());
        } catch (java.sql.SQLException error) {
            System.out.println("No se pudo crear la cuenta. ¿Ya existe ese email?");
        }
    }

    // POLIMORFISMO: una sola lista de Personas, y cada una se describe a su manera
    static void verPersonas(BaseDatos baseDatos) throws Exception {
        List<Persona> personas = new ArrayList<>();
        personas.addAll(baseDatos.listarDocentes());
        personas.addAll(baseDatos.listarAlumnos());
        for (Persona persona : personas) {
            System.out.println(persona.descripcion());
        }
    }

    static String barra(int porcentaje) {
        return "#".repeat(porcentaje / 5);
    }

    // Pide un número y repite la pregunta si escriben otra cosa
    static int leerNumero(Scanner teclado, String pregunta) {
        while (true) {
            System.out.print(pregunta);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException error) {
                System.out.println("Escribí un número, por favor.");
            }
        }
    }
}
