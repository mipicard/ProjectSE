#!/usr/bin/python3

import cv2
import sys
import os


## Convert the source image to an edgy image, save in output, using threshold
def canny(source, threshold_min, threshold_max, output):
    image = cv2.imread(source)
    edge_image = cv2.Canny(image, threshold_min, threshold_max)
    cv2.imwrite(output, edge_image)


## Convert a number to 4digit string
def convert_number_to_string(number):
    if number >= 10000:
        raise ValueError(str(number) + " value over 9999.")
    ret = ""
    if number < 1000:
        ret += "0"
    if number < 100:
        ret += "0"
    if number < 10:
        ret += "0"
    return ret + str(number)


## Get the number of file in a directory
def number_of_file(directory):
    files = [file for file in os.listdir(directory) if os.path.isfile(os.path.join(directory, file))]
    return len(files)


if __name__ == "__main__":
    (path, directory) = os.path.split(sys.argv[1])
    output_directory = os.path.join(path, "{}_canny".format(directory))
    os.makedirs(
        output_directory,
        exist_ok=False
    )
    for i in [n+1 for n in range(number_of_file(sys.argv[1]))]:
        canny(
            os.path.join(
                sys.argv[1],
                "frame{}.jpg".format(convert_number_to_string(i))
            ),
            int(sys.argv[2]),
            int(sys.argv[3]),
            os.path.join(
                output_directory,
                "frame{}.bmp".format(convert_number_to_string(i))
            )
        )
