package com.aaravr.martian.robot.navigator.model;


import com.aaravr.martian.robot.navigator.exception.RobotDiedException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@Slf4j
public class Robot {

    private String name;
    private ScentType scentType;
    private Position currentPosition;
    private Position forwardMovementPosition;
    private DirectionType currentFacingDirection;
    private boolean lost;


    public void followInstruction(MovementType movementType, MarsSurface marsSurface, DirectionType previousOffEdgeDirection) {
        log.info("{} {} {}", this, movementType, previousOffEdgeDirection);

        if (MovementType.FORWARD == movementType) {
            moveForward(marsSurface, previousOffEdgeDirection);
        } else {
            changeDirection(movementType);
        }

        this.forwardMovementPosition = marsSurface.calculateNextPositionByDirection(currentPosition, this.currentFacingDirection);
    }

    private void changeDirection(MovementType movementType) {
        int targetAngle = rotateBy90Degrees(currentFacingDirection.getCurrentAngle(), MovementType.RIGHT == movementType);
        this.currentFacingDirection = DirectionType.getDirectionByAngle(targetAngle);
        log.debug("Robot is facing {} ", this.currentFacingDirection.getKey());
    }

    //TODO: Robust exception handling and consistent updates
    private void moveForward(MarsSurface marsSurface, DirectionType previousOffEdgeDirection) {
        if (this.currentFacingDirection == previousOffEdgeDirection) {
            log.debug("Scented position {} identified. Robot {} decided to ignore the command to jump off the grid", forwardMovementPosition, this.getName());
            return;
        }

        if (!marsSurface.doesPositionExistInSurface(this.forwardMovementPosition)) {
            this.currentPosition.setDirectionType(this.currentFacingDirection);
            log.warn("Robot {} jumped from {} facing towards {}", this.getName(), this.currentPosition, this.currentFacingDirection);
            throw new RobotDiedException("Robot jumped off the grid", this.currentPosition, this.forwardMovementPosition);
        }
        currentPosition = this.forwardMovementPosition;
    }


    //TODO: Find a better way to implement this using math library
    private int rotateBy90Degrees(int currentAngle, boolean isClockwise) {
        if (!isClockwise && currentAngle == 0) {
            currentAngle = 360;
        } else if (isClockwise && currentAngle == 360) {
            currentAngle = 0;
        }

        int result = isClockwise ? currentAngle + 90 : currentAngle - 90;
        return result == 360 ? 0 : result;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Robot robot = (Robot) o;

        return name != null ? name.equals(robot.name) : robot.name == null;
    }

}
