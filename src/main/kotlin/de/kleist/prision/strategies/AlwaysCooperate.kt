package de.kleist.prision.strategies

import de.kleist.prision.Context
import de.kleist.prision.Strategy
import de.kleist.prision.Strategy.Decision
import java.util.*

class AlwaysCooperate(uuid: UUID) : Strategy(uuid) {
    override fun nextDecision(context: Context): Decision {
        return Decision.COOPERATE
    }
}
