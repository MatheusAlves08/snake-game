import java.awt.Point;

/**
 * Suite de testes automatizados para validação das regras de negócio do Snake Game
 * e dos pilares da Programação Orientada a Objetos.
 */
public class GameTest {
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  INICIANDO BATERIA DE TESTES - SNAKE GAME (POO) ");
        System.out.println("==================================================");

        testSnakeInitialization();
        testSnakeMovement();
        testPrevent180DegreeTurn();
        testSnakeGrowth();
        testWallCollision();
        testSelfCollision();
        testAppleRespawnNotOnSnake();
        testPolymorphismContract();

        System.out.println("\n--------------------------------------------------");
        System.out.println("RESULTADO FINAL: " + testsPassed + " passaram, " + testsFailed + " falharam.");
        System.out.println("--------------------------------------------------");

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        if (expected == null && actual == null || (expected != null && expected.equals(actual))) {
            System.out.println("[PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println("[FAIL] " + testName + " - Esperado: " + expected + ", mas obteve: " + actual);
            testsFailed++;
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("[PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println("[FAIL] " + testName + " - Esperava true, mas obteve false.");
            testsFailed++;
        }
    }

    private static void testSnakeInitialization() {
        Snake snake = new Snake(10, 10, 25, 3);
        assertEquals("Tamanho inicial da cobra deve ser 3", 3, snake.getLength());
        assertEquals("Cabeça da cobra deve iniciar em (10, 10)", new Point(10, 10), snake.getHead());
        assertEquals("Direção inicial deve ser RIGHT", Direction.RIGHT, snake.getCurrentDirection());
        assertTrue("Corpo deve ocupar (10, 10)", snake.occupies(10, 10));
        assertTrue("Corpo deve ocupar (9, 10)", snake.occupies(9, 10));
        assertTrue("Corpo deve ocupar (8, 10)", snake.occupies(8, 10));
    }

    private static void testSnakeMovement() {
        Snake snake = new Snake(10, 10, 25, 3);
        snake.move(); // Move para a direita (padrão)
        assertEquals("Cabeça após mover para a direita deve ser (11, 10)", new Point(11, 10), snake.getHead());
        assertEquals("Tamanho deve permanecer 3 sem crescer", 3, snake.getLength());

        snake.setDirection(Direction.DOWN);
        snake.move();
        assertEquals("Cabeça após virar para baixo deve ser (11, 11)", new Point(11, 11), snake.getHead());
    }

    private static void testPrevent180DegreeTurn() {
        Snake snake = new Snake(10, 10, 25, 3);
        // Direção atual: RIGHT. Tentar ir para LEFT (180 graus oposto) deve ser bloqueado!
        snake.setDirection(Direction.LEFT);
        snake.move();
        assertEquals("Mudança de 180 graus imediata deve ser bloqueada; cobra deve continuar indo para RIGHT",
                     new Point(11, 10), snake.getHead());
    }

    private static void testSnakeGrowth() {
        Snake snake = new Snake(10, 10, 25, 3);
        snake.grow();
        snake.move();
        assertEquals("Tamanho da cobra após crescer e mover deve ser 4", 4, snake.getLength());
        assertEquals("Nova cabeça deve ser (11, 10)", new Point(11, 10), snake.getHead());
    }

    private static void testWallCollision() {
        Snake snake = new Snake(23, 10, 25, 3); // Na borda direita de um grid 24x24 (índices 0..23)
        assertTrue("Não deve estar colidindo com a borda antes do movimento", !snake.checkWallCollision(24, 24));
        snake.move(); // Cabeça vai para x = 24 (fora do grid)
        assertTrue("Deve detectar colisão com a parede leste ao ultrapassar gridWidth", snake.checkWallCollision(24, 24));
    }

    private static void testSelfCollision() {
        Snake snake = new Snake(10, 10, 25, 5);
        // Cobra com tamanho 5: (10,10), (9,10), (8,10), (7,10), (6,10)
        // Manobra em 'U' para colidir consigo mesma: UP -> LEFT -> DOWN
        snake.setDirection(Direction.UP);
        snake.move(); // (10, 9)
        assertTrue("Ainda sem colisão", !snake.checkSelfCollision());

        snake.setDirection(Direction.LEFT);
        snake.move(); // (9, 9)
        assertTrue("Ainda sem colisão", !snake.checkSelfCollision());

        snake.setDirection(Direction.DOWN);
        snake.move(); // (9, 10) -> Onde está o corpo da cobra!
        assertTrue("Deve detectar colisão da cabeça com o próprio corpo", snake.checkSelfCollision());
    }

    private static void testAppleRespawnNotOnSnake() {
        Snake snake = new Snake(5, 5, 25, 4);
        Apple apple = new Apple(10, 10, 25);

        for (int i = 0; i < 50; i++) {
            apple.respawn(snake);
            assertTrue("A maçã gerada não deve coincidir com nenhuma coordenada da cobra",
                       !snake.occupies(apple.getX(), apple.getY()));
        }
    }

    private static void testPolymorphismContract() {
        // Testando a abstração e polimorfismo via referência da classe abstrata GameObject
        GameObject obj1 = new Snake(5, 5, 25, 3);
        GameObject obj2 = new Apple(24, 24, 25);

        assertTrue("obj1 deve ser instância de GameObject", obj1 instanceof GameObject);
        assertTrue("obj2 deve ser instância de GameObject", obj2 instanceof GameObject);
        assertEquals("Tamanho do tile deve ser 25 em ambos", 25, obj1.getTileSize());
        assertEquals("Tamanho do tile deve ser 25 em ambos", 25, obj2.getTileSize());
    }
}
