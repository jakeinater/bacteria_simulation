import networkx as nx
import matplotlib.pyplot as plt
import sys
from datetime import date

DG = nx.read_graphml('../assets/graphs/LJunction/' + str(sys.argv[1]) + '.graphml')

G = DG.to_undirected(reciprocal=True)

for node in DG:
    for neighbor in nx.neighbors(DG, node):
        if node in nx.neighbors(DG, neighbor):
            G.edges[node, neighbor]['weight'] = (
                    DG.edges[node, neighbor]['weight'] + DG.edges[neighbor, node]['weight']
                    )

edge_labels = dict( [ ((u,v),'%.1f' % d['weight']) for u,v,d in G.edges(data=True)] )
edge_colors = [ d['weight'] for v,n,d in G.edges(data=True)]

# parse the x and y attributes and create a node:coord dict
x = nx.get_node_attributes(G, 'x')
y = nx.get_node_attributes(G, 'y')
pos = dict( [(n, (xcoord, y[n])) for (n, xcoord) in x.items()] )

node_colors = [w for (n, w) in nx.get_node_attributes(G, 'weight').items()]

fig, ax = plt.subplots()
img = plt.imread("../../assets/non-uni-maze2-2.png")
#img = plt.imread("../../assets/uni-maze2-edges.png")
ax.imshow(img)

nx.draw(G, pos, with_labels=False, node_size=0, font_color='red', font_size=7,
        edge_color=edge_colors, edge_cmap=plt.cm.inferno, width=9, alpha=.8)

nx.draw_networkx_nodes(G,pos, node_color=node_colors, node_size=85, alpha=1, cmap=plt.cm.inferno)

cb = plt.cm.ScalarMappable(cmap=plt.cm.inferno, norm=plt.Normalize(vmin=min(edge_colors), vmax=max(edge_colors)))
cb._A = []

plt.colorbar(cb)

if len(sys.argv) > 2 and str(sys.argv[2]) == '-l':
    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_size=4, bbox=dict(alpha=0), font_color='white')
    plt.savefig('../assets/figures/' + date.today().isoformat() + '_' + str(sys.argv[1]) + '_labelled.tif', dpi=300)
else:
    plt.savefig('../assets/figures/' + date.today().isoformat() + '_' + str(sys.argv[1]) + '.tif', dpi=300)

#plt.show()
