package de.kleist.prision

import java.util.*

interface Strategy {
    enum class Decision(private val toStringValue: String) {
        COOPERATE("C"),
        DEFECT("D");

        override fun toString(): String {
            return toStringValue
        }
    }

    fun nextDecision(context: Context): Decision

    val name: String
        get() = javaClass.simpleName
}
