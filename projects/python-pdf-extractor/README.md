# OpsBot — Server Log Analyzer

IT teams dealing with thousands of log lines daily shouldn't be doing that manually. Built this script to automate the boring part — read the raw log, throw away the noise, surface only what matters.

Point it at any server log file and it tells you how many ERRORs, CRITICALs, and FAILED LOGINs happened, which IPs were suspicious, and saves a clean security alert report with just the dangerous lines.

## What it does

Reads the log file line by line. Skips all the INFO lines (noise). Keeps only CRITICAL, ERROR, and FAILED LOGIN lines. Counts each threat type. Finds all IP addresses in the threat lines and counts how many times each one showed up — useful for spotting brute force attempts. Saves everything to security_alert_YYYY-MM-DD.txt and confirms the file size using os.

## How to run

```bash
cd projects/python-pdf-extractor
python3 extractor.py
```

Three options when it starts:
1. Drag your log file into the terminal and press Enter
2. Type the full file path
3. Just press Enter to use the included server.log

## Sample output

```
[LOG] Loaded 'server.log' — 40 lines total
[LOG] Filtered out 16 INFO lines (noise)
[LOG] Found 24 threat lines

Threat Summary
  CRITICAL        : 6
  ERROR           : 12
  FAILED LOGIN    : 6

Suspicious IPs
  192.168.1.45   — 4 attempt(s)
  10.0.0.88      — 2 attempt(s)
```

## Concepts used

File I/O, re module (regex for IP detection), Counter, dictionaries, os module, f-strings, datetime
