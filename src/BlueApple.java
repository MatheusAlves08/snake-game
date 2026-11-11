import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Representa a Maçã Azul (item especial desacelerador).
 *
 * Pilares da Programação Orientada a Objetos:
 * - Herança: estende Apple (BlueApple É-UMA Apple que É-UM GameObject),
 *   reaproveitando coordenadas, dimensão e o algoritmo de respawn seguro.
 * - Polimorfismo: sobrescreve o método draw(Graphics g) fornecendo a estilização
 *   visual em tons azuis vibrantes e sobrescreve reset() para reiniciar seu ciclo.
 * - Encapsulamento: controla de forma privada seu estado de ativação (active)
 *   no tabuleiro, expondo métodos controlados isActive() e setActive().
 */
public class BlueApple extends Apple {
    // Encapsulamento: indica se a fruta azul está presente no tabuleiro
    private boolean active;

    /**
     * Construtor da classe BlueApple.
     *
     * @param gridWidth largura total do grid em colunas
     * @param gridHeight altura total do grid em linhas
     * @param tileSize tamanho em pixels de cada célula da grade
     */
    public BlueApple(int gridWidth, int gridHeight, int tileSize) {
        super(gridWidth, gridHeight, tileSize);
        this.active = false; // Inicia desativada até atingir o gatilho de spawn
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Polimorfismo: redefine o estado da maçã azul ao reiniciar a partida.
     */
    @Override
    public void reset() {
        super.reset();
        this.active = false;
    }

    /**
     * Polimorfismo: implementação visual exclusiva da maçã azul.
     * Desenha a fruta com aura brilhante ciano, sombra suave, corpo azul safira,
     * ponto de luz 2D e folha verde-esmeralda/metálica.
     *
     * @param g contexto gráfico Java AWT
     */
    @Override
    public void draw(Graphics g) {
        if (!active) {
            return; // Se a maçã azul não estiver ativa no mapa, não é desenhada
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = x * tileSize;
        int py = y * tileSize;
        int padding = 3;
        int size = tileSize - (padding * 2);

        // 1. Aura / Brilho suave externo azul ciano
        g2d.setColor(new Color(52, 152, 219, 80));
        g2d.fillOval(px + padding - 2, py + padding - 2, size + 4, size + 4);

        // 2. Sombra inferior suave
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(px + padding + 1, py + padding + 2, size, size);

        // 3. Corpo da fruta (Azul Safira Vibrante)
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillOval(px + padding, py + padding, size, size);

        // 4. Ponto de reflexo de luz e profundidade 2D
        g2d.setColor(new Color(255, 255, 255, 170));
        g2d.fillOval(px + padding + (size / 4), py + padding + (size / 5), size / 4, size / 4);

        // 5. Folha em tom ciano metálico
        g2d.setColor(new Color(26, 188, 156));
        g2d.fillOval(px + (tileSize / 2) - 1, py + 1, 4, 6);
    }
}

