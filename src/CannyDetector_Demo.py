#!/usr/bin/python3

from __future__ import print_function
import cv2 as cv
import argparse

max_lowThreshold = 500
window_name = 'Edge Map'
title_trackbar = 'Min Threshold:'
ratio = 3
kernel_size = 3

def CannyThreshold(val):
    low_threshold = val
    img_blur = cv.blur(src_gray, (3,3))
    detected_edges = cv.Canny(img_blur, low_threshold, low_threshold*ratio, kernel_size)
    mask = detected_edges != 0
    dst = src * (mask[:,:,None].astype(src.dtype))
    cv.imshow(window_name, dst)

parser = argparse.ArgumentParser(description='Code for Canny Edge Detector tutorial.')
parser.add_argument('input', help='Path to input image.')
args = parser.parse_args()

src = cv.imread(args.input)
if src is None:
    print('Could not open or find the image: ', args.input)
    exit(0)

src_gray = cv.cvtColor(src, cv.COLOR_BGR2GRAY)

cv.namedWindow(window_name, 0)
cv.resizeWindow(window_name, 1000, 1000)
cv.createTrackbar(title_trackbar, window_name , 0, max_lowThreshold, CannyThreshold)
# cv.createTrackbar('Max Threshold', window_name , 0, max_lowThreshold, CannyThreshold)

CannyThreshold(0)
cv.waitKey()
