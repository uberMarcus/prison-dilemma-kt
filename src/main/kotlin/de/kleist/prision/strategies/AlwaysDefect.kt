package de.kleist.prision.strategies

import de.kleist.prision.Context
import de.kleist.prision.Strategy
import de.kleist.prision.Strategy.Decision

class AlwaysDefect : Strategy {
    override fun nextDecision(context: Context): Decision {
        return Decision.DEFECT
    }
}
