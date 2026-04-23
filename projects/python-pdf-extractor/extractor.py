import os
import re
from datetime import datetime
from collections import Counter

DEFAULT_FILE = "server.log"

THREAT_LEVELS = {
    "CRITICAL":     "🔴",
    "ERROR":        "🟠",
    "FAILED LOGIN": "🟡"
}


def load_file(path):
    path = path.strip("'\"")
    if not os.path.exists(path):
        print(f"[ERROR] File not found: {path}")
        return None
    if os.path.getsize(path) == 0:
        print(f"[WARN] File is empty: {path}")
        return None
    with open(path, "r", encoding="utf-8") as f:
        lines = f.readlines()
    print(f"[LOG] Loaded '{path}' — {len(lines)} lines total")
    return lines


def filter_threats(lines):
    threats = []
    noise_count = 0

    for line in lines:
        matched = False
        for keyword in THREAT_LEVELS:
            if keyword in line.upper():
                threats.append(line.strip())
                matched = True
                break
        if not matched:
            noise_count += 1

    print(f"[LOG] Filtered out {noise_count} INFO lines (noise)")
    print(f"[LOG] Found {len(threats)} threat lines")
    return threats


def count_by_type(threat_lines):
    counts = Counter()
    for line in threat_lines:
        for keyword in THREAT_LEVELS:
            if keyword in line.upper():
                counts[keyword] += 1
                break
    return counts


def extract_ips(threat_lines):
    ips = Counter()
    for line in threat_lines:
        found = re.findall(r'\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b', line)
        for ip in found:
            ips[ip] += 1
    return ips


def save_alert(source_path, threat_lines, counts, ips, total_lines):
    today = datetime.now().strftime("%Y-%m-%d")
    out_name = f"security_alert_{today}.txt"

    with open(out_name, "w", encoding="utf-8") as f:
        f.write("=" * 55 + "\n")
        f.write("         OPSBOT — SECURITY ALERT REPORT\n")
        f.write("=" * 55 + "\n")
        f.write(f"Source File   : {source_path}\n")
        f.write(f"Generated     : {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        f.write(f"Total Lines   : {total_lines}\n")
        f.write(f"Threat Lines  : {len(threat_lines)}\n")
        f.write(f"Noise Removed : {total_lines - len(threat_lines)} INFO lines\n")
        f.write("\n")

        f.write("Threat Summary\n")
        f.write("-" * 30 + "\n")
        for threat, icon in THREAT_LEVELS.items():
            f.write(f"  {icon} {threat:<15} : {counts.get(threat, 0)}\n")
        f.write("\n")

        if ips:
            f.write("Suspicious IPs\n")
            f.write("-" * 30 + "\n")
            for ip, count in ips.most_common():
                f.write(f"  {ip:<18} — {count} attempt(s)\n")
            f.write("\n")

        f.write("Threat Log\n")
        f.write("-" * 30 + "\n")
        for line in threat_lines:
            f.write(f"  {line}\n")

        f.write("\n" + "=" * 55 + "\n")

    size = os.path.getsize(out_name)
    print(f"[LOG] Alert saved -> {out_name} ({size} bytes)")
    return out_name


def run():
    print("=" * 55)
    print("        OpsBot — Server Log Analyzer")
    print("=" * 55)

    print("\nOptions:")
    print("  1. Drag & drop log file into terminal and press Enter")
    print("  2. Type the full file path")
    print("  3. Press Enter to use default (server.log)")
    path = input("\nLog file: ").strip().strip("'\"")

    if not path:
        path = DEFAULT_FILE
        print(f"[LOG] Using default: {path}")

    lines = load_file(path)
    if lines is None:
        print("Nothing to process. Exiting.")
        return

    print("\n[LOG] Scanning for threats...")
    threat_lines = filter_threats(lines)

    if not threat_lines:
        print("[LOG] No threats found. System looks clean.")
        return

    counts = count_by_type(threat_lines)
    ips    = extract_ips(threat_lines)

    print("\n--- Threat Summary ---")
    for threat, icon in THREAT_LEVELS.items():
        print(f"  {icon}  {threat:<15} : {counts.get(threat, 0)}")

    if ips:
        print("\n--- Suspicious IPs ---")
        for ip, count in ips.most_common():
            print(f"  {ip:<18} — {count} attempt(s)")

    out = save_alert(path, threat_lines, counts, ips, len(lines))
    print(f"\nDone. Alert report: {out}")


if __name__ == "__main__":
    run()
