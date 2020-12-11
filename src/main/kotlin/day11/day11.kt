/*
--- Day 11: Seating System ---

Your plane lands with plenty of time to spare. The final leg of your journey is a ferry that goes directly to the tropical island where you can finally start your vacation. As you reach the waiting area to board the ferry, you realize you're so early, nobody else has even arrived yet!

By modeling the process people use to choose (or abandon) their seat in the waiting area, you're pretty sure you can predict the best place to sit. You make a quick map of the seat layout (your puzzle input).

The seat layout fits neatly on a grid. Each position is either floor (.), an empty seat (L), or an occupied seat (#). For example, the initial seat layout might look like this:

L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL

Now, you just need to model the people who will be arriving shortly. Fortunately, people are entirely predictable and always follow a simple set of rules. All decisions are based on the number of occupied seats adjacent to a given seat (one of the eight positions immediately up, down, left, right, or diagonal from the seat). The following rules are applied to every seat simultaneously:

    If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
    If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
    Otherwise, the seat's state does not change.

Floor (.) never changes; seats don't move, and nobody sits on the floor.

After one round of these rules, every seat in the example layout becomes occupied:

#.##.##.##
#######.##
#.#.#..#..
####.##.##
#.##.##.##
#.#####.##
..#.#.....
##########
#.######.#
#.#####.##

After a second round, the seats with four or more occupied adjacent seats become empty again:

#.LL.L#.##
#LLLLLL.L#
L.L.L..L..
#LLL.LL.L#
#.LL.LL.LL
#.LLLL#.##
..L.L.....
#LLLLLLLL#
#.LLLLLL.L
#.#LLLL.##

This process continues for three more rounds:

#.##.L#.##
#L###LL.L#
L.#.#..#..
#L##.##.L#
#.##.LL.LL
#.###L#.##
..#.#.....
#L######L#
#.LL###L.L
#.#L###.##

#.#L.L#.##
#LLL#LL.L#
L.L.L..#..
#LLL.##.L#
#.LL.LL.LL
#.LL#L#.##
..L.L.....
#L#LLLL#L#
#.LLLLLL.L
#.#L#L#.##

#.#L.L#.##
#LLL#LL.L#
L.#.L..#..
#L##.##.L#
#.#L.LL.LL
#.#L#L#.##
..L.L.....
#L#L##L#L#
#.LLLLLL.L
#.#L#L#.##

At this point, something interesting happens: the chaos stabilizes and further applications of these rules cause no seats to change state! Once people stop moving around, you count 37 occupied seats.

Simulate your seating area by applying the seating rules repeatedly until no seats change state. How many seats end up occupied?

 */
package day11

import day11.SeatType.*
import java.lang.Exception

fun main() {

    val input = AdventOfCode.file("day11/input")
        .lines().filterNot { it.isBlank() }

    input.map { it.seatTypes() }
        .let { simulate(it) }
        .also { println("solution: $it") }
}

enum class SeatType(val char: Char) {
    FLOOR('.'),
    EMPTY('L'),
    OCCUPIED('#');

    companion object {
        fun forChar(c: Char) = values().find { it.char == c }
            ?: throw IllegalArgumentException("unexpected seat type: $c")
    }
}

fun String.seatTypes(): List<SeatType> = toCharArray().map { SeatType.forChar(it) }

fun List<List<SeatType>>.seatCount(type: SeatType): Int {
    return map { it.count { it == type } }.sum()
}

fun List<List<SeatType>>.occupationCounts(): List<List<Int>> {
    val heatMap = mutableListOf<MutableList<Int>>()
    indices.forEach { y ->
        heatMap.add(mutableListOf())
        this[y].indices.forEach { x ->
            heatMap[y].add(x, adjacentOccupationCount(this, x, y))
        }
    }
    return heatMap
}

fun adjacentOccupationCount(input: List<List<SeatType>>, x: Int, y: Int): Int {
    var count = 0
    ((y - 1)..(y + 1)).forEach { j ->
        ((x - 1)..(x + 1)).forEach { i ->
            if ((i != x || j != y) && j in input.indices && i in input[j].indices) {
                if (input[j][i] == OCCUPIED) ++count
            }
        }
    }
    return count
}

fun simulate(input: List<List<SeatType>>): Int {
    val maxRounds = 1_000
    var round = 0
    var prevOccupied = 0
    val seats = mutableListOf<MutableList<SeatType>>().apply {
        input.forEach { add(it.toMutableList()) }
    }

    while (round++ < maxRounds) {
        val heatmap = seats.occupationCounts()
        heatmap.forEachIndexed { y, row ->
            row.forEachIndexed { x, adjacentOccupationCount ->
                when (seats[y][x]) {
                    FLOOR -> Unit
                    EMPTY -> if (adjacentOccupationCount == 0) seats[y][x] = OCCUPIED
                    OCCUPIED -> if (adjacentOccupationCount >= 4) seats[y][x] = EMPTY
                }
            }
        }
//        seats.onEach { println(it.map { it.char }) }

        seats.seatCount(OCCUPIED)
            //.also { println("round $round: seats occupied: $it (prev = $prevOccupied)") }
            .takeIf { it != prevOccupied }
            ?.let { prevOccupied = it }
            ?: return prevOccupied
    }

    throw Exception("not stabilized by round $round")
}
