package de.kleist.prision

import java.util.*

abstract class Strategy(val uuid: UUID) {
    enum class Decision(private val toStringValue: String) {
        COOPERATE("C"),
        DEFECT("D");

        override fun toString(): String {
            return toStringValue
        }
    }

    abstract fun nextDecision(context: Context): Decision

    val name: String
        get() = javaClass.simpleName
}
