import networkx as nx
import matplotlib.pyplot as plt

DG = nx.read_graphml('../assets/test_solved.graphml')

edge_labels = dict( [ ((u,v),'%.1f' % d['weight']) for u,v,d in DG.edges(data=True)] )
edge_colors = [ d['weight'] for u,v,d in DG.edges(data=True)]

# parse the x and y attributes and create a node:coord dict
x = nx.get_node_attributes(DG, 'x')
y = nx.get_node_attributes(DG, 'y')
pos = dict( [(n, (xcoord, y[n])) for (n, xcoord) in x.items()] )

img = plt.imread("../../assets/uni-maze2.tif")
fig, ax = plt.subplots()

nx.draw(DG, pos, with_labels=True, node_size = 100, font_size=7,
        connectionstyle='arc3, rad = 0.1', edge_color=edge_colors, edge_cmap=plt.cm.Blues, width=4)

cb = plt.cm.ScalarMappable(cmap=plt.cm.Blues, norm=plt.Normalize(vmin=min(edge_colors), vmax=max(edge_colors)))
cb._A = []

plt.colorbar(cb)
#nx.draw_networkx_edge_labels(DG, pos, edge_labels=edge_labels, label_pos=.2, font_size=7)

ax.imshow(img)
plt.show()
