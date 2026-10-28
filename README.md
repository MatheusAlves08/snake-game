# Snake Game em Java (POO)

Um jogo 2D clássico inspirado no **Snake Game (Jogo da Cobrinha)**, desenvolvido em Java utilizando **Java Swing** e **Java AWT**, sem dependências externas. Este projeto foi elaborado com foco didático e acadêmico para demonstrar, de forma clara e prática, os quatro pilares da **Programação Orientada a Objetos (POO)**: **Abstração**, **Encapsulamento**, **Herança** e **Polimorfismo**.

---

## 🎯 Objetivo Acadêmico

O projeto tem como finalidade aplicar os conceitos teóricos de POO em um software interativo e funcional:
- **Abstração:** modelagem de entidades do cenário através da classe abstrata base `GameObject`.
- **Encapsulamento:** proteção dos dados internos, listas imutáveis e regras de validação (como impedimento de curvas de 180° imediatas).
- **Herança:** extensão e especialização de classes (`Snake extends GameObject`, `Apple extends GameObject`, `GamePanel extends JPanel`, `GameWindow extends JFrame`).
- **Polimorfismo:** renderização polimórfica via `List<GameObject>`, permitindo desenhar múltiplos elementos por meio do método sobrescrito `draw(Graphics g)` sem necessidade de condicionais de tipo.

Para a explicação detalhada de cada pilar com exemplos do código-fonte, consulte o documento [EXPLICACAO_POO.md](EXPLICACAO_POO.md).

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java (versão 17 ou superior recomendada).
- **Interface Gráfica:** Java Swing e Java AWT (nativas da JDK, zero dependências externas).
- **Paradigma:** Programação Orientada a Objetos (POO).
- **Plataforma:** Multiplataforma (Windows, Linux, macOS).

---

## 🎮 Funcionalidades do Jogo

- **Tela Inicial (Menu):** Apresenta o nome do jogo, resumo de controles e opção de início rápido com `ESPAÇO` ou `ENTER`.
- **Movimentação Baseada em Grade:** A cobra se desloca continuamente em um grid 24x24 células (25px por célula).
- **Prevenção de Inversão de 180°:** A cobra bloqueia guinadas instantâneas na direção oposta, prevenindo colisões automáticas contra o próprio corpo.
- **Sistema de Alimento (Maçãs):**
  - Surgimento aleatório garantindo que a maçã nunca apareça sobre o corpo da cobra.
  - Ao ser coletada, a cobra cresce 1 segmento e a pontuação aumenta em +10 pontos.
- **Detecção de Colisões:**
  - Colisão com qualquer uma das quatro paredes do cenário.
  - Colisão da cabeça com os segmentos do próprio corpo.
- **Sistema de Pontuação:**
  - Exibição em tempo real da pontuação da partida no cabeçalho superior.
  - Registro da **Melhor Pontuação (Recorde)** durante a sessão.
- **Tela de Fim de Jogo (Game Over):** Exibe a pontuação final, recorde e opções para reiniciar a partida (`ESPAÇO` ou `R`) ou retornar ao menu (`ESC`).
- **Pausa da Partida:** Possibilidade de pausar e retomar a qualquer momento com a tecla `P`.
- **Interface Visual Moderna:** Paleta de cores escura com grid sutil, cobra verde esmeralda com olhos orientados na direção do movimento e maçã vermelha com folha verde e brilho 2D.

---

## 📁 Estrutura de Arquivos

```text
snake-game/
├── bin/                          # Arquivos compilados (.class)
├── src/                          # Código-fonte Java do jogo
│   ├── Apple.java                # Entidade da maçã (comida) - extends GameObject
│   ├── Direction.java            # Enum com vetores de direção e lógica anti-180°
│   ├── GameObject.java           # Classe abstrata base (Abstração / Herança / Polimorfismo)
│   ├── GamePanel.java            # Painel do jogo (loop, teclado, HUD e renderização polimórfica)
│   ├── GameState.java            # Enum dos estados da partida (MENU, RUNNING, PAUSED, GAME_OVER)
│   ├── GameWindow.java           # Janela desktop da aplicação - extends JFrame
│   ├── Main.java                 # Ponto de entrada (inicia na Event Dispatch Thread)
│   └── Snake.java                # Entidade da cobra - extends GameObject
├── test/
│   └── GameTest.java             # Suite com 71 testes automatizados de regras e POO
├── compile.bat                   # Script de compilação rápida para Windows
├── run.bat                       # Script para executar o jogo diretamente
├── EXPLICACAO_POO.md             # Documento explicativo dos 4 pilares da POO
└── README.md                     # Documentação geral do projeto
```

---

## ⌨️ Controles

| Tecla | Ação |
|---|---|
| `↑` ou `W` | Mover para Cima |
| `↓` ou `S` | Mover para Baixo |
| `←` ou `A` | Mover para a Esquerda |
| `→` ou `D` | Mover para a Direita |
| `ESPAÇO` ou `ENTER` | Iniciar jogo (no Menu) / Reiniciar partida (após Game Over) |
| `R` | Reiniciar partida (na tela de Game Over) |
| `P` | Pausar / Retomar a partida |
| `ESC` | Voltar ao Menu Inicial |

---

## 🚀 Como Compilar e Executar

### Pré-requisitos
- Ter o **JDK (Java Development Kit)** versão 17 ou superior instalado no computador.
- O utilitário `javac` e `java` devem estar configurados no `PATH` do sistema.

### Opção 1: Via Scripts Prontos (Windows)

1. **Para compilar:** dê um duplo clique no arquivo `compile.bat` ou execute no terminal:
   ```cmd
   compile.bat
   ```
2. **Para jogar:** dê um duplo clique no arquivo `run.bat` ou execute no terminal:
   ```cmd
   run.bat
   ```

### Opção 2: Linha de Comando Manual (Terminal / CMD / PowerShell)

1. Abra o terminal na pasta raiz do projeto (`snake-game`):
2. **Compile as classes:**
   ```bash
   javac -d bin src/*.java
   ```
3. **Execute o jogo:**
   ```bash
   java -cp bin Main
   ```

### Opção 3: Via Docker (Subsistema Linux no Navegador) 🐳

Ideal caso você **não queira ou não possa instalar o Java / VS Code** diretamente no seu computador. Um ambiente Linux (Ubuntu XFCE) completo com Java 21 e VS Code é executado em container e acessado pelo seu navegador.

1. **Inicie o container (execute no terminal na pasta raiz do projeto):**
   ```bash
   cd docker
   docker compose up -d --build
   ```
2. **Acesse o ambiente no navegador:**
   - Abra: `http://localhost:3000` (ou `https://localhost:3001`)
3. **Desenvolva e jogue dentro do Linux:**
   - O projeto estará montado na pasta `/config/workspace`.
   - Você pode abrir o atalho do **Visual Studio Code** na área de trabalho do navegador.
   - Para compilar e rodar o jogo pelo terminal do Linux:
     ```bash
     cd /config/workspace
     javac -d bin src/*.java
     java -cp bin Main
     ```
   - A tela gráfica do jogo aparecerá diretamente dentro da área de trabalho no navegador.
4. **Para encerrar o ambiente:**
   ```bash
   cd docker
   docker compose down
   ```

### Executar a Bateria de Testes Automatizados

Para validar todas as regras do jogo (movimento, colisões, não-sobreposição da maçã e polimorfismo):
```bash
javac -cp bin -d bin test/GameTest.java
java -cp bin GameTest
```

---

## 📖 Documentação dos Pilares de POO

Consulte o arquivo **[EXPLICACAO_POO.md](EXPLICACAO_POO.md)** para uma leitura detalhada com:
- Definições didáticas de cada pilar.
- Trechos reais de código retirados deste projeto.
- Tabela comparativa correlacionando classes, atributos e métodos a cada conceito.
- Conclusão pedagógica para apresentação em sala de aula.