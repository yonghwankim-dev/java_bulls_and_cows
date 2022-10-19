package kr.yh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Game {
    private static class Score{
        private int strike;
        private int ball;
        private int out;

        public String toString(){
            return String.format("%d %d %d", strike, ball, out);
        }
    }

    private int numberOfBall;
    private int currentRound;
    private int[] rightAnswers;
    private Set<Integer> rightAnswerSet;
    private static BufferedReader br;

    public Game(int numberOfBall) {
        this.numberOfBall = numberOfBall;
        this.currentRound = 1;
        this.rightAnswers = new int[numberOfBall];
        this.rightAnswerSet = new HashSet<>();
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start(){
        initGame();
        while(isPlaying()){
            displayCurrentRound();
            displayInputMessage();
            Score score = match(inputNumbersFromClient());
            displayScore(score);
            if(checkSuccess(score)){
                displaySuccess();
                return;
            }
            passRound();
        }
        displayFail();
    }

    private void initGame(){
        createRightAnswers();
        resetCurrentRound();
    }

    private boolean isPlaying(){
        return currentRound <= 9;
    }

    private void createRightAnswers(){
        int idx = 0;
        rightAnswerSet.clear();
        while(rightAnswerSet.size() < 3){
            int randomNumber = createRandomNumber();
            if(!rightAnswerSet.contains(randomNumber)){
                rightAnswerSet.add(randomNumber);
                rightAnswers[idx++] = randomNumber;
            }
        }
    }

    private int createRandomNumber(){
        return new Random().nextInt(10);
    }

    private void resetCurrentRound(){
        currentRound = 1;
    }

    private void passRound(){
        currentRound++;
    }

    private void displayCurrentRound(){
        System.out.printf("현재 라운드 : %d\n", currentRound);
    }

    private int[] inputNumbersFromClient() {
        int[] result;

        try {
            result = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Score match(int[] numbersOfClient){
        Score score = new Score();
        for(int i = 0; i < numberOfBall; i++){
            if(rightAnswers[i] == numbersOfClient[i]){
                score.strike++;
            }else if(rightAnswerSet.contains(numbersOfClient[i])) {
                score.ball++;
            }else{
                score.out++;
            }
        }
        return score;
    }

    private void displayScore(Score score){
        System.out.printf("%d스트라이크 %d볼 %d아웃\n\n", score.strike, score.ball, score.out);
    }

    private boolean checkSuccess(Score score){
        return score.strike == numberOfBall;
    }

    private void displaySuccess(){
        System.out.println("플레이어 승!");
    }

    private void displayFail(){
        System.out.println("플레이어 패!. 정답 : " + Arrays.toString(rightAnswers));
    }

    private void displayInputMessage(){
        System.out.print("입력 : ");
    }

}
