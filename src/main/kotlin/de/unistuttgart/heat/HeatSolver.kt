package de.unistuttgart.heat

/**
 * Simple 1D heat equation solver using an explicit finite-difference scheme.
 *
 * Spatial grid: x_i = i * dx, i = 0..N-1 (dx = L / (N-1)).
 * Time grid: t_n = n * dt, n = 0..Nt.
 *
 * Boundary conditions (Dirichlet):
 *   u(0,t) = 0, u(L,t) = 0.
 *
 * Initial condition (example):
 *   u(x,0) = sin(pi * x / L).
 */
class HeatSolver(private val config: Config) {

    init {
        require(config.alpha > 0.0) { "alpha must be > 0" }
        require(config.length > 0.0) { "length must be > 0" }
        require(config.numPoints >= 3) { "numPoints must be >= 3" }
        require(config.finalTime > 0.0) { "finalTime must be > 0" }
        require(config.timeStep > 0.0) { "timeStep must be > 0" }
        require(config.outputInterval >= 1) { "outputInterval must be >= 1" }
    }

    data class SolutionSnapshot(
        val time: Double,
        val x: DoubleArray,
        val u: DoubleArray
    )

    /**
     * Runs the simulation and returns all snapshots that should be written to disk.
     * For large simulations, consider streaming output instead of keeping all snapshots in memory.
     */
    fun solve(): List<SolutionSnapshot> {
        val n = config.numPoints
        val dx = config.length / (n - 1)
        val dt = config.timeStep
        val alpha = config.alpha

        val r = alpha * dt / (dx * dx)
        require(r <= 0.5) {
            "Stability condition violated for explicit scheme: r = alpha * dt / dx^2 = $r > 0.5"
        }

        val numTimeSteps = MathUtils.ceilToInt(config.finalTime / dt)
        val x = DoubleArray(n) { i -> i * dx }

        // Initial condition u(x, 0) = sin(pi * x / L)
        var u = DoubleArray(n) { i ->
            kotlin.math.sin(Math.PI * x[i] / config.length)
        }
        var uNew = DoubleArray(n)

        val snapshots = mutableListOf<SolutionSnapshot>()
        snapshots.add(SolutionSnapshot(0.0, x.copyOf(), u.copyOf()))

        for (step in 1..numTimeSteps) {
            // Dirichlet BC: u[0] = 0, u[n-1] = 0
            uNew[0] = 0.0
            uNew[n - 1] = 0.0

            for (i in 1 until n - 1) {
                uNew[i] = u[i] + r * (u[i + 1] - 2.0 * u[i] + u[i - 1])
            }

            // Swap references (avoid re-allocation)
            val tmp = u
            u = uNew
            uNew = tmp

            val time = step * dt
            if (step % config.outputInterval == 0 || step == numTimeSteps) {
                snapshots.add(SolutionSnapshot(time, x.copyOf(), u.copyOf()))
            }
        }

        return snapshots
    }
}