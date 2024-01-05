package de.kleist.prision.strategies

import de.kleist.prision.Context
import de.kleist.prision.Strategy
import de.kleist.prision.Strategy.Decision
import java.util.*

class TwoTits4Tat(uuid: UUID) : Strategy(uuid) {
    override fun nextDecision(context: Context): Decision {
        return context.getOpponentsLastDecisions(this, 2)
            .find { it == Decision.DEFECT } ?: Decision.COOPERATE
    }
}
