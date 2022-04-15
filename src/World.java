import java.util.Random;
public class World
{
    private Organism[][] game;

    public World()
    {
        game = new Organism[20][20];
        for (int i = 0; i < game.length; i++)
            for (int j = 0; j < game[0].length; j++)
                game[i][j] = null;

    }
    // Our move method will return number form 1 to 4 I will consider them as
    // 1 --> North | 2 --> West | 3 --> East | 4 --> South

    public void StartGame() // Starting the game with 100 ants and 5 Doodlebugs as said in the book.
    {
        int count1 = 0, count2 = 0;
        Random rand = new Random();
        while (count1 != 5)
        {
            int index1 = rand.nextInt(20);
            int index2 = rand.nextInt(20);
            if (game[index1][index2] == null)
            {
                game[index1][index2] = new Doodlebug();
                count1++;
            }
        }
        while (count2 != 100)
        {
            int index1 = rand.nextInt(20);
            int index2 = rand.nextInt(20);
            if (game[index1][index2] == null)
            {
                game[index1][index2] = new Ant();
                count2++;
            }
        }
        printWorld();
    }

    public void PlayGame() // Plays the turns starting from DoodleBug and then to Ant.
    {
        String coordinates = "";
        for (int i = 0; i < game.length; i++)
            for (int j = 0; j < game[i].length; j++)
                if (game[i][j] instanceof Doodlebug)
                    coordinates += i + "," + j + ",";
        while (coordinates.length() != 0)
        {
            int index1 = coordinates.indexOf(",");
            int x = Integer.parseInt(coordinates.substring(0,index1));
            coordinates = coordinates.substring(index1+1);
            int index2 = coordinates.indexOf(",");
            int y =  Integer.parseInt(coordinates.substring(0,index2));
            doodlebugTurn(x,y);
            coordinates = coordinates.substring(index2+1);
        }
        for (int i = 0; i < game.length; i++)
            for (int j = 0; j < game.length; j++)
                if (game[i][j] instanceof Ant)
                    coordinates += i + "," + j + ",";
        while (coordinates.length() != 0)
        {
            int index1 = coordinates.indexOf(",");
            int x = Integer.parseInt(coordinates.substring(0,index1));
            coordinates = coordinates.substring(index1+1);
            int index2 = coordinates.indexOf(",");
            int y =  Integer.parseInt(coordinates.substring(0,index2));
            antTurn(x,y);
            coordinates = coordinates.substring(index2+1);
        }
        // the reason I used Strings here is because if I did it with a for loop it would move the Organism twice for some cases.

        printWorld();
    }

    public void removeOrganism(int index1, int index2) // removes the Organism from the World.
    {
        game[index1][index2] = null;
    }

    public void printWorld() // prints every Organism in the wanted Character.
    {
        for (int i = 0; i < game.length; i++)
        {
            for (int j = 0; j < game[0].length; j++)
            {
                if (game[i][j] instanceof Ant)
                    System.out.print("o ");
                else if (game[i][j] instanceof Doodlebug)
                    System.out.print("X ");
                else
                    System.out.print("_");

            }
            System.out.println();
        }
        System.out.println();
    }

    public void doodlebugTurn(int x, int y) // Doodlebug turn.
    {
        Random random = new Random();
        if (((Doodlebug)game[x][y]).starve()) // If it starves then it should be removed first.
        {
            removeOrganism(x, y);
            return;
        }
        int index1_ant = -1, index2_ant = -1;
        for (int i = x-1; i <= x+1; i+=2)
            if (proper_index(i,y) && game[i][y] instanceof Ant)
            {
                index1_ant = i;
                index2_ant = y;
            }
        for (int i = y-1; i <= y+1; i+=2)
            if (proper_index(x,i) && game[x][i] instanceof Ant)
            {
                index1_ant = x;
                index2_ant = i;
            }
        if (index1_ant != -1 && index2_ant != -1)
        {
            removeOrganism(index1_ant,index2_ant);
            game[index1_ant][index2_ant] = game[x][y];
            removeOrganism(x,y);
            ((Doodlebug)game[index1_ant][index2_ant]).could_eat();
            if (((Doodlebug) game[index1_ant][index2_ant]).breed()) // A beautiful Algorithm for randomising the breeding square.
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(index1_ant,index2_ant+1) && game[index1_ant][index2_ant+1] == null)
                {
                    poss[0] = index1_ant;
                    poss[1] = index2_ant + 1;
                }
                if (proper_index(index1_ant+1,index2_ant) && game[index1_ant+1][index2_ant] == null)
                {
                    poss[2] = index1_ant + 1;
                    poss[3] = index2_ant;
                }
                if (proper_index(index1_ant-1,index2_ant) && game[index1_ant-1][index2_ant] == null)
                {
                    poss[4] = index1_ant - 1;
                    poss[5] = index2_ant;
                }

                if (proper_index(index1_ant,index2_ant-1) && game[index1_ant][index2_ant-1] == null)
                {
                    poss[6] = index1_ant;
                    poss[7] = index2_ant - 1;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i+=2)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }
                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Doodlebug();
                ((Doodlebug) game[index1_ant][index2_ant]).breedAccomplished();
            }
            return;
        }
        int direction = game[x][y].move(); // getting a random direction if it didn't it any ants.
        if (direction == 1 && proper_index(x-1,y) && game[x-1][y] == null)
        {
             game[x-1][y] = game[x][y];
             removeOrganism(x,y);
            ((Doodlebug)game[x-1][y]).could_not_eat();
            if (((Doodlebug) game[x-1][y]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x-1,y+1) && game[x-1][y+1] == null)
                {
                    poss[0] = x - 1;
                    poss[1] = y + 1;
                }
                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }
                if (proper_index(x-1,y-1) && game[x-1][y-1] == null)
                {
                    poss[4] = x - 1;
                    poss[5] = y - 1;
                }

                if (proper_index(x-2,y) && game[x-2][y] == null)
                {
                    poss[6] = x - 2;
                    poss[7] = y;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }
                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Doodlebug();
                ((Doodlebug) game[x-1][y]).breedAccomplished();
            }
            return;
        }
        else if (direction == 4 && proper_index(x+1,y) && game[x+1][y] == null)
        {
            game[x+1][y] = game[x][y];
            removeOrganism(x,y);
            ((Doodlebug)game[x+1][y]).could_not_eat();
            if (((Doodlebug) game[x+1][y]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x+1,y+1) && game[x+1][y+1] == null)
                {
                    poss[0] = x + 1;
                    poss[1] = y + 1;
                }
                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }
                if (proper_index(x+1,y-1) && game[x+1][y-1] == null)
                {
                    poss[4] = x + 1;
                    poss[5] = y - 1;
                }

                if (proper_index(x+2,y) && game[x+2][y] == null)
                {
                    poss[6] = x + 2;
                    poss[7] = y;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }

                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Doodlebug();
                ((Doodlebug) game[x+1][y]).breedAccomplished();
            }
            return;
        }
        else if (direction == 2 && proper_index(x,y-1) && game[x][y-1] == null)
        {
            game[x][y-1] = game[x][y];
            removeOrganism(x,y);
            ((Doodlebug)game[x][y-1]).could_not_eat();
            if (((Doodlebug) game[x][y-1]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x+1,y-1) && game[x+1][y-1] == null)
                {
                    poss[0] = x + 1;
                    poss[1] = y - 1;
                }

                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }

                if (proper_index(x+1,y-1) && game[x+1][y-1] == null)
                {
                    poss[4] = x + 1;
                    poss[5] = y - 1;
                }

                if (proper_index(x,y-2) && game[x][y-2] == null)
                {
                    poss[6] = x;
                    poss[7] = y - 2;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }

                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Doodlebug();
                ((Doodlebug) game[x][y-1]).breedAccomplished();
            }
            return;
        }
        else if (direction == 3 && proper_index(x,y+1) && game[x][y+1] == null)
        {
            game[x][y+1] = game[x][y];
            removeOrganism(x,y);
            ((Doodlebug)game[x][y+1]).could_not_eat();
            if (((Doodlebug) game[x][y+1]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x+1,y+1) && game[x+1][y+1] == null)
                {
                    poss[0] = x + 1;
                    poss[1] = y + 1;
                }

                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }

                if (proper_index(x-1,y+1) && game[x-1][y+1] == null)
                {
                    poss[4] = x - 1;
                    poss[5] = y + 1;
                }

                if (proper_index(x,y-1) && game[x][y-1] == null)
                {
                    poss[6] = x;
                    poss[7] = y - 1;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }

                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Doodlebug();
                ((Doodlebug) game[x][y+1]).breedAccomplished();
            }
            return;
        }
        if (((Doodlebug)game[x][y]).breed()) //According to the questions asked in Piazza it might not move but breed so we wrote the rest of the code considering that.
        {
            int[] poss = new int[8];

            for (int i = 0; i < poss.length; i++)
                poss[i] = -1;

            if (proper_index(x,y+1) && game[x][y+1] == null)
            {
                poss[0] = x;
                poss[1] = y + 1;
            }

            if (proper_index(x+1,y) && game[x+1][y] == null)
            {
                poss[2] = x + 1;
                poss[3] = y;
            }

            if (proper_index(x-1,y) && game[x-1][y] == null)
            {
                poss[4] = x - 1;
                poss[5] = y;
            }

            if (proper_index(x,y-1) && game[x][y-1] == null)
            {
                poss[6] = x;
                poss[7] = y - 1;
            }

            int count = 0;
            for (int i = 0; i < poss.length; i++)
                if (poss[i] != -1)
                    count++;
            if (count == 0)
                return;
            int[] all = new int[count];
            int index = 0;
            for (int i = 0; i < poss.length; i++)
                if (poss[i] != -1)
                {
                    all[index] = poss[i];
                    index++;
                }

            int rand = random.nextInt(count/2);
            int index1 = all[rand*2];
            int index2 = all[rand*2+1];
            game[index1][index2] = new Doodlebug();
            ((Doodlebug) game[x][y]).breedAccomplished();
        }
        ((Doodlebug)game[x][y]).oneStep();
    }

    public void antTurn(int x, int y) // Ants turn to play.
    {
        Random random = new Random();
        int direction = game[x][y].move();
        if (direction == 1 && proper_index(x-1,y) && game[x-1][y] == null)
        {

            game[x - 1][y] = game[x][y];
            removeOrganism(x, y);
            ((Ant)game[x-1][y]).moveAccomplished();
            if (((Ant)game[x - 1][y]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x-1,y+1) && game[x-1][y+1] == null)
                {
                    poss[0] = x - 1;
                    poss[1] = y + 1;
                }

                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }

                if (proper_index(x-1,y-1) && game[x-1][y-1] == null)
                {
                    poss[4] = x - 1;
                    poss[5] = y - 1;
                }

                if (proper_index(x+1,y) && game[x+1][y] == null)
                {
                    poss[6] = x + 1;
                    poss[7] = y;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }

                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Ant();
                ((Ant) game[x-1][y]).breedAccomplished();
            }
            return;
        }

        else if (direction == 4 && proper_index(x+1,y) && game[x+1][y] == null)
        {
            game[x+1][y] = game[x][y];
            removeOrganism(x,y);
            ((Ant)game[x+1][y]).moveAccomplished();
            if (((Ant) game[x+1][y]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x+1,y+1) && game[x+1][y+1] == null)
                {
                    poss[0] = x + 1;
                    poss[1] = y + 1;
                }

                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }

                if (proper_index(x+1,y-1) && game[x+1][y-1] == null)
                {
                    poss[4] = x + 1;
                    poss[5] = y - 1;
                }

                if (proper_index(x+2,y) && game[x+2][y] == null)
                {
                    poss[6] = x + 2;
                    poss[7] = y;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];

                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }

                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Ant();
                ((Ant) game[x+1][y]).breedAccomplished();
            }
            return;
        }

        else if (direction == 2 && proper_index(x,y-1) && game[x][y-1] == null)
        {
            game[x][y-1] = game[x][y];
            removeOrganism(x,y);
            ((Ant)game[x][y-1]).moveAccomplished();
            if (((Ant) game[x][y-1]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x-1,y-1) && game[x-1][y-1] == null)
                {
                    poss[0] = x - 1;
                    poss[1] = y - 1;
                }

                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }

                if (proper_index(x+1,y-1) && game[x+1][y-1] == null)
                {
                    poss[4] = x + 1;
                    poss[5] = y - 1;
                }

                if (proper_index(x,y+1) && game[x][y+1] == null)
                {
                    poss[6] = x;
                    poss[7] = y + 1;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }

                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Ant();
                ((Ant) game[x][y-1]).breedAccomplished();
            }
            return;
        }

        else if (direction == 3 && proper_index(x,y+1) && game[x][y+1] == null)
        {
            game[x][y+1] = game[x][y];
            removeOrganism(x,y);
            ((Ant)game[x][y+1]).moveAccomplished();
            if (((Ant) game[x][y+1]).breed())
            {
                int[] poss = new int[8];

                for (int i = 0; i < poss.length; i++)
                    poss[i] = -1;

                if (proper_index(x-1,y+1) && game[x-1][y+1] == null)
                {
                    poss[0] = x - 1;
                    poss[1] = y + 1;
                }

                if (proper_index(x,y) && game[x][y] == null)
                {
                    poss[2] = x;
                    poss[3] = y;
                }

                if (proper_index(x+1,y+1) && game[x+1][y+1] == null)
                {
                    poss[4] = x + 1;
                    poss[5] = y + 1;
                }

                if (proper_index(x,y-1) && game[x][y-1] == null)
                {
                    poss[6] = x;
                    poss[7] = y - 1;
                }

                int count = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                        count++;
                if (count == 0)
                    return;
                int[] all = new int[count];
                int index = 0;
                for (int i = 0; i < poss.length; i++)
                    if (poss[i] != -1)
                    {
                        all[index] = poss[i];
                        index++;
                    }

                int rand = random.nextInt(count/2);
                int index1 = all[rand*2];
                int index2 = all[rand*2+1];
                game[index1][index2] = new Ant();
                ((Ant) game[x][y+1]).breedAccomplished();
            }
            return;
        }
        if (((Ant)game[x][y]).breed())
        {
            int[] poss = new int[8];

            for (int i = 0; i < poss.length; i++)
                poss[i] = -1;

            if (proper_index(x,y+1) && game[x][y+1] == null)
            {
                poss[0] = x;
                poss[1] = y + 1;
            }

            if (proper_index(x,y-1) && game[x][y-1] == null)
            {
                poss[2] = x;
                poss[3] = y - 1;
            }

            if (proper_index(x+1,y) && game[x+1][y] == null)
            {
                poss[4] = x + 1;
                poss[5] = y;
            }

            if (proper_index(x-1,y) && game[x-1][y] == null)
            {
                poss[6] = x - 1;
                poss[7] = y;
            }

            int count = 0;
            for (int i = 0; i < poss.length; i++)
                if (poss[i] != -1)
                    count++;
            if (count == 0)
                return;
            int[] all = new int[count];
            int index = 0;
            for (int i = 0; i < poss.length; i++)
                if (poss[i] != -1)
                {
                    all[index] = poss[i];
                    index++;
                }

            int rand = random.nextInt(count/2);
            int index1 = all[rand*2];
            int index2 = all[rand*2+1];
            game[index1][index2] = new Ant();
            ((Ant) game[x][y]).breedAccomplished();
        }
        ((Ant)game[x][y]).oneStep();
    }

    public boolean proper_index(int a, int b) // returns true if the indexes are appropriate for the 2D Array.
    {
        return 0 <= a && a < game.length && 0 <= b && b < game.length;
    }
}