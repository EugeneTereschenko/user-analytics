export enum AppointmentStatus {
  SCHEDULED = 'SCHEDULED',
  CONFIRMED = 'CONFIRMED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  NO_SHOW = 'NO_SHOW',
  RESCHEDULED = 'RESCHEDULED'
}

export enum AppointmentType {
  CONSULTATION = 'CONSULTATION',
  FOLLOW_UP = 'FOLLOW_UP',
  EMERGENCY = 'EMERGENCY',
  ROUTINE_CHECKUP = 'ROUTINE_CHECKUP',
  VACCINATION = 'VACCINATION',
  LABORATORY = 'LABORATORY',
  IMAGING = 'IMAGING',
  SURGERY = 'SURGERY',
  THERAPY = 'THERAPY'
}

export interface Appointment {
  id?: number;
  patientId: number;
  doctorId: number;
  userId: number;
  appointmentDateTime: string;
  durationMinutes: number;
  status: AppointmentStatus;
  appointmentType: AppointmentType;
  reason?: string;
  notes?: string;
  patientName?: string;
  patientEmail?: string;
  patientPhone?: string;
  doctorName?: string;
  doctorSpecialization?: string;
  createdAt?: string;
  updatedAt?: string;
  cancelledAt?: string;
  cancellationReason?: string;
}

export interface AppointmentDTO {
  id?: number;
  patientId: number;
  doctorId: number;
  userId: number;
  appointmentDateTime: string;
  durationMinutes: number;
  status: AppointmentStatus;
  appointmentType: AppointmentType;
  reason?: string;
  notes?: string;
  patientName?: string;
  patientEmail?: string;
  patientPhone?: string;
  doctorName?: string;
  doctorSpecialization?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
