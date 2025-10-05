#!/usr/bin/ksh

# Input Parameters
file=$1
typeset -i mode=$2

chmod -f $mode $file
