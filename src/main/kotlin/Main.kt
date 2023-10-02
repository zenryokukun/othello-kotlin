fun main() {

    val game = Game()
    var render = true
    while (true) {

        if (render) {
            game.print()
        }

        var x: String?
        var y: String?
        if (game.state.isGameOver()) {
            break
        }
        while (true) {
            print("縦(半角1~8で入力)：")
            y = readLine()
            if (checkRowInput(y)) break
            println("半角1~8の数字で入力してください！")
        }
        while (true) {
            print("横（半角a～hで入力）:")
            x = readLine()
            if (checkColumnInput(x)) break
            println("半角a～hで入力してください！")
        }

        // 事前チェックしているのでnullはありえない。
        val indexY = rowToIndex(y!!)
        val indexX = columnToIndex(x!!)

        val valid = game.isValidCell(indexX, indexY)
        render = if (valid) {
            game.next(indexX, indexY)
            true
        } else {
            println("ひっくり返せないマスです。入力しなおし！")
            false
        }
    }
}

fun checkRowInput(y: String?): Boolean {
    if (y == null) return false
    if (y !in "12345678") return false
    return true
}

fun checkColumnInput(x: String?): Boolean {
    if (x == null) return false
    if (x !in "abcdedfgh") return false
    return true
}

fun rowToIndex(y: String): Int {
    return y.toInt() - 1
}

fun columnToIndex(x: String): Int {
    return when (x) {
        "a" -> 0
        "b" -> 1
        "c" -> 2
        "d" -> 3
        "e" -> 4
        "f" -> 5
        "g" -> 6
        "h" -> 7
        // 事前チェックしているのにここには流れない想定
        else -> 99
    }

}