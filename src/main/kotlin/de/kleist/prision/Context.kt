package de.kleist.prision

import de.kleist.prision.Strategy.Decision
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.min

class Context {

    private var currentRound = 0

    private val strategies : MutableMap<UUID, Strategy> = HashMap()
    private val currentScore: MutableMap<UUID, AtomicLong> = HashMap()
    private val decisions: MutableMap<UUID, MutableList<Decision>> = HashMap()
    private val opponents : Map<UUID, UUID> = HashMap()

    public fun registerStrategy(s: Strategy) : UUID{

        val uuid = UUID.randomUUID()
        currentScore[uuid] = AtomicLong(0)
        decisions[uuid] = ArrayList<Decision> ()

        strategies[uuid] = s

        if(opponents.size)

        return uuid
    }

    fun processNextRound() {
        currentRound++

        if(strategies.keys.size != 2){
            throw IllegalStateException("exact 2 strategies are allowed")
        }

        val s1Uuid = strategies.firstNotNullOf { it.key }
        val s2Uuid = strategies.filter { it.key != s1Uuid }.firstNotNullOf { it.key }

        val d1 = strategies[s1Uuid]!!.nextDecision(this)
        val d2 = strategies[s2Uuid]!!.nextDecision(this)

        decisions[s1Uuid]!!.add(d1)
        decisions[s2Uuid]!!.add(d2)

        if (d1 == Decision.COOPERATE && d2 == Decision.COOPERATE) {
            currentScore[s1Uuid]!!.addAndGet(SCORE_BOTH_COOPERATE.toLong())
            currentScore[s2Uuid]!!.addAndGet(SCORE_BOTH_COOPERATE.toLong())
        } else if (d1 == Decision.DEFECT && d2 == Decision.COOPERATE) {
            currentScore[s1Uuid]!!.addAndGet(SCORE_SINGLE_DEFECT.toLong())
            currentScore[s2Uuid]!!.addAndGet(SCORE_SINGLE_COOPERATE.toLong())
        } else if (d1 == Decision.COOPERATE && d2 == Decision.DEFECT) {
            currentScore[s1Uuid]!!.addAndGet(SCORE_SINGLE_COOPERATE.toLong())
            currentScore[s2Uuid]!!.addAndGet(SCORE_SINGLE_DEFECT.toLong())
        } else {
            currentScore[s1Uuid]!!.addAndGet(SCORE_BOTH_DEFECT.toLong())
            currentScore[s2Uuid]!!.addAndGet(SCORE_BOTH_DEFECT.toLong())
        }
    }

    fun getMyLastDecisions(uuid: UUID, n: Int): List<Decision?> {
        val size = decisions[uuid]!!.size
        return decisions[uuid]!!.subList((size - min(size.toDouble(), n.toDouble())).toInt(), size)
    }

    fun getOpponentsLastDecisions(uuid: Strategy, n: Int): List<Decision> {
        val size = decisions[opponents[s]]!!.size
        return decisions[opponents[s]]!!
            .subList((size - min(size.toDouble(), n.toDouble())).toInt(), size)
    }

    fun getMyCurrentScore(s: Strategy?): Long {
        return currentScore[s]!!.get()
    }

    fun getOpponentsCurrentScore(s: Strategy): Long {
        return currentScore[opponents[s]]!!.get()
    }

    fun getCurrentRound(): Long {
        return currentRound.toLong()
    }

    companion object {
        private const val SCORE_BOTH_COOPERATE = 3
        private const val SCORE_BOTH_DEFECT = 1

        private const val SCORE_SINGLE_DEFECT = 5

        private const val SCORE_SINGLE_COOPERATE = 0
    }
}
