#!/usr/bin/env bash

readonly SCRIPTDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
readonly CWD=$(pwd)

cd ${SCRIPTDIR}/../build

python kalah/kalah.py

cd ${CWD}
