import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders BookStore', () => {
  render(<App />);
  const linkElement = screen.getByText(/BookStore/i);
  expect(linkElement).toBeInTheDocument();
});
