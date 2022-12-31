import React from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import Products from "./pages/Products";
import NavBar from "./components/NavBar";
import './App.css';
import Cart from "./pages/Cart";
import OrderConfirmation from "./pages/OrderConfirmation";

function App() {
    return (
        <div className="App">
            <NavBar/>
            <main role="main" className="container-fluid">
                <Switch>
                    <Route exact path="/">
                        <Redirect to="/products"/>
                    </Route>
                    <Route path="/products" render={(props) => (
                        <Products key={props.location.search} {...props} />)
                    }/>
                    <Route path="/cart" component={Cart}/>
                    <Route exact path="/orders/:orderId" component={OrderConfirmation} />
                </Switch>
            </main>
        </div>
    );
}

export default App;
