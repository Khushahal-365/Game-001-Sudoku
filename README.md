# Sudoku: Daily Logic — Native Android Edition

**Sudoku: Daily Logic** is a polished, native Android Sudoku game built with Kotlin, Jetpack Compose, and Material Design 3.

## Game Modes

### 1. Classic Sudoku
- Standard 9×9 grid with rows, columns, and 3×3 blocks containing numbers 1–9.
- 4 difficulty levels: Easy (36–40 clues), Medium (30–35 clues), Hard (26–29 clues), and Expert (22–25 clues).
- Daily Challenges with deterministic generation and calendar streak tracking.

### 2. Killer Sudoku (Sumdoku / Addoku)
- 9×9 grid combining classic Sudoku constraints with arithmetic **cages**.
- **Cages**: Groups of contiguous cells bounded by **dashed line outlines**.
- **Target Sum**: Displayed as a small number in the upper-left corner of each cage.
- **The Sum Rule**: Numbers within each cage must add up to the cage's target sum.
- **No Duplicates in Cages**: Numbers cannot repeat within a cage, row, column, or 3×3 block.
- **Dynamic Cage Feedback**: Real-time cage sum counter (`Cage Sum: X / Target`), cage selection highlights, and conflict detection when sums exceed the target.
- 4 difficulty levels: Easy, Medium, Hard, and Expert (pure "45 rule" deductions).

## Features
- Interactive 9×9 grid with 3×3 block borders, cell highlighting, matching digit highlights, and dual input (direct digits + pencil notes).
- Full Undo/Redo stack, Erase, Notes toggle, and intelligent Hint system.
- Daily Challenge calendar card with 3D trophy illustration, monthly completion progress, and day selection.
- Comprehensive statistics, 10 local achievements, and 5 curated themes (System, Light, Dark, Parchment, Midnight).
