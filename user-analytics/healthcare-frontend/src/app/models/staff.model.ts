import { Gender, Address, PageResponse } from './doctor.model';

export enum StaffRole {
  NURSE = 'NURSE',
  RECEPTIONIST = 'RECEPTIONIST',
  PHARMACIST = 'PHARMACIST',
  LAB_TECHNICIAN = 'LAB_TECHNICIAN',
  RADIOLOGIST = 'RADIOLOGIST',
  ADMINISTRATOR = 'ADMINISTRATOR',
  MANAGER = 'MANAGER',
  SECURITY = 'SECURITY',
  CLEANER = 'CLEANER'
}

export enum StaffStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  ON_LEAVE = 'ON_LEAVE',
  SUSPENDED = 'SUSPENDED'
}

export interface Staff {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  dateOfBirth: string;
  gender: Gender;
  employeeId?: string;
  role: StaffRole;
  department: string;
  shift?: string;
  salary?: number;
  joinedDate?: string;
  address?: Address;
  status: StaffStatus;
  supervisorId?: number;
  emergencyContactName?: string;
  emergencyContactPhone?: string;
  profileImageUrl?: string;
  userId?: number;
  createdAt?: string;
  updatedAt?: string;
}

export { Gender, Address, PageResponse };
