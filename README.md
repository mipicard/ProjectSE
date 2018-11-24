# ProjectSE
SE project for M1 Course

This project goal is to try to extract and compute movement detection on one and
only one point of view, without moving the point of view.

In order to do so, we use some step :

1. We extract frames from the videos (you can choose how much by seconds).
2. We ask the user to choose threshold for one sample frame from the videos.
3. Then, we converted all frame with the given threshold, using Canny algorithm.
4. We read all edges frames and :
   1. We remove motionless edges.
   2. We group edges pixels.
   3. We use Manhattan computation to determine link of group between two frames.
   4. We color all group, using the same colour for linked group across the 
   frames (or at least we try).
   5. We export again all the frames.
5. Finaly, we compile all exported frames into one video, to watch and enjoy
only the movement of the given video.

All this step can be done simply by running the bash script run.sh that way :
`run.sh <path_to_the_video> <fps_to_extract_and_compute>`.
Note that this global script haven't been tested, due to very long computation
time.

This script will automatically call all sub script, but we can also call each of
them one by one. Each of them perform one of the task above :

- `extractFrame.sh <video> <fps> <output_directory>` perform step 1. Note that
you need to provide an empty directory for output. If not, it will cause some
unexpected behaviour for next step.
- `CannyConverter.py <directory_with_frames>` perform step 2 and then step 3 by
creating another directory with the same name with "_canny" at the end, 
alongside the given one.
- `java src.Main <directory_with_edges_frames>` perform step 4 and all substeps.
It's java bytecode, stored in the bin directory, compile from the corresponding
java source file in the src folder. It will output another directory, alongside
the given one, with the same name with "_export" at the end.
- `createVideo.sh <directory_with_exported_frames> <fps> <output_video_name>`
perform step 5.
