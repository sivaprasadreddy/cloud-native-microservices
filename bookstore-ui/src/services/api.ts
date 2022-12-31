import axios from "./axios";
import config from './config'
import {
    CartModel, CreateOrderModel, OrderConfirmationModel, OrderModel,
    ProductModel
} from "../models/Models";

import * as cartApi from "./cart";

const PRODUCT_SERVICE_API_BASE_URL = config.PRODUCT_SERVICE_API_BASE_URL;
const ORDER_SERVICE_API_BASE_URL = config.ORDER_SERVICE_API_BASE_URL;

export function fetchProducts(page: number) {
    let queryString = "?page=" + page;
    return axios.get<ProductModel[]>(`${PRODUCT_SERVICE_API_BASE_URL}/api/products` + queryString);
}

export function createOrder(order: CreateOrderModel): Promise<OrderConfirmationModel> {
    return axios.post<OrderConfirmationModel>(`${ORDER_SERVICE_API_BASE_URL}/api/orders`, order)
        .then(response => {
            cartApi.removeCart();
            return response.data;
        });
}

export function fetchOrder(orderId: string): Promise<OrderModel> {
    return axios.get<OrderModel>(`${ORDER_SERVICE_API_BASE_URL}/api/orders/${orderId}`)
        .then(response => {
            return response.data;
        });
}

export function getCart(): Promise<CartModel> {
    return Promise.resolve(cartApi.getCart());
}

export function addProductToCart(product: ProductModel): Promise<CartModel> {
    cartApi.addProduct({name: product.name, price: product.price, quantity: 1, code: product.code});
    return Promise.resolve(cartApi.getCart());
}

export function updateProductQuantityInCart(code: string, quantity: number): Promise<CartModel> {
    cartApi.updateProductQuantity(code, quantity);
    return Promise.resolve(cartApi.getCart());
}
