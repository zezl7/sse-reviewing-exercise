package de.unistuttgart.heat

/**
 * Entry point for the heat equation solver.
 *
 * Usage:
 *   java -jar heat-solver.jar path/to/config.json
 *
 * The config file defines physical and numerical parameters
 * as well as the output file path.
 */
fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: java -jar heat-solver.jar <config.json>")
        return
    }

    val configPath = args[0]
    val config = IOUtils.readConfig(configPath)

    val solver = HeatSolver(config)
    val snapshots = solver.solve()

    IOUtils.writeSnapshotsToCsv(config.outputFile, snapshots)

    println("Simulation finished. Output written to: ${config.outputFile}")
}