// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private VictorSP leftDriveMotor = new VictorSP(1);
  private VictorSP rightDriveMotor = new VictorSP(0);

  private SparkMax shooterIndexer = new SparkMax(2, SparkLowLevel.MotorType.kBrushless);
  private SparkMax shooterFlyWheelA = new SparkMax(1, SparkLowLevel.MotorType.kBrushless);
  private SparkMax shooterFlyWheelB = new SparkMax(7, SparkLowLevel.MotorType.kBrushless);
  private SparkMax shooterHood = new SparkMax(8, SparkLowLevel.MotorType.kBrushless);

  private RelativeEncoder shooterEncoder = shooterFlyWheelA.getEncoder();
  private RelativeEncoder hoodEncoder = shooterHood.getEncoder();

  private XboxController controller = new XboxController(0);

  private DifferentialDrive m_robotDrive;

  private AddressableLED leds = new AddressableLED(9);
  private AddressableLEDBuffer ledBuffer;
  
  private final LEDPattern m_rainbow = LEDPattern.rainbow(255, 128);
  private static final Distance kLedSpacing = Meters.of(1 / 60.0);
  private final LEDPattern m_scrollingRainbow =
      m_rainbow.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), kLedSpacing);
 
  public Robot() {
    leftDriveMotor.setInverted(true);

    shooterFlyWheelB.configure(new SparkMaxConfig().follow(shooterFlyWheelA, true),
      ResetMode.kResetSafeParameters,
      PersistMode.kNoPersistParameters);

    shooterHood.configure(new SparkMaxConfig().idleMode(IdleMode.kCoast),
     ResetMode.kResetSafeParameters,
     PersistMode.kNoPersistParameters);

    m_robotDrive = new DifferentialDrive(leftDriveMotor::set, rightDriveMotor::set);

    ledBuffer = new AddressableLEDBuffer(60);
    leds.setLength(ledBuffer.getLength());
    leds.setData(ledBuffer);
    leds.start();
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Hood Position", hoodEncoder.getPosition());

    m_scrollingRainbow.applyTo(ledBuffer);
    leds.setData(ledBuffer);
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    hoodEncoder.setPosition(0);
  }

  @Override
  public void teleopPeriodic() {
    m_robotDrive.tankDrive(-controller.getLeftY(), -controller.getRightX());

    if (controller.getLeftBumperButton()) {
      shooterFlyWheelA.setVoltage(3);
    } else {
      shooterFlyWheelA.setVoltage(0);
    }

    if (controller.getRightBumperButton()) {
      shooterIndexer.setVoltage(1);
    } else {
      shooterIndexer.setVoltage(0);
    }

    if (controller.getXButton()) {
      shooterFlyWheelA.setVoltage(3);
      if (shooterEncoder.getVelocity() < 1200) {
        shooterIndexer.setVoltage(0);
      } else {
        shooterIndexer.setVoltage(1);
      }
    } else {
      shooterFlyWheelA.setVoltage(0);
      shooterIndexer.setVoltage(0);
    }

//THIS NEEDS TO BE EDITED WITH THE REAL ROBOT!!!
    if (controller.getAButton()) {
      shooterHood.set(0); //raise hood
      if (hoodEncoder.getPosition() < 0) {
        shooterHood.set(0); //stops hood at desired height
      } else {
        shooterHood.set(0); //if not at desired position, continue
      }
    } else {
      shooterHood.set(0); //stop motor when no button is pressed
    }

    if (controller.getBButton()) {
      shooterHood.set(0); //lowers hood
      if (hoodEncoder.getPosition() < 0) {
        shooterHood.set(0); //stops hood when its lowered
      } else {
        shooterHood.set(0); //if hood is not fully lowered, continue
      }
    } else {
      shooterHood.set(0); //stops motor
    }
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
