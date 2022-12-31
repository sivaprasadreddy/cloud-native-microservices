import React, {useEffect, useState} from 'react';
import {RouteComponentProps} from 'react-router-dom';
import {Address, CreateOrderModel, Customer, OrderItemModel, Payment} from "../models/Models";
import * as api from "../services/api";

interface CartProps extends RouteComponentProps<any> {

}

const Cart: React.FC<CartProps> = (props: CartProps): JSX.Element => {
    const [orderItems, setOrderItems] = useState<OrderItemModel[]>([]);
    //Too lazy to fill the data :-)
    const [customer, setCustomer] = useState<Customer>({name: "Siva", email: "siva@gmail.com", phone: "99999999"});
    const [payment, setPayment] = useState<Payment>({cardNumber: "1111222233334444", cvv: "123", expiryMonth: 2, expiryYear: 2030});
    const [deliveryAddress, setDeliveryAddress] = useState<Address>({
        addressLine1: "KPHB",
        addressLine2: "Kukatpally",
        city: "Hyderabad",
        state: "TS",
        zipCode: "500072",
        country: "India"
    });

    useEffect(() => {
        api.getCart().then(cart => {
            const orderItems = cart.items.map(item => {
                return {
                    code: item.code,
                    name: item.name,
                    price: item.price,
                    quantity: item.quantity
                } as OrderItemModel;
            });
            setOrderItems(orderItems)
        });
    }, []);

    const getOrderTotal = () => {
        return orderItems.map(item => item.price * item.quantity)
            .reduce((accumulator, currentValue) => accumulator + currentValue, 0);
    }

    const onQuantityChangeHandle = (code: string, quantity: string) => {
        //console.log("Update code:" + code + " quantity to :" + quantity);
        api.updateProductQuantityInCart(code, parseInt(quantity)).then(r => {
            setOrderItems(r.items)
            let itemCount = r.items.map(item => item.quantity)
                .reduce((accumulator, currentValue) => accumulator + currentValue, 0);
            //console.log("Cart Item Count: " + itemCount)
            // @ts-ignore
            document.getElementById('cart-link').innerHTML = "Cart (" + itemCount + ")";
        })
    }
    const placeOrder = () => {
        const order: CreateOrderModel = {
            items: orderItems,
            customer: customer,
            deliveryAddress: deliveryAddress,
            payment: payment
        };
        api.createOrder(order).then(orderConfirmation => {
            alert("Order placed successfully");
            console.log("Created Order with orderId:" + orderConfirmation.orderId);
            props.history.push(`/orders/${orderConfirmation.orderId}`);
        });
    }
    return (
        <div className="row">
            <div className="col-md-8 offset-2">
                <div className="pb-3">
                    <table className="table">
                        <thead>
                        <tr>
                            <th scope="col">Product Name</th>
                            <th scope="col">Price</th>
                            <th scope="col">Quantity</th>
                            <th scope="col">Sub Total</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orderItems.map(item => {
                            return (
                                <tr key={item.code}>
                                    <td>{item.name}</td>
                                    <td>{item.price}</td>
                                    <td>
                                        <input type="number" min="0" value={item.quantity}
                                               onChange={(event) => onQuantityChangeHandle(item.code, event.target.value)}/>
                                    </td>
                                    <td>{item.quantity * item.price}</td>
                                </tr>
                            );
                        })}
                        </tbody>
                        <tfoot>
                        <tr>
                            <th></th>
                            <th></th>
                            <th></th>
                            <th>
                                Total Amount: {getOrderTotal()}
                            </th>
                        </tr>
                        </tfoot>
                    </table>
                    <form className="row g-3" method="post" action="#">
                        <div className="col-md-4">
                            <label htmlFor="customerName" className="form-label">Customer Name</label>
                            <input type="text" className="form-control"
                                   id="customerName"
                                   name="customerName"
                                   value={customer.name}
                                   onChange={(event) => setCustomer(Object.assign({}, customer, {name: event.target.value}))}/>
                        </div>
                        <div className="col-md-4">
                            <label htmlFor="customerEmail" className="form-label">Customer Email</label>
                            <input type="email" className="form-control"
                                   id="customerEmail"
                                   name="customerEmail"
                                   value={customer.email}
                                   onChange={(event) => setCustomer(Object.assign({}, customer, {email: event.target.value}))}/>
                        </div>
                        <div className="col-md-4">
                            <label htmlFor="customerPhone" className="form-label">Customer Phone</label>
                            <input type="text" className="form-control"
                                   id="customerPhone"
                                   name="customerPhone"
                                   value={customer.phone}
                                   onChange={(event) => setCustomer(Object.assign({}, customer, {phone: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressLine1" className="form-label">Delivery Address Line 1</label>
                            <input className="form-control"
                                   id="deliveryAddressLine1"
                                   name="deliveryAddressLine1"
                                   value={deliveryAddress.addressLine1}
                                   onChange={(event) => setDeliveryAddress(Object.assign({}, deliveryAddress, {addressLine1: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressLine2" className="form-label">Delivery Address Line 2</label>
                            <input className="form-control"
                                   id="deliveryAddressLine2"
                                   name="deliveryAddressLine2"
                                   value={deliveryAddress.addressLine2}
                                   onChange={(event) => setDeliveryAddress(Object.assign({}, deliveryAddress, {addressLine2: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressCity" className="form-label">Delivery Address City</label>
                            <input className="form-control"
                                   id="deliveryAddressCity"
                                   name="deliveryAddressCity"
                                   value={deliveryAddress.city}
                                   onChange={(event) => setDeliveryAddress(Object.assign({}, deliveryAddress, {city: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressState" className="form-label">Delivery Address State</label>
                            <input className="form-control"
                                   id="deliveryAddressState"
                                   name="deliveryAddressState"
                                   value={deliveryAddress.state}
                                   onChange={(event) => setDeliveryAddress(Object.assign({}, deliveryAddress, {state: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressZipCode" className="form-label">Delivery Address ZipCode</label>
                            <input className="form-control"
                                   id="deliveryAddressZipCode"
                                   name="deliveryAddressZipCode"
                                   value={deliveryAddress.zipCode}
                                   onChange={(event) => setDeliveryAddress(Object.assign({}, deliveryAddress, {zipCode: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressCountry" className="form-label">Delivery Address Country</label>
                            <input className="form-control"
                                   id="deliveryAddressCountry"
                                   name="deliveryAddressCountry"
                                   value={deliveryAddress.country}
                                   onChange={(event) => setDeliveryAddress(Object.assign({}, deliveryAddress, {country: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="cardNumber" className="form-label">Card Number</label>
                            <input type="text" className="form-control"
                                   id="cardNumber"
                                   name="cardNumber"
                                   value={payment.cardNumber}
                                   onChange={(event) => setPayment(Object.assign({}, payment, {cardNumber: event.target.value}))}/>
                        </div>
                        <div className="col-md-6">
                            <label htmlFor="cvv" className="form-label">CVV</label>
                            <input type="text" className="form-control"
                                   id="cvv"
                                   name="cvv"
                                   value={payment.cvv}
                                   onChange={(event) => setPayment(Object.assign({}, payment, {cvv: event.target.value}))}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="expiryMonth" className="form-label">ExpiryMonth</label>
                            <input type="text" className="form-control"
                                   id="expiryMonth"
                                   name="expiryMonth"
                                   value={payment.expiryMonth}
                                   onChange={(event) => setPayment(Object.assign({}, payment, {expiryMonth: event.target.value}))}/>
                        </div>
                        <div className="col-md-6">
                            <label htmlFor="expiryYear" className="form-label">ExpiryYear</label>
                            <input type="text" className="form-control"
                                   id="expiryYear"
                                   name="expiryYear"
                                   value={payment.expiryYear}
                                   onChange={(event) => setPayment(Object.assign({}, payment, {expiryYear: event.target.value}))}/>
                        </div>
                        <div className="col-12">
                            <button type="button" className="btn btn-primary"
                                    onClick={() => placeOrder()}>Place Order
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Cart;


