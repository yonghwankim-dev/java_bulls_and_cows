package kr.yh;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameTest {
    @Test
    public void testCreateRightAnswers(){
        //given
        Game game = new Game(3);
        //when
        int[] rightAnswers = game.createRightAnswers();
        //then
        assertThat(game.rightAnswers).isEqualTo(rightAnswers);
    }

}
