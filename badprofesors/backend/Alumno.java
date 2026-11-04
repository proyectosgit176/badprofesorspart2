// HERENCIA: Alumno "extends" Persona, así que ya tiene id y nombre
public class Alumno extends Persona {

    private String email;
    private String curso;

    public Alumno(int id, String nombre, String email, String curso) {
        super(id, nombre);   // llama al constructor de Persona
        this.email = email;
        this.curso = curso;
    }

    public String getEmail() { return email; }
    public String getCurso() { return curso; }

    // POLIMORFISMO: mismo método que Persona, pero con otro resultado
    @Override
    public String descripcion() {
        return "Alumno: " + getNombre() + " (" + curso + ")";
    }
}
