import java.io.Serializable;

public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String telefone;

    public Paciente(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }
}
