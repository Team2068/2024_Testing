// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.utility.IO;

public class CloseUpShooting extends SequentialCommandGroup {

  public CloseUpShooting(IO io, ProfiledShooter profiledShoot, double angle) {
    addRequirements(io.shooter, io.intake);
    addCommands(
        new SequentialCommandGroup(
          new InstantCommand(() -> io.profiledShoot.setAngle(angle)),
            new InstantCommand(() -> profiledShoot.setAngle(angle)),
            new InstantCommand(() -> io.shooter.flywheelVoltage(-16)),
            new WaitCommand(0.7),
            new WaitUntilCommand(() -> Math.abs(profiledShoot.controller.getPositionError()) < 3).withTimeout(.5),
            new InstantCommand(() -> io.shooter.helperVoltage(12)),
            new InstantCommand(() -> io.intake.speed(-1)),
            new WaitCommand(0.5),
            new InstantCommand(() -> profiledShoot.stop()),
            new InstantCommand(() -> io.shooter.flywheelVoltage(0.0)),
            new InstantCommand(() -> io.shooter.helperVoltage(0.0)),
            new InstantCommand(() -> io.intake.speed(0.0))));
  }
}