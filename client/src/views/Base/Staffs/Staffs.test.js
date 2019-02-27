import React from 'react';
import ReactDOM from 'react-dom';
import Staffs from './Staffs';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<Staffs />, div);
  ReactDOM.unmountComponentAtNode(div);
});
