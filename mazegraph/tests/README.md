# Formatting

### Maze config data
```
SOURCE: ID SOURCE edgeID
Y: ID Y leftID baseID rightID (imagine branches of Y, left and right dont
really matter)
T: ID T leftID baseID rightID (imagine branches of T, left and right dont
really matter)
L: ID L leftID rightID (doesnt really matter)
X: ID X firstID secondID thirdID fourthID (start at any one and count counter clockwise)
SINK: ID edgeID 
```

### Probability data
XProb:
coming from top (imagine +): 
```
pLeft, pForward, pRight, pBack
```

TProb:
```
Entering from middle:   pLeft, pRight, pMiddle
Entering from left:     pLeft, pRight, pMiddle
Entering from right:    pLeft, pRight, pMiddle
```

YProb:
```
Entering from middle:   pLeft, pRight, pMiddle
Entering from left:     pLeft, pRight, pMiddle
Entering from right:    pLeft, pRight, pMiddle
```
