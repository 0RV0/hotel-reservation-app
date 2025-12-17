# 🏨 Hotel Reservation Application

A command-line based Hotel Reservation Application built in **Java**.  
This project demonstrates strong **Object-Oriented Programming (OOP)** principles, layered application architecture, and effective use of **Java collections** to manage hotel room bookings.

---

## 📌 Overview

The Hotel Reservation Application allows customers to:
- Search for available hotel rooms
- Book rooms based on availability and dates
- View reservations

Administrators can:
- View all customers
- View all rooms
- View all reservations
- Add new rooms to the system

The application runs entirely in the **Command Line Interface (CLI)** and stores data **in memory** using Java collections.

---

## 🧱 Application Architecture

The application follows a **layered architecture** to ensure modularity and loose coupling:

UI (CLI Menus)
 → 
Resources (API layer)
 → 
Services (Business Logic)
 → 
Models (Domain Objects)

### 🔹 Layers Explained
- **UI Layer**:  
  Handles user interaction via CLI menus (Main Menu and Admin Menu).
- **Resource Layer**:  
  Acts as an API between UI and services.
- **Service Layer**:  
  Contains business logic and manages data processing.
- **Model Layer**:  
  Represents domain entities such as `Room`, `Customer`, and `Reservation`.

This separation allows the UI to be replaced (e.g., with a web UI) without changing the business logic.

---

## ✨ Features

### User Features
- Search for available rooms by date
- Book rooms
- View existing reservations
- Receive room recommendations if no rooms are available

### Admin Features
- View all customers
- View all rooms
- View all reservations
- Add new rooms via CLI input

---

## 🛠️ Technologies & Concepts Used

### Object-Oriented Programming
- **Interfaces & Polymorphism**
  - `IRoom` interface implemented by `Room`
  - `FreeRoom` extends `Room`
- **Encapsulation**
  - Private fields with public getters/setters
- **Inheritance**
- **Method Overriding**
  - Custom `toString()` implementations
- **Access Modifiers**
  - `public`, `private`, and `final`

### Core Java Concepts
- Java Collections (`Map`, `Set`, `List`)
- Singleton pattern using `static` references in service classes
- Enums (`RoomType`)
- Exception handling with `try-catch`
- Regular Expressions for email validation
- `Date` and `Calendar` for reservation dates
- `switch` statements for menu-driven CLI navigation

---

## 📂 Data Storage & Processing

- Rooms, customers, and reservations are stored using **Java collections**
- The system prevents:
  - Double-booking the same room
  - Booking the same room for overlapping date ranges
- Iteration with `for` and `while` loops to:
  - Search for available rooms
  - Generate room recommendations

---

## ▶️ How to Run the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/0RV0/hotel-reservation-app.git

2. Open the project in IntelliJ IDEA

3. Locate and run the MainMenu (or main class)

4. Interact with the application through the CLI

## 🔮 Future Improvements

* Add persistent storage (database)

* Replace CLI with a web-based UI

* Improve room recommendation logic

* Add user authentication

* Add automated unit tests
