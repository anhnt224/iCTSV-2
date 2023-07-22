package com.bk.ctsv.models
import kotlin.random.Random

class Item(
    val n: Int, private val m: Int, private val t: Int, val a: Array<Array<Int>>,
    private val values: IntArray, private val group: IntArray, private val maxGroup: IntArray
) : Comparable<Item> {
    var pos = IntArray(n)
    var used = IntArray(m + 1)
    var valGroup = IntArray(t + 1)
    var curVal = 0
    var potential = 0

    init {
        pos.fill(0)
        used.fill(0)
        valGroup.fill(0)
    }

    fun setPos(i: Int, v: Int) {
        if (v == 0 || used[v] == 0) {
            if (used[pos[i]] == 1) {
                valGroup[group[pos[i]]] -= values[pos[i]]
            }
            used[pos[i]] = 0
            pos[i] = v
            if (used[pos[i]] == 0) {
                valGroup[group[pos[i]]] += values[pos[i]]
            }
            used[pos[i]] = 1
        }
    }

    fun update() {
        curVal = 0
        for (i in 0 until t) {
            curVal += if (valGroup[i] < maxGroup[i]) valGroup[i] else maxGroup[i]
        }

        potential = 0
        for (i in 0 until n) {
            if (used[a[i].last()] == 0) {
                potential += values[a[i].last()]
            }
        }
    }

    override fun compareTo(other: Item): Int {
        if (curVal != other.curVal) {
            return other.curVal - curVal
        }
        return other.potential - potential
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        if (curVal != other.curVal) return false
        if (potential != other.potential) return false

        return true
    }

    override fun hashCode(): Int {
        var result = curVal
        result = 31 * result + potential
        return result
    }
}

class GA(private val n: Int, private val m: Int, private val t: Int, private val a: Array<Array<Int>>,
         private val values: IntArray, private val group: IntArray, private val maxGroup: IntArray,
         private val isIn: Array<IntArray>) {

    private val SAMPLES = 1000
    private val GENERATIONS = 250
    private val candidates: MutableList<Item> = mutableListOf()

    init {
        candidates.addAll(List(SAMPLES) { Item(n, m, t, a, values, group, maxGroup) })
    }

    private fun crossover(pos1: Int, pos2: Int): Item {
        val npos = Random.nextInt(0, n)
        val res = Item(n, m, t, a, values, group, maxGroup)
        for (i in 0 until n) {
            if (i < npos) {
                res.setPos(i, candidates[pos1].pos[i])
            } else {
                res.setPos(i, candidates[pos2].pos[i])
            }
        }
        fill(res)
        return res
    }

    private fun localSearch(i: Item) {
        val pSkip = 20
        val pSwap = 5
        for (j in 0 until n) {
            val p = Random.nextInt(0, 100)
            if (p > pSkip) {
                continue
            }
            if (p > pSwap) {
                val available = listOf(0) + a[j].filter { u -> i.used[u] == 0 }
                val u = Random.nextInt(0, available.size)
                i.setPos(j, available[u])
                break
            } else {
                repeat(n) {
                    val k = Random.nextInt(0, n)
                    if (k == j || (i.pos[j] == 0) == (i.pos[k] == 0)) {
                        return@repeat
                    }
                    if (i.pos[j] != 0 && isIn[i.pos[j]][k] == 0) {
                        return@repeat
                    }
                    if (i.pos[k] != 0 && isIn[i.pos[k]][j] == 0) {
                        return@repeat
                    }
                    val old = i.pos[k]
                    i.setPos(k, i.pos[j])
                    i.setPos(j, old)
                    return
                }
            }
        }
        fill(i)
    }

    fun solve(): Item {
        var best = candidates[0]
        repeat(GENERATIONS) {
            candidates.sort()
            if (candidates[0] < best) {
                best = candidates[0]
            }

            for (i in SAMPLES / 2 until SAMPLES) {
                val pos1 = Random.nextInt(0, SAMPLES / 2)
                val pos2 = Random.nextInt(0, SAMPLES / 2)

                val child = crossover(pos1, pos2)

                localSearch(child)

                candidates[i] = child
            }
        }
        return best
    }
}


fun fill(i: Item) {
    for (j in 0 until i.n) {
        if (i.pos[j] != 0) {
            continue
        }
        val available = listOf(0) + i.a[j].filter { u -> i.used[u] == 0 }
        val u = Random.nextInt(0, available.size)
        i.setPos(j, available[u])
    }
    i.update()
}

