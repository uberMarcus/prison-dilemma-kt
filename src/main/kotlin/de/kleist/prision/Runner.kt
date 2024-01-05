package de.kleist.prision

import de.kleist.prision.strategies.*
import de.kleist.prision.strategies.Random
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.IntStream
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object Runner {
    private const val ROUNDS = 200
    private val RND = SecureRandom()

    @JvmStatic
    fun main(args: Array<String>) {

        val strategies = listOf(
            { uuid: UUID -> Tit4Tat(uuid) },
            { uuid: UUID -> TwoTits4Tat(uuid) },
            { uuid: UUID -> Random(uuid) },
            { uuid: UUID -> AlwaysCooperate(uuid) },
            { uuid: UUID -> AlwaysDefect(uuid) },
            { uuid: UUID -> Friedman(uuid) },
            { uuid: UUID -> DefectingJoss(uuid) },
            { uuid: UUID -> CooperatingJoss(uuid) },
        )

        val processedPairs: HashSet<String> = HashSet()
        val scores = HashMap<String, AtomicLong>()

        for (strategy in strategies) {

            for (opponent in strategies) {

                val context = Context()
                val s1 = context.registerStrategy(strategy)
                val s2 = context.registerStrategy(opponent)

                val pairName = getPairName(s1, s2)
                if(processedPairs.contains(pairName)){
                    //println("$pairName is already processed!")
                    continue;
                }

                IntStream.range(0, ROUNDS + RND.nextInt(ROUNDS / 2 ))
                    .forEach { i: Int -> context.processNextRound() }

                println()
                println("# # # # # # # # # # # ")
                println("SCORE (${context.getCurrentRound()} rounds): ")
                println("${s1.name}:\t\t ${context.getMyCurrentScore(s1)}")
                println("${s2.name}:\t\t ${context.getMyCurrentScore(s2)}")

                println("\t${s1.name} : \t\t ${context.getMyLastDecisions(s1, ROUNDS)}")
                println("\t${s2.name} : \t\t ${context.getMyLastDecisions(s2, ROUNDS)}")

               scores.computeIfAbsent(s1.name) { AtomicLong(0) }
               scores[s1.name]!!.addAndGet(context.getMyCurrentScore(s1))

                scores.computeIfAbsent(s2.name) { AtomicLong(0) }
                scores[s2.name]!!.addAndGet(context.getMyCurrentScore(s2))

                processedPairs.add(pairName);
            }
        }

        println()
        println("# # # # # # # # # # # # # # ")

        val sortedScores = scores.toList().sortedBy { (_, value) -> value.get() }.reversed().toMap()

        sortedScores.forEach { (t, u) ->
            println("$t : $u")
        }
    }

    private fun getPairName(s1: Strategy, s2: Strategy): String {
        val strategies = mutableListOf(s1, s2)
        strategies.sortBy { it.name }

        return strategies.joinToString { it.name }
    }
}
