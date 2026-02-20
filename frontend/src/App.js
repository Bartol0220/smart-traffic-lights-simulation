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

// UTILS
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

const normalizeDir = (dir) => {
  if (!dir) return "";
  return dir.charAt(0).toUpperCase() + dir.slice(1).toLowerCase();
};


// COMPONENTS
const Vehicle = ({ height=32, color = "blue", position = 0 }) => {
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
        width="16" height={height}
        fill={color} 
        stroke="white" 
        rx="3"
      />
    </g>
  );
};

const TrafficLane = ({ laneData, type, currentRoadLength, onLaneClick, hideVehicles }) => {
  const isIncoming = type === 'incoming';
  const trafficLightMargin = 10;

  const VEHICLE_GAP = 40; 
  const START_OFFSET = 52;
  const maxCapacity = Math.floor((currentRoadLength - START_OFFSET) / VEHICLE_GAP);
  const availableSpace = currentRoadLength - START_OFFSET

  let usedSpace = 0;
  let visibleVehicles = [];
  let spaceCounter = [];
  let lastVehicleLength = [0];
  let overflowCount = 0;

  const vehicles = (hideVehicles ? [] : laneData?.vehicles) || [];

  for (const vehicle of vehicles) {
    const vehicleLength = vehicle.type === 'BUS' ? 2 : 1;

    if (usedSpace + VEHICLE_GAP * vehicleLength > availableSpace) {
      break;
    }

    visibleVehicles.push(vehicle);
    if (spaceCounter.length == 0) {
      spaceCounter.push(lastVehicleLength[lastVehicleLength.length-1]);
      lastVehicleLength.push(vehicleLength)
    } else {
      spaceCounter.push(lastVehicleLength[lastVehicleLength.length-1] + spaceCounter[spaceCounter.length-1]);
      lastVehicleLength.push(vehicleLength)
    }
    usedSpace += VEHICLE_GAP * vehicleLength;
  }

  if (isIncoming && vehicles.length > 0) {
    overflowCount = vehicles.length - visibleVehicles.length;
  }
  
  // Lights Animation
  const incomingColor = laneData?.trafficLight?.color;
  const [displayedColor, setDisplayedColor] = useState(incomingColor);

  useEffect(() => {    
    const next = incomingColor ? incomingColor.toUpperCase() : 'RED';
    const curr = displayedColor ? displayedColor.toUpperCase() : 'RED';
    
    if (incomingColor !== displayedColor) {
        let intermediate = null;

        if ((curr === 'RED') && next === 'GREEN') {
            intermediate = 'RED_YELLOW';
        }
        else if (curr === 'GREEN' && next === 'RED') {
            intermediate = 'YELLOW';
        }

        if (intermediate) {
            setDisplayedColor(intermediate);
            const timer = setTimeout(() => {
                setDisplayedColor(incomingColor);
            }, 700);
            return () => clearTimeout(timer);
        } else {
            setDisplayedColor(incomingColor);
        }
    }
  }, [incomingColor]);

  const lightImg = isIncoming 
    ? getTrafficLightImage(displayedColor)
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

  const isInteractive = isIncoming && !!onLaneClick;
  const laneStyle = isInteractive ? { cursor: 'pointer', fill: '#535353' } : { fill: '#535353' };

  return (
    <g 
      onClick={() => isInteractive && onLaneClick(laneData)}
      className={isInteractive ? "interactive-lane" : ""}
    >
      <title>{isInteractive ? "Click to remove lane" : ""}</title>
      <rect 
        x="0" y="0" 
        width={LANE_WIDTH} height={currentRoadLength} 
        style={laneStyle}
        stroke="white" 
        strokeWidth="1" 
        strokeDasharray="0"  
        fillOpacity={1}
      />

      {isIncoming && (
        <>
          <line x1="4" y1="4" x2={LANE_WIDTH-4} y2="4" stroke="white" strokeWidth="4" />
          
          <image 
            href={lightImg} 
            x={trafficLightMargin} 
            y={2*trafficLightMargin - LANE_WIDTH - 2} 
            width={LANE_WIDTH - 2*trafficLightMargin}
            height={LANE_WIDTH - 2*trafficLightMargin}
          />
          
          {renderArrows()}

          {laneData?.lanePriority !== undefined && (
            <text 
              x={LANE_WIDTH / 2} 
              y={currentRoadLength + 20} 
              fill="white" 
              textAnchor="middle" 
              fontSize="12"
              fontWeight="bold"
            >
              P: {laneData.lanePriority}
            </text>
          )}

          {visibleVehicles.map((veh, i) => (
            <Vehicle
                height={veh.type === 'BUS' ? 32 + VEHICLE_GAP : '32'}
                key={veh.id || i} 
                position={START_OFFSET + (spaceCounter[i] * VEHICLE_GAP)} 
                color={veh.type === 'BUS' ? 'blue' : (veh.type === 'AMBULANCE' ? 'white' : 'orange')} 
            />
          ))}

          {overflowCount > 0 && (
            <g transform={`translate(${LANE_WIDTH/2}, ${currentRoadLength - 20})`}>
               <circle r="12" fill="red" stroke="white" />
               <text y="4" fill="white" textAnchor="middle" fontSize="12" fontWeight="bold">+{overflowCount}</text>
            </g>
          )}
        </>
      )}
    </g>
  );
};

const Road = ({ roadData, intersectionDims, onLaneClick, hideVehicles }) => {
  const incomingLanes = roadData.trafficLanes;
  const laneCount = incomingLanes.length;
  const outgoingLanesCount = laneCount; 
  
  let rotation = 0;
  let distFromCenter = 0;
  let currentRoadLength = roadLength;

  const direction = normalizeDir(roadData.entryDirection);

  switch (direction) {
    case 'North': 
      rotation = 180; distFromCenter = intersectionDims.height / 2; currentRoadLength = roadLength; break;
    case 'South': 
      rotation = 0;   distFromCenter = intersectionDims.height / 2; currentRoadLength = roadLength; break;
    case 'East':  
      rotation = 270; distFromCenter = intersectionDims.width / 2; currentRoadLength = MAX_ROAD_LENGTH; break;
    case 'West':  
      rotation = 90;  distFromCenter = intersectionDims.width / 2; currentRoadLength = MAX_ROAD_LENGTH; break;
    default: break;
  }

  return (
    <g transform={`rotate(${rotation})`}>
      <g transform={`translate(0, ${distFromCenter})`}>
        {incomingLanes.map((lane, idx) => (
          <g key={`in-${idx}`} transform={`translate(${idx * LANE_WIDTH}, 0)`}>
             <TrafficLane 
               laneData={lane} 
               type="incoming" 
               currentRoadLength={currentRoadLength} 
               onLaneClick={onLaneClick ? () => onLaneClick(roadData.entryDirection, lane.index) : undefined}
               hideVehicles={hideVehicles} 
             />
          </g>
        ))}

        {Array.from({ length: outgoingLanesCount }).map((_, idx) => (
          <g key={`out-${idx}`} transform={`translate(-${(idx + 1) * LANE_WIDTH}, 0)`}>
             <TrafficLane laneData={null} type="outgoing" currentRoadLength={currentRoadLength} />
          </g>
        ))}

        {laneCount > 0 && (
          <>
            <line x1="0" y1="0" x2="0" y2={currentRoadLength} stroke="yellow" strokeWidth="2" />
            <line x1="-3" y1="0" x2="-3" y2={currentRoadLength} stroke="yellow" strokeWidth="2" />
          </>
        )}
      </g>
    </g>
  );
};

// VIEW 1: PRIORITIES CONFIG
const PrioritiesConfig = ({ onNext, onDefaultStart }) => {
  const [inputs, setInputs] = useState({ North: '', South: '', East: '', West: '' });

  const handleSubmit = async () => {
    const prioritiesMap = {};
    let isAnyFilled = false;
    Object.keys(inputs).forEach(key => {
      if (inputs[key] !== '') {
        prioritiesMap[key] = parseInt(inputs[key], 10);
        isAnyFilled = true;
      }
    });

    try {
      const response = await fetch('http://localhost:8080/config/priorities', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(isAnyFilled ? prioritiesMap : null)
      });
      if (!response.ok) throw new Error("Configuration failed");
      const data = await response.json();

      if (data.errorMessage) {
          window.alert(data.errorMessage);
      } else {
          onNext();
      }
    } catch (err) {
      window.alert("Error saving configuration: " + err.message);
    }
  };

  return (
    <div className="config-container">
      <div className="config-box">
        <h1>Configuration</h1>
        <h2>Road Priorities</h2>
        <p className="config-desc">Assign priority for each direction. Higher number = higher priority.</p>
        <div className="input-grid">
          {['North', 'East', 'South', 'West'].map(dir => (
            <div key={dir} className="input-group">
              <label>{dir}</label>
              <input 
                type="number" 
                value={inputs[dir]} 
                onChange={e => setInputs({...inputs, [dir]: e.target.value})} 
                placeholder="Default"
              />
            </div>
          ))}
        </div>
        
        <button className="next-btn" onClick={handleSubmit}>Next</button>
        
        <div style={{ margin: '15px 0', color: '#666', fontSize: '0.9rem' }}>— OR —</div>
        
        <button className="default-btn" onClick={onDefaultStart}>
           Quick Start: Default Simulation
        </button>
      </div>
    </div>
  );
};

// VIEW 2: LANE CONFIGURATION
const LaneConfiguration = ({ onNext, onBack }) => {
  const [config, setConfig] = useState(null);
  
  const [globalSettings, setGlobalSettings] = useState({
    maxPhaseTime: 10,
    controllerType: 'TIME',
    emergencyLightController: false
  });

  const [addLaneForm, setAddLaneForm] = useState({
    entryDirection: 'North',
    lanePriority: 1,
    exitDirections: ['South'],
    addOpositeLane: false,
  });

  const [intersectionType, setIntersectionType] = useState('Simple intersection');

  useEffect(() => {
    fetchConfig();
  }, []);

  const fetchConfig = async () => {
    try {
      const res = await fetch('http://localhost:8080/config');
      if (!res.ok) throw new Error("Failed to fetch config");
      const data = await res.json();
      setConfig(data);
      
      setGlobalSettings({
          maxPhaseTime: data.maxPhaseTime,
          controllerType: data.type, 
          emergencyLightController: data.isEmergencyLightController
      });
      
    } catch (e) {
      window.alert(e.message);
    }
  };

  const updateGlobalSettings = async (newSettings) => {
    const payload = {
        maxPhaseTime: parseInt(newSettings.maxPhaseTime),
        controllerType: newSettings.controllerType,
        emergencyLightController: newSettings.emergencyLightController
    };

    try {
        const res = await fetch('http://localhost:8080/config', {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error("Update failed");
        const data = await res.json();

        if (data.errorMessage) {
            window.alert(data.errorMessage);
        } else {
            setConfig(data);
        }
    } catch (e) {
        window.alert("Error updating settings: " + e.message);
    }
  };

  const handleSettingChange = (field, value) => {
    const newSettings = { ...globalSettings, [field]: value };
    setGlobalSettings(newSettings);
    updateGlobalSettings(newSettings);
  };

  const handleAddLane = async () => {
    const payload = {
        entryDirection: addLaneForm.entryDirection, 
        lanePriority: parseInt(addLaneForm.lanePriority),
        exitDirections: addLaneForm.exitDirections,
        addOpositeLane: addLaneForm.addOpositeLane
    };

    try {
        const res = await fetch('http://localhost:8080/config/lanes', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        if (data.errorMessage) {
            window.alert(data.errorMessage);
        } else {
            setConfig(data);
        }
    } catch (e) {
        window.alert("Error adding lane: " + e.message);
    }
  };

  const handleRemoveLane = async (dir, index) => {
    if (!window.confirm(`Are you sure you want to remove lane #${index} from ${dir}?`)) return;
    const dirParam = dir.toUpperCase(); 

    try {
        const res = await fetch(`http://localhost:8080/config/lanes/${dirParam}/${index}`, {
            method: 'DELETE'
        });
        const data = await res.json();
        setConfig(data);
    } catch (e) {
        window.alert("Error removing lane: " + e.message);
    }
  };

  const handlePrepareIntersection = async () => {
    try {
        const res = await fetch('http://localhost:8080/config/intersection', {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(intersectionType)
        });
        
        if (!res.ok) throw new Error("Failed to load intersection");
        const data = await res.json();

        if (data.errorMessage) {
            window.alert(data.errorMessage);
        } else {
            setConfig(data);
        }
    } catch (e) {
        window.alert("Error loading intersection: " + e.message);
    }
  };

  const handleStartSimulation = async () => {
    try {
        const res = await fetch('http://localhost:8080/simulation/init', { method: 'POST' });
        if (!res.ok) throw new Error("Could not start simulation");
        
        const data = await res.json();
        if (data.errorMessage) {
         window.alert(data.errorMessage);
        } else {
          onNext();
        }
    } catch (e) {
        window.alert(e.message);
    }
  };

  const toggleExitDirection = (dir) => {
    setAddLaneForm(prev => {
        const exits = prev.exitDirections.includes(dir)
            ? prev.exitDirections.filter(d => d !== dir)
            : [...prev.exitDirections, dir];
        return { ...prev, exitDirections: exits };
    });
  };

  const intersectionGeometry = useMemo(() => {
    if (!config || !config.intersection || !config.intersection.roads) {
        return { width: 100, height: 100 };
    }

    const roads = config.intersection.roads;
    const getLanes = (dirTitleCase) => {
        const road = roads.find(r => normalizeDir(r.entryDirection) === dirTitleCase);
        return road?.trafficLanes?.length || 0;
    };
    
    const n = getLanes('North'); const s = getLanes('South');
    const e = getLanes('East'); const w = getLanes('West');
    return { width: Math.max(n, s) * 2 * LANE_WIDTH, height: Math.max(e, w) * 2 * LANE_WIDTH };
  }, [config]);

  if (!config) return <div className="loading">Loading Configuration...</div>;
  roadLength = MAX_ROAD_LENGTH - intersectionGeometry.height/2;

  return (
    <div className="app-container config-mode">
      <div className="config-sidebar">
        <h2>Global Settings</h2>
        <div className="input-group">
            <label>Max Phase Time (steps)</label>
            <input 
                type="number" 
                value={globalSettings.maxPhaseTime}
                onChange={(e) => handleSettingChange('maxPhaseTime', e.target.value)}
            />
        </div>
        <div className="input-group">
            <label>Controller Type</label>
            <select
                value={globalSettings.controllerType}
                onChange={(e) => handleSettingChange('controllerType', e.target.value)}
            >
                <option value="TIME">Time Based</option>
                <option value="VEHICLES_PRIORITY">Vehicle Priority</option>
                <option value="LANE_PRIORITY">Lane Priority</option>
                <option value="PHASE_PRIORITY">Phase Priority</option>
            </select>
        </div>
        <div className="input-group checkbox-group">
            <label>
                <input 
                    type="checkbox" 
                    checked={globalSettings.emergencyLightController}
                    onChange={(e) => handleSettingChange('emergencyLightController', e.target.checked)}
                />
                Emergency Controller
            </label>
        </div>

        <h2>Add Lane</h2>
        <div className="input-row">
          <div className="input-group">
              <label>Entry Direction</label>
              <select 
                  value={addLaneForm.entryDirection}
                  onChange={(e) => setAddLaneForm({...addLaneForm, entryDirection: e.target.value})}
              >
                  {['North', 'East', 'South', 'West'].map(d => <option key={d} value={d}>{d}</option>)}
              </select>
          </div>
          <div className="input-group">
              <label>Priority</label>
              <input 
                  type="number" 
                  value={addLaneForm.lanePriority}
                  onChange={(e) => setAddLaneForm({...addLaneForm, lanePriority: e.target.value})}
              />
          </div>
        </div>
        <div className="input-group">
            <label>Exits to:</label>
            <div className="exit-checkboxes">
                {['North', 'East', 'South', 'West'].map(dir => (
                    <label key={dir} style={{display:'block'}}>
                        <input 
                            type="checkbox" 
                            checked={addLaneForm.exitDirections.includes(dir)}
                            onChange={() => toggleExitDirection(dir)}
                        />
                        {dir}
                    </label>
                ))}
            </div>
        </div>
        <div className="input-group checkbox-group" style={{marginTop: '15px'}}>
            <label title="Adds a symmetrical lane from the opposite direction">
                <input 
                    type="checkbox" 
                    checked={addLaneForm.addOpositeLane}
                    onChange={(e) => setAddLaneForm({...addLaneForm, addOpositeLane: e.target.checked})}
                />
                Add Opposite Lane
            </label>
        </div>
        <button className="add-btn" onClick={handleAddLane} style={{width:'100%', marginTop:'10px'}}>Add Lane</button>

        <h2>Default Intersections</h2>
        <div className="input-group">
            <select 
                value={intersectionType}
                onChange={(e) => setIntersectionType(e.target.value)}
            >
              <option value="Simple intersection">Simple intersection</option>
              <option value="Intersection with main road">Intersection with main road</option>
              <option value="Large intersection">Large intersection</option>
            </select>
        </div>
        <button className="add-btn" onClick={handlePrepareIntersection} style={{width:'100%', marginTop:'10px'}}>Load Preset</button>

        <div className="nav-buttons">
            <button className="back-btn" onClick={onBack}>&laquo; Back</button>
            <button className="start-sim-btn" onClick={handleStartSimulation}>Start Simulation &raquo;</button>
        </div>
      </div>

      <div className="config-visualization">
        <h3 style={{position: 'absolute', top: 10, left: 20, zIndex: 100}}>Click on a lane to remove it.</h3>
        <h3 style={{position: 'absolute', top: 35, left: 20, zIndex: 100}}>"P" below the lane represents its priority.</h3>
        <svg viewBox="-600 -500 1200 1000" className="simulation-svg">
          <rect x="-1000" y="-1000" width="2000" height="2000" fill="#4a854a" />
          <rect 
             x={-intersectionGeometry.width / 2} 
             y={-intersectionGeometry.height / 2} 
             width={intersectionGeometry.width} 
             height={intersectionGeometry.height} 
             fill="#535353" 
             stroke="#555"
          />
          {config.intersection && config.intersection.roads ? config.intersection.roads.map((road, index) => (
            <Road 
              key={index} 
              roadData={road} 
              intersectionDims={intersectionGeometry}
              onLaneClick={handleRemoveLane}
              hideVehicles={true} 
            />
          )) : null}
        </svg>
      </div>
    </div>
  );
};

// MAIN APP
function App() {
  const [currentView, setCurrentView] = useState('config-priorities'); 
  
  const [simulationState, setSimulationState] = useState(null);
  const [error, setError] = useState(null);

  const [vehIdCounter, setVehIdCounter] = useState(1);
  const [newVehType, setNewVehType] = useState('CAR');
  const [startRoad, setStartRoad] = useState('North');
  const [endRoad, setEndRoad] = useState('South');

  useEffect(() => {
    if (currentView === 'simulation') {
      fetchState();
    }
  }, [currentView]);

  const fetchState = async () => {
    try {
      const response = await fetch(`http://localhost:8080/simulation/state`);
      if (!response.ok) throw new Error("Error during getting simulation state.");

      const data = await response.json();
      setSimulationState(data);
      setError(null);
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };
  
  const transitionToSimulation = () => {
      setSimulationState(null); 
      setVehIdCounter(1); 
      setCurrentView('simulation');
  };

  const handleDefaultStart = async () => {
    try {
        const res = await fetch('http://localhost:8080/simulation/init/default', { method: 'POST' });
        if (!res.ok) throw new Error("Failed to initialize default simulation");
        transitionToSimulation();
    } catch (e) {
        window.alert(e.message);
    }
  };

  const handleStep = async () => {
    try {
      const response = await fetch('http://localhost:8080/simulation/step', { method: 'POST' });
      if (!response.ok) throw new Error("Communication Error");
      const data = await response.json();
      setSimulationState(data);
      setError(null);
    } catch (err) {
      window.alert("Error: " + err.message);
    }
  };

  const handleStopAndConfigure = () => {
      setSimulationState(null);
      setCurrentView('config-priorities');
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
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(vehicleDto)
      });
      if (!response.ok) throw new Error("Network error during adding vehicle");
      const data = await response.json();

      if (data.errorMessage) {
         window.alert(data.errorMessage);
      } else {
         setSimulationState(data);
         setVehIdCounter(prev => prev + 1);
         setError(null);
      }
    } catch (err) {
      window.alert("Error during adding vehicle: " + err.message);
    }
  };

  const intersectionGeometry = useMemo(() => {
    if (!simulationState) return { width: 100, height: 100 };
    const roads = simulationState.intersection.roads;
    const getLanes = (dir) => roads.find(r => r.entryDirection === dir)?.trafficLanes.length || 0;
    const n = getLanes('North'); const s = getLanes('South');
    const e = getLanes('East'); const w = getLanes('West');
    return { width: Math.max(n, s) * 2 * LANE_WIDTH, height: Math.max(e, w) * 2 * LANE_WIDTH };
  }, [simulationState]);

  // RENDER LOGIC
  if (currentView === 'config-priorities') {
    return (
      <PrioritiesConfig 
        onNext={() => setCurrentView('config-lanes')} 
        onDefaultStart={handleDefaultStart} 
      />
    );
  }

  if (currentView === 'config-lanes') {
      return (
          <LaneConfiguration 
             onNext={transitionToSimulation}
             onBack={() => setCurrentView('config-priorities')}
          />
      );
  }

  if (currentView === 'simulation') {
    if (error) return <div className="error-box">Error: {error}</div>;
    if (!simulationState) return <div className="loading">Loading Simulation...</div>;
    
    roadLength = MAX_ROAD_LENGTH - intersectionGeometry.height;

    return (
      <div className="app-container">
        <div className="header">
          <button className="back-btn" style={{marginRight: '20px'}} onClick={handleStopAndConfigure}>
             &laquo; Stop & Configure
          </button>
          
          <h1>Step: {simulationState.step}</h1>
          <button className="step-btn" onClick={handleStep}>NEXT STEP</button>
          <div className="separator">|</div>
          <div className="add-vehicle-panel">
            <select value={newVehType} onChange={e => setNewVehType(e.target.value)}>
              <option value="CAR">Car</option>
              <option value="BUS">Bus</option>
              <option value="AMBULANCE">Ambulance</option>
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
        
        <svg viewBox="-600 -450 1200 900" className="simulation-svg">       
          <rect x="-1300" y="-1000" width="2800" height="2000" fill="#4a854a" />
          <rect 
            x={-intersectionGeometry.width / 2} 
            y={-intersectionGeometry.height / 2} 
            width={intersectionGeometry.width} 
            height={intersectionGeometry.height} 
            fill="#535353" 
            stroke="#555"
          />
          {simulationState.intersection.roads.map((road, index) => (
            <Road 
              key={index} 
              roadData={road} 
              intersectionDims={intersectionGeometry} 
            />
          ))}
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

  return null;
}

export default App;