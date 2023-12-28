package de.kleist.prision.strategies

import de.kleist.prision.Context
import de.kleist.prision.Strategy
import de.kleist.prision.Strategy.Decision
import java.security.SecureRandom

class Random : Strategy {
    private val rnd = SecureRandom()
    override fun nextDecision(context: Context): Decision {
        if (rnd.nextBoolean()) {
            return Decision.COOPERATE
        }
        return Decision.DEFECT
    }
}
