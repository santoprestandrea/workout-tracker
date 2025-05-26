# Workout Tracker App

Exam project for 'Object Oriented Programming' course - Epicode - 14/06/2025 by Santo Prestandrea

## Project Description

TODO

## Project Organization

The project is structured using Maven, which organizes the codebase into two main sections:

- **Source code** (`src` directory): Contains the main application logic.
- **Test code** (`test` directory): Contains unit tests.

Within the source code, the Model-View-Controller (MVC) architectural pattern is adopted to separate concerns and improve maintainability. The organization is as follows:

- `model` package: Defines the core data structures and business logic.
- `view` package: Handles user interface components and presentation logic.
- `control` package: Manages the flow of data between the model and the view, implementing the application's control logic.

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

## Meeting of project criteria and specifications

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