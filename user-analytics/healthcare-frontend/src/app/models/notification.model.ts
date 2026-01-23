export interface Notification {
  id?: number;
  recipientId: number;
  recipientEmail?: string;
  recipientPhone?: string;
  recipientName?: string;
  notificationType: string;
  channel: string;
  subject?: string;
  message: string;
  status?: string;
  scheduledTime?: string;
  sentTime?: string;
  errorMessage?: string;
  createdAt?: string;
}

export enum NotificationType {
  APPOINTMENT_REMINDER = 'APPOINTMENT_REMINDER',
  APPOINTMENT_CONFIRMATION = 'APPOINTMENT_CONFIRMATION',
  TEST_RESULT_ALERT = 'TEST_RESULT_ALERT',
  PRESCRIPTION_REMINDER = 'PRESCRIPTION_REMINDER'
}

export enum NotificationChannel {
  EMAIL = 'EMAIL',
  SMS = 'SMS',
  BOTH = 'BOTH'
}

export enum NotificationStatus {
  PENDING = 'PENDING',
  SENT = 'SENT',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED'
}
