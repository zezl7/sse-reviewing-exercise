package de.unistuttgart.heat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.io.File

class ConfigTest {

    @Test
    fun testConfigReadWriteCycle() {
        val tmpConfigFile = File("build/tmp-config.json")
        tmpConfigFile.parentFile.mkdirs()
        tmpConfigFile.writeText(
            """
            {
              "alpha": 0.01,
              "length": 1.0,
              "num_points": 11,
              "final_time": 0.1,
              "time_step": 0.001,
              "output_file": "build/tmp-output.csv",
              "output_interval": 5
            }
            """.trimIndent()
        )

        val cfg = IOUtils.readConfig(tmpConfigFile.absolutePath)
        assertEquals(0.01, cfg.alpha)
        assertEquals(1.0, cfg.length)
        assertEquals(11, cfg.numPoints)
        assertEquals(0.1, cfg.finalTime)
        assertEquals(0.001, cfg.timeStep)
        assertEquals("build/tmp-output.csv", cfg.outputFile)
        assertEquals(5, cfg.outputInterval)
    }

    @Test
    fun testCsvOutputCreated() {
        val cfg = Config(
            alpha = 0.01,
            length = 1.0,
            numPoints = 11,
            finalTime = 0.001,
            timeStep = 1e-4,
            outputFile = "build/test-output/heat.csv",
            outputInterval = 1
        )
        val solver = HeatSolver(cfg)
        val snapshots = solver.solve()
        IOUtils.writeSnapshotsToCsv(cfg.outputFile, snapshots)

        val outFile = File(cfg.outputFile)
        assertTrue(outFile.exists())
        val lines = outFile.readLines()
        assertTrue(lines.size > 1)  // header + data
        assertEquals("time,x,u", lines.first())
    }
}