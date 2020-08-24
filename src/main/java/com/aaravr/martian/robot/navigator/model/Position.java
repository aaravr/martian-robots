package com.aaravr.martian.robot.navigator.model;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class Position {
    private int xaxis;
    private int yaxis;
    private DirectionType directionType;

    @Override
    public String toString() {
        return "Position{" +
                "xaxis=" + xaxis +
                ", yaxis=" + yaxis +
                ", directionType=" + directionType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (xaxis != position.xaxis) return false;
        return yaxis == position.yaxis;
    }

    @Override
    public int hashCode() {
        int result = 25;
        result = 31 * result + xaxis;
        result = 31 * result + yaxis;
        return result;
    }
}
