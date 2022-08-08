/**
 * Służy do intuicyjnego porządkowania parametrów funkcji oceny heurystycznej.
 */
public enum HParam {
    PAWNS,
    KINGS,
    ENEMY_PAWNS,
    ENEMY_KINGS,
    SAFE_PAWNS,     // adjacent to the wall
    SAFE_KINGS,
    ENEMY_SAFE_PAWNS,
    ENEMY_SAFE_KINGS,
    POSSIBLE_MOVES,
    ENEMY_POSSIBLE_MOVES
}
