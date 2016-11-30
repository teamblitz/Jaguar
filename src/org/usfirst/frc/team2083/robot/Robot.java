
package org.usfirst.frc.team2083.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;
    Joystick stick;
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    SendableChooser chooser;

    int codesPerRev = 1200;
    double initPos = 0, upperLimit = 0, lowerLimit = 0;
    
    CANJaguar motor1;
    
    double deadZone = 0.05;
    
    
    public Robot() {
        myRobot = new RobotDrive(0, 1);
        myRobot.setExpiration(0.1);
        stick = new Joystick(0);
        motor1 = new CANJaguar(1);
        
       
        motor1.setSpeedMode(CANJaguar.kQuadEncoder, codesPerRev, 1, 0, 0);
       
      
    }
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto modes", chooser);
        motor1.enableControl();
        
        initPos = motor1.getPosition();
        motor1.set(30.0);
        while (motor1.getForwardLimitOK()) {}
        motor1.set(0);
        upperLimit = motor1.getPosition();
        motor1.set(-30.0);
        while (motor1.getReverseLimitOK()) {}
        motor1.set(0);
        lowerLimit = motor1.getPosition();
        
        motor1.setPositionMode(CANJaguar.kQuadEncoder, codesPerRev, 1, 0, 0);
        motor1.set(initPos);
        
        
    }

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the if-else structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomous() {
    	
    	}


    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	double jsx = stick.getX(); 
        	if (jsx > deadZone){
        		motor1.set((upperLimit - initPos) * jsx + initPos);
        	} 
        	else if (jsx < -deadZone){
        		motor1.set((lowerLimit - initPos) * -jsx + initPos);
        	}
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
}
