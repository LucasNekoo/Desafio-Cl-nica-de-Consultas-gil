import java.io.Serializable;
import java.time.LocalDateTime;

public class Agendamento implements Serializable {
    private static final long serialVersionUID = 1L;
    private Paciente paciente;
    private LocalDateTime dataHora;
    private String especialidade;

    public Agendamento(Paciente paciente, LocalDateTime dataHora, String especialidade) {
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.especialidade = especialidade;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getEspecialidade() {
        return especialidade;
    }
}
