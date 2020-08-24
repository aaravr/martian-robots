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

When prompted, please provide in the surface coordinates as X and Y coordinates as shown below:

```   
 5 3
```   

Once the surface is initiated based on the above coordinates, please provide the coordinates for the Robot movement
in the following format, once robot at a time


- Line 1: 1 1 E (Space separated X and Y coordinate along with direction indicator)
- Line 2: FRLRFF (String of maximum 50 chars in length, in char indicating movement position)
    
```   
 1 1 E
 FFRLRFL
 
```  


### TODO
- [ ] Improve input handling and input processing to be more robust
- [ ] Use better algorithm for changing direction
- [ ] Refactor Controlcenter and attach behaviour to robots to be make them more autonomous 
- [ ] Improve test coverage and code quality
- [ ] Overall code clean