public class ParticleSystem {
    public Particle[] Particles;
    public void GetNewRandomParticles (int TotalParticles) {
        for (int i = 0; i < TotalParticles; i++) {
            Particles[i] = new Particle(
                    new Vector(rand.nextInt(xRes), rand.nextInt(yRes)),
                    new Vector((Math.random() * newBoidsVelocity) - (newBoidsVelocity/2), (Math.random() * newBoidsVelocity) - (newBoidsVelocity/2))
            );
        }
    }
}
