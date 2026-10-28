import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a cobra controlada pelo jogador no Snake Game.
 *
 * Pilares da Programação Orientada a Objetos:
 * - Herança: estende a classe base GameObject (extends GameObject).
 * - Encapsulamento: o corpo da cobra, as direções e as variáveis de controle são privados.
 *   Modificações de estado só ocorrem através de métodos seguros como move(), grow() e setDirection().
 *   O método getBody() retorna uma coleção não-modificável para proteger a estrutura interna.
 * - Polimorfismo: sobrescreve os métodos abstratos draw(Graphics g) e reset() da classe GameObject.
 */
public class Snake extends GameObject {
    // Encapsulamento: atributos privados
    private final List<Point> body;
    private Direction currentDirection;
    private Direction nextDirection;
    private boolean growing;
    private final int initialX;
    private final int initialY;
    private final int initialLength;

    /**
     * Construtor da classe Snake.
     *
     * @param startX posição inicial X da cabeça na grade
     * @param startY posição inicial Y da cabeça na grade
     * @param tileSize tamanho em pixels de cada célula da grade
     * @param initialLength quantidade inicial de segmentos da cobra
     */
    public Snake(int startX, int startY, int tileSize, int initialLength) {
        super(startX, startY, tileSize);
        this.initialX = startX;
        this.initialY = startY;
        this.initialLength = initialLength;
        this.body = new ArrayList<>();
        reset();
    }

    /**
     * Polimorfismo: redefine o estado inicial da cobra para uma nova partida.
     */
    @Override
    public void reset() {
        body.clear();
        this.x = initialX;
        this.y = initialY;
        this.currentDirection = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
        this.growing = false;

        // Monta o corpo inicial da cobra alinhado horizontalmente para a direita
        for (int i = 0; i < initialLength; i++) {
            body.add(new Point(initialX - i, initialY));
        }
    }

    /**
     * Atualiza a direção do próximo passo da cobra.
     * Encapsulamento: impede que o jogador inverta a direção em 180 graus instantaneamente,
     * o que causaria colisão imediata com o próprio pescoço da cobra.
     *
     * @param newDirection nova direção solicitada pelo teclado
     */
    public void setDirection(Direction newDirection) {
        if (newDirection != null && !currentDirection.isOpposite(newDirection)) {
            this.nextDirection = newDirection;
        }
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Movimenta a cobra um passo na direção selecionada.
     * Atualiza a posição da cabeça e gerencia a cauda ou o crescimento.
     */
    public void move() {
        this.currentDirection = nextDirection;

        // Pega a coordenada da cabeça atual
        Point currentHead = body.get(0);
        int newHeadX = currentHead.x + currentDirection.getDeltaX();
        int newHeadY = currentHead.y + currentDirection.getDeltaY();
        Point newHead = new Point(newHeadX, newHeadY);

        // Atualiza as coordenadas x e y herdadas da classe GameObject
        this.x = newHeadX;
        this.y = newHeadY;

        // Adiciona a nova cabeça na primeira posição
        body.add(0, newHead);

        // Se a cobra comeu maçã, ela cresce (mantém a cauda antiga); caso contrário, remove o último segmento
        if (growing) {
            growing = false;
        } else {
            body.remove(body.size() - 1);
        }
    }

    /**
     * Ativa a flag de crescimento para que o tamanho aumente no próximo passo.
     */
    public void grow() {
        this.growing = true;
    }

    /**
     * Retorna a coordenada da cabeça da cobra.
     */
    public Point getHead() {
        return body.get(0);
    }

    /**
     * Retorna a quantidade de segmentos do corpo da cobra.
     */
    public int getLength() {
        return body.size();
    }

    /**
     * Encapsulamento: retorna uma visão somente leitura da lista de segmentos
     * para evitar que código externo altere ou corrompa o corpo da cobra diretamente.
     */
    public List<Point> getBody() {
        return Collections.unmodifiableList(body);
    }

    /**
     * Verifica se qualquer segmento da cobra ocupa a coordenada (targetX, targetY).
     */
    public boolean occupies(int targetX, int targetY) {
        for (Point segment : body) {
            if (segment.x == targetX && segment.y == targetY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica colisão da cabeça com as bordas do cenário.
     *
     * @param gridWidth largura do grid em colunas
     * @param gridHeight altura do grid em linhas
     * @return true se a cabeça ultrapassou qualquer borda
     */
    public boolean checkWallCollision(int gridWidth, int gridHeight) {
        Point head = getHead();
        return head.x < 0 || head.x >= gridWidth || head.y < 0 || head.y >= gridHeight;
    }

    /**
     * Verifica se a cabeça da cobra colidiu com qualquer parte do seu próprio corpo.
     *
     * @return true se houver auto-colisão
     */
    public boolean checkSelfCollision() {
        Point head = getHead();
        // Começa do índice 1 para não comparar a cabeça com ela mesma
        for (int i = 1; i < body.size(); i++) {
            Point segment = body.get(i);
            if (head.x == segment.x && head.y == segment.y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Polimorfismo: implementação do método draw da classe GameObject específica para a cobra.
     * Renderiza cada segmento com cantos arredondados, gradiente e olhos dinâmicos na cabeça.
     *
     * @param g contexto gráfico
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 1;
        int size = tileSize - (padding * 2);

        for (int i = 0; i < body.size(); i++) {
            Point segment = body.get(i);
            int px = segment.x * tileSize + padding;
            int py = segment.y * tileSize + padding;

            if (i == 0) {
                // Desenho da Cabeça da cobra (verde esmeralda vibrante)
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillRoundRect(px, py, size, size, 10, 10);

                // Desenho dos olhos orientados de acordo com a direção do movimento
                drawEyes(g2d, px, py, size);
            } else {
                // Desenho dos segmentos do corpo com variação sutil de tom
                int colorShift = Math.min(50, i * 2);
                Color bodyColor = new Color(39 + (colorShift / 3), 174 - (colorShift / 2), 96);
                g2d.setColor(bodyColor);
                g2d.fillRoundRect(px + 1, py + 1, size - 2, size - 2, 8, 8);
            }
        }
    }

    /**
     * Desenha os olhos da cobra com esclera branca e pupilas pretas voltadas para a direção do movimento.
     */
    private void drawEyes(Graphics2D g2d, int px, int py, int size) {
        g2d.setColor(Color.WHITE);
        int eyeSize = Math.max(4, size / 5);
        int pupilSize = Math.max(2, eyeSize / 2);

        int eye1X, eye1Y, eye2X, eye2Y;
        int pupilOffsetX = 0;
        int pupilOffsetY = 0;

        switch (currentDirection) {
            case UP:
                eye1X = px + size / 4 - eyeSize / 2;
                eye1Y = py + size / 4 - eyeSize / 2;
                eye2X = px + (3 * size) / 4 - eyeSize / 2;
                eye2Y = py + size / 4 - eyeSize / 2;
                pupilOffsetY = -1;
                break;
            case DOWN:
                eye1X = px + size / 4 - eyeSize / 2;
                eye1Y = py + (3 * size) / 4 - eyeSize / 2;
                eye2X = px + (3 * size) / 4 - eyeSize / 2;
                eye2Y = py + (3 * size) / 4 - eyeSize / 2;
                pupilOffsetY = 1;
                break;
            case LEFT:
                eye1X = px + size / 4 - eyeSize / 2;
                eye1Y = py + size / 4 - eyeSize / 2;
                eye2X = px + size / 4 - eyeSize / 2;
                eye2Y = py + (3 * size) / 4 - eyeSize / 2;
                pupilOffsetX = -1;
                break;
            case RIGHT:
            default:
                eye1X = px + (3 * size) / 4 - eyeSize / 2;
                eye1Y = py + size / 4 - eyeSize / 2;
                eye2X = px + (3 * size) / 4 - eyeSize / 2;
                eye2Y = py + (3 * size) / 4 - eyeSize / 2;
                pupilOffsetX = 1;
                break;
        }

        // Desenha a parte branca dos dois olhos
        g2d.fillOval(eye1X, eye1Y, eyeSize, eyeSize);
        g2d.fillOval(eye2X, eye2Y, eyeSize, eyeSize);

        // Desenha as pupilas escuras
        g2d.setColor(new Color(25, 25, 25));
        g2d.fillOval(eye1X + (eyeSize - pupilSize) / 2 + pupilOffsetX,
                     eye1Y + (eyeSize - pupilSize) / 2 + pupilOffsetY,
                     pupilSize, pupilSize);
        g2d.fillOval(eye2X + (eyeSize - pupilSize) / 2 + pupilOffsetX,
                     eye2Y + (eyeSize - pupilSize) / 2 + pupilOffsetY,
                     pupilSize, pupilSize);
    }
}
