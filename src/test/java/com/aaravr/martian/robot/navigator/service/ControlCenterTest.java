package com.aaravr.martian.robot.navigator.service;

import com.aaravr.martian.robot.navigator.exception.InvalidInputException;
import com.aaravr.martian.robot.navigator.model.Position;
import com.aaravr.martian.robot.navigator.model.Robot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.is;


public class ControlCenterTest {

    ControlCenter sut;

    @Before
    public void setup() {
        sut = new ControlCenter();
    }


    @Test
    public void should_move_to_expected_target_for_simple_case() throws Exception {
        sut.initialiseMarsSurface("5 3");
        Optional<Robot> robot = sut.validateAndProcessRobotMovementInput("1 1 E", "RFRFRFRF");
        Assert.assertThat(getPositionString(robot.get(), false), is("1 1 E"));
    }

    @Test
    public void should_move_back_original_position_after_round_trip() throws Exception {
        sut.initialiseMarsSurface("5 3");
        Optional<Robot> robot = sut.validateAndProcessRobotMovementInput("3 2 W", "FFFLFLFFFLF");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 N"));
    }

    @Test
    public void should_turn_360_clockwise() throws Exception {
        sut.initialiseMarsSurface("5 3");
        Optional<Robot> robot = sut.validateAndProcessRobotMovementInput("3 2 N", "R");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 E"));

        robot = sut.validateAndProcessRobotMovementInput("3 2 N", "R");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 E"));

        robot = sut.validateAndProcessRobotMovementInput("3 2 N", "RR");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 S"));

        robot = sut.validateAndProcessRobotMovementInput("3 2 N", "RRRR");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 N"));
    }

    @Test
    public void should_turn_360_anti_clockwise() throws Exception {
        sut.initialiseMarsSurface("5 3");
        Optional<Robot> robot = sut.validateAndProcessRobotMovementInput("3 2 N", "L");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 W"));

        robot = sut.validateAndProcessRobotMovementInput("3 2 N", "LL");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 S"));

        robot = sut.validateAndProcessRobotMovementInput("3 2 N", "LLL");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 E"));

        robot = sut.validateAndProcessRobotMovementInput("3 2 N", "LLLL");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 2 N"));
    }

    @Test
    public void should_jump_off_the_grid_and_report_position() throws Exception {
        sut.initialiseMarsSurface("5 3");
        Optional<Robot> robot = sut.validateAndProcessRobotMovementInput("3 2 N", "FRRFLLFFRRFLL");
        Assert.assertThat(getPositionString(robot.get(), true), is("3 3 N LOST"));
    }

    @Test
    public void should_ignore_moving_forward_from_previous_scented_position() throws Exception {
        sut.initialiseMarsSurface("5 3");
        sut.validateAndProcessRobotMovementInput("3 2 N", "FRRFLLFFRRFLL");
        Optional<Robot> robot = sut.validateAndProcessRobotMovementInput("3 2 N", "FRRF");
        Assert.assertThat(getPositionString(robot.get(), false), is("3 1 S"));
    }

    @Test(expected = InvalidInputException.class)
    public void should_throw_exception_for_invalid_surface_coordinates() throws Exception {
        sut.initialiseMarsSurface("50 3");
    }

    @Test(expected = InvalidInputException.class)
    public void should_throw_exception_for_off_grid_starting_position() throws Exception {
        sut.initialiseMarsSurface("49 3");
        sut.validateAndProcessRobotMovementInput("50 1 E", "RFRFRFRF");
    }


    private String getPositionString(Robot robot,  boolean isLostRobotTestCase) {
        Position positionToReport = isLostRobotTestCase ? robot.getForwardMovementPosition() : robot.getCurrentPosition();
        return isLostRobotTestCase ? String.format("%d %d %s LOST", positionToReport.getXaxis(), positionToReport.getYaxis(), robot.getCurrentFacingDirection().getKey() )
                : String.format("%d %d %s", positionToReport.getXaxis(), positionToReport.getYaxis(), robot.getCurrentFacingDirection().getKey());
    }

}