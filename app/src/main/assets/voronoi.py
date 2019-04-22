
#from scipy.spatial import Voronoi, voronoi_plot_2d
import numpy as np
import scipy.spatial as spatial
import matplotlib.pyplot as plt
from scipy.spatial import Voronoi, voronoi_plot_2d
from shapely.geometry import Polygon, mapping, Point
import shapely.geometry
import shapely.ops
# first we need to get the coordinates from file
# format : [[latitude,longitude]..]
fileInput = open("Coordinates.txt", "r")
count = 0
points = []
for line in fileInput :
    if count != 0 :
        words = line.split()
        points.append([float(words[5]), float(words[4])])
    count = count + 1
fileInput.close

vor = spatial.Voronoi(points)
voronoi_plot_2d(vor)
plt.show()

# get polygon coordinates
lines = [
    shapely.geometry.LineString(vor.vertices[line])
    for line in vor.ridge_vertices
    if -1 not in line
]

# polygonize lines and save into a file
# each line : first two values is the input point associated to each polygon,
# pair of coords of vertices for a polygon
fileOutput = open("CoordinatesPolygon.txt", "w+")

for poly in shapely.ops.polygonize(lines) :
    index = 0
    while poly.contains(Point(vor.points[index][0], vor.points[index][1])) == False :
        index = index + 1
    fileOutput.write("%.20f " % vor.points[index][0])
    fileOutput.write("%.20f " % vor.points[index][1])
    for x,y in poly.exterior.coords :
        fileOutput.write("%.20f " % x)
        fileOutput.write("%.20f " % y )
    index = index + 1
    fileOutput.write("\n")
fileOutput.close
