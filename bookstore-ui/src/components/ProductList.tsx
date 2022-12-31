import React from "react";
import Product from "./Product";
import {ProductModel} from "../models/Models";

export interface ProductListModel {
    products: ProductModel[],
}

const ProductList: React.FC<ProductListModel> = (productList: ProductListModel): JSX.Element => {
    //console.log("ProductList:", productList.products);
    return (
        <div className={"container"}>
            {productList.products.length === 0 &&
                <div className={"container"}>
                    <h3>No products found</h3>
                </div>
            }

            <div className="row row-cols-1 row-cols-md-4">
                {productList.products.map(p => {
                    return (
                        <Product key={p.id}  {...p} />
                    );
                })}
            </div>

        </div>
    );
}

export default ProductList;
