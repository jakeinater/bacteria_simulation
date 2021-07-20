import networkx as nx
import matplotlib.pyplot as plt
import sys
import getopt
from datetime import date
from pathlib import Path

#undir <filename> -l(labelled) -n(non-uniform)
#options
opts, args = getopt.getopt(sys.argv[1:], "ln")

iofile = ''
iofile = args[0]

uniform = True
labelled = False
for opt in args:
    if opt == "-l":
        labelled = True
    elif opt == "-n":
        uniform = False


#reading graphML file
DG = nx.read_graphml('../assets/graphs/LJunction/' + iofile + '.graphml')
#DG = nx.read_graphml('../assets/graphs/' + iofile + '.graphml')

#convert to undirected graph
G = DG.to_undirected(reciprocal=True)

#setting the undir edge weighting from dir graph
for node in DG:
    for neighbor in nx.neighbors(DG, node):
        if node in nx.neighbors(DG, neighbor):
            G.edges[node, neighbor]['weight'] = (
                    DG.edges[node, neighbor]['weight'] + DG.edges[neighbor, node]['weight']
                    )

#edge and node attributes
edge_labels = dict( [ ((u,v),'%.1f' % d['weight']) for u,v,d in G.edges(data=True)] )
edge_colors = [ d['weight'] for v,n,d in G.edges(data=True)]

# parse the x and y attributes and create a node:coord dict
x = nx.get_node_attributes(G, 'x')
y = nx.get_node_attributes(G, 'y')
pos = dict( [(n, (xcoord, y[n])) for (n, xcoord) in x.items()] )

node_colors = [w for (n, w) in nx.get_node_attributes(G, 'weight').items()]


#background image
fig, ax = plt.subplots()

if uniform:
    img = plt.imread("../../assets/uni-maze2-edges.png")
else:
    img = plt.imread("../../assets/non-uni-maze2-2.png")

ax.imshow(img)


#drawing the graph
VMAX = 2000
#VMIN=min(edge_colors)
#VMAX=max(edge_colors)

nx.draw(G, pos, with_labels=False, node_size=0, font_color='red', font_size=7,
        edge_color=edge_colors, edge_cmap=plt.cm.inferno, width=9, alpha=.8, edge_vmin=0, edge_vmax=VMAX)

nx.draw_networkx_nodes(G, pos, node_color=node_colors, node_size=80, alpha=1, cmap=plt.cm.inferno, vmin=0, vmax=VMAX, node_shape='s')

#color bar
cb = plt.cm.ScalarMappable(cmap=plt.cm.inferno, norm=plt.Normalize(0,VMAX))
cb._A = []

plt.colorbar(cb)


#drawing and saving figure
if uniform:
    if labelled:
        nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_size=4, 
                bbox=dict(alpha=0), font_color='white')
        Path('D:/SURE/Figures/uni-maze/labelled/' + date.today().isoformat()).mkdir(parents=True, exist_ok=True)
        plt.savefig('D:/SURE/Figures/uni-maze/labelled/' + date.today().isoformat() + '/' 
                + iofile + '_labelled.tif', dpi=300)
    else:
        Path('D:/SURE/Figures/uni-maze/unlabelled/' + date.today().isoformat()).mkdir(parents=True, exist_ok=True)
        plt.savefig('D:/SURE/Figures/uni-maze/unlabelled/' + date.today().isoformat() + '/' 
                + iofile + '.tif', dpi=300)
else:
    if labelled:
        nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_size=4, 
                bbox=dict(alpha=0), font_color='white')
        Path('D:/SURE/Figures/non-uni-maze/labelled/' + date.today().isoformat()).mkdir(parents=True, exist_ok=True)
        plt.savefig('D:/SURE/Figures/non-uni-maze/labelled/' + date.today().isoformat() + '/' 
                + iofile + '_labelled.tif', dpi=300)
    else:
        Path('D:/SURE/Figures/non-uni-maze/unlabelled/' + date.today().isoformat()).mkdir(parents=True, exist_ok=True)
        plt.savefig('D:/SURE/Figures/non-uni-maze/unlabelled/' + date.today().isoformat() + '/'
                + iofile + '.tif', dpi=300)


#plt.show()
