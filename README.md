# HireFlow

**HireFlow** is a Java-based application manager designed to streamline the application lifecycle. Built with a focus on efficiency and flexibility, HireFlow allows users to manage applications in various states, offering robust functionality for academic or professional application tracking.

## Features
- **Application State Management**: Track applications across four states: Review, Closed, Offer, and Waitlist.
- **State Pattern**: Utilizes the State Pattern to manage transitions between application states dynamically.
- **Data Persistence**: Load applications from a text file and save them back for seamless data handling.
- **User-Friendly Interface**: Built using Java AWT and Swing libraries for an interactive and responsive user interface.

## Technologies Used
- **Programming Language**: Java
- **Libraries**: AWT, Swing
- **Design Patterns**: State Pattern
- **Data Structures**: List

## How to Use
1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/CourseMate.git
   ```

2. **Compile the Code**
   Navigate to the project directory and compile the source files:
   ```bash
   javac -d bin Project1/src/edu/ncsu/csc216/app_manager/*.java
   ```

3. **Run the Application**
   Execute the main class to launch the application:
   ```bash
   java -cp bin Project1/src/edu/ncsu/csc216/app_manager/view/ui/AppManagerGUI.java
   ```

4. **Load Data**
   Use the file menu to load existing data from a text file.

5. **Save Data**
   Save your changes and course allocations to a text file for future use.

