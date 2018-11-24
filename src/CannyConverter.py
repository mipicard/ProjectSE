#!/usr/bin/python3

import cv2
import os
import argparse

# This class goal is to display a windows to determine threshold and return it
# for further use.
class CannyDetector:
    def __init__(self, windows, image_src):
        self._min_threshold = 0
        self._max_threshold = 500
        self._windows = windows
        self._img_src = image_src
        self._img = cv2.blur(cv2.cvtColor(image_src, cv2.COLOR_BGR2GRAY), (3,3))
	
	# Display the window
    def display(self):
        edge = cv2.Canny(self._img, self._min_threshold, self._max_threshold, 3)
        mask = edge != 0
        cv2.imshow(self._windows, self._img_src * (mask[:,:,None].astype(self._img_src.dtype)))

    def update_min_threshold(self, value):
        self._min_threshold = value
        self.display()

    def update_max_threshold(self, value):
        self._max_threshold = value
        self.display()

    def get_min_threshold(self):
        return self._min_threshold

    def get_max_threshold(self):
        return self._max_threshold


# Convert the source image to an edgy image, save in output, using threshold
def canny_writer(source, threshold_min, threshold_max, output):
    image = cv2.imread(source)
    edge_image = cv2.Canny(image, threshold_min, threshold_max)
    cv2.imwrite(output, edge_image)


# Convert a number to 4digit string
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


# Get the number of file in a directory
def number_of_file(directory):
    files = [file for file in os.listdir(directory) if os.path.isfile(os.path.join(directory, file))]
    return len(files)


# Run the canny detection
def run_canny_detection(image):
    windows = 'Edge detector - hit enter to end'
    canny_detector = CannyDetector(windows, image)
    cv2.namedWindow(windows, 0)  # Create a windows
    cv2.resizeWindow(windows, 1000, 1000)
    cv2.createTrackbar('Min Threshold', windows, 0, 500, canny_detector.update_min_threshold)  # Add some trackbar
    cv2.createTrackbar('Max Threshold', windows, 200, 500, canny_detector.update_max_threshold)  # Add some trackbar
    canny_detector.display() # Display the windows
    cv2.waitKey() # Wait for one keyboard key to be pressed
    return canny_detector


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description='Code for Canny Edge Detector tutorial.')
    parser.add_argument('input', help='Path to input directory.')
    args = parser.parse_args()

    n_file = number_of_file(args.input)

    path_image = "{}/frame{}.jpg".format(
        args.input,
        convert_number_to_string(int(n_file/2)))
    image = cv2.imread(path_image)
    if image is None:
        print('Could not open or find the image: ', path_image)
        exit(0)
    canny_detector = run_canny_detection(image)

    min_threshold = canny_detector.get_min_threshold()
    max_threshold = canny_detector.get_max_threshold()
    (path, directory) = os.path.split(args.input)
    output_directory = os.path.join(path, "{}_canny".format(directory))
    os.makedirs(
        output_directory,
        exist_ok=False
    )
    # Read, convert and output all frame from 1 to the number of them.
    for i in [n + 1 for n in range(n_file)]:
        canny_writer(
            os.path.join(
                args.input,
                "frame{}.jpg".format(convert_number_to_string(i))
            ),
            min_threshold,
            max_threshold,
            os.path.join(
                output_directory,
                "frame{}.bmp".format(convert_number_to_string(i))
            )
        )
