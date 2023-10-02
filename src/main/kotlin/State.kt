private const val PLAY = 0
private const val GAME_OVER = 1
private const val SKIP = 2

class State {

    private var status: Int = PLAY

    fun gameOver() {
        status = GAME_OVER
    }

    fun skip() {
        status = SKIP
    }

    fun isGameOver(): Boolean {
        return status == GAME_OVER
    }

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

    private fun isSkip(): Boolean {
        return status == SKIP
    }

    private fun play() {
        status = PLAY
    }

}