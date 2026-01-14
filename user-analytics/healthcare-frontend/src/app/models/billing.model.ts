export interface Invoice {
  id?: number;
  invoiceNumber?: string;
  patientId: number;
  appointmentId?: number;
  doctorId?: number;
  invoiceDate: string;
  dueDate: string;
  subtotal: number;
  taxAmount: number;
  discountAmount: number;
  totalAmount: number;
  paidAmount: number;
  balanceDue: number;
  status: InvoiceStatus;
  items: InvoiceItem[];
  payments: Payment[];
  patientName?: string;
  patientEmail?: string;
  patientPhone?: string;
  insuranceProvider?: string;
  insurancePolicyNumber?: string;
  insuranceClaimAmount: number;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
  sentAt?: string;
  paidAt?: string;
  userId?: number;
}

export interface InvoiceItem {
  id?: number;
  itemType: ItemType;
  description: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  serviceCode?: string;
  medicalRecordId?: number;
}

export interface Payment {
  id?: number;
  paymentReference?: string;
  paymentDate: string;
  amount: number;
  paymentMethod: PaymentMethod;
  status: PaymentStatus;
  transactionId?: string;
  cardLastFour?: string;
  cardType?: string;
  receiptNumber?: string;
  notes?: string;
  processedBy?: string;
  createdAt?: string;
  userId?: number;
}

export enum InvoiceStatus {
  DRAFT = 'DRAFT',
  PENDING = 'PENDING',
  SENT = 'SENT',
  PARTIALLY_PAID = 'PARTIALLY_PAID',
  PAID = 'PAID',
  OVERDUE = 'OVERDUE',
  CANCELLED = 'CANCELLED',
  REFUNDED = 'REFUNDED'
}

export enum ItemType {
  CONSULTATION = 'CONSULTATION',
  PROCEDURE = 'PROCEDURE',
  MEDICATION = 'MEDICATION',
  LAB_TEST = 'LAB_TEST',
  IMAGING = 'IMAGING',
  ROOM_CHARGE = 'ROOM_CHARGE',
  SURGERY = 'SURGERY',
  EMERGENCY_SERVICE = 'EMERGENCY_SERVICE',
  THERAPY = 'THERAPY',
  VACCINATION = 'VACCINATION',
  EQUIPMENT = 'EQUIPMENT',
  OTHER = 'OTHER'
}

export enum PaymentMethod {
  CASH = 'CASH',
  CREDIT_CARD = 'CREDIT_CARD',
  DEBIT_CARD = 'DEBIT_CARD',
  BANK_TRANSFER = 'BANK_TRANSFER',
  CHECK = 'CHECK',
  INSURANCE = 'INSURANCE',
  MOBILE_PAYMENT = 'MOBILE_PAYMENT',
  ONLINE_PAYMENT = 'ONLINE_PAYMENT',
  OTHER = 'OTHER'
}

export enum PaymentStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  REFUNDED = 'REFUNDED',
  CANCELLED = 'CANCELLED'
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
