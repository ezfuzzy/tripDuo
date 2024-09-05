import React, { useState } from 'react';

function TravelCostCalculator() {
    const [accommodation, setAccommodation] = useState(0);
    const [food, setFood] = useState(0);
    const [transportation, setTransportation] = useState(0);
    const [activities, setActivities] = useState(0);

    const totalCost = accommodation + food + transportation + activities;

    return (
        <div>
            <h1>Travel Cost Calculator</h1>
            <label>
                Accommodation: 
                <input type="number" value={accommodation} onChange={(e) => setAccommodation(Number(e.target.value))} />
            </label>
            <br />
            <label>
                Food: 
                <input type="number" value={food} onChange={(e) => setFood(Number(e.target.value))} />
            </label>
            <br />
            <label>
                Transportation: 
                <input type="number" value={transportation} onChange={(e) => setTransportation(Number(e.target.value))} />
            </label>
            <br />
            <label>
                Activities: 
                <input type="number" value={activities} onChange={(e) => setActivities(Number(e.target.value))} />
            </label>
            <br />
            <h3>Total Travel Cost: {totalCost}</h3>
        </div>
    );
}

export default TravelCostCalculator;
