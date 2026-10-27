# Domus Control 🏠💡

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![POO](https://img.shields.io/badge/POO-Object_Oriented_Programming-blue?style=for-the-badge)
![CLI](https://img.shields.io/badge/Interface-CLI-darkgreen?style=for-the-badge)

**Domus Control** é um sistema robusto e interativo desenvolvido em Java para a simulação e gestão inteligente de um ambiente de *Smart Home*. 

Este projeto foi desenvolvido no âmbito da Unidade Curricular de **Programação Orientada aos Objetos (POO)** da **Universidade do Minho**, com o objetivo de aplicar conceitos avançados de arquitetura de software, design patterns e estruturação de dados.

---

## ✨ Principais Funcionalidades

O sistema opera através de uma interface de linha de comandos (CLI) intuitiva e divide-se em 4 eixos principais:

### 1. Gestão de Entidades e Infraestrutura
- **Multihabitação:** Capacidade de criar e gerir múltiplas casas em simultâneo.
- **Controlo de Acessos:** Sistema de *login* com diferenciação de permissões entre **Administradores** e **Utilizadores Normais**.
- **Mapeamento Físico:** Organização lógica dos dispositivos por divisões (Sala, Quarto, Garagem, etc.).
- **Catálogo de Dispositivos:** Suporte para Lâmpadas, Tomadas, Relés, Cortinas, Colunas de Som e Portões de Garagem, cada um com parâmetros específicos (ex: temperatura de cor, grau de abertura, volume).

### 2. Inteligência e Lógica de Controlo 🧠
O verdadeiro "cérebro" da casa inteligente, desenvolvido com um motor de execução universal que interpreta comandos em *String* (ex: `"abertura:50"`):
- **🎬 Cenários:** Agrupamento de múltiplas ações em simultâneo (ex: Cenário "Sair de Casa" desliga todas as luzes e fecha os portões com um só comando).
- **⏱️ Escalonamentos:** Agendamento de tarefas baseadas na hora do dia (utilizando `LocalTime`).
- **⚙️ Automações (Triggers):** Reações em cadeia entre aparelhos (ex: "Se fechar as cortinas, ligar a lâmpada com brilho a 30%").
- **💡 Sugestões Inteligentes:** O sistema atua como um observador. Se detetar que o utilizador repete a mesma ação no mesmo horário 3 vezes (ex: Desligar a luz da garagem às 14:00), sugere automaticamente a criação de um escalonamento, aprendendo com os hábitos do utilizador.

### 3. Monitorização e Estatísticas 📊
Acompanhamento do estado físico do hardware através de métricas precisas:
- **Globais:** Casa com maior consumo energético (kWh) e Top 3 Divisões com mais dispositivos.
- **Locais (Por Casa):** Top 3 dispositivos mais ativados e Top 3 dispositivos com maior tempo de uso.

### 4. Persistência de Dados 💾
Nenhuma configuração é perdida. O sistema utiliza a interface `Serializable` do Java para guardar e carregar instantaneamente todo o estado da aplicação (casas, utilizadores, dispositivos, automações e histórico) em ficheiros binários (`.dat`).

---

## 🏗️ Arquitetura e Princípios POO

O **Domus Manager** foi construído com forte ênfase nas boas práticas de engenharia de software:

* **Separação de Responsabilidades:** Lógica de negócio (`DomusManager`) totalmente isolada da camada de apresentação visual (`TextUI`).
* **Herança e Polimorfismo:** Todos os equipamentos herdam da superclasse abstrata `Dispositivo`. Isto permitiu criar coleções polimórficas (ex: uma lista de dispositivos numa divisão) onde o sistema pode invocar ações universais sem precisar de saber a classe específica em tempo de compilação, utilizando mecanismos como `instanceof`.
* **Encapsulamento e Segurança:** Utilização intensiva de coleções dinâmicas (ex: `HashMap`, `Set`) e do método `clone()` para garantir cópias profundas (*deep copies*) na memória, evitando a corrupção de dados originais ao manipular cenários ou estados da casa.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) instalado (versão 8 ou superior).

### Instalação

1. Clone este repositório para a sua máquina local:
   ```bash
   git clone https://github.com/o-teu-user/domus-control.git
   ```
2. Navegue até ao diretório do projeto:
   ```bash
   cd domus-control/src
   ```
3. Compile os ficheiros Java:
   ```bash
   javac *.java
   ```
4. Execute o programa principal:
   ```bash
   java TextUI
   ```

---

## 👥 Autores

Projeto desenvolvido pelo **Grupo 46** de POO (Universidade do Minho):

* **Gabriel Matos Rodrigues** (a110987)
* **Luís Araújo Dias** (a111474)
* **Luís Pedro Matos Machado** (a110729)

---
*Relatório completo da arquitetura e decisões de design disponível nos ficheiros do repositório (Relatório POO.pdf).*
