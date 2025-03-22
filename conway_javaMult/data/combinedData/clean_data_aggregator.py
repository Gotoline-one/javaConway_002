import os
import re
import glob
import argparse
import pandas as pd

# Define the expected header columns (adjust if needed)
EXPECTED_COLUMNS = [
    "java_version",
    "seed",
    "num_measurements",
    "start_elapsed_ms",
    "end_elapsed_ms",
    "total_elapsed_ms",
    "total_elapsed_sec",
    "avg_fps",
    "avg_frame_count",
    "sum_frame_count"
]

def try_read_csv(file_path, max_skip=3):
    """
    Try reading the CSV file with skiprows values from 0 to max_skip.
    Returns the DataFrame and the skip value used if the expected columns are found.
    """
    for skip in range(max_skip + 1):
        try:
            df = pd.read_csv(file_path, skiprows=skip)
            if set(EXPECTED_COLUMNS).issubset(set(df.columns)):
                return df, skip
        except Exception as e:
            continue
    return None, None

def process_file(file_path):
    """
    Process a single aggregated CSV file:
      - Tries to read the file using various skiprows values until the expected header is found.
      - Keeps only the expected columns (in the defined order).
      - Extracts a test identifier from the filename (e.g. "aggregated_comparison2.csv" → test_id "2").
    """
    df, skip_used = try_read_csv(file_path, max_skip=3)
    if df is None:
        print(f"Could not read file {file_path} with expected header after skipping up to 3 rows.")
        return None

    # Keep only the expected columns (drop any extra columns)
    df = df[EXPECTED_COLUMNS]

    # Extract test_id from the filename using a regex.
    base_name = os.path.basename(file_path)
    match = re.search(r"aggregated_comparison(\d+)\.csv", base_name)
    if match:
        test_id = match.group(1)
    else:
        test_id = "unknown"
    df["test_id"] = test_id
    return df

def aggregate_files(folder, pattern="aggregated_comparison*.csv"):
    """
    Finds and processes all CSV files in the folder matching the pattern.
    Returns a master DataFrame with data from all files.
    """
    csv_files = glob.glob(os.path.join(folder, pattern))
    all_dfs = []
    for file_path in csv_files:
        df = process_file(file_path)
        if df is not None:
            print(f"Processed file: {file_path} with {df.shape[0]} rows.")
            all_dfs.append(df)
    if all_dfs:
        master_df = pd.concat(all_dfs, ignore_index=True)
        return master_df
    return pd.DataFrame(columns=EXPECTED_COLUMNS + ["test_id"])

def main(folder, output):
    master_df = aggregate_files(folder)
    if master_df.empty:
        print("No valid aggregated CSV files found.")
    else:
        # Save the clean master aggregated data to a CSV file
        master_df.to_csv(output, index=False)
        print(f"Clean master aggregated data saved to {output}")
        print(master_df.head())

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Clean Data Aggregator Pipeline")
    parser.add_argument("folder", help="Folder containing aggregated CSV files (e.g. aggregated_comparison1.csv, etc.)")
    parser.add_argument("--output", default="clean_master_aggregated.csv", help="Output CSV file for the clean master aggregated data.")
    args = parser.parse_args()
    main(args.folder, args.output)

