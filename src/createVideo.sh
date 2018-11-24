#!/bin/bash

ffmpeg -framerate $2 -i $1/frame%04d.bmp $3
