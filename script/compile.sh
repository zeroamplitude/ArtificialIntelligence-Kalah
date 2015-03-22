#!/usr/bin/env bash


readonly SCRIPTDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
readonly PROJECTDIR=${SCRIPTDIR}/..
readonly SRCDIR=${PROJECTDIR}/src
readonly BUILDDIR=${PROJECTDIR}/build

echo ${PROJECTDIR}
echo ${SRCDIR}
echo ${BUILDDIR}

if ! [ -d ${BUILDDIR} ]; then
    mkdir build
    echo "making build directory.."
fi

if ! [ -d ${BUILDDIR}/. ]; then
    echo rm -r "${BUILDDIR}"/*
fi

javac -d ${BUILDDIR} ${SRCDIR}/*/*.java

for pyfile in $(find ${SRCDIR} -type f -name *.py); do
    cp ${pyfile} ${BUILDDIR}/kalah/$(basename ${pyfile})
done