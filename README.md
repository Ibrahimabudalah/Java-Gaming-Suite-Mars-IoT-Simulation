# Java Gaming Suite & Mars IoT Simulation

A multi-module desktop application developed in Java 21 showcasing Object-Oriented Software Design, event-driven JavaFX graphical user interfaces, and automated unit testing.

## Features

- **Mars IoT Mission Control Simulation**:
    - Models a multi-node IoT sensor array (temperature, humidity, camera) inspired by ESP microcontroller networks.
    - Implements energy budgeting, battery degradation, solar recharge mechanics, and stochastic planetary events (dust storms, radiation spikes, solar flares).
    - Generates post-mission telemetry and performance metrics saved to disk.
- **Interactive JavaFX Number Game**:
    - Event-driven 4x5 grid game requiring strategic placement of randomized numbers in ascending order.
    - Built using JavaFX controls, custom components (`NumberSquare`), and styled with modern CSS.
    - Tracks running placement averages, win-loss ratios, and displays custom modal dialogs.
- **Geography Trivia Engine**:
    - Terminal-based geography challenge with multi-tier scoring (awarding higher weight for first-attempt accuracy).
    - File-based leaderboard tracking and statistical analytics.
- **Automated Testing Suite**:
    - Full test coverage of core domain logic and edge cases using JUnit 5.

## Architecture & Design Patterns
- **Inheritance & Polymorphism**: Abstract base `Sensor` class with specialized subclasses adhering to the Liskov Substitution Principle.
- **Command & State Patterns**: Sensor management via `PowerPolicy` states and decoupled command execution.
- **Clean Architecture**: Clear separation of concerns between game logic, UI scene graph, and persistent storage.