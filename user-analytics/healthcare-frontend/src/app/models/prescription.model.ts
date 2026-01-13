export interface Prescription {
  id?: number;
  prescriptionNumber?: string;
  patientId: number;
  doctorId: number;
  medicalRecordId?: number;
  appointmentId?: number;
  prescriptionDate: string;
  validUntil?: string;
  status: PrescriptionStatus;
  medications: Medication[];
  patientName?: string;
  patientDateOfBirth?: string;
  doctorName?: string;
  doctorLicense?: string;
  pharmacyName?: string;
  pharmacyAddress?: string;
  diagnosis?: string;
  notes?: string;
  isRefillable?: boolean;
  refillsAllowed?: number;
  refillsRemaining?: number;
  dispensedAt?: string;
  dispensedBy?: string;
  createdAt?: string;
  updatedAt?: string;
  cancelledAt?: string;
  cancellationReason?: string;
  userId?: number;
}

export interface Medication {
  id?: number;
  medicationName: string;
  genericName?: string;
  drugCode?: string;
  dosage: string;
  dosageForm?: string;
  strength?: string;
  frequency: string;
  duration?: string;
  quantity?: number;
  unit?: string;
  route?: Route;
  instructions?: string;
  startDate?: string;
  endDate?: string;
  warnings?: string;
  sideEffects?: string;
  isGenericAllowed?: boolean;
  isControlledSubstance?: boolean;
  priority?: number;
}

export enum PrescriptionStatus {
  ACTIVE = 'ACTIVE',
  DISPENSED = 'DISPENSED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  EXPIRED = 'EXPIRED',
  ON_HOLD = 'ON_HOLD'
}

export enum Route {
  ORAL = 'ORAL',
  TOPICAL = 'TOPICAL',
  INTRAVENOUS = 'INTRAVENOUS',
  INTRAMUSCULAR = 'INTRAMUSCULAR',
  SUBCUTANEOUS = 'SUBCUTANEOUS',
  INHALATION = 'INHALATION',
  RECTAL = 'RECTAL',
  OPHTHALMIC = 'OPHTHALMIC',
  OTIC = 'OTIC',
  NASAL = 'NASAL',
  TRANSDERMAL = 'TRANSDERMAL',
  OTHER = 'OTHER'
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
