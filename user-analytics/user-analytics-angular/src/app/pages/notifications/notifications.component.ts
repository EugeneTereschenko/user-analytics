import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../services/notification.service';
import { Subscription } from 'rxjs';


export interface NotificationDTO {
  id?: number;
  title: string;
  message: string;
  timestamp: string;
  type?: 'info' | 'success' | 'warning' | 'error' | 'message';
  priority?: 'low' | 'medium' | 'high' | 'urgent';
  isRead?: boolean;
  readAt?: string;
  category?: string;
  actionUrl?: string;
  sender?: string;
  metadata?: any;
}

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent implements OnInit, OnDestroy {
  notifications: NotificationDTO[] = [];
  filteredNotifications: NotificationDTO[] = [];
  unreadCount = 0;
  expandedIndex: number | null = null;
  
  // Filters
  selectedType = 'all';
  selectedPriority = 'all';
  showOnlyUnread = false;
  searchQuery = '';
  
  // Sorting
  sortBy: 'date' | 'priority' | 'type' = 'date';
  sortOrder: 'asc' | 'desc' = 'desc';
  
  // UI State
  isLoading = false;
  showCreateModal = false;
  
  // New notification form
  newNotification: NotificationDTO = {
    title: '',
    message: '',
    timestamp: new Date().toISOString(),
    type: 'info',
    priority: 'medium'
  };
  
  private subscriptions = new Subscription();

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.loadNotifications();
    
    // Subscribe to notifications
    this.subscriptions.add(
      this.notificationService.getNotifications().subscribe(data => {
        this.notifications = data;
        this.applyFilters();
      })
    );
    
    // Subscribe to unread count
    this.subscriptions.add(
      this.notificationService.getUnreadCount().subscribe(count => {
        this.unreadCount = count;
      })
    );
    
    // Request notification permission
    this.notificationService.requestPermission();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  loadNotifications() {
    this.isLoading = true;
    this.notificationService.getAll().subscribe({
      next: () => {
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading notifications:', error);
        this.isLoading = false;
      }
    });
  }

  applyFilters() {
    let filtered = [...this.notifications];
    
    // Filter by type
    if (this.selectedType !== 'all') {
      filtered = filtered.filter(n => n.type === this.selectedType);
    }
    
    // Filter by priority
    if (this.selectedPriority !== 'all') {
      filtered = filtered.filter(n => n.priority === this.selectedPriority);
    }
    
    // Filter by unread
    if (this.showOnlyUnread) {
      filtered = filtered.filter(n => !n.isRead);
    }
    
    // Filter by search
    if (this.searchQuery.trim()) {
      const query = this.searchQuery.toLowerCase();
      filtered = filtered.filter(n => 
        n.title.toLowerCase().includes(query) || 
        n.message.toLowerCase().includes(query)
      );
    }
    
    // Sort
    filtered.sort((a, b) => {
      let comparison = 0;
      
      switch (this.sortBy) {
        case 'date':
          comparison = new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime();
          break;
        case 'priority':
          const priorityOrder = { urgent: 4, high: 3, medium: 2, low: 1 };
          comparison = (priorityOrder[a.priority || 'medium'] || 0) - 
                      (priorityOrder[b.priority || 'medium'] || 0);
          break;
        case 'type':
          comparison = (a.type || '').localeCompare(b.type || '');
          break;
      }
      
      return this.sortOrder === 'asc' ? comparison : -comparison;
    });
    
    this.filteredNotifications = filtered;
  }

  toggleAccordion(notification: NotificationDTO, index: number) {
    if (this.expandedIndex === index) {
      this.expandedIndex = null;
      this.onAccordionHide(notification, index);
    } else {
      this.expandedIndex = index;
      this.onAccordionShow(notification, index);
    }
  }

  onAccordionShow(notification: NotificationDTO, index: number) {
    console.log('Accordion opened:', notification);
    
    // Mark as read when opened
    if (notification.id && !notification.isRead) {
      this.notificationService.markAsRead(notification.id).subscribe();
    }
  }

  onAccordionHide(notification: NotificationDTO, index: number) {
    console.log('Accordion closed:', notification);
  }

  markAsRead(notification: NotificationDTO, event: Event) {
    event.stopPropagation();
    if (notification.id) {
      this.notificationService.markAsRead(notification.id).subscribe();
    }
  }

  markAllAsRead() {
    if (confirm('Mark all notifications as read?')) {
      this.notificationService.markAllAsRead().subscribe();
    }
  }

  deleteNotification(notification: NotificationDTO, event: Event) {
    event.stopPropagation();
    if (notification.id && confirm('Delete this notification?')) {
      this.notificationService.delete(notification.id).subscribe();
    }
  }

  deleteAllRead() {
    if (confirm('Delete all read notifications?')) {
      this.notificationService.deleteAllRead().subscribe();
    }
  }

  createNotification() {
    if (!this.newNotification.title || !this.newNotification.message) {
      alert('Please fill in all required fields');
      return;
    }
    
    this.notificationService.create(this.newNotification).subscribe({
      next: () => {
        this.closeCreateModal();
        this.resetForm();
      },
      error: (error) => {
        console.error('Error creating notification:', error);
        alert('Failed to create notification');
      }
    });
  }

  openCreateModal() {
    this.showCreateModal = true;
  }

  closeCreateModal() {
    this.showCreateModal = false;
  }

  resetForm() {
    this.newNotification = {
      title: '',
      message: '',
      timestamp: new Date().toISOString(),
      type: 'info',
      priority: 'medium'
    };
  }

  clearFilters() {
    this.selectedType = 'all';
    this.selectedPriority = 'all';
    this.showOnlyUnread = false;
    this.searchQuery = '';
    this.applyFilters();
  }

  getTypeIcon(type?: string): string {
    switch (type) {
      case 'success': return 'bi-check-circle-fill text-success';
      case 'warning': return 'bi-exclamation-triangle-fill text-warning';
      case 'error': return 'bi-x-circle-fill text-danger';
      case 'info': return 'bi-info-circle-fill text-info';
      case 'message': return 'bi-envelope-fill text-primary';
      default: return 'bi-bell-fill text-secondary';
    }
  }

  getPriorityBadge(priority?: string): string {
    switch (priority) {
      case 'urgent': return 'badge bg-danger';
      case 'high': return 'badge bg-warning text-dark';
      case 'medium': return 'badge bg-info';
      case 'low': return 'badge bg-secondary';
      default: return 'badge bg-secondary';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getRelativeTime(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    const minutes = Math.floor(diff / (1000 * 60));
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    
    if (days > 0) return `${days}d ago`;
    if (hours > 0) return `${hours}h ago`;
    if (minutes > 0) return `${minutes}m ago`;
    return 'Just now';
  }

  setSortBy(sortBy: 'date' | 'priority' | 'type') {
    if (this.sortBy === sortBy) {
      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = sortBy;
      this.sortOrder = 'desc';
    }
    this.applyFilters();
  }

  exportNotifications() {
    const dataStr = JSON.stringify(this.notifications, null, 2);
    const dataBlob = new Blob([dataStr], {type: 'application/json'});
    const url = URL.createObjectURL(dataBlob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `notifications_${new Date().toISOString()}.json`;
    link.click();
    URL.revokeObjectURL(url);
  }

  getUrgentCount(): number {
    return this.notifications.filter(n => n.priority === 'urgent').length;
  }
}