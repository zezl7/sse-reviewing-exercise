package de.unistuttgart.heat

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.PrintWriter

object IOUtils {

    private val mapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())

    fun readConfig(path: String): Config {
        val file = File(path)
        require(file.exists()) { "Config file not found: ${file.absolutePath}" }
        return mapper.readValue(file)
    }

    /**
     * Writes solution snapshots to CSV.
     * Format:
     *   time,x,u
     *   t0,x0,u0
     *   ...
     *   t0,xN-1,uN-1
     *   t1,x0,u0
     *   ...
     */
    fun writeSnapshotsToCsv(path: String, snapshots: List<HeatSolver.SolutionSnapshot>) {
        File(path).parentFile?.mkdirs()
        PrintWriter(File(path)).use { pw ->
            pw.println("time,x,u")
            for (snapshot in snapshots) {
                val t = snapshot.time
                val x = snapshot.x
                val u = snapshot.u
                require(x.size == u.size) { "x and u arrays must have same length" }
                for (i in x.indices) {
                    pw.println("$t,${x[i]},${u[i]}")
                }
            }
        }
    }
}