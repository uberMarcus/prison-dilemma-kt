package de.kleist.prision.strategies

import de.kleist.prision.Context
import de.kleist.prision.Strategy
import java.util.UUID

class Friedman(uuid: UUID) : Strategy(uuid) {

    private var opponentHasOnceDefected = false

    override fun nextDecision(context: Context): Decision {

        if(opponentHasOnceDefected){
            return Decision.DEFECT
        }

        if(context.getOpponentsLastDecision(this) == Decision.DEFECT){
            opponentHasOnceDefected = true
            return Decision.DEFECT
        }
        return Decision.COOPERATE
    }
}