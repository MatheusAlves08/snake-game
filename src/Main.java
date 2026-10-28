import javax.swing.SwingUtilities;

/**
 * Ponto de entrada (Entry Point) da aplicação Snake Game.
 * Inicializa a interface gráfica de maneira segura através da Event Dispatch Thread (EDT) do Swing.
 */
public class Main {
    public static void main(String[] args) {
        // Garante que os componentes gráficos do Swing sejam iniciados na thread correta
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}
