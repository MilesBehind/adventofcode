package de.smartsteuer.frank.katas

interface TicTacToe {
  /**
   * Evaluates a game state.
   * @param gameState game state like this example:
   *  ```kotlin
   *  gameState = listOf("XOX",
   *                     "O  ",
   *                     "X O")
   *  ```
   * @return evaluation result
   */
  fun evaluate(gameState: List<String>): TicTacToeEvaluationResult
}

enum class TicTacToeEvaluationResult {
  X_WINS,
  O_WINS,
  DRAW,
  IN_PROGRESS
}
