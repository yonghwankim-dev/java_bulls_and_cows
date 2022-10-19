package kr.yh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BullsAndCowsApplication {

    public static void main(String[] args) {
        new Game(3).start();
    }

}
