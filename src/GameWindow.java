import javax.swing.JFrame;

/**
 * Janela principal da aplicação gráfica desktop.
 *
 * Pilares da Programação Orientada a Objetos:
 * - Herança: estende JFrame (extends JFrame) para configurar e gerenciar a janela da aplicação.
 * - Encapsulamento: encapsula a inicialização e os parâmetros visuais da janela.
 */
public class GameWindow extends JFrame {

    public GameWindow() {
        super("Snake Game - Jogo da Cobrinha (POO em Java)");
        initWindow();
    }

    private void initWindow() {
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack(); // Ajusta o tamanho da janela exatamente às dimensões preferidas do GamePanel
        setLocationRelativeTo(null); // Centraliza a janela na tela do usuário
    }
}
