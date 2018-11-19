#!/usr/bin/python3

import cv2
from matplotlib import pyplot as plt
import sys

img = cv2.imread(sys.argv[1])
edges = cv2.Canny(img, int(sys.argv[2]), int(sys.argv[3]))

plt.subplot(121), plt.imshow(img, cmap='gray')
plt.title('Original Image'), plt.xticks([]), plt.yticks([])
plt.subplot(122), plt.imshow(edges, cmap='gray')
plt.title('Edge Image'), plt.xticks([]), plt.yticks([])

plt.show()
