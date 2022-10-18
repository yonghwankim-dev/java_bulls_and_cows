package kr.yh;

public class Game {
    public int numberOfBall;
    public int[] rightAnswers;

    public Game(int numberOfBall) {
        this.numberOfBall = numberOfBall;
        this.rightAnswers = new int[numberOfBall];
    }

    public int[] createRightAnswers(){
        return new int[]{0,0,0};
    }
}
