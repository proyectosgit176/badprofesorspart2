// Una Encuesta "tiene un" Alumno y "tiene un" Docente (objetos dentro de otro objeto)
public class Encuesta {

    private Alumno alumno;
    private Docente docente;
    private String comunicacion;
    private int mejoras;
    private String comentarios;
    private int puntaje;

    // Al crear la encuesta, el puntaje se calcula solo
    public Encuesta(Alumno alumno, Docente docente, String comunicacion, int mejoras, String comentarios) {
        this.alumno = alumno;
        this.docente = docente;
        this.comunicacion = comunicacion;
        this.mejoras = mejoras;
        this.comentarios = comentarios.trim();
        this.puntaje = calcularPuntaje();
    }

    // Es la misma cuenta que hacía script.js (más puntaje = peor)
    private int calcularPuntaje() {
        int total = 0;
        if (comunicacion.equals("buena")) total = 20;
        if (comunicacion.equals("regular")) total = 50;
        if (comunicacion.equals("mala")) total = 80;

        total = total + mejoras * 8;

        String texto = comentarios.toLowerCase();
        String[] malas = {"malo", "pesimo", "peor", "horrible", "nunca", "no entiendo", "no explica", "aburrido"};
        String[] buenas = {"bueno", "claro", "fácil", "entretenido", "agradable", "excelente"};
        for (String palabra : malas) {
            if (texto.contains(palabra)) total = total + 10;
        }
        for (String palabra : buenas) {
            if (texto.contains(palabra)) total = total - 8;
        }

        if (total < 0) total = 0;
        if (total > 100) total = 100;
        return total;
    }

    // Porcentajes para las tres barras
    public int getMalo() { return puntaje; }
    public int getRegular() { return 100 - Math.abs(puntaje - 50) * 2; }
    public int getBueno() { return 100 - puntaje; }

    public String getResultado() {
        if (puntaje >= 50) return "Malo";
        if (puntaje >= 30) return "Regular";
        return "Bueno";
    }

    public String getMensaje() {
        if (puntaje >= 75) return "Muy malo. " + docente.getNombre() + " necesita mejora urgente.";
        if (puntaje >= 50) return "Malo. " + docente.getNombre() + " muestra problemas importantes.";
        if (puntaje >= 30) return "Regular. Hay áreas a mejorar para que la experiencia sea más positiva.";
        return "Aceptable. La percepción no es mala, pero siempre hay espacio para mejorar.";
    }

    public Alumno getAlumno() { return alumno; }
    public Docente getDocente() { return docente; }
    public String getComunicacion() { return comunicacion; }
    public int getMejoras() { return mejoras; }
    public String getComentarios() { return comentarios; }
    public int getPuntaje() { return puntaje; }
}
