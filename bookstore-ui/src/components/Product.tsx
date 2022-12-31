import React from "react";
import {ProductModel} from '../models/Models'
import {NavLink} from "react-router-dom";
import * as api from "../services/api";

const Product: React.FC<ProductModel> = (product: ProductModel): JSX.Element => {

    const addProductToCart = (product: ProductModel) => {
        api.addProductToCart(product).then(r => {
            //console.log("Product added to CartId: " + r.id)
            let itemCount = r.items.map(item => item.quantity)
                .reduce((accumulator, currentValue) => accumulator + currentValue, 0);
            //console.log("Cart Item Count: " + itemCount)
            // @ts-ignore
            document.getElementById('cart-link').innerHTML = "Cart (" + itemCount + ")";
        })
    }

    return (
        <div className="col mb-3">
            <div className="card h-100">
                <NavLink to={'/products/' + product.code}>
                    <img src={product.imageUrl}
                         className="card-img-top" alt="Movie"
                         height="300" width="200"
                    />
                </NavLink>
                <div className="card-body">
                    <h5 className="card-title">{product.name}</h5>
                    <p className="card-text">{product.description}</p>
                    <button
                        className="btn btn-primary"
                        onClick={() => addProductToCart(product)}>
                        Add to Cart
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Product;
