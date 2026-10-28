public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int deltaX;
    private final int deltaY;

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    /**
     * Verifica se a direção informada é oposta à atual,
     * impedindo que a cobra reverta 180 graus instantaneamente e colida consigo mesma.
     *
     * @param other direção a ser comparada
     * @return true se as direções forem exatamente opostas
     */
    public boolean isOpposite(Direction other) {
        if (other == null) {
            return false;
        }
        return (this.deltaX + other.deltaX == 0) && (this.deltaY + other.deltaY == 0);
    }
}
