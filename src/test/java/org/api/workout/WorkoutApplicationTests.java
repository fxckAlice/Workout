package org.api.workout;

import org.api.workout.entities.workout.WorkoutType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorkoutApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    void test(){
        System.out.println(WorkoutType.valueOf("CARDIO"));
    }

}
