import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Painel principal do jogo responsável pela lógica do jogo, ciclo de atualização,
 * captura de teclado e renderização gráfica.
 *
 * Pilares da Programação Orientada a Objetos:
 * - Herança: estende JPanel e reutiliza toda a infraestrutura gráfica do Java Swing.
 * - Polimorfismo: armazena os elementos visuais em uma lista de GameObject (List<GameObject>)
 *   e invoca draw(Graphics g) polimorficamente sobre cada objeto sem precisar conhecer
 *   sua classe concreta durante o laço de desenho.
 * - Encapsulamento: campos privados (score, highScore, timer, estado atual) com
 *   métodos dedicados para manipular o ciclo de vida da partida.
 */
public class GamePanel extends JPanel implements ActionListener {
    // Dimensões do grid e do tabuleiro
    public static final int TILE_SIZE = 25;
    public static final int GRID_WIDTH = 24;
    public static final int GRID_HEIGHT = 24;
    public static final int BOARD_WIDTH = GRID_WIDTH * TILE_SIZE;   // 600 pixels
    public static final int BOARD_HEIGHT = GRID_HEIGHT * TILE_SIZE; // 600 pixels
    public static final int HEADER_HEIGHT = 50;                     // Altura da barra superior (HUD)
    public static final int TOTAL_HEIGHT = BOARD_HEIGHT + HEADER_HEIGHT; // 650 pixels

    // Configurações de velocidade e aceleração progressiva
    public static final int INITIAL_DELAY_MS = 110; // Velocidade inicial (110ms por passo)
    public static final int MIN_DELAY_MS = 45;      // Velocidade máxima permitida (limite de 45ms por passo)
    public static final int SPEED_STEP = 3;         // Aceleração a cada maçã consumida (-3ms por fruta)

    // Componentes do jogo
    private final Snake snake;
    private final Apple apple;

    /**
     * Polimorfismo na prática:
     * Lista genérica que armazena qualquer subclasse de GameObject (Snake, Apple).
     * O laço de renderização itera por esta lista chamando draw(), executando o
     * comportamento especializado de cada classe derivada.
     */
    private final List<GameObject> gameObjects;

    // Gerenciador do ciclo de atualização
    private final Timer timer;

    // Estados e pontuações (Encapsulamento)
    private GameState currentState;
    private int score;
    private int highScore;
    private int currentDelay;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, TOTAL_HEIGHT));
        setBackground(new Color(24, 26, 36));
        setFocusable(true);

        // Inicialização dos objetos do jogo
        this.snake = new Snake(GRID_WIDTH / 2, GRID_HEIGHT / 2, TILE_SIZE, 3);
        this.apple = new Apple(GRID_WIDTH, GRID_HEIGHT, TILE_SIZE);

        // Registra os objetos na lista polimórfica de GameObject
        this.gameObjects = new ArrayList<>();
        this.gameObjects.add(apple); // Desenha a maçã primeiro (abaixo da cobra)
        this.gameObjects.add(snake); // Desenha a cobra por cima da maçã

        this.currentState = GameState.MENU;
        this.score = 0;
        this.highScore = 0;

        // Configuração do Timer para o loop do jogo
        this.currentDelay = INITIAL_DELAY_MS;
        this.timer = new Timer(currentDelay, this);
  
        // Captura de eventos do teclado
        addKeyListener(new GameKeyAdapter());
    }

    /**
     * Inicia uma nova partida a partir do menu ou do reinício.
     */
    public void startNewGame() {
        score = 0;
        currentDelay = INITIAL_DELAY_MS;
        timer.setDelay(currentDelay);
        snake.reset();
        apple.respawn(snake);
        currentState = GameState.RUNNING;
        if (!timer.isRunning()) {
            timer.start();
        }
        repaint();
    }

    /**
     * Pausa ou retoma a partida em andamento.
     */
    public void togglePause() {
        if (currentState == GameState.RUNNING) {
            currentState = GameState.PAUSED;
            timer.stop();
            repaint();
        } else if (currentState == GameState.PAUSED) {
            currentState = GameState.RUNNING;
            timer.start();
        }
    }

    /**
     * Ciclo principal de atualização acionado a cada tick do Timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == GameState.RUNNING) {
            updateGame();
        }
        repaint();
    }

    /**
     * Executa as regras de negócio de cada passo do jogo.
     */
    private void updateGame() {
        snake.move();

        // 1. Verificação de colisão com as bordas do cenário
        if (snake.checkWallCollision(GRID_WIDTH, GRID_HEIGHT)) {
            triggerGameOver();
            return;
        }

        // 2. Verificação de colisão da cabeça com o próprio corpo
        if (snake.checkSelfCollision()) {
            triggerGameOver();
            return;
        }

        // 3. Verificação de coleta da maçã
        Point head = snake.getHead();
        if (head.x == apple.getX() && head.y == apple.getY()) {
            score += 10;
            if (score > highScore) {
                highScore = score;
            }
            snake.grow();
            apple.respawn(snake);

            // Aceleração progressiva: reduz o intervalo do Timer a cada fruta coletada
            if (currentDelay > MIN_DELAY_MS) {
                currentDelay = Math.max(MIN_DELAY_MS, currentDelay - SPEED_STEP);
                timer.setDelay(currentDelay);
            }
        }
    }

    /**
     * Altera o estado do jogo para Fim de Jogo e pausa o timer.
     */
    private void triggerGameOver() {
        currentState = GameState.GAME_OVER;
        timer.stop();
    }

    /**
     * Renderização de todos os elementos visuais na janela.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. Desenha a barra superior de informações (HUD)
        drawHeader(g2d);

        // 2. Desenha a grade e o fundo do tabuleiro
        Graphics2D boardG = (Graphics2D) g2d.create();
        boardG.translate(0, HEADER_HEIGHT); // Translada a origem para abaixo do cabeçalho

        drawBoardBackground(boardG);

        /*
         * DEMONSTRAÇÃO DO POLIMORFISMO:
         * Iteramos sobre a lista de GameObjects. Cada objeto (Apple, Snake)
         * executa sua própria versão do método draw(Graphics g).
         * O GamePanel não precisa saber se o objeto é uma cobra ou maçã.
         */
        for (GameObject obj : gameObjects) {
            obj.draw(boardG);
        }

        boardG.dispose();

        // 3. Desenha telas de sobreposição de acordo com o estado do jogo
        switch (currentState) {
            case MENU:
                drawMenuOverlay(g2d);
                break;
            case PAUSED:
                drawPausedOverlay(g2d);
                break;
            case GAME_OVER:
                drawGameOverOverlay(g2d);
                break;
            case RUNNING:
            default:
                break;
        }
    }

    /**
     * Renderiza o cabeçalho superior com pontuação atual e recorde da sessão.
     */
    private void drawHeader(Graphics2D g2d) {
        g2d.setColor(new Color(18, 20, 28));
        g2d.fillRect(0, 0, BOARD_WIDTH, HEADER_HEIGHT);

        g2d.setColor(new Color(36, 40, 54));
        g2d.fillRect(0, HEADER_HEIGHT - 2, BOARD_WIDTH, 2);

        // Nome do Jogo
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(new Color(46, 204, 113));
        g2d.drawString("SNAKE GAME", 16, 32);

        // Pontuação Atual
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        g2d.setColor(Color.WHITE);
        String scoreText = "Pontos: " + score;
        g2d.drawString(scoreText, 250, 32);

        // Melhor Pontuação (Recorde)
        g2d.setColor(new Color(241, 196, 15));
        String recordText = "Recorde: " + highScore;
        g2d.drawString(recordText, 440, 32);
    }

    /**
     * Desenha o fundo do tabuleiro com padrão sutil de grade para guiar o jogador.
     */
    private void drawBoardBackground(Graphics2D g2d) {
        Color darkTile = new Color(28, 30, 42);
        Color lightTile = new Color(33, 35, 48);

        for (int r = 0; r < GRID_HEIGHT; r++) {
            for (int c = 0; c < GRID_WIDTH; c++) {
                if ((r + c) % 2 == 0) {
                    g2d.setColor(darkTile);
                } else {
                    g2d.setColor(lightTile);
                }
                g2d.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Desenha a tela inicial com instruções completas.
     */
    private void drawMenuOverlay(Graphics2D g2d) {
        drawOverlayBackground(g2d, new Color(15, 17, 24, 230));

        int centerX = BOARD_WIDTH / 2;

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 38));
        g2d.setColor(new Color(46, 204, 113));
        drawCenteredString(g2d, "SNAKE GAME", centerX, 180);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        g2d.setColor(new Color(189, 195, 199));
        drawCenteredString(g2d, "Projeto Acadêmico - Programação Orientada a Objetos", centerX, 220);

        // Painel de controles
        g2d.setColor(new Color(25, 28, 38, 200));
        g2d.fillRoundRect(centerX - 180, 260, 360, 160, 16, 16);
        g2d.setColor(new Color(52, 73, 94));
        g2d.drawRoundRect(centerX - 180, 260, 360, 160, 16, 16);

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "CONTROLES", centerX, 290);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2d.setColor(new Color(220, 221, 225));
        drawCenteredString(g2d, "Setas do Teclado ou W, A, S, D : Mover a cobra", centerX, 325);
        drawCenteredString(g2d, "Tecla P : Pausar a partida", centerX, 355);
        drawCenteredString(g2d, "Tecla ESC : Voltar ao Menu Inicial", centerX, 385);

        // Botão / Instrução para iniciar
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2d.setColor(new Color(46, 204, 113));
        drawCenteredString(g2d, "Pressione ESPAÇO ou ENTER para Iniciar", centerX, 470);
    }

    /**
     * Desenha a tela de pausa.
     */
    private void drawPausedOverlay(Graphics2D g2d) {
        drawOverlayBackground(g2d, new Color(15, 17, 24, 190));

        int centerX = BOARD_WIDTH / 2;

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
        g2d.setColor(new Color(241, 196, 15));
        drawCenteredString(g2d, "JOGO PAUSADO", centerX, 300);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "Pressione P para Retomar a Partida", centerX, 350);
    }

    /**
     * Desenha a tela de fim de jogo.
     */
    private void drawGameOverOverlay(Graphics2D g2d) {
        drawOverlayBackground(g2d, new Color(15, 17, 24, 220));

        int centerX = BOARD_WIDTH / 2;

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.setColor(new Color(231, 76, 60));
        drawCenteredString(g2d, "FIM DE JOGO!", centerX, 230);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "Pontuação Final: " + score, centerX, 285);

        g2d.setColor(new Color(241, 196, 15));
        drawCenteredString(g2d, "Melhor Pontuação: " + highScore, centerX, 325);

        // Painel de opções
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(new Color(46, 204, 113));
        drawCenteredString(g2d, "Pressione ESPAÇO ou R para Jogar Novamente", centerX, 400);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2d.setColor(new Color(189, 195, 199));
        drawCenteredString(g2d, "Pressione ESC para Voltar ao Menu", centerX, 440);
    }

    private void drawOverlayBackground(Graphics2D g2d, Color color) {
        g2d.setColor(color);
        g2d.fillRect(0, HEADER_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);
    }

    private void drawCenteredString(Graphics2D g2d, String text, int centerX, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = centerX - (fm.stringWidth(text) / 2);
        g2d.drawString(text, x, y);
    }

    // Getters para fins de encapsulamento e testes
    public GameState getCurrentState() {
        return currentState;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public Snake getSnake() {
        return snake;
    }

    public Apple getApple() {
        return apple;
    }

    public int getCurrentDelay() {
        return currentDelay;
    }

    /**
     * Adaptador interno para captura de eventos do teclado.
     */
    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // Ações dependentes do estado atual
            if (currentState == GameState.MENU) {
                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
                    startNewGame();
                }
                return;
            }

            if (currentState == GameState.GAME_OVER) {
                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_R || key == KeyEvent.VK_ENTER) {
                    startNewGame();
                } else if (key == KeyEvent.VK_ESCAPE) {
                    currentState = GameState.MENU;
                    repaint();
                }
                return;
            }

            if (currentState == GameState.RUNNING || currentState == GameState.PAUSED) {
                // Tecla de pausa (P)
                if (key == KeyEvent.VK_P) {
                    togglePause();
                    return;
                }

                // Tecla para voltar ao menu (ESC)
                if (key == KeyEvent.VK_ESCAPE) {
                    timer.stop();
                    currentState = GameState.MENU;
                    repaint();
                    return;
                }
            }

            // Controle de direção da cobra durante a partida
            if (currentState == GameState.RUNNING) {
                switch (key) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        snake.setDirection(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        snake.setDirection(Direction.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        snake.setDirection(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        snake.setDirection(Direction.RIGHT);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
