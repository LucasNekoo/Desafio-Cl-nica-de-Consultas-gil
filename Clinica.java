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

    private final String PACIENTES_CSV = "pacientes.csv";
    private final String AGENDAMENTOS_CSV = "agendamentos.csv";

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
        LocalDateTime dataHoraConsulta = null;
        boolean dataValida = false;

        while (!dataValida) {
            System.out.print("Dia da consulta (dd/MM/yyyy HH:mm): ");
            String dataHoraStr = scanner.nextLine();

            try {
                dataHoraConsulta = LocalDateTime.parse(dataHoraStr, formatter);
                if (dataHoraConsulta.isBefore(LocalDateTime.now())) {
                    System.out.println("Não é possível agendar consultas retroativas.");
                } else {
                    dataValida = true;
                }
            } catch (Exception e) {
                System.out.println("Data ou hora inválida! Tente novamente.");
            }
        }

        System.out.print("Especialidade desejada: ");
        String especialidade = scanner.nextLine();

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
        try (PrintWriter writerPacientes = new PrintWriter(new FileWriter(PACIENTES_CSV));
             PrintWriter writerAgendamentos = new PrintWriter(new FileWriter(AGENDAMENTOS_CSV))) {

            for (Paciente paciente : pacientes) {
                writerPacientes.println(paciente.getNome() + "," + paciente.getTelefone());
            }

            for (Agendamento agendamento : agendamentos) {
                writerAgendamentos.println(agendamento.getPaciente().getNome() + "," +
                        agendamento.getDataHora().format(formatter) + "," + agendamento.getEspecialidade());
            }

            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try (BufferedReader readerPacientes = new BufferedReader(new FileReader(PACIENTES_CSV));
             BufferedReader readerAgendamentos = new BufferedReader(new FileReader(AGENDAMENTOS_CSV))) {

            String linha;
            while ((linha = readerPacientes.readLine()) != null) {
                String[] dados = linha.split(",");
                pacientes.add(new Paciente(dados[0], dados[1]));
            }

            while ((linha = readerAgendamentos.readLine()) != null) {
                String[] dados = linha.split(",");
                LocalDateTime dataHora = LocalDateTime.parse(dados[1], formatter);
                agendamentos.add(new Agendamento(buscarPaciente(dados[0]), dataHora, dados[2]));
            }

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado. Será criado um novo ao salvar os dados.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private Paciente buscarPaciente(String nome) {
        for (Paciente paciente : pacientes) {
            if (paciente.getNome().equals(nome)) {
                return paciente;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Clinica clinica = new Clinica();
        clinica.menuPrincipal();
    }
}
