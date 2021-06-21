# Formatting

### Maze config data
ID's must start at 0. Standard procedure: use multipoint tool to highlight each junction in imageJ, then use the measure tool to find all the pixel coords. Copy the data into excel and get rid of the unnessecary columns, subtract 1 from the ID column, and add in the junction types and the neighboring junctions. Copy this into a text file and you're done.
```
SOURCE: ID SOURCE coordX coordY edgeID
Y: ID Y coordX coordY leftID baseID rightID (imagine branches of Y, upright)
T: ID T coordX coordY leftID baseID rightID (imagine branches of T, upright) 
L: ID L coordX coordY leftID rightID (order doesnt really matter)
X: ID X coordX coordY firstID secondID thirdID fourthID (start at any one and count counter clockwise)
SINK: ID SINK coordX coordY edgeID 
```

### Probability data
XProb-uni/non-uni.txt:
coming from top (imagine +): 
```
pLeft, pForward, pRight, pBack
```

TProb-uni/non-uni.txt:
```
Entering from middle:   pLeft, pRight, pMiddle
Entering from left:     pLeft, pRight, pMiddle
Entering from right:    pLeft, pRight, pMiddle
```

YProb-uni/non-uni.txt:
```
Entering from middle:   pLeft, pRight, pMiddle
Entering from left:     pLeft, pRight, pMiddle
Entering from right:    pLeft, pRight, pMiddle
```

LProb-uni/non-uni.txt
```
pPass, pU-turn, pStuck
```
