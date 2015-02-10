package org.usfirst.frc.team2642.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	Joystick stick;
	int autoLoopCounter;
	Encoder liftEncoder;
	Jaguar lift;
	DigitalInput toteIn;
	boolean liftUp;
	boolean liftDown;
	DigitalInput fiveTotesIn;
	DigitalInput liftUpperLimit;
	DigitalInput liftLowerLimit;
	boolean fiveUpToggle;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	//myRobot = new RobotDrive(0,1);
    	stick = new Joystick(0);
    	liftEncoder = new Encoder(1, 0);
    	lift = new Jaguar(2);
    	toteIn = new DigitalInput(8);
    	fiveTotesIn = new DigitalInput(9);
    	liftUpperLimit = new DigitalInput(6);
    	liftLowerLimit = new DigitalInput(7);
    	
    	
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 0;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
		{
			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
			autoLoopCounter++;
			} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	liftEncoder.reset();
    	liftUp = false;
    	liftDown = false;
    	fiveUpToggle = false;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() { 
    Timer.delay(0.05);
    	
    	if(stick.getRawButton(12) || stick.getRawButton(11)){ // human override
    		liftUp = false;
        	liftDown = false;
        	fiveUpToggle = false;
        	
    		if(stick.getRawButton(6) && liftEncoder.getDistance() < 800 && liftUpperLimit.get()){
    			lift.set(-0.5);
    			
    		}else if(stick.getRawButton(4) && liftEncoder.getDistance() > 5 && liftLowerLimit.get()){
    			lift.set(0.5);
    			
    		}else if(stick.getRawButton(11)){// go to co-op set point
    			if(liftEncoder.getDistance() > 200){ 
    				lift.set(0.5);
    			}else if(liftEncoder.getDistance() < 140){
    				lift.set(-0.5);
    			}else{
    				lift.set(0);
    			}
    		}else{
    			lift.set(0);
    		}
    	}else{ //auto pick
    	
    	if(liftUpperLimit.get() && liftLowerLimit.get()){ //if overide limit not made
    		if(!toteIn.get() && !fiveTotesIn.get()){ //if tote in robot and five in hopper pick up 2in
    		fiveUpToggle = true;
    	
    		}else if(fiveUpToggle){  
    			if(liftEncoder.getDistance() > 100){
    				fiveUpToggle = false;
    		}
    			
    			 lift.set(-0.5);
    			 
    		}else{
    			if(!toteIn.get() && !liftUp && !liftDown){//auto load up to dogs
    				liftUp = true; 
    				liftDown = false;
    				
    			}else if(liftUp){ //go up to dogs
    				if(liftEncoder.getDistance() > 800){
    					liftUp = false;
    					liftDown = true;
    				}
    				
    				lift.set(-0.5);
    				
    			}else if(liftDown){//go down to start
    				if(liftEncoder.getDistance() < 5){
    					liftUp = false;
    					liftDown = false;
    				}
    				
    				lift.set(0.5);
    	   
    			}else{
    				lift.set(0);
    			}
       
    		}
    		}else{ //if overide switchs are hit. stop auto load
    			liftUp = false;
    			liftDown = false;
    			fiveUpToggle = false;
    		}
    	}
        //System.out.println(liftDown);
        //System.out.println(liftLowerLimit.get());
        System.out.println(liftEncoder.getDistance());
        
    }
    	
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
