# Skills Matrix: Terminal ASCII Shark (Jetpack Compose Edition)

This document outlines the core technical competencies, theoretical knowledge, and Jetpack Compose-specific skills required to build the idle ASCII shark application. By completing this project, a developer will demonstrate proficiency in the following areas:

## 1. Jetpack Compose Fundamentals
Building a dynamic UI in Compose requires shifting from the imperative XML model to a declarative, state-driven paradigm.

* **Declarative UI Paradigm:** Understanding how to describe the UI state at any given moment and letting the Compose compiler handle the UI updates (recomposition) when the underlying data changes.
* **Layouts and Scopes:** 
    * Mastery of `BoxWithConstraints`, a crucial layout in this project used to read the exact screen dimensions (`maxWidth`, `maxHeight`) dynamically. This forms the mathematical boundaries of the "tank."
* **Modifiers:** Utilizing `Modifier.offset` to translate the shark's logical coordinates (X, Y) into actual on-screen rendering positions without triggering heavy layout recalculations.
* **Theming and Typography:** Emulating a retro CRT terminal by utilizing pure color styling (`Color.Black`, `Color.Green`) and manipulating `TextStyle` to enforce a `FontFamily.Monospace` typeface.

## 2. Advanced State Management
The "brain" of the shark relies entirely on Compose's state tracking.

* **State Hoisting & Persistence:** Using `remember { mutableStateOf(...) }` (or `mutableFloatStateOf`) to retain the shark's X/Y coordinates and velocity across recompositions.
* **Derived State Logic:** Understanding how the visual representation of the shark (facing left vs. right) is derived directly from the current velocity state (`velocityX`).

## 3. Game Loop and Animation Mechanics
Unlike standard static apps, this project requires a continuous loop running at 60+ frames per second (FPS).

* **Lifecycle-Aware Coroutines:** Using `LaunchedEffect(Unit)` to safely kick off a continuous loop that starts when the shark appears and is automatically canceled when the app goes into the background or the view is destroyed.
* **Frame Synchronization:** Moving away from rudimentary `Thread.sleep` or Android `Handler` loops. Implementing `withFrameNanos` or `withFrameMillis` to tie the shark's movement calculations directly to the device's screen refresh rate, ensuring perfectly smooth rendering.

## 4. 2D Coordinate Physics and Mathematics
The underlying rules that keep the shark swimming realistically within the constraints.

* **Vector Movement:** Calculating frame-by-frame movement by applying velocity vectors to current positions ($X_{new} = X_{current} + v_X$).
* **Collision Detection (AABB):** Implementing basic Axis-Aligned Bounding Box (AABB) collision checks. The logic must account for both the screen boundaries and the dimensions (width/height) of the text element itself so the shark doesn't clip through the walls before bouncing.
* **Directional Reversal:** Modifying state by multiplying velocity by $-1$ to simulate a physical "bounce" upon hitting a constraint constraint.

## 5. Kotlin Language Proficiency
* **Immutability vs. Mutability:** Deciding which variables (like the ASCII strings) should be immutable `val` and which (like coordinates) must be mutable `var`.
* **Float Manipulation:** Handling precise floating-point mathematics for fluid pixel movement instead of rigid integer grids.
* **Scope Functions:** Utilizing Kotlin standard library functions (like `let`, `apply`, or `coerceIn`) to elegantly clamp the shark's coordinates within the safe zones if it accidentally breaches a boundary.
