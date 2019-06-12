package core

enum class Player {
    BLUE,
    RED;

    fun opposite(): Player {
        return if (this == BLUE)
            RED
        else
            BLUE
    }
}