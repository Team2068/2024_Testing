// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.



package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Limelight;

Command[] paths = {autobuilder.followPath("Middle left"), autobuilder.followPath("Middle midleft"), autobuilder.followPath("Middle mid"), autobuilder.followPath("Middle midright"), autobuilder.followPath("Middle right")};
String[] skipPaths = {"Midleft to mid", "Mid to midright", "midright to right"};

public class AdaptivePath extends Command {
  /** Creates a new AdaptivePath. */
  public AdaptivePath() {
    // Use addRequirements() here to declare subsystem dependencies.

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    autobuilder.followPath("Middle left");

    for (int i = 0; i <= 4; i = i + 1) {
      if (Limelight.TargetData.hastargets()) {
        paths[i];
      }
      else {
        autobuilder.followPath(skipPaths[i]).schedule();
      }
    }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
