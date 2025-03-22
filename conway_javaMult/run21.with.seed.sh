#!/bin/bash
# run_with_seeds.sh - Generate seeds (if needed), then run the board simulator with each seed.
# Usage:
#   ./run_with_seeds.sh [seed_file]
# If [seed_file] is provided and exists, it uses that file; otherwise, it generates seeds.

# Settings
DEFAULT_SEED_FILE="seeds.txt"    # Default seed file if none provided.
DEFAULT_NUM_SEEDS=20             # Number of seeds to generate if not provided.
TIME_RECORD="timeRecord_java21.txt"     # File to record timing data.

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

# Clear previous timing records.
> "$TIME_RECORD"

# Loop through each seed in the file and run the board simulator.
while IFS= read -r seed; do
  # Create an output file name that incorporates the seed.
  fname="./data/saved_java21_${seed}"
  echo "Running board simulator with seed: ${seed}, output file: ${fname}"
  
  # Run the command using 'time' and capture both stdout and stderr.
  
  output=$( { time ./run21.sh -h 50 -w 50 -t 120 -q -f "${fname}" -s "${seed}" -c; } 2>&1 )
  # Append the seed, file name, and timing output to the record file.
  {
    echo "Seed: ${seed}, Output File: ${fname}"
    echo "${output}"
    echo "------------------------------------"
  } >> "$TIME_RECORD"
done < "$seedFile"

echo "All runs complete. Timing data is recorded in ${TIME_RECORD}."
