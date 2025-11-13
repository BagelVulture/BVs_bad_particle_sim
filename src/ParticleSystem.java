import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class ParticleSystem {
    public Particle[] Particles;
    Random rand = new Random();

    public ParticleSystem (int TotalParticles, int width, int height, double newParticlesVelocity) {
        Particles = new Particle[TotalParticles];
        for (int i = 0; i < TotalParticles; i++) {
            Particles[i] = new Particle(
                    new Vector(rand.nextInt(width), rand.nextInt(height)),
                    new Vector((Math.random() * newParticlesVelocity) - (newParticlesVelocity/2), (Math.random() * newParticlesVelocity) - (newParticlesVelocity/2))
            );
        }
    }

    public void addParticle(int x, int y) {
        Particles = Arrays.copyOf(Particles, Particles.length + 1);
        Particles[Particles.length - 1] = new Particle(new Vector(x, y), new Vector(0, 0));
    }

    public boolean removeParticle(int x, int y, int size) {
        for (int i = 0; i < Particles.length; i++) {
            Particle p = Particles[i];

            double distance = p.position.distanceTo(new Vector(x, y));

            if (distance <= (double) size / 2) {
                Particle[] newParticles = new Particle[Particles.length - 1];

                if (i > 0) {
                    System.arraycopy(Particles, 0, newParticles, 0, i);
                }
                if (i < Particles.length - 1) {
                    System.arraycopy(Particles, i + 1, newParticles, i, Particles.length - i - 1);
                }

                Particles = newParticles;
                return true;
            }
        }
        return false;
    }

    public void moveTowards(int x, int y, int rangeDiameter, double strengthModifier) {
        Vector mouse = new Vector(x, y);
        double rangeRadius = rangeDiameter / 2.0;

        for (Particle p : Particles) {
            Vector toMouse = mouse.minus(p.position);
            double distance = toMouse.magnitude();

            if (distance > 0 && distance <= rangeRadius) {
                Vector direction = toMouse.div(distance);
                double strength = (rangeRadius - distance) / rangeRadius;
                Vector attraction = direction.times(strength * strengthModifier);
                p.velocity = p.velocity.plus(attraction);
            }
        }
    }


    public void update(int width, int height, double terminalVelocity, double gravity, double entropy, int size) {
        for (Particle p : Particles) {
            p.update(width, height, terminalVelocity, gravity, entropy, Particles, size);
        }
    }

    public void draw(Graphics g, int size) {
        for (Particle p : Particles) {
            g.setColor(p.color);
            g.fillOval((int) p.position.data[0] - (size/2), (int) p.position.data[1] - (size/2), size, size);
        }
    }
}