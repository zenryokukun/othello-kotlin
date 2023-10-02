const val BLACK = 1
const val WHITE = 2
const val EMPTY = 0
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

class Board {

    private val board: MutableList<MutableList<Int>> = mutableListOf()

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

    fun isEmpty(x: Int, y: Int): Boolean {
        return board[y][x] == EMPTY
    }

    fun isOpponentColor(x: Int, y: Int, v: Int): Boolean {
        if (isEmpty(x, y)) return false
        return board[y][x] != v
    }

    fun isSameColor(x: Int, y: Int, v: Int): Boolean {
        if (isEmpty(x, y)) return false
        return board[y][x] == v
    }

    fun set(x: Int, y: Int, v: Int) {
        board[y][x] = v
    }

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

class Game {

    private var turn: Int = BLACK
    private val board: Board = Board()
    val state: State = State()

    private fun change() {
        turn = if (turn == BLACK) WHITE else BLACK
    }

    private fun isIn(x: Int, y: Int): Boolean {
        return (x in 0..7 && y in 0..7)
    }

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

    private fun flip(x: Int, y: Int) {
        for (d in DIR) {
            val isValid = checkDir(x, y, d)
            if (isValid) {
                flipLine(x, y, d)
            }
        }
    }

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

    private fun checkTurn(): Boolean {
        for (y in 0..7) {
            for (x in 0..7) {
                val isValid = isValidCell(x, y)
                if (isValid) return true
            }
        }
        return false
    }


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
