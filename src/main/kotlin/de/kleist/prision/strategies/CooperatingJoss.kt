package de.kleist.prision.strategies

import de.kleist.prision.Context
import de.kleist.prision.Strategy
import java.security.SecureRandom
import java.util.UUID

class CooperatingJoss(uuid: UUID) : Strategy(uuid) {

    private val rnd = SecureRandom()

    override fun nextDecision(context: Context): Decision {

        if(rnd.nextInt(10) == 0){
            return Decision.COOPERATE
        }
        return context.getOpponentsLastDecision(this) ?: Decision.COOPERATE
    }
}