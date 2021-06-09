# Formatting

### Maze config data
```
SOURCE: ID SOURCE coordX coordY edgeID
Y: ID Y coordX coordY leftID baseID rightID (imagine branches of Y, left and right dont
really matter)
T: ID T coordX coordY leftID baseID rightID (imagine branches of T, left and right dont
really matter)
L: ID L coordX coordY leftID rightID (doesnt really matter)
X: ID X coordX coordY firstID secondID thirdID fourthID (start at any one and count counter clockwise)
SINK: ID SINK coordX coordY edgeID 
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
