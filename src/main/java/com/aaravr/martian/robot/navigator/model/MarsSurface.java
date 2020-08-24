package com.aaravr.martian.robot.navigator.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


@Builder
@Data
@Slf4j
public class MarsSurface {
    private int[][] coordinates;



    public boolean doesPositionExistInSurface(Position position) {
        int x = position.getXaxis();
        int y = position.getYaxis();

        return x >= 0 &&  x < coordinates.length && y >= 0 && y < coordinates[1].length;
    }



    public Position calculateNextPositionByDirection(Position currentPosition, DirectionType direction) {

        Position nextPosition;

        switch (direction) {
            case NORTH:
                nextPosition = getPosition(currentPosition.getXaxis(), currentPosition.getYaxis() + 1);
                break;
            case SOUTH:
                nextPosition = getPosition(currentPosition.getXaxis(), currentPosition.getYaxis() - 1);
                break;
            case EAST:
                nextPosition = getPosition(currentPosition.getXaxis() + 1, currentPosition.getYaxis());
                break;
            case WEST:
                nextPosition = getPosition(currentPosition.getXaxis() - 1, currentPosition.getYaxis());
                break;
             default:
                 nextPosition = currentPosition;
                 break;
        }
        return nextPosition;
    }

    private Position getPosition(int xAxis, int yAxis) {
       return Position.builder().xaxis(xAxis).yaxis(yAxis).build();
    }
}
