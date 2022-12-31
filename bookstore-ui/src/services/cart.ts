import {CartItemModel, CartModel} from "../models/Models";

const BOOKSTORE_STATE_KEY = "BOOKSTORE_STATE"

export const getCart = (): CartModel => {
    let cart = localStorage.getItem(BOOKSTORE_STATE_KEY)
    if (!cart) {
        cart = JSON.stringify({"id": generateUUID(), items:[] });
        localStorage.setItem(BOOKSTORE_STATE_KEY, cart)
    }
    return JSON.parse(cart) as CartModel
}

export const addProduct = (item: CartItemModel) => {
    let cart = getCart();
    let cartItem = cart.items.find(itemModel => itemModel.code === item.code);
    if (cartItem) {
        cartItem.quantity = cartItem.quantity + 1;
    } else {
        cart.items.push(item)
    }
    localStorage.setItem(BOOKSTORE_STATE_KEY, JSON.stringify(cart))
}

export const updateProductQuantity = (code: string, quantity: number) => {
    let cart = getCart();
    if(quantity < 1) {
        cart.items = cart.items.filter(itemModel => itemModel.code !== code);
    } else {
        let cartItem = cart.items.find(itemModel => itemModel.code === code);
        if (cartItem) {
            cartItem.quantity = quantity;
        } else {
            console.log("Product code is not already in Cart, ignoring")
        }
    }
    localStorage.setItem(BOOKSTORE_STATE_KEY, JSON.stringify(cart))
}

export const removeProduct = (code: string) => {
    let cart = getCart();
    cart.items = cart.items.filter(itemModel => itemModel.code !== code);
    localStorage.setItem(BOOKSTORE_STATE_KEY, JSON.stringify(cart))
}

export const removeCart = () => {
    localStorage.removeItem(BOOKSTORE_STATE_KEY)
}

const generateUUID = () => {
    let u = Date.now().toString(16) + Math.random().toString(16) + '0'.repeat(16);
    return [u.substring(0, 8), u.substring(8, 4), '4000-8' + u.substring(13, 3), u.substring(16, 12)].join('-');
}

