# Clínica de Consultas Ágil

## Descrição do Sistema

Este sistema foi desenvolvido para gerenciar uma clínica de consultas. As principais funcionalidades incluem o cadastro de pacientes, marcação de consultas e cancelamento de consultas. Os dados são persistidos em arquivos para garantir que eles permaneçam após a saída do sistema.

## Funcionalidades

1. **Cadastrar um paciente**: Permite cadastrar um novo paciente com nome e telefone. O sistema verifica se o telefone já está cadastrado para evitar duplicidade.
2. **Marcar uma consulta**: Lista os pacientes cadastrados e permite escolher um paciente para marcar uma consulta. O usuário deve fornecer a data, hora e especialidade da consulta. O sistema verifica se a data e hora não estão em conflito com outras consultas e se não são retroativas.
3. **Cancelar uma consulta**: Lista as consultas agendadas e permite cancelar uma consulta selecionada.
4. **Sair**: Encerra o programa e salva os dados.

## Decisões de Design

1. **Persistência de Dados**: Foi utilizado o mecanismo de serialização do Java para salvar e carregar dados de pacientes e agendamentos.
2. **Validação de Dados**: Foram incluídas validações para evitar cadastros duplicados de pacientes e marcações de consultas em horários já ocupados ou retroativos.