
// ゲーム状態
private const val PLAY = 0
private const val GAME_OVER = 1
private const val SKIP = 2

/**
 * Game状態のクラス。Gameクラスのメンバとして使う
 */
class State {

    // ゲーム状態
    private var status: Int = PLAY

    /**
     * ゲームオーバ状態に更新
     */
    fun gameOver() {
        status = GAME_OVER
    }

    /**
     * スキップ状態に更新
     */
    fun skip() {
        status = SKIP
    }

    /**
     * ゲームオーバ状態かチェック
     *
     * @return ゲームオーバ状態ならtrue、以外はfalse
     */
    fun isGameOver(): Boolean {
        return status == GAME_OVER
    }

    /**
     * ゲーム状態を描写。マス数もここで描写したいのでBoardも受け取る
     *
     * @param turn 手番
     * @param board ボード情報
     */
    fun print(turn: Int, board: Board) {
        val count = board.count()
        println("------------------------")
        println("黒:${count["black"]!!} 白:${count["white"]!!}")

        if (isGameOver()) {
            val winner = if (count["black"]!! > count["white"]!!) "黒" else "白"
            println("ゲーム終了！${winner}の勝ち！")
        } else {
            if (isSkip()) {
                println("おけるマスがなかったのでスキップします！")
                play()
            }
            val player = if (turn == BLACK) "黒" else "白"
            println("${player}の手番")
        }
    }

    /**
     * スキップ状態かチェック
     *
     * @return スキップ状態ならtrue、以外はfalse
     */
    private fun isSkip(): Boolean {
        return status == SKIP
    }

    /**
     * プレイ状態に更新
     */
    private fun play() {
        status = PLAY
    }

}