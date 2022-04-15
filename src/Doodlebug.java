import java.util.Random;

public class Doodlebug extends Organism
{
    private int times_till_starve;
    private int breed_countdown;

    public Doodlebug()
    {
        times_till_starve = 0;
        breed_countdown = 0;
    }

    public boolean starve()
    {
        if (times_till_starve == 3)
            return true;
        return false;
    }

    public int move()
    {
        Random random = new Random();
        int direction = random.nextInt(4) + 1;
        return direction;
    }

    public boolean breed()
    {
        if (7 <= breed_countdown)
            return true;
        return false;
    }

    public void could_not_eat()
    {
        times_till_starve++;
        breed_countdown++;
    }

    public void could_eat()
    {
        times_till_starve = 0;
        breed_countdown++;
    }

    public void breedAccomplished()
    {
        breed_countdown = 0;
    }

    public void oneStep()
    {
        times_till_starve++;
        breed_countdown++;
    }
}
