# Smart traffic lights simulation

Simulation of an intelligent traffic light system at a four-way intersection.
The system dynamically adjusts traffic light cycles based on vehicle flow, ensuring safe and efficient traffic management.

The project supports two execution modes:
1. Terminal (file-based) mode
2. Web application mode

## Requirements

#### Terminal mode

*   **Java Development Kit (JDK) 25**
The project was developed and built using JDK 25. Please ensure your environment is compatible.

#### Web Mode

##### Backend
* The same backend application is used for both terminal and web modes.

##### Frontend
* **Node.js (v18 or newer recommended)**
  Required to run the React frontend application.
* **npm** (comes with Node.js)
  Used to install frontend dependencies and start the development server.

  

## Installation
Clone the repository:
```
git clone https://github.com/Bartol0220/smart-traffic-lights-simulation.git
cd smart-traffic-lights-simulation
```

## Usage

### Terminal application
To run the simulation in terminal mode, use the Gradle wrapper with the input and output file paths as arguments:
```
./gradlew run --args="<input path> <output path>"
```

If no arguments are provided, the simulation will run using default input and output files.

Example:
```bash
./gradlew run --args="example/inputExample.json example/outputExample.json"
```

### Web application

To run the web application:

1. Start the backend from the `/backend` directory:
```bash
./gradlew bootRun
```
2. Start the frontend from the `/frontend` directory:
```bash
npm start
```

Open [http://localhost:3000](http://localhost:3000) to view app in your browser.


## Data Formats

### Input Format (`.json`)

The input file defines a list of simulation commands to be executed sequentially.

**Structure:**
 - `commands`: An array of objects representing actions.
   - `addVehicle`: Adds a vehicle to the specified start road with a target end road.
     - `vehicleId`: Unique identifier for the vehicle (String).
     - `startRoad`: The road the vehicle is arriving from. Valid values: `"north"`, `"south"`, `"east"`, `"west"`.
     - `endRoad`: The destination road. Valid values: `"north"`, `"south"`, `"east"`, `"west"`.
   - `step`:  Advances the simulation by one discrete time unit.

**Input example:**
```json
{
  "commands": [
    {
      "type": "addVehicle",
      "vehicleId": "vehicle1",
      "startRoad": "south",
      "endRoad": "north"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle2",
      "startRoad": "north",
      "endRoad": "south"
    },
    {
      "type": "step"
    },
    {
      "type": "step"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle3",
      "startRoad": "west",
      "endRoad": "south"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle4",
      "startRoad": "west",
      "endRoad": "south"
    },
    {
      "type": "step"
    },
    {
      "type": "step"
    }
  ]
}
```

### Output Format (`.json`)

The output file records the state of the simulation after every step.

**Structure:**
- `stepStatuses`: An array of objects, where each object represents the outcome of a single simulation step.
  - `leftVehicles`: A list of IDs for vehicles that have left the intersection during that specific step.

**Output example:**
```json
{
  "stepStatuses" : [ {
    "leftVehicles" : [ "vehicle2", "vehicle1" ]
  }, {
    "leftVehicles" : [ ]
  }, {
    "leftVehicles" : [ "vehicle3" ]
  }, {
    "leftVehicles" : [ "vehicle4" ]
  } ]
}
```

## Traffic Light Algorithms

The simulation logic is modular. Before the simulation starts, the system pre-calculates the minimum number of phases
required to maximize traffic flow. The algorithm groups non-conflicting lanes (lanes that can have a green light simultaneously)
into phases. It attempts to add as many lanes as possible to a single phase, starting with the highest priority lanes.
This allows the light controller to simply select which pre-calculated phase to activate.

### Time light controller

A basic, fixed-time controller commonly found on city streets. It cycles through the pre-defined phases in a round,
changing the active phase every fixed number of steps.

### Vehicles priority light controller

More complicated controller that calculates priority for every lane. The priority is calculated using the following formula:
```math
P(l) = V_p(l) \cdot max(T(l) \cdot 0.5, 1)
```
Where:
- $P(l)$ is final priority of lane $l$,
- $V_p(l)$ is sum of the priority values of all vehicles currently waiting in lane $l$,
- $T(l)$ is the waiting time (number of steps the lane has had a red light).

A maximum wait time limit can be applied. If a lane's red light duration exceeds this limit, the lane automatically receives
maximum priority to prevent starvation.

### Lane priority light controller

Similar to the Vehicle Priority controller but incorporates a static weight for specific traffic lanes (e.g., to prioritize main roads over side streets).
The priority is calculated as:
```math
P(l) = (V_p(l) + L_p(l)) \cdot max(T(l) \cdot 0.5, 1)
```
Where:
- $P(l)$ is final priority of lane $l$,
- $V_p(l)$ is sum of the priority values of all vehicles currently waiting in lane $l$,
- $L_p(l)$  is the static priority assigned to lane $l$,
- $T(l)$ is the waiting time (number of steps the lane has had a red light).

Like the previous controller, a maximum wait time mechanism ensures that side roads eventually get a green light.

### Vehicles priority light controller

This controller calculates the priority of each traffic light phase. The priority is determined based on the priority of the lanes included in the phase, as well as the priority of the vehicles on each lane.

### Emergency vehicles light controller

This controller acts as a decorator for any other light controller. It monitors incoming traffic for emergency vehicles.
When an emergency vehicle is detected, this controller overrides the standard logic and forces a green light on the
relevant lane until the vehicle has passed the intersection.

## Testing

The project unit tests were created to ensure the reliability of traffic light algorithms, simulation logic, and file handling mechanisms.

### Running Tests
To execute all backend unit tests, use the following command:

```bash
./gradlew test
```
