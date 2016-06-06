#! /bin/sh
awk '{sum+=$3} END {print sum}' $1
