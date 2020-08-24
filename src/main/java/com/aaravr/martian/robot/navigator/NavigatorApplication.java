package com.aaravr.martian.robot.navigator;

import com.aaravr.martian.robot.navigator.exception.InvalidInputException;
import com.aaravr.martian.robot.navigator.service.ControlCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class NavigatorApplication implements CommandLineRunner {

    @Autowired
    private ControlCenter controlCenter;

    public static void main(String[] args) {
        SpringApplication.run(NavigatorApplication.class, args);
    }

    public void run(String... strings) throws Exception {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            String surfaceCoordinates = scanner.nextLine();


            //TODO: use common utils to perform all basic validations
            try {

                controlCenter.initialiseMarsSurface(surfaceCoordinates);

            } catch (InvalidInputException e) {
                throw new InvalidInputException("X and Y axis must be within the range of 0 to 49");
            }

            System.out.println("Mars surface created - Please provide movement instructions for one Robot at a time, as shown below");
            System.out.println("Line 1: Xaxis Yaxis DirectionType char (Example: N E S W)");
            System.out.println("Line 2: Movment directions (Example: FLLRRFF)");
            System.out.println("Example:");
            System.out.println("1 1 E");
            System.out.println("RFRFRFRF");


            String robotInitialPosition;
            String movesDirections;

            while (true) {
               if (scanner.hasNext()) {
                   try {
                       robotInitialPosition = scanner.nextLine();
                       movesDirections = scanner.nextLine();
                       controlCenter.validateAndProcessRobotMovementInput(robotInitialPosition, movesDirections);
                   } catch(InvalidInputException e) {
                       log.error(" Invalid Input: " + e.getMessage());
                   }
               }
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
