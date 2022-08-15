/**
 * Służy do intuicyjnego porządkowania parametrów funkcji oceny heurystycznej.
 */
public enum HParam {
    PAWNS,
    KINGS,
    ENEMY_PAWNS,
    ENEMY_KINGS,
    SAFE_PAWNS,     // adjacent to wall
    SAFE_KINGS,
    SAFE_ENEMY_PAWNS,
    SAFE_ENEMY_KINGS,
    MOVABLE_PAWNS,  // can make any move (without counting in captures)
    MOVABLE_KINGS,
    MOVABLE_ENEMY_PAWNS,
    MOVABLE_ENEMY_KINGS,
    POSSIBLE_MOVES,
    POSSIBLE_ENEMY_MOVES,
    CAPTURE_MOVE_EXISTS,       // is next move a capture move
    CAPTURE_MOVES,
    LONGEST_CAPTURE_MOVE,
    DISTANCE_TO_PROMOTION,
    DISTANCE_TO_ENEMY_PROMOTION,
    UNOCCUPIED_PROMOTION_FIELDS,
    UNOCCUPIED_ENEMY_PROMOTION_FIELDS,
    LOWERMOST_PAWNS,    // in two lowermost rows (from the perspective from the player)
    LOWERMOST_KINGS,
    LOWERMOST_ENEMY_PAWNS,
    LOWERMOST_ENEMY_KINGS,
    CENTRAL_PAWNS,      // in two central rows
    CENTRAL_KINGS,
    CENTRAL_ENEMY_PAWNS,
    CENTRAL_ENEMY_KINGS,
    UPPERMOST_PAWNS,    // in three uppermost rows
    UPPERMOST_KINGS,
    UPPERMOST_ENEMY_PAWNS,
    UPPERMOST_ENEMY_KINGS,
    LONER_PAWNS,
    LONER_KINGS,
    LONER_ENEMY_PAWNS,
    LONER_ENEMY_KINGS,
    CORNER_PAWN,
    CORNER_KING,
    DOUBLE_CORNER,
    CORNER_ENEMY_PAWN,
    CORNER_ENEMY_KING,
    DOUBLE_ENEMY_CORNER,
    TRIANGLE_PATTERN,   // pattern params can only have value of 0 or 1; some patterns are taken from [1] page 6
    OREO_PATTERN,
    BRIDGE_PATTERN,
    DOG_PATTERN,
    ENEMY_TRIANGLE_PATTERN,
    ENEMY_OREO_PATTERN,
    ENEMY_BRIDGE_PATTERN,
    ENEMY_DOG_PATTERN,
    BLOCKING_PIECES,    // behind another friendly piece
    BLOCKING_LINES,     // lines of at least 2 blocking pieces
    LONGEST_BLOCKING_LINE,
    BLOCKING_ENEMY_PIECES,
    ENEMY_BLOCKING_LINES,
    LONGEST_ENEMY_BLOCKING_LINE
}

/*  SOURCES
[1] https://pages.mini.pw.edu.pl/~mandziukj/PRACE/es_init.pdf
 */
