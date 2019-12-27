import game.Agent;
import model.Board;
import util.FileReader;

public class HalmaGame {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        try {
            //Step 1: Read input file
            String filename = "game.txt";
            FileReader fileReader = new FileReader(".\\resources\\" + filename);
            Board board = fileReader.readFile();

            //Step 2: Initialize the agent and provide the board to it
            Agent agent = new Agent(board);
            agent.playGame();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(System.currentTimeMillis() - time);
        }
    }
}
