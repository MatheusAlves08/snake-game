/**
 * Enumeração que define os possíveis estados da partida.
 */
public enum GameState {
    MENU,       // Tela inicial com instruções e controles
    RUNNING,    // Partida em andamento
    PAUSED,     // Jogo pausado
    GAME_OVER   // Fim de jogo com exibição de pontuação e opção de reinício
}
