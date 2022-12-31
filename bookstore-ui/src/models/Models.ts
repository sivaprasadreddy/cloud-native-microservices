export interface ProductModel {
    id: string
    code: string
    name: string
    description: string
    imageUrl: string
    price: number
    discount: number
    salePrice: number
}

export interface CartModel {
    id: string
    items: CartItemModel[]
}

export interface CartItemModel {
    code: string
    name: string
    price: number
    quantity: number
}

export interface Customer {
    name: string
    email: string
    phone: string
}

export interface Payment {
    cardNumber: string
    cvv: string
    expiryMonth: number
    expiryYear: number
}

export interface Address {
    addressLine1: string
    addressLine2: string
    city: string
    state: string
    zipCode: string
    country: string
}

export interface OrderItemModel {
    code: string
    name: string
    price: number
    quantity: number
}

export interface CreateOrderModel {
    items: OrderItemModel[]
    customer: Customer
    payment: Payment
    deliveryAddress: Address
}

export interface OrderModel {
    orderId: string
    status: string
    items: OrderItemModel[]
    customer: Customer
    deliveryAddress: Address
}

export interface OrderConfirmationModel {
    orderId: string
    status: string
}