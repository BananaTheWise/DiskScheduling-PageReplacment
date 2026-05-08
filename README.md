# Disk Scheduling and Page Replacement Project

This project implements disk scheduling algorithms (FCFS, SSTF, SCAN, LOOK, CSCAN, CLOOK) and page replacement algorithms (FIFO, LRU, Optimal, Second Chance) with a graphical user interface. It also includes comparative graphs and performance metric tables for detailed analysis.

## Requirements

- Java JDK 11 or higher
- Swing (included with JDK)
- (See `requirements.txt` for additional details)

## Installation

### Linux

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-11-jdk

# CentOS/RHEL/Fedora
sudo yum install java-11-openjdk-devel  # CentOS/RHEL
sudo dnf install java-11-openjdk-devel  # Fedora
```

### Windows

```cmd
# Using Chocolatey (recommended)
choco install openjdk11

# Or download from Oracle website:
# 1. Go to https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
# 2. Download and install JDK 11 for Windows
# 3. Add to PATH: set PATH=%PATH%;C:\Program Files\Java\jdk-11\bin
```

### macOS

```bash
# Using Homebrew
brew install openjdk@11

# Or download from Oracle website:
# 1. Go to https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
# 2. Download and install JDK 11 for macOS
```

## Running the Project

1. Compile the project:
```bash
cd /path/to/OSProject
javac -d bin algorithms/PageReplacement/src/os/project/*.java algorithms/DiskScheduling/src/disk/scheduling/*.java gui/*.java
```

2. Run the GUI:
```bash
java -cp bin gui.Main
```

## Project Structure

```
OSProject/
├── algorithms/
│   ├── PageReplacement/     # Page replacement algorithms
│   └── DiskScheduling/      # Disk scheduling algorithms
├── gui/                     # Graphical user interface
└── README.md               # This file
```