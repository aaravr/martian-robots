package com.aaravr.martian.robot.navigator.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class MovementInstruction {
   private Position currentPosition;
   private List<MovementType> nextMoves;
}
