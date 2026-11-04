// CLASE ABSTRACTA: es la "plantilla" general. No se puede crear una Persona sola,
// solo sirven sus hijas (Alumno y Docente).
public abstract class Persona {

    // ENCAPSULAMIENTO: los datos son private, solo se leen con los getters
    private int id;
    private String nombre;

    public Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    // Método abstracto: cada hija lo escribe a su manera
    public abstract String descripcion();
}
