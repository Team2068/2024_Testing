// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utility.IO;

public class DistanceShoot extends Command {
  IO io;
   //TODO: PUT IN REAL VALUES
   //Y = angle
   //X = dist from speaker
  double yone = 60; 
  double xone = 0;
  
  double ytwo = 10;
  double xtwo = 30;
  double x = io.shooter_light.distance();
  ProfiledShooter profiledShoot;
  /** Creates a new DistanceShoot. */
  public DistanceShoot() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    double y = yone + (((x-xone)*(ytwo-yone))/(xtwo - xone)); 
    profiledShoot.setAngle(y);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
   
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
