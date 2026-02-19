import { useState, useEffect, useMemo } from 'react';
import './App.css';

import imgGreen from './assets/tl_Green.png';
import imgYellow from './assets/tl_Yellow.png';
import imgRed from './assets/tl_Red.png';
import imgRedYellow from './assets/tl_Red_Yellow.png';

import imgArrowL from './assets/arrow_L.png';
import imgArrowR from './assets/arrow_R.png';
import imgArrowS from './assets/arrow_S.png';
import imgArrowU from './assets/arrow_U.png';
import imgArrowUW from './assets/arrow_UW.png';

const LANE_WIDTH = 50;
const MAX_ROAD_LENGTH = 430;
let roadLength = MAX_ROAD_LENGTH;

const getTrafficLightImage = (color) => {
  if (!color) return imgRed;
  switch (color.toLowerCase()) {
    case 'green': return imgGreen;
    case 'yellow': return imgYellow;
    case 'red': return imgRed;
    case 'red_yellow': return imgRedYellow;
    default: return imgRed;
  }
};

const Vehicle = ({ color = "blue", position = 0 }) => {
  return (
    <g 
      style={{ 
        transform: `translate(${LANE_WIDTH / 2}px, ${position}px)`,
        transition: 'transform 0.5s ease-in-out' 
      }}
    > 
      <rect 
        className="vehicle-shape"
        x="-8" y="-16" 
        width="16" height="32" 
        fill={color} 
        stroke="white" 
        rx="3"
      />
    </g>
  );
};

const TrafficLane = ({ laneData, type, currentRoadLength }) => {
  const isIncoming = type === 'incoming';
  const trafficLightMargin = 10;

  const VEHICLE_GAP = 40; // vehicle + gap
  const START_OFFSET = 52;
  const maxCapacity = Math.floor((currentRoadLength - START_OFFSET) / VEHICLE_GAP);

  let visibleVehicles = [];
  let overflowCount = 0;

  if (isIncoming && laneData.vehicles) {
    visibleVehicles = laneData.vehicles.slice(0, maxCapacity);
    overflowCount = laneData.vehicles.length - visibleVehicles.length;
  }
  
  const lightImg = isIncoming 
    ? getTrafficLightImage(laneData.trafficLight?.color) 
    : null;

  const renderArrows = () => {
    if (!laneData.laneType) return null;

    const isOnlyOne = laneData.laneType.length === 1;

    return laneData.laneType.map((type, index) => {
      let arrowSrc = null;

      if (type === 'L') arrowSrc = imgArrowL;
      else if (type === 'R') arrowSrc = imgArrowR;
      else if (type === 'S') arrowSrc = imgArrowS;
      else if (type === 'U') arrowSrc = isOnlyOne ? imgArrowU : imgArrowUW;

      if (!arrowSrc) return null;

      return (
        <image
          key={index}
          href={arrowSrc}
          x={(LANE_WIDTH - 30) / 2}
          y={6}
          width="30"
          height="30"
        />
      );
    });
  };

  return (
    <g>
      <rect 
        x="0" y="0" 
        width={LANE_WIDTH} height={currentRoadLength} 
        fill="#535353" 
        stroke="white" 
        strokeWidth="1" 
        strokeDasharray="0"  
        fillOpacity={1}
      />

      {isIncoming && (
        <>
          {/* Stop line */}
          <line x1="4" y1="4" x2={LANE_WIDTH-4} y2="4" stroke="white" strokeWidth="4" />
          
          {/* Traffic light IMAGE */}
          <image 
            href={lightImg} 
            x={trafficLightMargin} 
            y={2*trafficLightMargin - LANE_WIDTH - 2} 
            width={LANE_WIDTH - 2*trafficLightMargin}
            height={LANE_WIDTH - 2*trafficLightMargin}
          />
          
          {renderArrows()}

          {/* Vehicles */}
          {visibleVehicles.map((veh, i) => (
            <Vehicle 
                key={veh.id || i} 
                position={START_OFFSET + (i * VEHICLE_GAP)} 
                color={veh.type === 'AMBULANCE' ? 'white' : 'orange'} 
            />
          ))}

          {/* overflow count */}
          {overflowCount > 0 && (
            <g transform={`translate(${LANE_WIDTH/2}, ${currentRoadLength - 20})`}>
               <circle r="12" fill="red" stroke="white" />
               <text 
                 y="4" 
                 fill="white" 
                 textAnchor="middle" 
                 fontSize="12" 
                 fontWeight="bold"
               >
                 +{overflowCount}
               </text>
            </g>
          )}
        </>
      )}
    </g>
  );
};

const Road = ({ roadData, intersectionDims }) => {
  const incomingLanes = roadData.trafficLanes;
  const laneCount = incomingLanes.length;
  const outgoingLanesCount = laneCount; 
  
  let rotation = 0;
  let distFromCenter = 0;
  let currentRoadLength = roadLength;

  switch (roadData.entryDirection) {
    case 'North': 
      rotation = 180; 
      distFromCenter = intersectionDims.height / 2;
      currentRoadLength = roadLength;
      break;
    case 'South': 
      rotation = 0;   
      distFromCenter = intersectionDims.height / 2;
      currentRoadLength = roadLength;
      break;
    case 'East':  
      rotation = 270; 
      distFromCenter = intersectionDims.width / 2;
      currentRoadLength = MAX_ROAD_LENGTH;
      break;
    case 'West':  
      rotation = 90;  
      distFromCenter = intersectionDims.width / 2;
      currentRoadLength = MAX_ROAD_LENGTH;
      break;
  }

  return (
    <g transform={`rotate(${rotation})`}>
      <g transform={`translate(0, ${distFromCenter})`}>
        
        {/* Incoming lanes */}
        {incomingLanes.map((lane, idx) => (
          <g key={`in-${idx}`} transform={`translate(${idx * LANE_WIDTH}, 0)`}>
             <TrafficLane laneData={lane} type="incoming" currentRoadLength={currentRoadLength} />
          </g>
        ))}

        {/* Outgoing lanes */}
        {Array.from({ length: outgoingLanesCount }).map((_, idx) => (
          <g key={`out-${idx}`} transform={`translate(-${(idx + 1) * LANE_WIDTH}, 0)`}>
             <TrafficLane laneData={null} type="outgoing" currentRoadLength={currentRoadLength} />
          </g>
        ))}

        {/* Middle lines */}
        <line x1="0" y1="0" x2="0" y2={currentRoadLength} stroke="yellow" strokeWidth="2" />
        <line x1="-3" y1="0" x2="-3" y2={currentRoadLength} stroke="yellow" strokeWidth="2" />

      </g>
    </g>
  );
};

function App() {
  const [simulationState, setSimulationState] = useState(null);
  const [error, setError] = useState(null);

  const [vehIdCounter, setVehIdCounter] = useState(1);
  const [newVehType, setNewVehType] = useState('CAR');
  const [startRoad, setStartRoad] = useState('North');
  const [endRoad, setEndRoad] = useState('South');

  const fetchState = async () => {
    try {
      const response = await fetch('http://localhost:8080/simulation/state');
      if (!response.ok) throw new Error("Error during getting simulation state.");
      const data = await response.json();
      setSimulationState(data);
      setError(null);
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };

  useEffect(() => {
    fetchState();
  }, []);
  
  const handleStep = async () => {
    try {
      const response = await fetch('http://localhost:8080/simulation/step', { method: 'POST' });
      if (!response.ok) throw new Error("Communication Error");

      const data = await response.json();
      setSimulationState(data);
      setError(null);
    } catch (err) {
      alert("Error: " + err.message);
    }
  };

  const handleAddVehicle = async () => {
    const vehicleDto = {
      id: `v${vehIdCounter}`,
      startRoad: startRoad,
      endRoad: endRoad,
      type: newVehType
    };

    try {
      const response = await fetch('http://localhost:8080/simulation/vehicle', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(vehicleDto)
      });

      if (!response.ok) throw new Error("Network error during adding vehicle");

      const data = await response.json();

      if (data.errorMessage) {
         alert(data.errorMessage);
      } else {
         setSimulationState(data);

         setVehIdCounter(prev => prev + 1);
         setError(null);
      }
    } catch (err) {
      alert("Error during adding vehicle: " + err.message);
    }
  };

  // Intersection
  const intersectionGeometry = useMemo(() => {
    if (!simulationState) return { width: 100, height: 100 };

    const roads = simulationState.intersection.roads;
    const getLanes = (dir) => roads.find(r => r.entryDirection === dir)?.trafficLanes.length || 0;
    
    const nLanes = getLanes('North');
    const sLanes = getLanes('South');
    const eLanes = getLanes('East');
    const wLanes = getLanes('West');

    const width = Math.max(nLanes, sLanes) * 2 * LANE_WIDTH;
    const height = Math.max(eLanes, wLanes) * 2 * LANE_WIDTH;

    return { width, height };
  }, [simulationState]);

  if (error) return <div className="error-box">Error: {error}</div>;
  if (!simulationState) return <div className="loading">Loading...</div>;

  roadLength = MAX_ROAD_LENGTH - intersectionGeometry.height

  return (
    <div className="app-container">
      <div className="header">
        <h1>Step: {simulationState.step}</h1>
        <button className="step-btn" onClick={handleStep}>NEXT STEP</button>

        <div className="separator">|</div>

        {/* add vehicle panel */}
        <div className="add-vehicle-panel">
          <select value={newVehType} onChange={e => setNewVehType(e.target.value)}>
            <option value="CAR">Car</option>
            <option value="AMBULANCE">AMBULANCE</option>
          </select>

          <span className="label">Start:</span>
          <select value={startRoad} onChange={e => setStartRoad(e.target.value)}>
            <option value="North">North</option>
            <option value="East">East</option>
            <option value="South">South</option>
            <option value="West">West</option>
          </select>

          <span className="label">End:</span>
          <select value={endRoad} onChange={e => setEndRoad(e.target.value)}>
            <option value="North">North</option>
            <option value="East">East</option>
            <option value="South">South</option>
            <option value="West">West</option>
          </select>

          <button className="add-btn" onClick={handleAddVehicle}>Add Vehicle</button>
        </div>
      </div>
      
      <svg viewBox="-600 -400 1200 800" className="simulation-svg">       
        {/* Grass background */}
        <rect x="-1000" y="-1000" width="2000" height="2000" fill="#4a854a" />

        {/* Middle of intersection */}
        <rect 
          x={-intersectionGeometry.width / 2} 
          y={-intersectionGeometry.height / 2} 
          width={intersectionGeometry.width} 
          height={intersectionGeometry.height} 
          fill="#535353" 
          stroke="#555"
        />

        {/* Roads */}
        {simulationState.intersection.roads.map((road, index) => (
          <Road 
            key={index} 
            roadData={road} 
            intersectionDims={intersectionGeometry} 
          />
        ))}

        {/* left vehicles */}
        <g>
            {simulationState.leftVehicles.map((veh) => (
              <circle key={veh.id} r="5" fill="red">
                <animateTransform attributeName="transform" type="scale" from="1" to="3" dur="1s" begin="0s" fill="freeze"/>
                <animate attributeName="opacity" from="1" to="0" dur="0.5s" begin="0s" fill="freeze"/>
              </circle>
            ))}
        </g>
      </svg>
    </div>
  );
}

export default App;