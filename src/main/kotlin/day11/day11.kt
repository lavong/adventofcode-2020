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

--- Part Two ---

As soon as people start to arrive, you realize your mistake. People don't just care about adjacent seats - they care about the first seat they can see in each of those eight directions!

Now, instead of considering just the eight immediately adjacent seats, consider the first seat in each of those eight directions. For example, the empty seat below would see eight occupied seats:

.......#.
...#.....
.#.......
.........
..#L....#
....#....
.........
#........
...#.....

The leftmost empty seat below would only see one empty seat, but cannot see any of the occupied ones:

.............
.L.L.#.#.#.#.
.............

The empty seat below would see no occupied seats:

.##.##.
#.#.#.#
##...##
...L...
##...##
#.#.#.#
.##.##.

Also, people seem to be more tolerant than you expected: it now takes five or more visible occupied seats for an occupied seat to become empty (rather than four or more from the previous rules). The other rules still apply: empty seats that see no occupied seats become occupied, seats matching no rule don't change, and floor never changes.

Given the same starting layout as above, these new rules cause the seating area to shift around as follows:

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

#.LL.LL.L#
#LLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLL#
#.LLLLLL.L
#.LLLLL.L#

#.L#.##.L#
#L#####.LL
L.#.#..#..
##L#.##.##
#.##.#L.##
#.#####.#L
..#.#.....
LLL####LL#
#.L#####.L
#.L####.L#

#.L#.L#.L#
#LLLLLL.LL
L.L.L..#..
##LL.LL.L#
L.LL.LL.L#
#.LLLLL.LL
..L.L.....
LLLLLLLLL#
#.LLLLL#.L
#.L#LL#.L#

#.L#.L#.L#
#LLLLLL.LL
L.L.L..#..
##L#.#L.L#
L.L#.#L.L#
#.L####.LL
..#.#.....
LLL###LLL#
#.LLLLL#.L
#.L#LL#.L#

#.L#.L#.L#
#LLLLLL.LL
L.L.L..#..
##L#.#L.L#
L.L#.LL.L#
#.LLLL#.LL
..#.L.....
LLL###LLL#
#.LLLLL#.L
#.L#LL#.L#

Again, at this point, people stop shifting around and the seating area reaches equilibrium. Once this occurs, you count 26 occupied seats.

Given the new visibility method and the rule change for occupied seats becoming empty, once equilibrium is reached, how many seats end up occupied?

 */
package day11

import day11.SeatType.*
import java.lang.Exception
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() {
    val input = AdventOfCode.file("day11/input")
        .lines().filterNot { it.isBlank() }

    val seats = input.map { it.seatTypes() }

    measureTimedValue { gameOfSeats(seats, ::adjacentOccupationCount, 4) }
        .also { println("solution part 1: ${it.value} (time: ${it.duration})") }

    measureTimedValue { gameOfSeats(seats, ::raycastingOccupationCount, 5) }
        .also { println("solution part 2: ${it.value} (time: ${it.duration})") }
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

fun List<List<SeatType>>.occupationCounts(
    calcOccupation: (List<List<SeatType>>, Int, Int) -> Int
): List<List<Int>> {
    val heatMap = mutableListOf<MutableList<Int>>()
    indices.forEach { y ->
        heatMap.add(mutableListOf())
        this[y].indices.forEach { x ->
            heatMap[y].add(x, calcOccupation(this, x, y))
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

fun raycastingOccupationCount(input: List<List<SeatType>>, x: Int, y: Int): Int {
    var count = 0
    (-1..1).forEach { dy ->
        (-1..1).forEach { dx ->
            if (dy != 0 || dx != 0) {
                if (rayHitsOccupiedSeat(input, x, y, dx, dy)) ++count
            }
        }
    }
    return count
}

fun rayHitsOccupiedSeat(input: List<List<SeatType>>, x: Int, y: Int, dx: Int, dy: Int): Boolean {
    var j = y + dy
    var i = x + dx
    while (j in input.indices && i in input[j].indices) {
        when (input[j][i]) {
            FLOOR -> Unit
            EMPTY -> return false
            OCCUPIED -> return true
        }
        j += dy
        i += dx
    }
    return false
}

fun gameOfSeats(
    input: List<List<SeatType>>,
    calcOccupation: (List<List<SeatType>>, Int, Int) -> Int,
    occupationThreshold: Int
): Int {
    val maxRounds = 100
    var round = 0
    var prevOccupied = 0
    val seats = mutableListOf<MutableList<SeatType>>().apply {
        input.forEach { add(it.toMutableList()) }
    }

    while (round++ < maxRounds) {
        val heatmap = seats.occupationCounts(calcOccupation)
        heatmap.forEachIndexed { y, row ->
            row.forEachIndexed { x, adjacentOccupationCount ->
                when (seats[y][x]) {
                    FLOOR -> Unit
                    EMPTY -> if (adjacentOccupationCount == 0) seats[y][x] = OCCUPIED
                    OCCUPIED -> if (adjacentOccupationCount >= occupationThreshold) seats[y][x] = EMPTY
                }
            }
        }

        seats.seatCount(OCCUPIED)
            //.also { println("round $round: seats occupied: $it (prev = $prevOccupied)") }
            .takeIf { it != prevOccupied }
            ?.let { prevOccupied = it }
            ?: return prevOccupied
    }

    throw Exception("not stabilized by round $round")
}
