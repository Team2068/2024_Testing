// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.



package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Limelight;

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

    if (Limelight.TargetData.hastargets()) {
      autobuilder.followPath("Middle midleft");
    }
    else {
      autobuilder.followPath("Midleft to mid");
    }

    if (Limelight.TargetData.hastargets()) {
      autobuilder.followPath("Middle mid");
    }
    else {
      autobuilder.followPath("Mid to midright");
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
