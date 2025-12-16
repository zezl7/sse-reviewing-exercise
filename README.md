# 1D Heat Equation Solver (Kotlin)

## 1. Overview

This tool solves the 1D heat equation

$u_t = \alpha u_{xx}, \quad x \in (0, L), \ t \in (0, T]$

with homogeneous Dirichlet boundary conditions $u(0,t)=0$, $u(L,t)=0$, and initial condition $u(x,0) = \sin(\pi x / L)$.

The solver uses an explicit finite-difference scheme.

## 2. Configuration

Parameters are specified in a JSON configuration file, for example:

```json
{
  "alpha": 0.01,
  "length": 1.0,
  "num_points": 51,
  "final_time": 0.1,
  "time_step": 1e-4,
  "output_file": "output/heat_solution.csv",
  "output_interval": 100
}
```
- `alpha`: thermal diffusivity.
- `length`: domain length (L).
- `num_points`: number of spatial grid points (>= 3).
- `final_time`: final simulation time (T).
- `time_step`: time step (\Delta t).
- `output_file`: path for CSV output.
- `output_interval`: write every k-th time step.

Stability condition (explicit scheme): $r = \alpha \frac{\Delta t}{\Delta x^2} \le 0.5.$

The code enforces this condition via a runtime check.

## 3. Running the Solver

Build a runnable JAR with Gradle:

```bash
./gradlew clean build
```

Then run:

```bash
java -jar build/libs/heat-equation-solver-all.jar path/to/config.json
```

(Adjust JAR name according to your packaging.)

## 4. Output Format

The solver writes a CSV file with columns:

```text
time,x,u
t0,x0,u0
...
t0,xN-1,uN-1
t1,x0,u0
...
```

You can visualize the results, for example in Python:

```python
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("output/heat_solution.csv")
for t, group in df.groupby("time"):
    plt.plot(group["x"], group["u"], label=f"t={t:.3f}")
plt.legend()
plt.xlabel("x")
plt.ylabel("u")
plt.show()
```

## 5. Testing

Run tests with:

```bash
./gradlew test
```

Tests check basic properties (stability, boundary conditions, I/O).