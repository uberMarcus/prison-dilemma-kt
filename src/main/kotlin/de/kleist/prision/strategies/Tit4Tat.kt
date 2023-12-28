package de.kleist.prision.strategies

import de.kleist.prision.Context
import de.kleist.prision.Strategy
import de.kleist.prision.Strategy.Decision

class Tit4Tat : Strategy {
    override fun nextDecision(context: Context): Decision {
        return context.getOpponentsLastDecisions(this, 1).stream().findFirst()
            .orElse(Decision.COOPERATE)
    }
}
