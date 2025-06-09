export interface Address {
  street: string;
  city: string;
  postcode: string;
  country: string;
}

export interface PaymentInfo {
  cardHolderName: string;
  creditCardNumber: string;
  cvv: string;
  expiryDate: string;
}

export type DriverLicenseType = 'A' | 'B' | 'C' | 'D' | 'NONE';

export interface Customer {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  phoneNumber: string;
  email: { value: string };
  password: string;
  driverLicenseType: DriverLicenseType;
  address: Address;
  paymentinfo: PaymentInfo;
}
