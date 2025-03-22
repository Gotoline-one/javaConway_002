import os
import re
import glob
import argparse
import pandas as pd
import matplotlib.pyplot as plt

def analyze_file(filepath):
    """
    Analyze a CSV file with columns:
      index, frame_count, nano_time, elapsed_ms

    Computes:
      - Total elapsed time: difference between first and last elapsed_ms (in ms and sec)
      - Instantaneous FPS between consecutive rows:
             fps = (difference in frame_count) / (difference in elapsed_ms / 1000)
      - Average FPS (across valid intervals)
      - Average frame count and total frame count sum
    """
    try:
        df = pd.read_csv(filepath)
    except Exception as e:
        print(f"Error reading {filepath}: {e}")
        return None
    if df.empty:
        print(f"File {filepath} is empty.")
        return None

    required = {"index", "frame_count", "nano_time", "elapsed_ms"}
    if not required.issubset(df.columns):
        print(f"File {filepath} does not contain required columns: {required}")
        return None

    # Ensure rows are in order
    df = df.sort_values("index")
    start_time = df["elapsed_ms"].iloc[0]
    end_time = df["elapsed_ms"].iloc[-1]
    total_elapsed = end_time - start_time  # in milliseconds

    # Calculate differences
    df["time_diff"] = df["elapsed_ms"].diff()      # in ms
    df["frame_diff"] = df["frame_count"].diff()      # frame count difference
    df["fps"] = df["frame_diff"] / (df["time_diff"] / 1000.0)

    # Exclude first row (NaN) and any interval with non-positive time difference
    valid = df["fps"].notna() & (df["time_diff"] > 0)
    avg_fps = df.loc[valid, "fps"].mean() if valid.any() else None

    avg_frame_count = df["frame_count"].mean()
    sum_frames = df["frame_count"].sum()

    return {
        "num_measurements": len(df),
        "start_elapsed_ms": start_time,
        "end_elapsed_ms": end_time,
        "total_elapsed_ms": total_elapsed,
        "total_elapsed_sec": total_elapsed / 1000.0,
        "avg_fps": avg_fps,
        "avg_frame_count": avg_frame_count,
        "sum_frame_count": sum_frames
    }

def extract_info_from_filename(filename):
    """
    Extract the Java version and seed from the filename.
    Expected format: saved_java<VERSION>_<SEED>.csv
    Example: saved_java8_197181812.csv returns version "8" and seed "197181812".
    """
    pattern = r"saved_java(\d+)_(\d+)\.csv"
    match = re.search(pattern, filename)
    if match:
        java_version = match.group(1)
        seed = match.group(2)
        return java_version, seed
    else:
        return None, None

def main(data_folder, plot=False):
    # Find all CSV files with the expected naming
    file_pattern = os.path.join(data_folder, "saved_java*.csv")
    files = glob.glob(file_pattern)
    if not files:
        print("No CSV files found matching the pattern in the specified folder.")
        return

    records = []
    for filepath in files:
        filename = os.path.basename(filepath)
        java_version, seed = extract_info_from_filename(filename)
        if java_version is None:
            print(f"Filename {filename} does not match expected pattern. Skipping.")
            continue

        stats = analyze_file(filepath)
        if stats is None:
            continue

        record = {
            "java_version": java_version,
            "seed": seed
        }
        record.update(stats)
        records.append(record)

    df = pd.DataFrame(records)
    if df.empty:
        print("No valid data to analyze.")
        return

    #print("Aggregated results:")
    print(df)

    # Pivot data so that each seed has both Java 8 and Java 22 side by side
    pivot_cols = ["total_elapsed_sec", "avg_fps", "avg_frame_count", "sum_frame_count"]
    df_pivot = df.pivot(index="seed", columns="java_version", values=pivot_cols)
    print("\nPivoted results (metrics by seed and Java version):")
    print(df_pivot)

    # Calculate differences: (Java22 - Java8) for each metric
    comparison = {}
    for metric in pivot_cols:
        try:
            # Using .get to avoid KeyErrors if a metric is missing for one version
            java8 = df_pivot[metric].get("8")
            java22 = df_pivot[metric].get("22")
            if java8 is not None and java22 is not None:
                comparison[metric + "_diff"] = java22 - java8
        except Exception as e:
            print(f"Error computing difference for {metric}: {e}")

    df_comparison = pd.DataFrame(comparison)
    print("\nDifferences (Java22 - Java8) by seed:")
    print(df_comparison)

    if plot:
        # Plot total elapsed time for each seed (Java8 vs Java22)
        plt.figure(figsize=(10, 6))
        seeds_int = df["seed"].astype(int)
        for version, group in df.groupby("java_version"):
            plt.scatter(group["seed"].astype(int), group["total_elapsed_sec"],
                        label=f"Java {version}", s=50)
        plt.xlabel("Seed")
        plt.ylabel("Total Elapsed Time (sec)")
        plt.title("Total Elapsed Time by Java Version and Seed")
        plt.legend()
        plt.tight_layout()
        plt.show()

        # Plot average FPS for each seed (Java8 vs Java22)
        plt.figure(figsize=(10, 6))
        for version, group in df.groupby("java_version"):
            plt.scatter(group["seed"].astype(int), group["avg_fps"],
                        label=f"Java {version}", s=50)
        plt.xlabel("Seed")
        plt.ylabel("Average FPS")
        plt.title("Average FPS by Java Version and Seed")
        plt.legend()
        plt.tight_layout()
        plt.show()

    # Optionally, save the aggregated and comparison data
    output_csv = os.path.join(data_folder, "aggregated_comparison.csv")
    with open(output_csv, "w") as f:
        f.write("Aggregated Results:\n")
        df.to_csv(f, index=False)
        f.write("\n\nPivoted Results:\n")
        df_pivot.to_csv(f)
        f.write("\n\nDifferences (Java22 - Java8):\n")
        df_comparison.to_csv(f)
    print(f"\nAggregated comparison data saved to {output_csv}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Compare timing CSV data between Java 8 and Java 22 runs.")
    parser.add_argument("data_folder", help="Folder containing the CSV files.")
    parser.add_argument("--plot", action="store_true", help="Plot the comparison results.")
    args = parser.parse_args()
    main(args.data_folder, args.plot)

