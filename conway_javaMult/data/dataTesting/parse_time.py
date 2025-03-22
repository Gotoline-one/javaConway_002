import os
import re
import glob
import argparse
import pandas as pd
import matplotlib.pyplot as plt

def parse_time(time_str):
    """
    Parse a time string in the format 'XmY.Zs' into total seconds.
    Example: '1m31.740s' -> 91.740 seconds.
    """
    match = re.match(r'(\d+)m([\d\.]+)s', time_str)
    if match:
        minutes = int(match.group(1))
        seconds = float(match.group(2))
        return minutes * 60 + seconds
    else:
        raise ValueError(f"Time format not recognized: {time_str}")

def process_file(filepath):
    """
    Process a single CSV file.
    If the CSV doesn't have the expected header, assume that it contains two columns
    in order: metric, time.
    """
    try:
        # Try reading with header
        df = pd.read_csv(filepath)
    except Exception as e:
        print(f"Error reading {filepath}: {e}")
        return None

    # Check if the expected columns are present
    if not {'metric', 'time'}.issubset(df.columns):
        # Assume the file has no header and assign column names
        if len(df.columns) >= 2:
            df = pd.read_csv(filepath, header=None, names=['metric', 'time'])
        else:
            print(f"File {filepath} doesn't have at least two columns.")
            return None

    times = {}
    for idx, row in df.iterrows():
        metric = str(row['metric']).strip().lower()
        try:
            seconds = parse_time(str(row['time']).strip())
            times[metric] = seconds
        except Exception as e:
            print(f"Error parsing time in {filepath} for metric '{metric}': {e}")
            return None

    # Compute total CPU time if needed (user + sys)
    times['cpu'] = times.get('user', 0) + times.get('sys', 0)
    return times

def extract_info_from_filename(filename):
    """
    Extract the Java version and seed from the filename.
    Expected filename format: saved_java<VERSION>_<SEED>.csv
    Example: saved_java8_197181812.csv -> version: 8, seed: 197181812
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
    # Find all CSV files matching the pattern in the given folder
    file_pattern = os.path.join(data_folder, "saved_java*.csv")
    files = glob.glob(file_pattern)
    if not files:
        print("No CSV files found matching the pattern in the specified folder.")
        return

    # List to collect records
    records = []
    for filepath in files:
        filename = os.path.basename(filepath)
        java_version, seed = extract_info_from_filename(filename)
        if java_version is None:
            print(f"Filename {filename} does not match expected pattern. Skipping.")
            continue

        times = process_file(filepath)
        if times is None:
            continue

        record = {
            "java_version": java_version,
            "seed": seed,
            "real": times.get("real", None),
            "user": times.get("user", None),
            "sys": times.get("sys", None),
            "cpu": times.get("cpu", None)
        }
        records.append(record)

    # Create a DataFrame
    df = pd.DataFrame(records)
    if df.empty:
        print("No valid data to analyze.")
        return

    # Show summary statistics by Java version
    summary = df.groupby("java_version")[["real", "user", "sys", "cpu"]].describe()
    print("Summary statistics by Java version:")
    print(summary)

    # Optionally, plot real times by Java version
    if plot:
        plt.figure(figsize=(8, 6))
        for version, group in df.groupby("java_version"):
            plt.scatter(group['seed'], group['real'], label=f"Java {version}", s=50)
        plt.xlabel("Seed")
        plt.ylabel("Real Time (s)")
        plt.title("Real Time by Java Version and Seed")
        plt.legend()
        plt.tight_layout()
        plt.show()

    # Optionally, save the aggregated data to a CSV file
    output_csv = os.path.join(data_folder, "aggregated_results.csv")
    df.to_csv(output_csv, index=False)
    print(f"Aggregated data saved to {output_csv}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Analyze time CSV files for different Java versions.")
    parser.add_argument("data_folder", help="Folder containing the CSV files.")
    parser.add_argument("--plot", action="store_true", help="Plot the real times by Java version.")
    args = parser.parse_args()
    main(args.data_folder, args.plot)

