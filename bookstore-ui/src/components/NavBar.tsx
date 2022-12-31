import React, {useEffect} from "react";
import {NavLink} from "react-router-dom";
import * as api from "../services/api";

const NavBar: React.FC = (): JSX.Element => {

    useEffect(() => {
        api.getCart().then(cart => {
            let itemCount = cart.items.map(item => item.quantity)
                .reduce((accumulator, currentValue) => accumulator + currentValue, 0);
            console.log("Cart Item Count: " + itemCount)
            // @ts-ignore
            document.getElementById('cart-link').innerHTML = "Cart (" + itemCount + ")";
        });
    }, []);

    return (
        <nav className="navbar fixed-top navbar-expand-md navbar-dark bg-dark">
            <div className="container-fluid">
                <NavLink className="navbar-brand" to="/">
                    BookStore
                </NavLink>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item">
                            <NavLink className="nav-link" to="/cart">
                                <span id={'cart-link'}>Cart (0)</span>
                            </NavLink>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default NavBar;
