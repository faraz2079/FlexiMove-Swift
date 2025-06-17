export interface Address {
  street: string;
  city: string;
  postcode: string;
  country: string;
}

export interface Email {
  value: string;
}

export interface Password {
  value: string;
}

export interface PaymentInfo {
  creditCardNumber: string;
  cardHolderName: string;
  expiryDate: string;
  cvv: string;
}

export interface Provider {
  address: Address;
  password: Password;
  email: Email;
  phoneNumber: string;
  paymentinfo: PaymentInfo;
  companyName: string;
}
