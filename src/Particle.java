import java.awt.*;
import java.util.Random;

public class Particle {
    Vector position;
    Vector velocity;
    Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY,
        Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW
    };
    Color color;

    public Particle(Vector position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
        this.color = colors[new Random().nextInt(colors.length)];
    }

    public void update(int width, int height, double terminalVelocity, double gravity, double entropy, Particle[] Particles, int size) {
        //gravity
        velocity.data[1] = velocity.data[1] + gravity;

        //velocity limiter
        if (velocity.data[1] > terminalVelocity) {
            velocity.data[1] = terminalVelocity;
        }
        if (velocity.data[0] > terminalVelocity) {
            velocity.data[0] = terminalVelocity;
        }
        if (velocity.data[1] < terminalVelocity * -1) {
            velocity.data[1] = terminalVelocity * -1;
        }
        if (velocity.data[0] < terminalVelocity * -1) {
            velocity.data[0] = terminalVelocity * -1;
        }

        //actually moving it
        position = position.plus(velocity);

        //bouncing (wall collisions)
        if (position.data[0] < 0 && velocity.data[0] < 0) {
            position.data[0] = 0;
            velocity.data[0] = velocity.data[0] * -1 * entropy;
        } else if (position.data[0] > width && velocity.data[0] > 0) {
            position.data[0] = width;
            velocity.data[0] = velocity.data[0] * -1 * entropy;
        } else if (position.data[1] < 0 && velocity.data[1] < 0) {
            position.data[1] = 0;
            velocity.data[1] = velocity.data[1] * -1 * entropy;
        } else if (position.data[1] > height && velocity.data[1] > 0) {
            position.data[1] = height;
            velocity.data[1] = velocity.data[1] * -1 * entropy;
        }

        //particle collisions
        for (Particle p : Particles) {
            if (p == this) continue;

            double dx = p.position.data[0] - this.position.data[0];
            double dy = p.position.data[1] - this.position.data[1];
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0 && distance <= size) {
                double nx = dx / distance;
                double ny = dy / distance;

                double v1 = this.velocity.data[0] * nx + this.velocity.data[1] * ny;
                double v2 = p.velocity.data[0] * nx + p.velocity.data[1] * ny;

                double v1_new = v2;
                double v2_new = v1;

                v1_new *= entropy;
                v2_new *= entropy;

                double dv1x = (v1_new - v1) * nx;
                double dv1y = (v1_new - v1) * ny;
                double dv2x = (v2_new - v2) * nx;
                double dv2y = (v2_new - v2) * ny;

                this.velocity.data[0] += dv1x;
                this.velocity.data[1] += dv1y;
                p.velocity.data[0] += dv2x;
                p.velocity.data[1] += dv2y;

                double overlap = size - distance;
                this.position.data[0] -= nx * overlap / 2;
                this.position.data[1] -= ny * overlap / 2;
                p.position.data[0] += nx * overlap / 2;
                p.position.data[1] += ny * overlap / 2;
            }
        }
    }
}