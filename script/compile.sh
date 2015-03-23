#!/usr/bin/env bash


readonly SCRIPTDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
readonly PROJECTDIR=${SCRIPTDIR}/..
readonly SRCDIR=${PROJECTDIR}/src
readonly BUILDDIR=${PROJECTDIR}/build
readonly TESTDIR=${PROJECTDIR}/test

echo ${PROJECTDIR}
echo ${SRCDIR}
echo ${BUILDDIR}

if ! [ -d ${BUILDDIR} ]; then
    mkdir ${PROJECTDIR}/build
    echo "making build directory.."
fi

if ! [ -d ${BUILDDIR}/. ]; then
    echo rm -r "${BUILDDIR}"/*
fi

javac -d ${BUILDDIR} ${SRCDIR}/*/*.java
javac -d ${BUILDDIR} -cp  ${TESTDIR}/*/*.java

for pyfile in $(find ${SRCDIR} -type f -name *.py); do
    cp ${pyfile} ${BUILDDIR}/kalah/$(basename ${pyfile})
done