/**
 * コマンドラインで遊ぶオセロ。
 * kotlinに慣れるために作成。
 */

fun main() {

    // Game初期化
    val game = Game()

    // 描写フラグ。入力誤りの際等に再度描写したくないので。
    var render = true

    // ゲームが終了するまでループ
    while (true) {

        // 描写フラグがオンなら描写
        if (render) {
            game.print()
        }

        // ユーザ入力のx（横）とy（縦）
        var x: String?
        var y: String?

        // ゲーム終了ならループ抜ける
        if (game.state.isGameOver()) {
            break
        }

        // 縦（y）のユーザ入力。正しい入力がされるまで無限ループ
        while (true) {
            print("縦(半角1~8で入力)：")
            y = readLine()
            if (checkRowInput(y)) break
            println("半角1~8の数字で入力してください！")
        }

        // 横（x）のユーザ入力。正しい入力がされるまで無限ループ
        while (true) {
            print("横（半角a～hで入力）:")
            x = readLine()
            if (checkColumnInput(x)) break
            println("半角a～hで入力してください！")
        }

        // 入力値をindexに変換。
        // y: 1 -> 0, 2 -> 1... x: a -> 0, b -> 1,...
        // 事前チェックしているのでnullはありえない。
        val indexY = rowToIndex(y!!)
        val indexX = columnToIndex(x!!)

        // ひっくり返せるマスが選択されている場合、描写フラグをオンにしGameを更新。
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

/**
 * ユーザ入力（縦:y）のチェック。
 *
 * @param y ユーザ入力
 * @return 1～8であればtrue,以外はfalse
 */
fun checkRowInput(y: String?): Boolean {
    if (y == null) return false
    if (y !in "12345678") return false
    return true
}

/**
 * ユーザ入力（縦:x）のチェック。
 *
 * @param x ユーザ入力
 * @return a～hであればtrue,以外はfalse
 */
fun checkColumnInput(x: String?): Boolean {
    if (x == null) return false
    if (x !in "abcdedfgh") return false
    return true
}

/**
 * ユーザ入力yをindexに変換。checkRowInput後に呼ぶこと。
 *
 * @param y ユーザ入力のy。"1"～"8"である必要がある。
 * @return listのindexに変換したInt。
 */
fun rowToIndex(y: String): Int {
    return y.toInt() - 1
}

/**
 * ユーザ入力xをindexに変換。checkColumnInput後に呼ぶこと。
 *
 * @param x ユーザ入力のx。"a"～"h"である必要がある。
 * @return listのindexに変換したInt
 */
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