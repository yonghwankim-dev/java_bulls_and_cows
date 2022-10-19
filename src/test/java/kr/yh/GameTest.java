package kr.yh;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {

    private static ByteArrayOutputStream byteArrayOutputStream;

    @BeforeEach
    public void setUpStreams(){
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
    }

    @AfterEach
    public void restoresStreams(){
        System.setOut(System.out);
    }

    @Test
    public void testCreateRandomNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Game game = new Game(3);
        Method method = game.getClass().getDeclaredMethod("createRandomNumber");
        method.setAccessible(true);
        //when
        int randomNumber = (int) method.invoke(game);
        boolean actual = randomNumber >= 0 && randomNumber <= 9;
        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void testResetCurrentRound() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Game game = new Game(3);
        Field field = game.getClass().getDeclaredField("currentRound");
        Method method = game.getClass().getDeclaredMethod("resetCurrentRound");
        field.setAccessible(true);
        method.setAccessible(true);
        //when
        method.invoke(game);
        int actual = field.getInt(game);
        //then
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void testPassRound() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Game game = new Game(3);
        Field field = game.getClass().getDeclaredField("currentRound");
        Method method = game.getClass().getDeclaredMethod("passRound");
        field.setAccessible(true);
        method.setAccessible(true);
        //when
        method.invoke(game);
        int actual = field.getInt(game);
        //then
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void testCreateRightAnswers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        //given
        Game game = new Game(3);
        Method method = game.getClass().getDeclaredMethod("createRightAnswers");
        Field field = game.getClass().getDeclaredField("rightAnswers");
        method.setAccessible(true);
        field.setAccessible(true);
        //when
        method.invoke(game);
        int[] actual = (int[]) field.get(game);
        //then
        Arrays.stream(actual).allMatch((i)-> i >= 0 && i <= 9);
    }

    @Test
    public void testDisplayCurrentRound() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Game game = new Game(3);
        Method method = game.getClass().getDeclaredMethod("displayCurrentRound");
        method.setAccessible(true);
        //when
        method.invoke(game);
        //then
        assertThat(byteArrayOutputStream.toString()).isEqualTo("현재 라운드 : 1\n");
    }
    
    @Test
    public void testInputNumbersFromClient() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Game game = new Game(3);
        Method method = game.getClass().getDeclaredMethod("inputNumbersFromClient");
        method.setAccessible(true);
        InputStream in = generateUserInput("1 2 3");
        System.setIn(in);
        //when
        int[] numbersOfClient = (int[]) method.invoke(game);
        //then
        assertThat(numbersOfClient).isEqualTo(new int[]{1,2,3});
    }

    private static InputStream generateUserInput(String input){
        return new ByteArrayInputStream(input.getBytes());
    }
    
    @Test
    public void testMatch() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        //given
        int[] numbersOfClient = {1,2,3};
        Game game = new Game(3);
        Method method = game.getClass().getDeclaredMethod("match", int[].class);
        method.setAccessible(true);
        Field rightAnswers = game.getClass().getDeclaredField("rightAnswers");
        rightAnswers.setAccessible(true);
        Field rightAnswersSet = game.getClass().getDeclaredField("rightAnswerSet");
        rightAnswersSet.setAccessible(true);



        int[] arr = (int[]) rightAnswers.get(game);
        arr[0] = 1;
        arr[1] = 3;
        arr[2] = 4;

        Set<Integer> set = (Set<Integer>) rightAnswersSet.get(game);
        set.addAll(List.of(1,3,4));

        //when
        Object score = method.invoke(game, numbersOfClient);
        //then
        assertThat(score.toString()).isEqualTo("1 1 1");
    }

    @Test
    public void testDisplayScore() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        //given
        Game game = new Game(3);

        Class<?> scoreClass = game.getClass().getDeclaredClasses()[0];
        Constructor<?> constructor = scoreClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Object score = constructor.newInstance();

        Method displayScore = game.getClass().getDeclaredMethod("displayScore", game.getClass().getDeclaredClasses()[0]);
        displayScore.setAccessible(true);
        //when
        displayScore.invoke(game, score);
        //then
        assertThat(byteArrayOutputStream.toString()).isEqualTo("0스트라이크 0볼 0아웃\n");
    }

    @Test
    public void testCheckSuccess() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        //given
        Game game = new Game(3);
        Class<?> scoreClass = game.getClass().getDeclaredClasses()[0];
        Constructor<?> constructor = scoreClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Object score = constructor.newInstance();

        Method checkSuccess = game.getClass().getDeclaredMethod("checkSuccess", game.getClass().getDeclaredClasses()[0]);
        checkSuccess.setAccessible(true);
        //when
        boolean actual = (boolean) checkSuccess.invoke(game, score);
        //then
        assertThat(actual).isFalse();
    }

}
