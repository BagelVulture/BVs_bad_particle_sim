# Bad Particle Simulation

A simple Java-based particle physics sandbox built using Swing.  
This project simulates bouncing particles with gravity, collisions, entropy (energy loss), and mouse interactions. It includes a control panel for tweaking simulation parameters in real time.

---

## Features

### Particle Simulation
- Particles are affected by gravity and bounce off walls.
- Collisions between particles with adjustable elasticity (the entropy slider).
- Adjustable particle size, velocity, and count.

### Mouse Interaction Modes
- **Spawn/Kill Mode:**
  - Left-click or drag Left-click to spawn new particles.
  - Right-click or drag Right-click to delete particles.

- **Particle Move Mode:**
  - dragging left-click pulls particles toward the mouse.
  - dragging right-click pushes particles away from the mouse.

### Settings
Sliders:

- Entropy (energy loss on collisions)
- Terminal velocity
- Gravity strength
- Number of particles
- Particle size
- Velocity of newly spawned particles
- Simulation tick speed

Buttons:

- Pause/resume the simulation
- Reset sliders
- Reset particle system
- Toggle particle spawn/kill mode
- Toggle particle move mode
- Optional colorful particles mode

### How to Run
Releases can be found at https://github.com/BagelVulture/BVs_bad_particle_sim/releases, current version is 1.0.0
