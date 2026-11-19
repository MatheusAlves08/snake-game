# Implementação --- Golden Apple (Snake Game)

## 1. Objetivo

Implementar uma nova entidade especial chamada `GoldenApple.java` no
Snake Game em Java. Ao ser coletada, ela ativa temporariamente um modo
de baixa visibilidade: a tela de jogo fica preta, o cenário e as demais
frutas deixam de ser renderizados e a cobra passa a ser exibida em
dourado neon.

O efeito termina quando a cobra coleta outra maçã que não seja a Golden
Apple. Essa maçã pode estar invisível durante o efeito, mas continua
existindo, sendo atualizada e podendo ser coletada.

A implementação deve preservar as regras atuais do jogo, a arquitetura
orientada a objetos e o comportamento das maçãs vermelha e azul.

## 2. Contexto atual do projeto

Repositório: `MatheusAlves08/snake-game`\
Branch de trabalho: `develop`\
Arquivos relevantes:

-   `src/GameObject.java`
-   `src/Apple.java`
-   `src/BlueApple.java`
-   `src/Snake.java`
-   `src/GamePanel.java`
-   `src/GameState.java`

### Estrutura existente

-   `GameObject` é a classe abstrata base.
-   `Apple` estende `GameObject`.
-   `BlueApple` estende `Apple`.
-   `Snake` estende `GameObject`.
-   `GamePanel` coordena o ciclo do jogo, as colisões, o estado da
    partida e a renderização.
-   `GamePanel` mantém uma lista polimórfica `List<GameObject>` chamada
    `gameObjects`.
-   As bordas do tabuleiro são verificadas por
    `snake.checkWallCollision(...)`; não existe uma classe `Wall.java`
    na estrutura analisada.

**Importante:** antes de editar, o agente deve conferir o estado atual
da branch e ler os arquivos diretamente no workspace. Este documento
descreve a arquitetura observada, mas o código local é a fonte de
verdade.

## 3. Comportamento esperado

### 3.1 Surgimento da Golden Apple

-   A Golden Apple deve surgir a cada 10 maçãs vermelhas coletadas.
-   O intervalo deve ser definido por uma constante configurável, por
    exemplo `GOLDEN_APPLE_SPAWN_INTERVAL = 10`.
-   Deve existir no máximo uma Golden Apple disponível por vez.
-   A fruta especial deve ser posicionada dentro do grid e não pode
    surgir sobre a cobra ou sobre outra fruta.
-   Se não houver uma posição livre, a tentativa de posicionamento deve
    falhar de maneira segura, sem loop infinito. O agente deve escolher
    uma estratégia compatível com o código existente, como enumerar
    posições livres antes de selecionar uma aleatoriamente.
-   A Golden Apple deve ficar visível no modo normal e ser desenhada em
    dourado, com brilho neon.

### 3.2 Ativação

Quando a cabeça da cobra alcançar a posição da Golden Apple:

1.  A coleta deve ser reconhecida uma única vez.
2.  A Golden Apple deixa de estar disponível.
3.  O estado `goldenModeActive` do `GamePanel` passa a `true`.
4.  A cobra passa a usar sua paleta dourada neon.
5.  O painel de jogo passa a ser renderizado em preto.
6.  Fundo, padrão da grade e maçãs vermelha/azul deixam de ser
    desenhados.
7.  A lógica de atualização, movimentação, pontuação, crescimento e
    colisões continua funcionando normalmente.

O efeito deve começar no mesmo ciclo de atualização em que a Golden
Apple for coletada, respeitando o fluxo de atualização e repaint do
Swing.

### 3.3 Durante o modo dourado

-   A tela deve ficar preta.
-   A única entidade do jogo visível deve ser a cobra, desenhada em
    dourado neon.
-   Não desenhar a maçã vermelha, a maçã azul, a Golden Apple nem o
    fundo do tabuleiro.
-   Para cumprir a regra de "apenas a cobra visível", ocultar também o
    conteúdo do HUD enquanto o efeito estiver ativo. O painel pode
    continuar exibindo telas de menu, pausa ou Game Over conforme o
    fluxo normal de estados; durante uma partida em execução no modo
    dourado, a apresentação deve ser preta com a cobra.
-   As maçãs vermelha e azul continuam em suas posições e continuam
    elegíveis para coleta.
-   As regras de colisão com as bordas e com o próprio corpo continuam
    ativas.
-   A cobra continua se movimentando, crescendo e obedecendo aos
    controles.
-   O timer não deve ser pausado ou alterado apenas por causa do modo
    dourado.

### 3.4 Desativação

O modo dourado termina quando a cobra coleta uma maçã vermelha ou azul.

Ao detectar essa coleta:

1.  Aplicar normalmente os efeitos da maçã coletada (pontuação,
    crescimento, aceleração ou desaceleração, conforme a regra atual).
2.  Desativar `goldenModeActive`.
3.  Restaurar a aparência normal da cobra.
4.  Voltar a desenhar o cenário, HUD e frutas de acordo com o estado
    normal.
5.  Garantir que a fruta coletada seja reposicionada ou desativada
    conforme sua regra atual.

A desativação deve ocorrer uma única vez. A coleta da própria Golden
Apple não pode ser confundida com a coleta de uma maçã comum.

### 3.5 Reinício, menu e Game Over

-   `startNewGame()` deve sempre limpar o modo dourado.
-   `Snake.reset()` deve restaurar a aparência normal da cobra.
-   A Golden Apple deve voltar ao estado inicial e o contador de maçãs
    vermelhas deve ser zerado conforme a política de reinício já usada
    pelo jogo.
-   Sair para o menu deve impedir que o efeito fique ativo ao iniciar
    uma nova partida.
-   Game Over deve continuar funcionando normalmente, inclusive se
    ocorrer durante o modo dourado.
-   Pausar e retomar não deve ativar ou desativar o efeito por engano.

## 4. Arquitetura proposta

### 4.1 `GoldenApple.java`

Criar uma classe que estenda `Apple`.

Responsabilidades:

-   Reutilizar posição, tamanho e comportamento comum herdados de
    `Apple`.
-   Controlar se a fruta especial está disponível, caso a arquitetura da
    `BlueApple` seja reutilizada como referência.
-   Sobrescrever `draw(Graphics g)` para renderizar uma maçã dourada com
    brilho neon.
-   Sobrescrever `reset()` para restaurar seu estado inicial.
-   Não controlar `GamePanel`, `Snake`, o fundo ou as demais frutas.

A classe não deve conhecer a mecânica global de visibilidade. Ela é uma
entidade do domínio do jogo, não o gerenciador do efeito.

### 4.2 `Snake.java`

Adicionar um estado visual privado, por exemplo:

``` java
private boolean goldenMode;
```

Adicionar métodos de controle encapsulados, preferencialmente:

``` java
public void setGoldenMode(boolean active) {
    this.goldenMode = active;
}
```

No método `draw(Graphics g)`:

-   Se `goldenMode` for `false`, preservar a renderização atual sem
    regressões.
-   Se `goldenMode` for `true`, desenhar todos os segmentos com tons
    dourados.
-   A cabeça deve ter destaque visual mais intenso que o corpo.
-   Adicionar brilho/contorno neon se isso puder ser feito sem tornar o
    desenho excessivamente complexo.
-   Manter olhos e direção da cobra legíveis, adaptando as cores para
    contrastar com o dourado.
-   Não alterar `move()`, `grow()`, `checkWallCollision()` ou
    `checkSelfCollision()` por causa da aparência.

`reset()` deve sempre retornar `goldenMode` para `false`.

### 4.3 `GamePanel.java`

Adicionar:

-   Uma instância `GoldenApple`.
-   A instância à lista `gameObjects`, mantendo a ordem de desenho
    adequada no modo normal.
-   Uma constante de intervalo para o surgimento.
-   Um estado privado `goldenModeActive`.
-   Um contador específico ou reutilização criteriosa do contador
    existente, sem alterar o significado de `applesEatenCount` sem
    necessidade.

Criar métodos privados para coordenar o efeito, por exemplo:

``` java
private void activateGoldenMode() {
    goldenModeActive = true;
    snake.setGoldenMode(true);
}

private void deactivateGoldenMode() {
    goldenModeActive = false;
    snake.setGoldenMode(false);
}
```

Esses métodos devem ser idempotentes ou protegidos por verificações de
estado, para evitar ativações e desativações repetidas.

### 4.4 Renderização em `paintComponent(Graphics g)`

Centralizar a decisão de renderização no `GamePanel`.

Com `goldenModeActive == false`:

-   Preservar o fluxo atual: HUD, fundo, lista polimórfica de objetos e
    overlays.

Com `goldenModeActive == true` e estado `RUNNING`:

-   Pintar toda a área do painel de preto.
-   Não desenhar HUD nem fundo.
-   Desenhar somente a cobra.
-   Respeitar o deslocamento vertical do tabuleiro já usado pelo painel
    (`HEADER_HEIGHT`) para manter as coordenadas da cobra alinhadas ao
    grid. A área superior também deve permanecer preta.
-   Criar e descartar cópias de `Graphics2D` quando necessário para
    evitar vazamento de transformações ou configurações gráficas.

Não duplicar desnecessariamente o desenho da cobra nem chamar a
renderização normal da lista durante o efeito.

## 5. Regras de atualização e coleta

A lógica de coleta deve permanecer em `updateGame()`, e não em `draw()`.

Ordem sugerida para cada tick:

1.  Mover a cobra.
2.  Verificar colisão com as bordas.
3.  Verificar colisão com o próprio corpo.
4.  Capturar a posição da cabeça.
5.  Verificar coleta da Golden Apple, se disponível.
6.  Verificar coleta da maçã vermelha.
7.  Verificar coleta da maçã azul.
8.  Se o modo dourado estava ativo e uma maçã comum foi coletada,
    desativar o modo dourado.
9.  Atualizar spawn de frutas e demais regras sem perder a referência à
    fruta que foi coletada.

A implementação precisa evitar efeitos colaterais causados por
reposicionar uma fruta antes de concluir a verificação das demais
colisões.

### Atenção à ordem das operações

A maçã vermelha atual é reposicionada logo após ser coletada. Ao adaptar
o código, registrar primeiro se a cabeça estava na posição da fruta,
aplicar a coleta e só depois reposicionar. A mesma cautela vale para a
maçã azul.

A coleta de uma maçã comum durante o modo dourado deve restaurar a
visibilidade mesmo que a fruta seja imediatamente reposicionada.

### Posições das frutas

As frutas não devem ocupar a mesma célula. Sempre que possível, criar ou
reutilizar um mecanismo central de escolha de posições livres que
considere:

-   Todos os segmentos da cobra.
-   Maçã vermelha.
-   Maçã azul, quando ativa.
-   Golden Apple, quando disponível.

Não introduzir um `while` potencialmente infinito. Se o tabuleiro
estiver cheio, retornar falha de posicionamento de forma controlada.

## 6. Encapsulamento e POO

A implementação deve demonstrar claramente:

-   **Herança:** `GoldenApple extends Apple`.
-   **Polimorfismo:** `GoldenApple` implementa seu próprio `draw()` e
    `reset()`; pode continuar armazenada na lista `List<GameObject>`.
-   **Encapsulamento:** estados de ativação e aparência permanecem
    privados e são manipulados por métodos definidos.
-   **Abstração:** `GamePanel` coordena a mecânica sem precisar conhecer
    os detalhes internos do desenho da fruta.

Evitar:

-   Acessar ou alterar diretamente atributos internos de outras classes.
-   Colocar regras de renderização global dentro de `GoldenApple`.
-   Usar `instanceof` em cascata para decidir como desenhar todos os
    objetos.
-   Duplicar lógica de movimentação ou colisão.
-   Refatorar classes não relacionadas sem necessidade.

## 7. Critérios de aceitação

### Golden Apple

-   [ ] A classe `GoldenApple.java` estende `Apple`.
-   [ ] A fruta é desenhada em dourado neon.
-   [ ] A fruta aparece conforme o intervalo configurado.
-   [ ] Não existem duas Golden Apples disponíveis ao mesmo tempo.
-   [ ] A posição não coincide com a cobra ou outras frutas.

### Modo dourado

-   [ ] Coletar a Golden Apple ativa o efeito.
-   [ ] Toda a área do painel fica preta.
-   [ ] Apenas a cobra permanece visível durante o modo.
-   [ ] A cobra é renderizada em dourado neon.
-   [ ] Maçãs comuns continuam existindo e sendo coletáveis.
-   [ ] O movimento e as colisões continuam funcionando.
-   [ ] Coletar uma maçã vermelha ou azul desativa o efeito.
-   [ ] A aparência normal é restaurada imediatamente após a coleta
    comum.

### Ciclo de vida

-   [ ] Reiniciar a partida limpa o modo dourado.
-   [ ] Voltar ao menu não deixa o efeito preso.
-   [ ] Game Over funciona durante o modo dourado.
-   [ ] Pausar e retomar preserva corretamente o estado do efeito.
-   [ ] O comportamento original das maçãs vermelha e azul permanece
    intacto.

### Qualidade

-   [ ] O projeto compila.
-   [ ] Não há exceções durante o fluxo normal.
-   [ ] Não há loops infinitos na escolha de posições.
-   [ ] Não há regressões visuais no modo normal.
-   [ ] Não há mudanças fora do escopo sem justificativa.

## 8. Plano de implementação recomendado

1.  Inspecionar o código atual e confirmar branch, pacotes e estrutura.
2.  Criar `GoldenApple.java` e validar herança e renderização
    isoladamente.
3.  Implementar a paleta dourada em `Snake.java`.
4.  Integrar a entidade, contador e estado no `GamePanel.java`.
5.  Implementar ativação e desativação no fluxo de atualização.
6.  Implementar o caminho alternativo de renderização.
7.  Tratar reinício, menu, pausa e Game Over.
8.  Revisar posicionamento das frutas e evitar sobreposição/loops
    infinitos.
9.  Compilar e executar testes manuais.
10. Apresentar resumo dos arquivos alterados, decisões técnicas,
    comandos executados e pontos que precisam de validação humana.

## 9. Fora do escopo

Não implementar nesta tarefa, salvo solicitação expressa:

-   Novas fases ou níveis.
-   Efeitos sonoros.
-   Persistência ou banco de dados.
-   Alteração geral do sistema de pontuação.
-   Refatoração completa de todas as classes.
-   Novas classes de parede ou sistema de colisão.
-   Mudanças na interface do menu que não sejam necessárias para a
    mecânica.

## 10. Entrega esperada

Ao concluir, apresentar:

-   Arquivos criados e modificados.
-   Resumo do funcionamento.
-   Explicação de como a herança e o polimorfismo foram aplicados.
-   Resultado da compilação e dos testes executados.
-   Limitações ou testes que ainda dependem de validação manual.
-   Nenhum commit ou push sem autorização explícita do usuário.
