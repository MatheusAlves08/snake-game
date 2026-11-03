import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

/**
 * Representa a maçã (alimento) que a cobra deve coletar no cenário.
 *
 * Pilares da Programação Orientada a Objetos:
 * - Herança: herda atributos e contratos da classe GameObject (extends GameObject).
 * - Polimorfismo: sobrescreve os métodos abstratos draw(Graphics g) e reset() com @Override.
 * - Encapsulamento: atributos específicos privados (gridWidth, gridHeight, RANDOM) e
 *   lógica de reposicionamento encapsulada no método respawn(Snake snake).
 */
public class Apple extends GameObject {
    private static final Random RANDOM = new Random();
    private final int gridWidth;
    private final int gridHeight;

    /**
     * Construtor da classe Apple.
     *
     * @param gridWidth largura total do grid em colunas
     * @param gridHeight altura total do grid em linhas
     * @param tileSize tamanho em pixels de cada célula do grid
     */
    public Apple(int gridWidth, int gridHeight, int tileSize) {
        // Chamada ao construtor da classe base GameObject (super)
        super(0, 0, tileSize);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        reset();
    }

    /**
     * Reposiciona a maçã em uma posição aleatória do cenário, garantindo
     * que ela nunca apareça sobre nenhuma parte do corpo da cobra.
     *
     * @param snake referência à cobra para verificar colisões de posições
     */
    public void respawn(Snake snake) {
        boolean validPosition = false;
        while (!validPosition) {
            int newX = RANDOM.nextInt(gridWidth);
            int newY = RANDOM.nextInt(gridHeight);

            // Garante que a fruta não surja onde a cobra já está presente
            if (snake == null || !snake.occupies(newX, newY)) {
                this.x = newX;
                this.y = newY;
                validPosition = true;
            }
        }
    }

    /**
     * Polimorfismo: reinicia a posição da maçã para o início da partida.
     */
    @Override
    public void reset() {
        this.x = RANDOM.nextInt(gridWidth);
        this.y = RANDOM.nextInt(gridHeight);
    }

    /**
     * Polimorfismo: implementação específica da maçã para o método abstrato draw da classe GameObject.
     * Desenha a fruta com sombra, corpo vermelho vívido, brilho e folha verde.
     *
     * @param g contexto gráfico
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = x * tileSize;
        int py = y * tileSize;
        int padding = 3;
        int size = tileSize - (padding * 2);

        // Halo/brilho suave em volta da maçã (glow sutil)
        g2d.setColor(new Color(231, 76, 60, 40));
        g2d.fillOval(px + padding - 2, py + padding - 2, size + 4, size + 4);

        // Sombra suave sob a maçã
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(px + padding + 1, py + padding + 3, size - 2, size - 2);

        // Corpo da maçã com gradiente 3D (vermelho brilhante ao carmim rico)
        java.awt.GradientPaint applePaint = new java.awt.GradientPaint(
            px + padding, py + padding, new Color(255, 99, 88),
            px + padding, py + padding + size, new Color(192, 57, 43)
        );
        g2d.setPaint(applePaint);
        g2d.fillOval(px + padding, py + padding, size, size);

        // Ponto de brilho de luz na maçã (efeito de profundidade vítrea)
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillOval(px + padding + (size / 4), py + padding + (size / 5), size / 4, size / 4);

        // Caule/folha verde com contorno
        g2d.setColor(new Color(46, 204, 113));
        g2d.fillOval(px + (tileSize / 2) - 1, py + 1, 5, 7);
        g2d.setColor(new Color(30, 130, 76));
        g2d.drawOval(px + (tileSize / 2) - 1, py + 1, 5, 7);
    }
}
