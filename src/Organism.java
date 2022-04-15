import java.util.Random;
public class Organism
{

    public Organism()
    {

    }

    public int move()
    {
        Random random = new Random();
        int direction = random.nextInt(4) + 1;
        return direction;
    }

    // The question on the book only said to override the method move()
    // So that's why my code might look weird. (Anxiety of losing any points :S)

}
