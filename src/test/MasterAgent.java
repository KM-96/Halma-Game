package test;

import game.Agent;
import model.Board;

public class MasterAgent {

    public void compete(float playTime) throws Exception {
        long time = System.currentTimeMillis();
        int turn = 0;
        float myTime = playTime;
        float opponentTime = playTime;
        while (myTime > 0) {
            turn++;
            String filename = "game.txt";
            util.FileReader fileReader = new util.FileReader(".\\resources\\" + filename);
            Board board = fileReader.readFile();

            if (turn % 2 == 0) {
                board.setTimeRemaining(opponentTime);
                ClonedAgent opponent = new ClonedAgent(board);

                if (opponent.isGoalState(board.getState(), board.getCurrentPlayer())) {
                    System.out.println("Player " + board.getCurrentPlayer() + " Won!");
                    System.out.println("Total turns taken - " + turn);
                    break;
                }
                long t = System.currentTimeMillis();
                opponent.playGame();
                opponentTime = ((opponentTime * 1000) - ((System.currentTimeMillis() - t))) / 1000;
                System.out.println("OpponentTime:" +opponentTime);
            } else {
                board.setTimeRemaining(myTime);
                Agent agent = new Agent(board);
                if (agent.isGoalState(board.getState(), board.getCurrentPlayer())) {
                    System.out.println("Player " + board.getCurrentPlayer() + " Won!");
                    System.out.println("Total turns taken - " + turn);
                    break;
                }
                long t = System.currentTimeMillis();
                agent.playGame();
                myTime = ((myTime * 1000) - ((System.currentTimeMillis() - t))) / 1000;
                System.out.println("mytime:" +myTime);
            }
        }
        System.out.println(System.currentTimeMillis() - time);
        System.out.println("Turns:" + turn);
        System.out.println("Remaining mytime: " + myTime);
        System.out.println("Remaining opponentTime: " + opponentTime);
    }

    public static void main(String[] args) {
        try {
            MasterAgent agent = new MasterAgent();
            agent.compete(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}