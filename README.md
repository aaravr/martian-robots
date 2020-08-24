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


- Line 1: 1 1 E (Space separated X and Y coordinate along with direction indicator) ENTER
- Line 2: FRLRFF (String of maximum 50 chars in length, in char indicating movement position) ENTER
    
```   
 1 1 E 
 FFRLRFL
 
```  

Once the input is process the response is show in the following format:

- Case 1: Successful navigation

```  
3 1 S
```  

- Case 2: Fallen off grid

```  
3 1 S LOST
```  

Once the results are returned, you can provide the inputs for next Robot or terminate the application using Ctrl C.

### Test Case
Running tests cases is a alternative to running the command line application. 

Test cases can be found here [*ControlCenterTest*](src/test/java/com/aaravr/martian/robot/navigator/service/ControlCenterTest.java)


### TODO
- [ ] Improve input handling and input processing to be more robust
- [ ] Use better algorithm for changing direction
- [ ] Refactor Controlcenter and attach behaviour to robots to be make them more autonomous 
- [ ] Improve test coverage and code quality
- [ ] Overall code clean