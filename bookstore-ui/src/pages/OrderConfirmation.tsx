import React, {useEffect, useState} from 'react';
import {RouteComponentProps} from 'react-router-dom';
import {OrderModel} from "../models/Models";
import * as api from "../services/api";

interface OrderConfirmationProps extends RouteComponentProps<any> {
}

const OrderConfirmation: React.FC<OrderConfirmationProps> = (props: OrderConfirmationProps): JSX.Element => {
    const [orderId] = useState<string>(props.match.params.orderId)
    const [order, setOrder] = useState<OrderModel>({
        orderId: "",
        status: "",
        customer: {name: "", email: "", phone: ""},
        items: [],
        deliveryAddress: {
            addressLine1: "",
            addressLine2: "",
            city: "",
            state: "",
            zipCode: "",
            country: ""
        }
    });

    const fetchOrder = (orderId: string) => {
        api.fetchOrder(orderId)
            .then(orderResponse => {
                setOrder(orderResponse)
                console.log("Order details:", orderResponse)
            })
            .catch(e => console.log("error", e));
    }

    const getOrderTotal = () => {
        return order.items.map(item => item.price * item.quantity)
            .reduce((accumulator, currentValue) => accumulator + currentValue, 0);
    }

    useEffect(() => {
        api.getCart().then(cart => {
            let itemCount = cart.items.map(item => item.quantity)
                .reduce((accumulator, currentValue) => accumulator + currentValue, 0);
            console.log("Cart Item Count: " + itemCount)
            // @ts-ignore
            document.getElementById('cart-link').innerHTML = "Cart (" + itemCount + ")";
        });
    }, []);

    useEffect(() => {
        fetchOrder(orderId)
    }, [orderId]);

    return (
        <div className="row">
            <div className="col-md-8 offset-2">
                <h2>Your Order Details</h2>
                <h4>Order Number: <span>{order.orderId}</span></h4>
                <h4>Order Status: <span>{order.status}</span></h4>
                <hr/>
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
                        {order.items.map(item => {
                            return (
                                <tr key={item.code}>
                                    <td>{item.name}</td>
                                    <td>{item.price}</td>
                                    <td>
                                        {item.quantity}
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
                                   readOnly={true}
                                   value={order.customer.name}/>
                        </div>
                        <div className="col-md-4">
                            <label htmlFor="customerEmail" className="form-label">Customer Email</label>
                            <input type="email" className="form-control"
                                   id="customerEmail"
                                   name="customerEmail"
                                   readOnly={true}
                                   value={order.customer.email}/>
                        </div>
                        <div className="col-md-4">
                            <label htmlFor="customerPhone" className="form-label">Customer Phone</label>
                            <input type="text" className="form-control"
                                   id="customerPhone"
                                   name="customerPhone"
                                   readOnly={true}
                                   value={order.customer.phone}
                            />
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressLine1" className="form-label">Delivery Address Line 1</label>
                            <input className="form-control"
                                   id="deliveryAddressLine1"
                                   name="deliveryAddressLine1"
                                   readOnly={true}
                                   value={order.deliveryAddress.addressLine1}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressLine2" className="form-label">Delivery Address Line 2</label>
                            <input className="form-control"
                                   id="deliveryAddressLine2"
                                   name="deliveryAddressLine2"
                                   readOnly={true}
                                   value={order.deliveryAddress.addressLine2}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressCity" className="form-label">Delivery Address City</label>
                            <input className="form-control"
                                   id="deliveryAddressCity"
                                   name="deliveryAddressCity"
                                   readOnly={true}
                                   value={order.deliveryAddress.city}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressState" className="form-label">Delivery Address State</label>
                            <input className="form-control"
                                   id="deliveryAddressState"
                                   name="deliveryAddressState"
                                   readOnly={true}
                                   value={order.deliveryAddress.state}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressZipCode" className="form-label">Delivery Address ZipCode</label>
                            <input className="form-control"
                                   id="deliveryAddressZipCode"
                                   name="deliveryAddressZipCode"
                                   readOnly={true}
                                   value={order.deliveryAddress.zipCode}/>
                        </div>
                        <div className="col-6">
                            <label htmlFor="deliveryAddressCountry" className="form-label">Delivery Address Country</label>
                            <input className="form-control"
                                   id="deliveryAddressCountry"
                                   name="deliveryAddressCountry"
                                   readOnly={true}
                                   value={order.deliveryAddress.country}/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default OrderConfirmation;
