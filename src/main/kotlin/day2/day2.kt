/*
--- Part Two ---

The Elves in accounting are thankful for your help; one of them even offers you a starfish coin they had left over from a past vacation. They offer you a second one if you can find three numbers in your expense report that meet the same criteria.

Using the above example again, the three entries that sum to 2020 are 979, 366, and 675. Multiplying them together produces the answer, 241861950.

In your expense report, what is the product of the three entries that sum to 2020?
 */

package day2

import A
import kotlin.system.exitProcess

const val target = 2020

fun main() {
    val input = A::class.java.classLoader.getResource("day1/input")
        .readText()
        .lines()
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toHashSet()
        .filter { it < target }

    input.listIterator().forEach { n1 ->
        val n2CandidatesWithSums = mutableMapOf<Int, Int>().apply {
            input.filter { it < (target - n1) }
                .onEach { put(it, n1 + it) }
        }

        if (n2CandidatesWithSums.isNotEmpty()) {
            n2CandidatesWithSums.forEach { (n2, sumN1N2) ->
                (target - sumN1N2)
                    .takeIf { input.contains(it) }
                    ?.let { println("solution: $n1 * $n2 * $it = ${n1 * n2 * it}") }
                    ?.also { exitProcess(0) }
            }
        }
    }

    println("no solution, no good")
}