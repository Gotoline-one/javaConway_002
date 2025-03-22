import argparse
import pandas as pd
import matplotlib.pyplot as plt

def main(input_file, output, plot=False):
    try:
        master_df = pd.read_csv(input_file)
    except Exception as e:
        print(f"Error reading {input_file}: {e}")
        return

    if master_df.empty:
        print("Input file is empty.")
        return

    print("Master aggregated data (first few rows):")
    print(master_df.head())

    # Save the data to an output file
    master_df.to_csv(output, index=False)
    print(f"Master aggregated data saved to {output}")

    # Compute the correlation matrix for numeric columns
    numeric_cols = master_df.select_dtypes(include=['number']).columns
    if len(numeric_cols) > 0:
        print("\nCorrelation matrix for numeric columns:")
        print(master_df[numeric_cols].corr())
    else:
        print("\nNo numeric columns found for correlation analysis.")

    if plot:
        # Plot Total Elapsed Seconds vs Seed for each test group
        plt.figure(figsize=(10, 6))
        for test_id, group in master_df.groupby("test_id"):
            # Convert seed to int (if needed) for proper sorting
            seeds = group["seed"].astype(int)
            plt.scatter(seeds, group["total_elapsed_sec"], label=f"Test {test_id}", s=50)
        plt.xlabel("Seed")
        plt.ylabel("Total Elapsed Seconds")
        plt.title("Total Elapsed Seconds vs Seed by Test")
        plt.legend()
        plt.tight_layout()
        plt.show()

        # Plot Average FPS vs Seed for each test group
        plt.figure(figsize=(10, 6))
        for test_id, group in master_df.groupby("test_id"):
            seeds = group["seed"].astype(int)
            plt.scatter(seeds, group["avg_fps"], label=f"Test {test_id}", s=50)
        plt.xlabel("Seed")
        plt.ylabel("Average FPS")
        plt.title("Average FPS vs Seed by Test")
        plt.legend()
        plt.tight_layout()
        plt.show()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Master correlator using a single clean master aggregated CSV file with plotting."
    )
    parser.add_argument("--input-file", required=True, help="Input CSV file (clean master aggregated file)")
    parser.add_argument("--output", default="master_final.csv", help="Output CSV file for the master aggregated data.")
    parser.add_argument("--plot", action="store_true", help="If set, produce plots for the data.")
    args = parser.parse_args()
    main(args.input_file, args.output, args.plot)

