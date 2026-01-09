export enum DoctorStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  ON_LEAVE = 'ON_LEAVE',
  SUSPENDED = 'SUSPENDED'
}

export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}

export interface Address {
  id?: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
}

export interface Schedule {
  id?: number;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  slotDurationMinutes: number;
  isAvailable: boolean;
  notes?: string;
}

export interface Doctor {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  dateOfBirth: string;
  gender: Gender;
  licenseNumber?: string;
  specialization: string;
  qualifications?: string[];
  languages?: string[];
  yearsOfExperience?: number;
  consultationFee?: number;
  biography?: string;
  address?: Address;
  schedules?: Schedule[];
  status: DoctorStatus;
  joinedDate?: string;
  profileImageUrl?: string;
  department?: string;
  roomNumber?: string;
  emergencyContactName?: string;
  emergencyContactPhone?: string;
  userId?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
