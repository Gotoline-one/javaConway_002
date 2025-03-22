#!/bin/bash
# run_and_record.sh - Run runmvn.sh multiple times with incremental file names
# and record timing data in timeRecord.txt

DEFAULT_SEED_FILE="seeds.txt"   # Default seed file if none provided.

# Settings
DEFAULT_NUM_SEEDS=20             # Number of seeds to generate if not provided.
# Default base file name for output files
# (change as needed)
# Note: The base file name is used to create unique output files for each seed.

# Base file name (change as needed)
basedir="./data"

base21="$basedir/conway21_"
base8="$basedir/conway8_"


# File to record timing results
timeRecord21="$basedir/timeRecord21.txt"
timeRecord8="$basedir/timeRecord8.txt"

# Optionally, clear the time record file at the start
> "$timeRecord21"
> "$timeRecord8"

# Number of iterations (adjust as needed)
iterations=10
# Clear previous timing records.


# Determine which seed file to use.
if [ -n "$1" ] && [ -f "$1" ]; then
  seedFile="$1"
  echo "Using provided seed file: ${seedFile}"
elif [ -f "$DEFAULT_SEED_FILE" ]; then
  seedFile="$DEFAULT_SEED_FILE"
  echo "Using default seed file: ${seedFile}"
else
  seedFile="$DEFAULT_SEED_FILE"
  echo "No valid seed file provided. Generating $DEFAULT_NUM_SEEDS seeds into ${seedFile}"
  > "$seedFile"  # Clear the file.
  for ((i=1; i<=DEFAULT_NUM_SEEDS; i++)); do
    seed=$(od -vAn -N4 -tu4 < /dev/urandom | tr -d " ")
    echo "$seed" >> "$seedFile"
  done
fi


while IFS= read -r seed; do
  # Create an output file name that incorporates the seed.
  fname8="${base8}_${seed}"
  fname21="${base21}_${seed}"


  # Run the command using 'time' and capture both stdout and stderr.
  echo "Running board simulator with seed: ${seed}, output file: ${fname8}"
  output8=$( { time ./run8.sh -h 50 -w 50 -t 120 -q -f "${fname8}" -s "${seed}" -c; } 2>&1 )
  
  # Run the command using 'time' and capture both stdout and stderr.
  echo "Running board simulator with seed: ${seed}, output file: ${fname21}"  
  output21=$( { time ./run21.sh -h 50 -w 50 -t 120 -q -f "${fname21}" -s "${seed}" -c; } 2>&1 )

    # Append the seed, file name, and timing output to the record file.
  {
    echo "Seed: ${seed}, Output File: ${fname8}"
    echo "${output8}"
    echo "------------------------------------"
  } >> "$timeRecord8"

  # Append the seed, file name, and timing output to the record file.
  {
    echo "Seed: ${seed}, Output File: ${fname21}"
    echo "${output21}"
    echo "------------------------------------"
  } >> "$timeRecord21"
  
done < "$seedFile"
# Final message indicating completion


echo "All runs complete. Java21 timing data is recorded in ${timeRecord21}."
echo "All runs complete. Java8 timing data is recorded in ${timeRecord8}."
