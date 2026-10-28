# Explicação dos Quatro Pilares da POO no Snake Game

Este documento detalha como os quatro pilares da **Programação Orientada a Objetos (POO)** — **Abstração**, **Encapsulamento**, **Herança** e **Polimorfismo** — foram aplicados de maneira prática, didática e funcional no desenvolvimento deste projeto acadêmico do **Snake Game (Jogo da Cobrinha)** em Java.

---

## 1. Introdução

O **Snake Game** é um jogo 2D clássico no qual o jogador guia uma cobra por uma grade bidimensional, coletando maçãs para crescer e pontuar, enquanto evita colidir contra as bordas do cenário ou contra o próprio corpo.

O objetivo acadêmico deste projeto é demonstrar como os conceitos teóricos de POO aprendidos em aula resolvem problemas reais de arquitetura e lógica de software:
- Sem POO, um jogo frequentemente se torna um arquivo único e desorganizado, com dezenas de variáveis globais e verificações manuais de tipo.
- Com a Programação Orientada a Objetos, cada elemento do jogo (a cobra, a fruta, o cenário e a janela) torna-se um **objeto** independente, detentor de seu próprio estado e comportamento. Isso torna o código modular, legível, fácil de testar, de manter e de apresentar ao professor.

---

## 2. Abstração

### O que é Abstração em Java?
Abstração é o princípio de modelar entidades do mundo real ou do domínio do problema concentrando-se apenas nas suas **características essenciais** e nos seus **comportamentos públicos**, ocultando detalhes de implementação que não interessam a outros componentes.

### Qual classe representa esse conceito no projeto?
A classe abstrata [`GameObject`](file:///c:/Users/matheus.souza/Documents/snake-game/src/GameObject.java) representa a abstração de qualquer objeto que exista no espaço do jogo.

### O que foi abstraído?
Tanto a cobra (`Snake`) quanto a maçã (`Apple`) compartilham a necessidade de:
1. Possuir uma posição cartesiana no grid (`x`, `y`).
2. Conhecer a escala visual do jogo (`tileSize`).
3. Serem desenhados na tela gráfica (`draw(Graphics g)`).
4. Terem seus estados reiniciados quando uma nova partida se inicia (`reset()`).

Porém, a forma *como* uma maçã é desenhada (círculo vermelho com brilho e folha) é totalmente diferente de como uma cobra é desenhada (cabeça arredondada com olhos e segmentos do corpo conectados). A classe abstrata define o **contrato essencial**, deixando a implementação detalhada para cada classe concreta.

### Trecho de código real:

```java
// Localização: src/GameObject.java
public abstract class GameObject {
    protected int x;
    protected int y;
    protected int tileSize;

    public GameObject(int x, int y, int tileSize) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
    }

    public abstract void draw(Graphics g);

    public abstract void reset();
}
```

### Explicação em linguagem simples:
Ao criar a classe abstrata `GameObject`, estamos dizendo ao Java: *"No nosso jogo, tudo o que é um objeto de jogo tem posição x, y, tamanho de célula e sabe se desenhar na tela através do método `draw`"*. Dessa forma, evitamos criar propriedades soltas e garantimos uma base sólida e previsível para qualquer elemento visual do jogo.

---

## 3. Encapsulamento

### O que é Encapsulamento?
Encapsulamento é a prática de **isolar o estado interno de um objeto**, protegendo seus dados contra alterações indevidas ou estados inconsistentes causados por agentes externos. O acesso aos dados e sua modificação ocorrem de forma controlada através de métodos específicos (getters, setters e métodos de negócio).

### Como os atributos são protegidos no projeto?
- Todos os atributos sensíveis das classes são declarados com modificadores de acesso restritivos: `private` (visíveis apenas na própria classe) e `protected` (visíveis na classe base e suas subclasses diretas).
- A lista de segmentos do corpo da cobra (`List<Point> body`) é `private`. Caso outra classe precise consultar os segmentos, o método `getBody()` retorna uma cópia somente leitura via `Collections.unmodifiableList(body)`. Isso impede que outra classe adicione ou delete nós do corpo da cobra arbitrariamente.
- A alteração de direção da cobra é estritamente validada pelo método `setDirection(Direction newDirection)`. A cobra **rejeita** comandos que invertam o movimento em 180° (por exemplo, ir de DIREITA para ESQUERDA instantaneamente), evitando que o jogador colida imediatamente com o próprio pescoço.
- O reposicionamento da maçã (`respawn(Snake snake)`) é encapsulado na classe `Apple`. Ela sorteia novas coordenadas internamente e garante que nunca surja em cima de nenhum pedaço do corpo da cobra.

### Trecho de código real:

```java
// Localização: src/Snake.java
public class Snake extends GameObject {
    private final List<Point> body;
    private Direction currentDirection;
    private Direction nextDirection;

    public void setDirection(Direction newDirection) {
        // Validação que protege a integridade das regras do jogo:
        // Bloqueia giros de 180 graus imediatos
        if (newDirection != null && !currentDirection.isOpposite(newDirection)) {
            this.nextDirection = newDirection;
        }
    }

    public List<Point> getBody() {
        // Retorna uma visão imutável para proteger o encapsulamento interno
        return Collections.unmodifiableList(body);
    }
}
```

### Explicação em linguagem simples:
Imagine se qualquer parte do código pudesse alterar diretamente a variável `currentDirection` ou esvaziar a lista `body`. Um bug no painel visual poderia quebrar a cobra no meio da partida. Com o encapsulamento, a própria classe `Snake` é a dona de suas regras: ela só muda de direção se a nova direção for válida e ninguém de fora consegue bagunçar seus segmentos.

---

## 4. Herança

### O que é Herança em Java?
Herança é o mecanismo que permite que uma classe (chamada de subclasse ou classe filha) **herde atributos e métodos** de outra classe (superclasse ou classe mãe), promovendo reutilização de código e especialização de comportamento através da palavra-chave `extends`.

### Qual é a classe-base e quais classes herdam dela?
- **Classe-base:** [`GameObject`](file:///c:/Users/matheus.souza/Documents/snake-game/src/GameObject.java)
- **Classes filhas:** [`Snake`](file:///c:/Users/matheus.souza/Documents/snake-game/src/Snake.java) e [`Apple`](file:///c:/Users/matheus.souza/Documents/snake-game/src/Apple.java)

Além disso, as classes de interface gráfica também utilizam a herança nativa do Java Swing:
- [`GamePanel`](file:///c:/Users/matheus.souza/Documents/snake-game/src/GamePanel.java) estende `JPanel`, herdando o ciclo de pintura gráfica e manipulação de eventos.
- [`GameWindow`](file:///c:/Users/matheus.souza/Documents/snake-game/src/GameWindow.java) estende `JFrame`, herdando todas as capacidades de janela desktop do sistema operacional.

### O que é herdado e o que é especializado?
- Em `GameObject`, os campos `x`, `y`, `tileSize` e os métodos de acesso `getX()`, `getY()`, `getTileSize()` são definidos uma única vez e herdados por `Snake` e `Apple`.
- `Snake` especializa a classe base adicionando uma coleção de segmentos de corpo, direções de movimento, lógica de crescimento e detecção de colisões.
- `Apple` especializa a classe base adicionando limites de grid e algoritmo de geração aleatória que desvia da cobra.

### Trecho de código real:

```java
// Localização: src/Apple.java
public class Apple extends GameObject {
    private final int gridWidth;
    private final int gridHeight;

    public Apple(int gridWidth, int gridHeight, int tileSize) {
        // Invoca o construtor da superclasse GameObject para inicializar posição e tamanho
        super(0, 0, tileSize);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        reset();
    }
}
```

```java
// Localização: src/Snake.java
public class Snake extends GameObject {
    public Snake(int startX, int startY, int tileSize, int initialLength) {
        // Reutiliza o construtor da superclasse GameObject
        super(startX, startY, tileSize);
        // ... inicializações específicas da cobra ...
    }
}
```

### Explicação em linguagem simples:
Em vez de repetir o código de coordenadas `x`, `y` e `tileSize` tanto na cobra quanto na maçã, colocamos isso em `GameObject`. Usando `extends GameObject`, a cobra e a maçã já nascem sabendo onde ficam no espaço do jogo e respeitando a mesma estrutura de dados.

---

## 5. Polimorfismo

### O que é Polimorfismo?
Polimorfismo (do grego "muitas formas") é a capacidade de **tratar objetos de diferentes classes derivadas através de uma mesma interface ou superclasse comum**, executando o comportamento especializado de cada objeto em tempo de execução (*dynamic dispatch*).

### Como o Polimorfismo é aplicado no Snake Game?
No [`GamePanel`](file:///c:/Users/matheus.souza/Documents/snake-game/src/GamePanel.java), existe uma lista genérica:
```java
List<GameObject> gameObjects = new ArrayList<>();
gameObjects.add(apple);
gameObjects.add(snake);
```
Quando o Swing solicita que a tela seja desenhada no método `paintComponent(Graphics g)`, o `GamePanel` percorre a lista de objetos e chama o método `obj.draw(boardG)`.

O `GamePanel` **não precisa saber se o objeto é uma cobra ou uma maçã**. Ele não precisa usar `if (obj instanceof Snake)` nem `if (obj instanceof Apple)`. O próprio ambiente Java identifica qual é a classe concreta e chama a implementação correspondente:
- Se for a `Apple`, executa a pintura da maçã (esfera vermelha, sombra e folha verde).
- Se for a `Snake`, executa a pintura da cobra (cabeça com olhos dinâmicos e corpo verde arredondado).

### Trecho de código real:

#### Chamada polimórfica no `GamePanel.java`:
```java
// Localização: src/GamePanel.java
/*
 * DEMONSTRAÇÃO DO POLIMORFISMO:
 * Iteramos sobre a lista de GameObjects. Cada objeto (Apple, Snake)
 * executa sua própria versão do método draw(Graphics g).
 */
for (GameObject obj : gameObjects) {
    obj.draw(boardG); // Chamada polimórfica em ação!
}
```

#### Implementação especializada na maçã (`Apple.java`):
```java
// Localização: src/Apple.java
@Override
public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    // Desenha corpo esférico vermelho, brilho e folha verde
    g2d.setColor(new Color(231, 76, 60));
    g2d.fillOval(px + padding, py + padding, size, size);
    // ...
}
```

#### Implementação especializada na cobra (`Snake.java`):
```java
// Localização: src/Snake.java
@Override
public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    // Desenha cada segmento do corpo da cobra com cantos arredondados e olhos na cabeça
    for (int i = 0; i < body.size(); i++) {
        if (i == 0) {
            g2d.setColor(new Color(46, 204, 113));
            g2d.fillRoundRect(px, py, size, size, 10, 10);
            drawEyes(g2d, px, py, size);
        } else {
            g2d.setColor(new Color(39, 174, 96));
            g2d.fillRoundRect(px + 1, py + 1, size - 2, size - 2, 8, 8);
        }
    }
}
```

### Explicação em linguagem simples:
Se no futuro quisermos adicionar novos itens no jogo (como uma fruta dourada, um obstáculo de pedra ou um bônus especial), basta criar uma nova classe que herde de `GameObject` e implementar seu método `draw`. Não precisaremos alterar nenhuma linha do laço de desenho do `GamePanel`, pois o polimorfismo se encarrega de desenhar o novo objeto perfeitamente!

---

## 6. Relação entre os Quatro Pilares

A tabela abaixo resume onde e como cada um dos quatro pilares atua de forma coordenada no projeto:

| Conceito | Onde aparece no jogo | Como funciona |
|---|---|---|
| **Abstração** | Classe abstrata [`GameObject`](file:///c:/Users/matheus.souza/Documents/snake-game/src/GameObject.java) | Define a estrutura conceitual de uma entidade no cenário (`x`, `y`, `tileSize`) e estabelece o contrato obrigatório com os métodos abstratos `draw(Graphics g)` e `reset()`. |
| **Encapsulamento** | Classes [`Snake`](file:///c:/Users/matheus.souza/Documents/snake-game/src/Snake.java), [`Apple`](file:///c:/Users/matheus.souza/Documents/snake-game/src/Apple.java) e [`GamePanel`](file:///c:/Users/matheus.souza/Documents/snake-game/src/GamePanel.java) | Atributos privados (`body`, `score`, `direction`), acesso via métodos públicos e validações de integridade (como bloqueio de reversão de 180° e proteção de listas imutáveis). |
| **Herança** | `Snake extends GameObject`<br>`Apple extends GameObject`<br>`GamePanel extends JPanel`<br>`GameWindow extends JFrame` | As subclasses concretas herdam campos de posicionamento e lógica base, evitando duplicação de código e especializando os comportamentos específicos. |
| **Polimorfismo** | Lista `List<GameObject>` e método sobrescrito `@Override public void draw(Graphics g)` | O `GamePanel` renderiza todos os objetos da partida através de uma única coleção de `GameObject`, acionando a rotina gráfica própria de cada elemento em tempo de execução. |

---

## 7. Conclusão

O desenvolvimento deste Snake Game proporcionou uma visão clara e prática de como os quatro pilares da Programação Orientada a Objetos se complementam para construir um software limpo, robusto e escalável:

1. A **Abstração** permitiu conceber as entidades do jogo em um nível elevado de projeto, separando a ideia de "objeto desenhável na grade" das particularidades de cada item.
2. O **Encapsulamento** blindou as regras críticas do jogo, como a movimentação sem colisões acidentais contra o próprio pescoço e a proteção contra alterações externas de estado.
3. A **Herança** possibilitou o reaproveitamento natural de atributos comuns e a integração harmoniosa com a biblioteca gráfica nativa do Java (Swing/AWT).
4. O **Polimorfismo** desacoplou a camada de renderização do painel principal, permitindo que múltiplos objetos de naturezas distintas sejam manipulados por um mesmo canal abstrato.

Com essa estrutura, o projeto atinge plenamente seus objetivos acadêmicos e pedagógicos, entregando um jogo interativo completo, com código legível e pronto para ser explicado com convicção durante a apresentação em sala de aula.
