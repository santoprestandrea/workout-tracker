# Workout Tracker App

Exam project for 'Object Oriented Programming' course - Epicode - 14/06/2025 by Santo Prestandrea

---

## Project Description

Workout Tracker App is a Java desktop application that allows users to create, manage, and analyze their workouts, sessions, and training programs.  
It is designed with extensibility, maintainability, and usability in mind, leveraging several object-oriented design patterns and a clear MVC architecture.

**Main features:**

- Create and manage strength and cardio workouts
- Organize workouts into sessions and programs
- Save and load all data in JSON format
- View statistics and summaries of your training
- User-friendly graphical interface (Java Swing)
- Robust error handling and logging

---

## Project Organization

The project is structured using Maven, which organizes the codebase into two main sections:

- **Source code** (`src` directory): Contains the main application logic.
- **Test code** (`test` directory): Contains unit tests.

Within the source code, the Model-View-Controller (MVC) architectural pattern is adopted to separate concerns and improve maintainability. The organization is as follows:

- `model` package: Defines the core data structures and business logic.
- `view` package: Handles user interface components and presentation logic.
- `control` package: Manages the flow of data between the model and the view, implementing the application's control logic.

---

## Core of the Business Logic

The core business logic of the Workout Tracker App is encapsulated within the `model` package. This package defines the essential abstractions and concrete implementations for representing and managing workouts.

### 1. Workout Interface

The `Workout` interface establishes a contract for all workout types. It defines the common properties and behaviors that every workout must implement, such as:

- Retrieving workout details (name, duration)
- Displaying workout summaries

This abstraction enables the application to handle different workout types in a uniform way.

### 2. StrengthWorkout Class

The `StrengthWorkout` class implements the `Workout` interface and represents a strength-based workout session. Key features include:

- String name (e.g. "Bench", "Squat", etc)
- Integer durations (in minutes)
- Integer attributes for sets, repetitions

This class encapsulates all logic specific to strength training, ensuring accurate tracking and reporting.

### 3. CardioWorkout Class

The `CardioWorkout` class also implements the `Workout` interface and models a cardio workout. Its main characteristics are:

- A name specifying the type of activity and other details

This class provides the necessary logic for managing and analyzing cardio workouts.

---

By organizing the business logic in this way, the application achieves high cohesion and clear separation of responsibilities, making it easy to extend with new workout types or features in the future.

---

## Design Patterns

Implemented the following design patterns:

### Factory Pattern

To promote flexibility and scalability in the creation of workout objects, the Factory Pattern has been applied within the `model.factory` package. This approach encapsulates the instantiation logic for different types of workouts, allowing the application to create new workout objects without exposing the creation logic to the client code.

The following classes are involved:

- **WorkoutCreator.java**:  
    This is an abstract creator class that defines the factory method `createWorkout()`. It provides a common interface for all concrete workout creators.

- **StrengthWorkoutCreator.java**:  
    This class extends `WorkoutCreator` and implements the `createWorkout()` method to instantiate and return `StrengthWorkout` objects. It encapsulates all the details required to create a strength workout.

- **CardioWorkoutCreator.java**:  
    Similarly, this class extends `WorkoutCreator` and implements the `createWorkout()` method to instantiate and return `CardioWorkout` objects. It encapsulates the creation logic specific to cardio workouts.

By using the Factory Pattern, the application can easily be extended to support new workout types in the future by simply adding new creator classes, without modifying existing code. This design choice enhances maintainability and adheres to the Open/Closed Principle.

### Exception Shielding Pattern

To ensure robustness and prevent internal implementation details from leaking outside the core logic, the Exception Shielding Pattern has been applied throughout the application, especially in the model and utility layers.

How it is implemented:

- Through created custom exception `InvalidWorkoutDataCreation`.

### Composite Pattern

The Composite Pattern is used in the application to allow both individual workouts and groups of workouts (sessions and programs) to be treated uniformly.

**How it is implemented:**

- The core interface `Workout` defines the common operations for all workout-related entities, such as retrieving the name, duration, and details.
- The classes `StrengthWorkout` and `CardioWorkout` are *leaf* components that represent individual workouts.
- The classes `WorkoutSession` and `WorkoutProgram` are *composite* components:
  - `WorkoutSession` can contain multiple `Workout` objects (either leaf or composite), allowing a session to represent a collection of workouts.
  - `WorkoutProgram` can contain both individual workouts and entire sessions, enabling the creation of complex training programs.
- Both `WorkoutSession` and `WorkoutProgram` implement the `Workout` interface, so they can be used wherever a single workout is expected.
- The composite classes provide methods to add, remove, and iterate over their child components, and aggregate information such as total duration.

**Benefits:**

- Enables the application to treat individual workouts, sessions, and programs in a uniform way.
- Facilitates the creation of complex workout structures (e.g., a program made of sessions, each containing multiple workouts).
- Makes the codebase more extensible and maintainable.

### Iterator Pattern

The Iterator Pattern is used in the application to provide a uniform way to traverse collections of workouts, sessions, and programs without exposing their underlying representations.

**How it is implemented:**

- The `WorkoutIterator` interface defines the standard operations for iteration: `hasNext()`, `next()`, `isEmpty()`, and `reset()`.
- The `WorkoutCollection` interface is implemented by classes that can be iterated (such as `WorkoutSession` and `WorkoutProgram`). It provides a `createIterator()` method.
- Concrete iterator classes, such as `WorkoutSessionIterator` and `WorkoutProgramIterator`, implement the `WorkoutIterator` interface and encapsulate the logic for traversing the respective collections.
- This design allows the application to iterate over workouts in a session or program in a consistent and decoupled manner, regardless of the internal data structure.

**Benefits:**

- Decouples the traversal logic from the collection classes.
- Makes it easy to add new types of collections or change internal representations without affecting client code.
- Supports uniform access to elements in both simple and composite workout structures.

**Example:**

```java
WorkoutSession session = new WorkoutSession("Sessione A");
// ...aggiungi workout alla sessione...
WorkoutIterator iterator = session.createIterator();
while (iterator.hasNext()) {
    Workout w = iterator.next();
    System.out.println(w.getName());
}
```

### Singleton Pattern

The Singleton Pattern is used in the application to ensure that certain core components have only one instance throughout the application's lifecycle.

**How it is implemented:**

- The `WorkoutManager` class is implemented as a singleton. It provides a static `getInstance()` method that returns the single, shared instance of the manager.
- The constructor of `WorkoutManager` is private, preventing direct instantiation from outside the class.
- This pattern ensures that all parts of the application interact with the same instance of `WorkoutManager`, maintaining a consistent state for workouts, sessions, and programs.
- The `LoggerManager` class is also implemented as a singleton to provide a single, centralized logger instance for the entire application.

**Benefits:**

- Guarantees a single point of access and control for core resources (such as the workout data and logging).
- Prevents issues related to inconsistent state or duplicated data.
- Simplifies resource management and coordination across the application.

---

### Data Transfer Object (DTO) Pattern

The Data Transfer Object Pattern is used in the application to efficiently transfer groups of related data between different layers of the system, especially when saving to or loading from files.

**How it is implemented:**

- The class `WorkoutFileHandler` defines a static inner class called `WorkoutData`, which acts as a DTO.
- `WorkoutData` contains three public lists: `workouts`, `sessions`, and `programs`. These lists hold all the data needed to represent the application's state.
- When saving or loading data, the entire state (all workouts, sessions, and programs) is encapsulated in a single `WorkoutData` object, which is then passed between the file handler and the rest of the application.
- The DTO does not contain any business logic; it is used purely for data aggregation and transfer.

**Benefits:**

- Simplifies the process of saving and loading complex data structures by grouping related data into a single object.
- Reduces the number of method parameters and improves code readability.
- Decouples the data transfer process from the business logic, making the codebase easier to maintain and extend.

---

---

## Logging

The application uses a centralized logging system to record important events, errors, and user actions.  
Logging is managed by the `LoggerManager` singleton class, which configures a Java `Logger` to write all log messages to a file (`workout_tracker.log`) in the project directory.

**Key points:**

- **Singleton Pattern:**  
  `LoggerManager` ensures that only one logger instance is used throughout the application, avoiding duplicate or inconsistent logs.

- **File Logging:**  
  All logs are written to `workout_tracker.log`, making it easy to review the application's activity and diagnose issues.

- **Usage:**  
  Any class can obtain the logger by calling `LoggerManager.getLogger()` and use standard Java logging methods (`info`, `warning`, `severe`, etc.).

- **Configuration:**  
  The logger is configured to:
  - Write logs in append mode (old logs are preserved).
  - Use a simple, readable format.
  - Disable console logging to avoid duplicate messages.

**Example:**

```java
private static final Logger LOGGER = LoggerManager.getLogger();

LOGGER.info("Workout created successfully.");
LOGGER.warning("Invalid workout data provided.");
LOGGER.severe("Failed to save workouts: " + e.getMessage());
```

---

---

## File Management and Data Persistence

All workout data (workouts, sessions, and programs) is persisted in a single JSON file.  
The file management and data serialization/deserialization are handled by the `WorkoutFileHandler` utility class.

**Key points:**

- **Unified Data File:**  
  All user data is saved and loaded from a single JSON file (e.g., `workouts.json`). This file contains three main arrays: `workouts`, `sessions`, and `programs`.

- **JSON Structure Example:**

  ```json
  {
    "workouts": [
      { "name": "Bench Press", "duration": 45, "type": "strength", "sets": 4, "reps": 10 },
      { "name": "Running", "duration": 30, "type": "cardio", "distanceKm": 5.5 }
    ],
    "sessions": [
      {
        "name": "Morning Routine",
        "type": "session",
        "workouts": [
          { "name": "Bench Press", "duration": 45, "type": "strength", "sets": 4, "reps": 10 }
        ]
      }
    ],
    "programs": [
      {
        "name": "Full Body Program",
        "schedule": [
          { "type": "session_ref", "name": "Morning Routine" },
          { "name": "Running", "duration": 30, "type": "cardio", "distanceKm": 5.5 }
        ]
      }
    ]
  }



**Saving Data:**  
Use the "Save" or "Save As" options in the File menu to write all current data to a JSON file.  
The `WorkoutFileHandler.saveWorkouts()` method serializes all workouts, sessions, and programs and writes them to the chosen file.

**Loading Data:**  
Use the "Open" option in the File menu to load data from an existing JSON file.  
The `WorkoutFileHandler.loadWorkouts()` method reads the file, parses the JSON, and reconstructs all workouts, sessions, and programs in the application.

**Session and Program References:**  
Programs can reference sessions by name (using `"type": "session_ref"`), allowing for flexible and reusable training structures.

**Manual Editing:**  
The JSON file can be edited manually if needed, but it is recommended to use the application interface to avoid data inconsistencies.

**Benefits:**

- Ensures all user data is portable and easy to back up or share.
- Makes it simple to migrate data between different installations or users.
- Facilitates debugging and data recovery thanks to the human-readable JSON format.
