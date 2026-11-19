import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Representa a Golden Apple (maçã dourada especial).
 *
 * Pilares da Programação Orientada a Objetos:
 * - Herança: estende Apple (GoldenApple É-UMA Apple que É-UM GameObject),
 *   reutilizando coordenadas cartesianas, dimensões e comportamento base de entidade de jogo.
 * - Polimorfismo: sobrescreve os métodos draw(Graphics g) fornecendo estética dourada
 *   com brilho neon e reset() para redefinir seu estado.
 * - Encapsulamento: mantém privado seu estado de disponibilidade (active),
 *   manipulado de forma segura pelos métodos isActive() e setActive().
 */
public class GoldenApple extends Apple {
    // Encapsulamento: indica se a maçã dourada está atualmente disponível no tabuleiro
    private boolean active;

    /**
     * Construtor da classe GoldenApple.
     *
     * @param gridWidth largura da grade em colunas
     * @param gridHeight altura da grade em linhas
     * @param tileSize tamanho em pixels de cada célula
     */
    public GoldenApple(int gridWidth, int gridHeight, int tileSize) {
        super(gridWidth, gridHeight, tileSize);
        this.active = false; // Inicia desativada até atingir o intervalo de spawn
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Polimorfismo: redefine o estado da maçã dourada ao reiniciar a partida.
     */
    @Override
    public void reset() {
        super.reset();
        this.active = false;
    }

    /**
     * Polimorfismo: implementação do método draw da classe GameObject para a Golden Apple.
     * Desenha a fruta em tom dourado metálico vibrante com aura neon amarela/dourada,
     * sombra inferior, reflexo de brilho e folha dourada reluzente.
     *
     * @param g contexto gráfico Java AWT
     */
    @Override
    public void draw(Graphics g) {
        if (!active) {
            return; // Não renderiza caso a fruta não esteja ativa
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = x * tileSize;
        int py = y * tileSize;
        int padding = 3;
        int size = tileSize - (padding * 2);

        // 1. Aura neon exterior com transparência (efeito dourado pulsante/neon)
        g2d.setColor(new Color(255, 215, 0, 75));
        g2d.fillOval(px + padding - 3, py + padding - 3, size + 6, size + 6);
        g2d.setColor(new Color(255, 235, 59, 120));
        g2d.fillOval(px + padding - 1, py + padding - 1, size + 2, size + 2);

        // 2. Sombra inferior suave
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(px + padding + 1, py + padding + 2, size, size);

        // 3. Corpo da fruta em Ouro Puro (Gold vibrante)
        g2d.setColor(new Color(255, 193, 7));
        g2d.fillOval(px + padding, py + padding, size, size);

        // Gradiente/destaque interno dourado vivo
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillOval(px + padding + 1, py + padding + 1, size - 2, size - 2);

        // 4. Ponto de luz e reflexo intenso 2D (efeito metálico brilhante)
        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.fillOval(px + padding + (size / 4), py + padding + (size / 5), size / 4, size / 4);

        // 5. Folha/caule dourado reluzente
        g2d.setColor(new Color(255, 241, 118));
        g2d.fillOval(px + (tileSize / 2) - 1, py + 1, 4, 6);
    }
}

