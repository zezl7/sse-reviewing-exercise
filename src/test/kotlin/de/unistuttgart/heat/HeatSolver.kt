package de.unistuttgart.heat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HeatSolverTest {

    @Test
    fun testStabilityParameter() {
        val cfg = Config(
            alpha = 0.01,
            length = 1.0,
            numPoints = 11,
            finalTime = 0.01,
            timeStep = 1e-4,
            outputFile = "build/test-output.csv",
            outputInterval = 10
        )
        val solver = HeatSolver(cfg)
        val snapshots = solver.solve()
        assertTrue(snapshots.isNotEmpty())
    }

    @Test
    fun testInitialConditionSinShape() {
        val cfg = Config(
            alpha = 0.01,
            length = 1.0,
            numPoints = 11,
            finalTime = 0.0,
            timeStep = 1e-4,
            outputFile = "build/test-output.csv",
            outputInterval = 1
        )
        val solver = HeatSolver(cfg)
        val snapshots = solver.solve()
        assertEquals(1, snapshots.size)
        val s0 = snapshots[0]
        // Check that interior values are between -1 and 1
        for (i in 1 until cfg.numPoints - 1) {
            assertTrue(s0.u[i] <= 1.0 + 1e-12)
            assertTrue(s0.u[i] >= -1.0 - 1e-12)
        }
    }

    @Test
    fun testBoundaryValuesStayZero() {
        val cfg = Config(
            alpha = 0.01,
            length = 1.0,
            numPoints = 21,
            finalTime = 0.01,
            timeStep = 1e-4,
            outputFile = "build/test-output.csv",
            outputInterval = 10
        )
        val solver = HeatSolver(cfg)
        val snapshots = solver.solve()
        for (s in snapshots) {
            assertEquals(0.0, s.u.first(), 1e-12)
            assertEquals(0.0, s.u.last(), 1e-12)
        }
    }
}