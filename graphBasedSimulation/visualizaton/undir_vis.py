import networkx as nx
import matplotlib.pyplot as plt

DG = nx.read_graphml('../assets/graphs/ecoli_non-uni_both-ends.graphml')

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


fig, ax = plt.subplots()
img = plt.imread("../../assets/non-uni-maze2-2.png")
ax.imshow(img)

nx.draw(G, pos, with_labels=True, node_size = 350, node_color='black', font_color='red', font_size=7,
        edge_color=edge_colors, edge_cmap=plt.cm.inferno, width=20, alpha=.8)

cb = plt.cm.ScalarMappable(cmap=plt.cm.inferno, norm=plt.Normalize(vmin=min(edge_colors), vmax=max(edge_colors)))
cb._A = []

plt.colorbar(cb)

nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_size=6, bbox=dict(alpha=0), font_color='white')


plt.show()
