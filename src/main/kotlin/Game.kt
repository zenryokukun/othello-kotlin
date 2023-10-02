/**
 * Game盤情報。
 * BoardとGameクラスを定義。
 */

// Boardのマスの状態。
// 別ファイルのStateクラスでも使うのでprivateにはしていない。
const val BLACK = 1
const val WHITE = 2
const val EMPTY = 0


// ２次元配列の8方を走査するための方角。{"x":Int,"y":Int}
private val DIR = listOf(
    // right
    mapOf("x" to 1, "y" to 0),
    // right-down
    mapOf("x" to 1, "y" to 1),
    // down
    mapOf("x" to 0, "y" to 1),
    // left-down
    mapOf("x" to -1, "y" to 1),
    // left
    mapOf("x" to -1, "y" to 0),
    // left-up
    mapOf("x" to -1, "y" to -1),
    // up
    mapOf("x" to 0, "y" to -1),
    // up-right
    mapOf("x" to 1, "y" to -1),
)

/**
 * ボード情報。２次元配列で各マスの状態（EMPTY,WHITE,BLACK）を保持。
 */
class Board {
    // ボード版。二次元配列。
    private val board: MutableList<MutableList<Int>> = mutableListOf()

    // ボードを初期化。
    init {
        for (i in 0..7) {
            board.add(i, mutableListOf())
            for (j in 0..7) {
                board[i].add(0)
            }
        }
        board[3][4] = WHITE
        board[4][3] = WHITE
        board[3][3] = BLACK
        board[4][4] = BLACK
    }

    /**
     * x,yで指定したマスが空いている（EMPTY）かチェック。
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @return x,yで指定されたマスがEMPTYならtrue、以外ならfalse
     */
    fun isEmpty(x: Int, y: Int): Boolean {
        return board[y][x] == EMPTY
    }

    /**
     * x,yで指定したマスが敵の色かチェック。
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @return x,yで指定されたマスが敵色ならtrue、以外ならfalse
     */
    fun isOpponentColor(x: Int, y: Int, v: Int): Boolean {
        if (isEmpty(x, y)) return false
        return board[y][x] != v
    }

    /**
     * x,yで指定したマスが自分の色かチェック。
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @return x,yで指定されたマスが自分の色ならtrue、以外ならfalse
     */
    fun isSameColor(x: Int, y: Int, v: Int): Boolean {
        if (isEmpty(x, y)) return false
        return board[y][x] == v
    }

    /**
     * 指定したマスに指定した値をセット
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @param v セットする値：WHITEかBLACK。
     */
    fun set(x: Int, y: Int, v: Int) {
        board[y][x] = v
    }

    /**
     * 白マスと黒マスをカウントする
     *
     * @return {"black":Int,"white":Int}
     */
    fun count(): Map<String, Int> {
        var black = 0
        var white = 0
        for (row in board) {
            for (cell in row) {
                if (cell == BLACK) {
                    black++
                } else if (cell == WHITE) {
                    white++
                }
            }
        }
        return mapOf("black" to black, "white" to white)
    }

    /**
     * Boardをコマンドラインに描写する関数
     */
    fun print() {

        for ((i, row) in board.withIndex()) {
            var ch = ""
            if (i == 0) {
                ch += "   a b c d e f g h\n"
            }

            for ((j, v) in row.withIndex()) {
                if (j == 0) {
                    ch += (i + 1).toString() + " |"
                }
                ch += when (v) {
                    WHITE -> {
                        "●|"
                    }

                    BLACK -> {
                        "◯|"
                    }

                    else -> {
                        "_|"
                    }
                }
            }
            println(ch)
        }
    }
}


/**
 * BoardやStateを保持し、オセロを制御するためのクラス。
 */
class Game {

    // どちらの番か。BLACK | WHITE
    private var turn: Int = BLACK

    // ボード情報
    private val board: Board = Board()

    // ゲーム状態
    val state: State = State()

    /**
     * 黒←→白の番スイッチ
     */
    private fun change() {
        turn = if (turn == BLACK) WHITE else BLACK
    }

    /**
     * 指定されたx,yが、ボードの枠内かチェック
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @return 枠内ならtrue、以外はfalse
     */
    private fun isIn(x: Int, y: Int): Boolean {
        return (x in 0..7 && y in 0..7)
    }

    /**
     * 指定されたx,yが有効かチェック。
     * 枠内、空白でない、flip可能の全てを満たす場合、有効と判断。
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @return 有効ならtrue、以外はfalse
     */
    fun isValidCell(x: Int, y: Int): Boolean {
        if (!isIn(x, y)) {
            return false
        }
        if (!board.isEmpty(x, y)) {
            return false
        }

        for (d in DIR) {
            val isValid = checkDir(x, y, d)
            if (isValid) return true
        }
        return false
    }

    /**
     * x,yを起点に、flip可能なマスを全てflip
     *
     * @param x 横のindex
     * @param y 縦のindex
     */
    private fun flip(x: Int, y: Int) {
        for (d in DIR) {
            val isValid = checkDir(x, y, d)
            if (isValid) {
                flipLine(x, y, d)
            }
        }
    }

    /**
     * 指定されたx,yを起点にd（方角）に向かってflip
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @param d 方角 DIRの要素。{"x";Int,"y":Int}
     */
    private fun flipLine(x: Int, y: Int, d: Map<String, Int>) {
        val dx = d["x"]!!
        val dy = d["y"]!!
        var nx = x + dx
        var ny = y + dy
        while (board.isOpponentColor(nx, ny, turn)) {
            board.set(nx, ny, turn)
            nx += dx
            ny += dy
        }
    }

    /**
     * x,yを起点に、flip可能かチェックする。isValidCell内で使用
     *
     * @param x 横のindex
     * @param y 縦のindex
     * @param d 方角 DIRの要素。{"x";Int,"y":Int}
     * @return flip可能ならtrue、以外ならtrue
     */
    private fun checkDir(x: Int, y: Int, d: Map<String, Int>): Boolean {
        // 方角
        val dx = d["x"]!!
        val dy = d["y"]!!
        // クリックしたマスに方角を足し、隣接マスをチェック
        var nx = x + dx
        var ny = y + dy
        // 隣接マスが枠外ならfalse
        if (!isIn(nx, ny)) {
            return false
        }
        // 隣接マスが反対色でなければ次の方角へ
        if (!board.isOpponentColor(nx, ny, turn)) {
            return false
        }
        // ここまでくれば、隣接マスが反対色
        nx += dx
        ny += dy
        while (true) {
            if (!isIn(nx, ny) || board.isEmpty(nx, ny)) {
                return false
            }
            if (board.isSameColor(nx, ny, turn)) {
                return true
            }
            nx += dx
            ny += dy
        }
    }

    /**
     * ゲームが進行すると、どこにもおけるマスがない盤面も生じる。
     * 現在の手番でおけるマスが存在するかチェック。
     * ターンの最初にこのメソッドでチェックする必要がある。
     *
     * @return おけるマスがあればtrue、以外はfalse
     */
    private fun checkTurn(): Boolean {
        for (y in 0..7) {
            for (x in 0..7) {
                val isValid = isValidCell(x, y)
                if (isValid) return true
            }
        }
        return false
    }


    /**
     * 有効なマスがクリックされた場合に呼び出す。
     * クリックされたx,yを起点にflipし、クリックしたマス自身も更新し、
     * 手番をチェックし、手番とゲーム状態を更新する。
     *
     * @param x 横のindex
     * @param y 縦のindex
     */
    fun next(x: Int, y: Int) {
        flip(x, y)
        board.set(x, y, turn)
        change()
        if (!checkTurn()) {
            change()
            if (!checkTurn()) {
                state.gameOver()
            } else {
                state.skip()
            }
        }
    }

    /**
     * ゲーム状態とボードを描写する
     */
    fun print() {
        state.print(turn, board)
        board.print()
//        val player = if (turn == BLACK) "黒" else "白"
//        println("---------------------------------")
//        if (msg.isNotEmpty()) {
//            println(msg)
//        }
//        println("${player}の手番")
//        board.print()
    }
}
