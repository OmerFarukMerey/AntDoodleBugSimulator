import java.util.Scanner;
public class PredatorSim
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        World mygame = new World();
        mygame.StartGame();
        while (true)
        {
            System.out.println("Please enter any thing to play the game!");
            sc.next();
            mygame.PlayGame();
        }
    }
}
