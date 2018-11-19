#!/bin/bash

ffmpeg -i $1 -vf fps=10 $2/frame%04d.jpg -hide_banner