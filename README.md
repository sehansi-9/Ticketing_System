# Ticketing System

## Introduction

The **Ticketing System** is a multi-component application designed to simulate and manage ticket pools for events. It consists of the following components:

1. **Java CLI**: Used for bulk entry of customer and vendor details. Simulates multi-threaded ticket system after
2. **Spring Boot Backend**: Manages the ticket pool simulation logic and handles requests.
3. **Angular Frontend**: Provides a user interface for interacting with the system, allows addition of one customer/vendor per time and run the simulation, reflecting a real-world scenario

This system simulates a multi-threaded synchronized environment where customers and vendors can buy and release tickets. It includes functionality for bulk entry, real-time updates via WebSockets, and dynamic simulation based on real-world scenarios. Additionally, transaction logs of ticket buying and releasing actions are saved to text files for debugging and monitoring purposes.

---

## Setup Instructions

### Prerequisites

- **Java 17** (or newer)
- **Node.js** (version 16 or newer)
- **Maven** (for building the Spring Boot backend)
- **Angular CLI** (for running the Angular frontend)

### How to Build and Run the Application

#### Java CLI (Configuration Steps)

1. Clone the repository:
   ```bash
   git clone https://github.com/sehansi-9/Ticketing_System.git
   cd Ticketing_System/w2052049_cli_sehansi
   
2. Run the CLI:
   ```bash
   java -jar ticketing-cli.jar
or use an IDE - (Intellij, eclipse etc..)

The CLI will prompt you to enter:

- Ticket pool details (name, max capacity, release rate, retrieval rate)
- Customer and vendor names and the number of tickets they wish to buy/release (in bulk). - wll be saved to config.json file for later usage for the webapp
- Check the logs.txt file at /src directory for transaction logs
  
3. Check config.json file for the availability of the entered data of vendors and customers
   
#### Running the WebApp

1. Navigate to springboot directory and install dependencies
   ```bash
   cd Ticketing_System/ticketing application
   mvn install
   mvn spring-boot:run
   
2. Navigate to frontend directory and install dependencies
   ```bash
   cd Ticketing_System/ticketing_angular
   npm install
   ng serve

Now, you can interact with the system via the frontend, which communicates with the backend to add a new customer/vendor and start the multi-threaded environment.

### Explanation of UI

- Pool details: The web page displays the ticket pool configurations including name, maximum capacity, customer buying rate and vendor releasing rate
- Form: To eEnter a customer/vendor name and number of tickets to buy/release
- Start: Initiates the ticket pool.
- Stop: Halts the simulation and interrupts all active threads.
- Logs: View real-time updates from the system via WebSockets.

### Multi-threaded synchronized system simulation
- When the process starts from the frontend, the backend will fetch the customer and vendor details (previously entered through the CLI) and simulate the ticket buying/releasing process.
- WebSockets will send logs to the frontend in real-time, displaying the actions performed by customers and vendors.
- The ticket transactions (ticket buying and releasing actions and details) are logged in text files along with the timestamp for both the Java CLI and Spring Boot backend. These logs are saved as text files in the root directories of both the Java CLI and Spring Boot applications

### Troubleshooting

- WebSocket Issues: If WebSocket connections aren't working, ensure the backend and frontend are running on the correct ports and can communicate with each other.
- Configuration File: Ensure the config.json file exists in the correct file path and contains valid data. This file is critical for the backend simulation.
- Dependencies: If you encounter issues with dependencies, make sure you have the correct versions of Java, Node.js, and Maven installed.
   
