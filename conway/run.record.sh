#!/bin/bash
# run_and_record.sh - Run runmvn.sh multiple times with incremental file names
# and record timing data in timeRecord.txt

# Base file name (change as needed)
basedir="./data"
base="$basedir/conway22_"
# File to record timing results
timeRecord="$basedir/timeRecord.txt"
# Number of iterations (adjust as needed)
iterations=10

# Optionally, clear the time record file at the start
> "$timeRecord"

for ((i=1; i<=iterations; i++)); do
  # Create an incremental file name (e.g. dumb_1, dumb_2, etc.)
  fname="${base}_${i}"
  
  echo "Running with file: ${fname}"
  
  # Run the command using 'time' and capture the output.
  # The time command outputs its result to stderr, so we capture both stdout and stderr.
  output=$( { time ./runmvn.sh -h 50 -w 50 -t 60 -q -f "${fname}" -j; } 2>&1 )
  
  # Append the result to the record file with a header
  {
    echo "File: ${fname}"
    echo "${output}"
    echo "------------------------------------"
  } >> "$timeRecord"
done

echo "All runs complete. Timing data is recorded in ${timeRecord}."
