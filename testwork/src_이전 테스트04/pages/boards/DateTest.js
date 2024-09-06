import React, { useState } from 'react';
import { DateRangePicker } from 'react-date-range';
import { addDays, differenceInDays } from 'date-fns';
import 'react-date-range/dist/styles.css'; // main css file
import 'react-date-range/dist/theme/default.css'; // theme css file

function DateRangeSelector() {
  const [state, setState] = useState([
    {
      startDate: new Date(),
      endDate: addDays(new Date(), 7),
      key: 'selection'
    }
  ]);

  const calculateTotalDays = () => {
    const startDate = state[0].startDate;
    const endDate = state[0].endDate;
    return differenceInDays(endDate, startDate) + 1; // +1 to include the end date
  };

  return (
    <div>
      <DateRangePicker
        ranges={state}
        onChange={item => setState([item.selection])}
      />
      <div>
        <h3>Selected Range:</h3>
        <p>
          Start Date: {state[0].startDate.toDateString()}
          <br />
          End Date: {state[0].endDate.toDateString()}
          <br />
          Total Days: {calculateTotalDays()} days
        </p>
      </div>
    </div>
  );
}

export default DateRangeSelector;
