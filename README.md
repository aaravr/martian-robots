# Martian Robots

Sequential navigation of Robots on the rectangular surface of Mars

### Tech Stack
- SpringBoot2
- Java


### Step to run 

Just a simple Springboot app that run using default profile

```shell
$ mvn clean install -U
$ mvn spring-boot:run
```

When prompted, please provide the surface coordinates as X and Y coordinates as shown below:

```   
 5 3
```   

Once the surface is initiated based on the above coordinates, please provide the coordinates for the Robot movement
in the following format, one robot at a time.


* Line 1: 1 1 E (Space separated X and Y coordinate along with direction indicator) ENTER
    * Mars surface is represented using 2D array, starting lower-left as 0,0 and upper right as *(surfaceXaxis - 1, surfaceYaxis - 1)*. Example: (4 , 2) based on the above inputs
    * Direction indicator character: N - NORTH, E - EAST, S - SOUTH, W - WEST 
    
* Line 2: FRLRFF (String of maximum 50 chars in length, in char indicating movement position) ENTER
    * Navigation command charcated: F - FORWARD, L - LEFT (Robot turns 90 degrees anti-clockwise), R - RIGHT (Robot turns 90 degress clockwise)
    
```   
 1 1 E 
 FFRLRFL
 
```  

Once the input is processed the response is shown in the console, in the following format:

- Case 1: Successful navigation to the target position

```  
3 1 S
```  

- Case 2: Fallen off the grid

```  
3 1 S LOST
```  

Once the results are returned, you can provide the inputs for next Robot or terminate the application using Ctrl C.

### Test Case
Running tests cases is a alternative to running the command line application. 

Test cases can be found here [*ControlCenterTest*](src/test/java/com/aaravr/martian/robot/navigator/service/ControlCenterTest.java)

### Log Level
Logs levels can be controlled from the application.yml file

### TODO
- [ ] Improve input handling and input processing to be more robust
- [ ] Use better algorithm for changing direction
- [ ] Refactor Controlcenter and attach behaviour to robots to be make them more autonomous 
- [ ] Improve test coverage and code quality
- [ ] Overall code clean