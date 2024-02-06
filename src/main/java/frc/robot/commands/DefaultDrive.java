package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utility.IO;

import java.util.function.DoubleSupplier;

public class DefaultDrive extends Command {

    private final IO io;
    private final DoubleSupplier x_supplier;
    private final DoubleSupplier y_supplier;
    private final DoubleSupplier rotation_supplier;
    private final SlewRateLimiter xLimiter = new SlewRateLimiter(4);
    private final SlewRateLimiter yLimiter = new SlewRateLimiter(4);


    public DefaultDrive(IO io, ChassisSpeeds chassisSpeeds) {
        this(io, () -> chassisSpeeds.vxMetersPerSecond, () -> chassisSpeeds.vyMetersPerSecond, () -> chassisSpeeds.omegaRadiansPerSecond);
    }

    public DefaultDrive(IO io, CommandXboxController controller) {
        this(io, () -> -modifyAxis(controller.getLeftY()) * DriveSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
        () -> -modifyAxis(controller.getLeftX()) * DriveSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
        () -> modifyAxis(controller.getRightX())* DriveSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND);
    }
  
    public DefaultDrive(IO io,
        DoubleSupplier translationXSupplier,
        DoubleSupplier translationYSupplier,
        DoubleSupplier rotationSupplier) {
        
        this.io = io;
        this.x_supplier = translationXSupplier;
        this.y_supplier = translationYSupplier;
        this.rotation_supplier = rotationSupplier;

        addRequirements(io.chassis);
    }
    
    @Override
    public void execute() {
        double xSpeed = x_supplier.getAsDouble() * 0.7;
        double ySpeed = y_supplier.getAsDouble() * 0.7;
        double rotationSpeed = rotation_supplier.getAsDouble() * 0.7;
        
        ChassisSpeeds output = new ChassisSpeeds(xSpeed, ySpeed, rotationSpeed);

        Translation2d tr;
        Rotation2d adjustmentAngle;

        // if (!io.limelight.targetData().hasTargets && io.chassis.drive_mode > 1)
        //     io.chassis.drive_mode = 1;

        switch (io.chassis.drive_mode) {
            case 1: // Field-Oriented
            output = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotationSpeed, io.chassis.rotation());
            break;
            
            case 2: // Fixed-Point Tracking
            adjustmentAngle = io.chassis.getPose().getRotation().plus(new Rotation2d(io.limelight.targetData().horizontalOffset));
            tr = new Translation2d(xSpeed, ySpeed).rotateBy(adjustmentAngle.unaryMinus());
            output = new ChassisSpeeds(tr.getX(), tr.getY(), rotationSpeed);
            break;

            case 3: // Fixed Alignment
            Pose2d pose = io.chassis.getPose();
            adjustmentAngle = pose.getRotation().plus(new Rotation2d(io.limelight.targetData().horizontalOffset));
            tr = new Translation2d(0, xSpeed).rotateBy(adjustmentAngle.unaryMinus());
            output = new ChassisSpeeds(tr.getX(), tr.getY(), 0);
            break;
        }

        io.chassis.drive(output);
    }

    @Override
    public void end(boolean interrupted) {
        io.chassis.stop();
    }

    private static double deadband(double value, double deadband) {
        if (Math.abs(value) <= deadband) return 0.0;
        deadband *= (value > 0.0) ? 1 : -1;
        return (value + deadband) / (1.0 + deadband);
    }

    private static double modifyAxis(double value) {
        value = deadband(value, 0.05); // Deadband
        value = Math.copySign(value * value, value); // Square the axis
        return value;
    }
}