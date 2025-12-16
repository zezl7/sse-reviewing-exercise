package de.unistuttgart.heat

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Configuration for the 1D heat equation solver.
 *
 * Governing PDE:
 *   u_t = alpha * u_xx,  x in (0, L), t in (0, T]
 *
 * Discretization:
 *   Explicit finite difference:
 *     u_i^{n+1} = u_i^n + r * (u_{i+1}^n - 2 u_i^n + u_{i-1}^n),  r = alpha * dt / dx^2
 *
 * Stability (explicit scheme):
 *   r <= 0.5  (for interior points) is a typical stability criterion.
 */
data class Config(
    /** Thermal diffusivity alpha (>0). */
    val alpha: Double,

    /** Spatial domain length L (>0). */
    val length: Double,

    /** Number of spatial grid points N >= 3. */
    @JsonProperty("num_points")
    val numPoints: Int,

    /** Final time T (>0). */
    @JsonProperty("final_time")
    val finalTime: Double,

    /** Time step dt (>0). */
    @JsonProperty("time_step")
    val timeStep: Double,

    /** Output CSV file path. */
    @JsonProperty("output_file")
    val outputFile: String,

    /** Output every k-th time step (>=1). */
    @JsonProperty("output_interval")
    val outputInterval: Int = 1
)