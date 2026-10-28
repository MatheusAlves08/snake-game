import java.awt.Graphics;

/**
 * Classe abstrata base que representa qualquer entidade no mundo do jogo.
 *
 * Pilares da Programação Orientada a Objetos presentes nesta classe:
 * - Abstração: define características comuns (coordenadas x, y e tamanho) e a assinatura
 *   de métodos essenciais (draw e reset), ocultando os detalhes de implementação das entidades concretas.
 * - Encapsulamento: campos protegidos com métodos de acesso (getters/setters) controlados.
 * - Herança: serve como classe-mãe para Snake e Apple.
 * - Polimorfismo: estabelece contratos abstratos sobrescritos pelas subclasses.
 */
public abstract class GameObject {
    // Encapsulamento: atributos protegidos acessíveis apenas pelas subclasses e pelo pacote
    protected int x;
    protected int y;
    protected int tileSize;

    /**
     * Construtor da classe base GameObject.
     *
     * @param x posição no eixo horizontal da grade (em unidades de blocos)
     * @param y posição no eixo vertical da grade (em unidades de blocos)
     * @param tileSize tamanho em pixels de cada bloco da grade
     */
    public GameObject(int x, int y, int tileSize) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
    }

    // Métodos de acesso (Encapsulamento)
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Método abstrato obrigatório para desenhar o objeto na tela gráfica.
     * Cada classe filha concreta fornecerá sua própria representação visual.
     * (Abstração / Polimorfismo)
     *
     * @param g contexto gráfico Java AWT
     */
    public abstract void draw(Graphics g);

    /**
     * Método abstrato para redefinir o estado do objeto ao reiniciar a partida.
     */
    public abstract void reset();
}
