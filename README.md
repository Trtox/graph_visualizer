# Graph Visualizer

## General

This project is a Kotlin-based GUI application for visualizing and interacting with directed graphs using JavaFX. Users can define graphs through a text input area where edges are specified, and toggle vertex states (enabled/disabled). The diagram updates automatically in real-time, ensuring an interactive and responsive experience even with large graphs.

## Requirements

Before running the project, make sure you have the following installed:

1. **Java Development Kit (JDK)**:
    - JDK 17 or newer is required.
    - You can check if you have it installed by running:
      ```bash
      java -version
      ```

2. **Maven**:
    - Maven is required for building the project. Install Maven by following the [official installation guide](https://maven.apache.org/install.html).
    - You can verify Maven installation by running:
      ```bash
      mvn -v
      ```

## Setup

1. **Clone the repository**:
   ```bash
   git clone git@github.com:Trtox/graph_visualizer.git
   cd graph-visualizer
   ```

2. **Build and run the project**:
   ```bash
   ./build.sh
   ```

## Documentation

This project uses Dokka for generating documentation. You can build and run the documentation by running the following command:

   ```bash
   ./build_docs.sh
   ```

By default, the ```build_docs.sh``` will open the ```target/dokka/index.html``` using default web browser.
   
