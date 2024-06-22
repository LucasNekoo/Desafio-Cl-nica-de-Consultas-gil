import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Clinica {
    private ArrayList<Paciente> pacientes = new ArrayList<>();
    private ArrayList<Agendamento> agendamentos = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void menuPrincipal() {
        carregarDados();
        while (true) {
            exibirMenu();
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarPaciente();
                    break;
                case "2":
                    marcarConsulta();
                    break;
                case "3":
                    cancelarConsulta();
                    break;
                case "4":
                    System.out.println("Saindo do sistema...");
                    salvarDados();
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        }
    }

    private void exibirMenu() {
        System.out.println("=== Clínica de Consultas ===");
        System.out.println("1. Cadastrar Paciente");
        System.out.println("2. Marcar Consulta");
        System.out.println("3. Cancelar Consulta");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void cadastrarPaciente() {
        System.out.print("Nome do paciente: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone do paciente: ");
        String telefone = scanner.nextLine();

        for (Paciente paciente : pacientes) {
            if (paciente.getTelefone().equals(telefone)) {
                System.out.println("Paciente já cadastrado!");
                return;
            }
        }

        pacientes.add(new Paciente(nome, telefone));
        System.out.println("Paciente cadastrado com sucesso!");
    }

    private void marcarConsulta() {
        if (pacientes.isEmpty()) {
            System.out.println("Não há pacientes cadastrados.");
            return;
        }

        System.out.println("=== Pacientes Cadastrados ===");
        for (int i = 0; i < pacientes.size(); i++) {
            Paciente paciente = pacientes.get(i);
            System.out.println((i + 1) + ". " + paciente.getNome() + " - " + paciente.getTelefone());
        }

        System.out.print("Escolha o número do paciente: ");
        int escolhaPaciente = Integer.parseInt(scanner.nextLine()) - 1;

        if (escolhaPaciente < 0 || escolhaPaciente >= pacientes.size()) {
            System.out.println("Opção inválida!");
            return;
        }

        Paciente pacienteEscolhido = pacientes.get(escolhaPaciente);
        System.out.print("Dia da consulta (dd/MM/yyyy): ");
        String dia = scanner.nextLine();
        System.out.print("Hora da consulta (HH:mm): ");
        String hora = scanner.nextLine();
        System.out.print("Especialidade desejada: ");
        String especialidade = scanner.nextLine();

        LocalDateTime dataHoraConsulta;
        try {
            dataHoraConsulta = LocalDateTime.parse(dia + " " + hora, formatter);
            if (dataHoraConsulta.isBefore(LocalDateTime.now())) {
                System.out.println("Não é possível agendar consultas retroativas.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Data ou hora inválida!");
            return;
        }

        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getDataHora().equals(dataHoraConsulta)) {
                System.out.println("Horário indisponível! Por favor, escolha outro horário.");
                return;
            }
        }

        agendamentos.add(new Agendamento(pacienteEscolhido, dataHoraConsulta, especialidade));
        System.out.println("Consulta marcada com sucesso!");
    }

    private void cancelarConsulta() {
        if (agendamentos.isEmpty()) {
            System.out.println("Não há consultas agendadas.");
            return;
        }

        System.out.println("=== Consultas Agendadas ===");
        for (int i = 0; i < agendamentos.size(); i++) {
            Agendamento agendamento = agendamentos.get(i);
            System.out.println((i + 1) + ". " + agendamento.getPaciente().getNome() + " - " +
                    agendamento.getDataHora().format(formatter) + " - " + agendamento.getEspecialidade());
        }

        System.out.print("Escolha o número da consulta para cancelar: ");
        int escolhaAgendamento = Integer.parseInt(scanner.nextLine()) - 1;

        if (escolhaAgendamento < 0 || escolhaAgendamento >= agendamentos.size()) {
            System.out.println("Opção inválida!");
            return;
        }

        Agendamento agendamentoEscolhido = agendamentos.get(escolhaAgendamento);
        System.out.println("Consulta agendada para " +
                agendamentoEscolhido.getDataHora().format(formatter) + " - " + agendamentoEscolhido.getEspecialidade());
        System.out.print("Deseja cancelar essa consulta? (s/n): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("s")) {
            agendamentos.remove(agendamentoEscolhido);
            System.out.println("Consulta cancelada com sucesso!");
        }
    }

    private void salvarDados() {
        try (ObjectOutputStream oosPacientes = new ObjectOutputStream(new FileOutputStream("pacientes.dat"));
             ObjectOutputStream oosAgendamentos = new ObjectOutputStream(new FileOutputStream("agendamentos.dat"))) {
            oosPacientes.writeObject(pacientes);
            oosAgendamentos.writeObject(agendamentos);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try (ObjectInputStream oisPacientes = new ObjectInputStream(new FileInputStream("pacientes.dat"));
             ObjectInputStream oisAgendamentos = new ObjectInputStream(new FileInputStream("agendamentos.dat"))) {
            pacientes = readPacientes(oisPacientes);
            agendamentos = readAgendamentos(oisAgendamentos);
        } catch (FileNotFoundException e) {
            pacientes = new ArrayList<>();
            agendamentos = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Paciente> readPacientes(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (ArrayList<Paciente>) ois.readObject();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Agendamento> readAgendamentos(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (ArrayList<Agendamento>) ois.readObject();
    }

    public static void main(String[] args) {
        Clinica clinica = new Clinica();
        clinica.menuPrincipal();
    }
}
