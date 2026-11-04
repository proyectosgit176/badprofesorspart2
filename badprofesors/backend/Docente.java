public class Docente extends Persona {

    private String materia;

    public Docente(int id, String nombre, String materia) {
        super(id, nombre);
        this.materia = materia;
    }

    public String getMateria() { return materia; }

    @Override
    public String descripcion() {
        return "Docente: " + getNombre() + " - " + materia;
    }
}
