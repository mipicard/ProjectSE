#!/bin/bash

ffmpeg -framerate $2 -i $1/frame%04d.jpg $3
