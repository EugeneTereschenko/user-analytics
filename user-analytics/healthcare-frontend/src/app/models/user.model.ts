export enum UserType {
  PATIENT = 'PATIENT',
  DOCTOR = 'DOCTOR',
  ADMIN = 'ADMIN',
  STAFF = 'STAFF',
  PHARMACIST = 'PHARMACIST',
  RECEPTIONIST = 'RECEPTIONIST'
}

export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  userType: UserType;
  roles: string[];
  permissions: string[];
}

export interface AuthRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  userType: UserType;
  roles?: string[];
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  expiresIn: number;
  user: User;
}

export interface TokenValidationResponse {
  valid: boolean;
  username?: string;
  email?: string;
  userId?: number;
  userType?: UserType;
  roles?: string[];
  permissions?: string[];
  message: string;
}

export interface ApiResponse {
  success: boolean;
  message: string;
}

export interface PasswordResetRequest {
  email: string;
}

export interface PasswordResetConfirmRequest {
  token: string;
  newPassword: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}
