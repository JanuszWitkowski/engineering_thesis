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
    SAFE_ENEMY_PAWNS,
    SAFE_ENEMY_KINGS,
    MOVABLE_PAWNS,  // can make any move (without counting in captures)
    MOVABLE_KINGS,
    MOVABLE_ENEMY_PAWNS,
    MOVABLE_ENEMY_KINGS,
    POSSIBLE_MOVES,
    POSSIBLE_ENEMY_MOVES,
    DISTANCE_TO_PROMOTION,
    DISTANCE_TO_ENEMY_PROMOTION,
    UNOCCUPIED_PROMOTION_FIELDS,
    UNOCCUPIED_ENEMY_PROMOTION_FIELDS
}
