import React from 'react';
import ReactDOM from 'react-dom';
import ToDos from './ToDos';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<ToDos />, div);
  ReactDOM.unmountComponentAtNode(div);
});
