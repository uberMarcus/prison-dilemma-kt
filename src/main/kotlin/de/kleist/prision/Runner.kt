package de.kleist.prision

import de.kleist.prision.strategies.AlwaysCooperate
import de.kleist.prision.strategies.AlwaysDefect
import de.kleist.prision.strategies.Random
import de.kleist.prision.strategies.Tit4Tat
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.IntStream

object Runner {
    private const val ROUNDS = 500

    @JvmStatic
    fun main(args: Array<String>) {

        val strategies = listOf(Tit4Tat(), Random(), AlwaysCooperate(), AlwaysDefect())
        val processedPairs: HashSet<String> = HashSet()
        val scores = HashMap<String, AtomicLong>()

        for (strategy in strategies) {
            println("evaluating: " + strategy.name)

            for (opponent in strategies) {

                val pairName = getPairName(strategy, opponent)
                if(processedPairs.contains(pairName)){
                    println("$pairName is already processed!")
                    continue;
                }

                val context = Context(strategy, opponent)

                IntStream.range(0, ROUNDS)
                    .forEach { i: Int -> context.processNextRound() }

                println()
                println("# # # # # # # # # # # ")
                println("SCORE: ")
                println("${strategy.name}:\t\t ${context.getMyCurrentScore(strategy)}")
                println("${opponent.name}:\t\t ${context.getMyCurrentScore(opponent)}")

                println("\t${strategy.name} : \t\t ${context.getMyLastDecisions(strategy, ROUNDS)}")
                println("\t${opponent.name} : \t\t ${context.getMyLastDecisions(opponent, ROUNDS)}")

               scores.computeIfAbsent(strategy.name) { n -> AtomicLong(0) }
               scores[strategy.name]!!.addAndGet(context.getMyCurrentScore(strategy))

                scores.computeIfAbsent(opponent.name) { n -> AtomicLong(0) }
                scores[opponent.name]!!.addAndGet(context.getMyCurrentScore(opponent))

                processedPairs.add(pairName);
            }
        }

        println()
        println("# # # # # # # # # # # # # # ")
        scores.forEach { (t, u) ->
            println("$t : $u")
        }
    }

    private fun getPairName(s1: Strategy, s2: Strategy): String {
        val strategies = mutableListOf(s1, s2)
        strategies.sortBy { it.name }

        return strategies.joinToString { it.name }
    }
}
