// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.playingwithfusion.CANVenom;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Robot extends TimedRobot {
  private CANVenom frontLeftDrive;
  private CANVenom rearLeftDrive;
  private CANVenom frontRightDrive;
  private CANVenom rearRightDrive;

  private XboxController controller;

  private MecanumDrive m_robotDrive;

  public Robot() {
    frontLeftDrive = new CANVenom(3);
    rearLeftDrive = new CANVenom(4);
    frontRightDrive = new CANVenom(1);
    rearRightDrive = new CANVenom(5);

    frontRightDrive.setInverted(true);
    rearRightDrive.setInverted(true);

    controller = new XboxController(0);

    m_robotDrive = new MecanumDrive(frontLeftDrive::set, rearLeftDrive::set, frontRightDrive::set, rearRightDrive::set);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    m_robotDrive.driveCartesian(-controller.getLeftY(), -controller.getLeftX(), -controller.getRightX());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
