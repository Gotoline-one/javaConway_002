import os
import re
import glob
import argparse
import pandas as pd

def aggregate_files_from_csv(folder, pattern="aggregated_comparison*.csv"):
    """
    Scans the specified folder for CSV files matching the given pattern.
    Each file is expected to have columns like:
    java_version,seed,num_measurements,start_elapsed_ms,end_elapsed_ms,
    total_elapsed_ms,total_elapsed_sec,avg_fps,avg_frame_count,sum_frame_count

    This function extracts a test identifier from the filename (e.g.,
    from "aggregated_comparison2.csv" it extracts "2") and adds it as a column.
    """
    csv_files = glob.glob(os.path.join(folder, pattern))
    all_data = []
    for csv_file in csv_files:
        try:
            df = pd.read_csv(csv_file)
            # Extract test id from filename using regex: aggregated_comparison(\d+).csv
            match = re.search(r"aggregated_comparison(\d+)\.csv", os.path.basename(csv_file))
            if match:
                test_id = match.group(1)
            else:
                test_id = "unknown"
            df["test_id"] = test_id
            all_data.append(df)
            print(f"Loaded {csv_file} with {df.shape[0]} rows.")
        except Exception as e:
            print(f"Error reading {csv_file}: {e}")
    if all_data:
        master_df = pd.concat(all_data, ignore_index=True)
        return master_df
    else:
        return pd.DataFrame()

def main(folder, output):
    master_df = aggregate_files_from_csv(folder)
    if master_df.empty:
        print("No valid aggregated CSV files found in the specified folder.")
        return

    print("Master aggregated data (first few rows):")
    print(master_df.head())

    # Save the master aggregated data to a CSV file
    master_df.to_csv(output, index=False)
    print(f"\nMaster aggregated data saved to {output}")

    # Compute and display the correlation matrix for numeric columns
    numeric_cols = master_df.select_dtypes(include=['number']).columns
    if numeric_cols.any():
        print("\nCorrelation matrix for numeric columns:")
        print(master_df[numeric_cols].corr())
    else:
        print("\nNo numeric columns found for correlation analysis.")

    # Create a pivot table comparing a metric across tests.
    # Example: Compare total_elapsed_sec by seed and java_version, with columns for each test_id.
    try:
        pivot = master_df.pivot_table(index=["seed", "java_version"], 
                                      columns="test_id", 
                                      values="total_elapsed_sec")
        print("\nPivot table for total_elapsed_sec (per seed and java_version, by test_id):")
        print(pivot)
    except Exception as e:
        print(f"Error creating pivot table: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Master correlator for aggregated CSV files from multiple tests."
    )
    parser.add_argument("folder", help="Folder containing aggregated CSV files (e.g., aggregated_comparison2.csv, aggregated_comparison3.csv).")
    parser.add_argument("--output", default="master_aggregated.csv", help="Output CSV file for the master aggregated data.")
    args = parser.parse_args()
    main(args.folder, args.output)

