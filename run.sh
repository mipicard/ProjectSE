#!/bin/bash

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null && pwd )"

cd $DIR
V=$1
EXPORTDIR=${V/%????/}
mkdir $EXPORTDIR
src/extractFrame.sh $1 $EXPORTDIR $2
src/CannyConverter.py $EXPORTDIR
cd bin
java src.Main ../${EXPORTDIR}_canny ../${EXPORTDIR}_export
cd ..
src/createVideo.sh ${EXPORTDIR}_export $2 ${EXPORTDIR}_movement.mp4
