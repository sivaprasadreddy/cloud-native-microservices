import React from "react";
import ReactDOM from "react-dom";
import {HashRouter} from "react-router-dom";
import App from "./App";
import "./index.css";
import "bootstrap/dist/css/bootstrap.min.css";
import 'bootstrap/dist/js/bootstrap.bundle';

ReactDOM.render(
  <React.StrictMode>
    <HashRouter>
      <App />
    </HashRouter>
  </React.StrictMode>,
  document.getElementById('root')
);

