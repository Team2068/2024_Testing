package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Flywheel;
import frc.robot.utility.IO;

public class Trap extends SequentialCommandGroup{
    
  public Trap(IO io) {
    addRequirements(io.shooter, io.intake);
    ProfiledShooter profiledShoot = new ProfiledShooter(io, Flywheel.PASS_OFF_ANGLE);
    addCommands(new ParallelRaceGroup(profiledShoot,
            new SequentialCommandGroup(
                new InstantCommand(() -> io.climber.setHangPos(Climber.HANG_DOWN_POS)),
                new InstantCommand(() -> profiledShoot.setAngle(Flywheel.PASS_OFF_ANGLE)),
                new WaitCommand(0.3),
                new WaitUntilCommand(() -> Math.abs(io.climber.hangError()) < 9),
                new InstantCommand(() -> io.shooter.helperVoltage(3)),
                new InstantCommand(() -> io.intake.speed(-1.0)),
                new WaitCommand(0.1),
                new InstantCommand(() -> io.intake.speed(0)),
                new InstantCommand(() -> io.shooter.helperVoltage(0)),
                new InstantCommand(() -> profiledShoot.setAngle(135.0)),
                new InstantCommand(() -> io.climber.setElevatorPos(38.0)),
                new WaitCommand(0.5),
                new WaitUntilCommand(() -> Math.abs(profiledShoot.controller.getPositionError()) < 3.0).withTimeout(0.6),
                new InstantCommand(() -> io.shooter.helperVoltage(5)),
                new InstantCommand(() -> io.shooter.flywheelVoltage(-16)),
                new WaitCommand(1.25),
                new InstantCommand(() -> io.climber.setElevatorPos(2)),
                new InstantCommand(() -> io.shooter.helperVoltage(0)),
                new InstantCommand(() -> io.shooter.flywheelVoltage(0))
                )));



  }
}
