import java.util.Random;

public class Ant extends Organism
{
    private int breed_countdown;

    public Ant()
    {
        breed_countdown = 0;
    }

    public int move()
    {
        Random random = new Random();
        int direction = random.nextInt(4) + 1;
        return direction;
    }

    public boolean breed()
    {
       if (3 <= breed_countdown)
           return true;
       return false;
    }

    public void moveAccomplished()
    {
        if (3 <= breed_countdown)
            return;
        breed_countdown++;
    }

    public void breedAccomplished()
    {
        breed_countdown = 0;
    }

    public void oneStep()
    {
        breed_countdown++;
    }
}
