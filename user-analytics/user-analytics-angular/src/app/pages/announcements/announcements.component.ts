import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnnouncementsService } from '../../services/announcements.service';
import { Subscription } from 'rxjs';

export interface AnnouncementDTO {
  id?: number;
  title: string;
  body: string;
  date: string;
  author?: string;
  priority?: 'low' | 'medium' | 'high' | 'urgent';
  category?: 'general' | 'maintenance' | 'feature' | 'security' | 'event';
  isRead?: boolean;
  expiryDate?: string;
  attachments?: string[];
}


@Component({
  selector: 'app-announcements',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './announcements.component.html',
  styleUrl: './announcements.component.css'
})
export class AnnouncementsComponent implements OnInit, OnDestroy {
  announcements: AnnouncementDTO[] = [];
  filteredAnnouncements: AnnouncementDTO[] = [];
  unreadCount = 0;
  isLoading = false;
  errorMessage = '';
  
  // Expose Math to template
  Math = Math;
  
  // Filters
  selectedPriority = 'all';
  selectedCategory = 'all';
  searchQuery = '';
  showOnlyUnread = false;
  
  // Pagination
  currentPage = 1;
  itemsPerPage = 5;
  totalPages = 1;
  
  // Modal state
  showCreateModal = false;
  showDetailModal = false;
  selectedAnnouncement: AnnouncementDTO | null = null;
  
  // Form data
  newAnnouncement: AnnouncementDTO = {
    title: '',
    body: '',
    date: new Date().toISOString(),
    priority: 'medium',
    category: 'general'
  };
  
  private subscriptions = new Subscription();

  constructor(private announcementsService: AnnouncementsService) {}

  ngOnInit(): void {
    this.loadAnnouncements();
    
    // Subscribe to unread count
    this.subscriptions.add(
      this.announcementsService.getUnreadCount().subscribe(count => {
        this.unreadCount = count;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  loadAnnouncements(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.announcementsService.getAllAnnouncements().subscribe({
      next: (data) => {
        this.announcements = data;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error fetching announcements:', error);
        this.errorMessage = 'Failed to load announcements. Please try again.';
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    let filtered = [...this.announcements];
    
    // Filter by priority
    if (this.selectedPriority !== 'all') {
      filtered = filtered.filter(a => a.priority === this.selectedPriority);
    }
    
    // Filter by category
    if (this.selectedCategory !== 'all') {
      filtered = filtered.filter(a => a.category === this.selectedCategory);
    }
    
    // Filter by unread
    if (this.showOnlyUnread) {
      filtered = filtered.filter(a => !a.isRead);
    }
    
    // Filter by search query
    if (this.searchQuery.trim()) {
      const query = this.searchQuery.toLowerCase();
      filtered = filtered.filter(a => 
        a.title.toLowerCase().includes(query) || 
        a.body.toLowerCase().includes(query)
      );
    }
    
    this.filteredAnnouncements = filtered;
    this.totalPages = Math.ceil(filtered.length / this.itemsPerPage);
    this.currentPage = 1;
  }

  get paginatedAnnouncements(): AnnouncementDTO[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    return this.filteredAnnouncements.slice(start, end);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  viewAnnouncement(announcement: AnnouncementDTO): void {
    this.selectedAnnouncement = announcement;
    this.showDetailModal = true;
    
    // Mark as read
    if (announcement.id && !announcement.isRead) {
      this.announcementsService.markAsRead(announcement.id).subscribe(() => {
        this.loadAnnouncements();
      });
    }
  }

  createAnnouncement(): void {
    if (!this.newAnnouncement.title || !this.newAnnouncement.body) {
      alert('Please fill in all required fields');
      return;
    }
    
    this.announcementsService.createAnnouncement(this.newAnnouncement).subscribe({
      next: () => {
        this.loadAnnouncements();
        this.closeCreateModal();
        this.resetForm();
      },
      error: (error) => {
        console.error('Error creating announcement:', error);
        alert('Failed to create announcement');
      }
    });
  }

  deleteAnnouncement(id: number): void {
    if (confirm('Are you sure you want to delete this announcement?')) {
      this.announcementsService.deleteAnnouncement(id).subscribe({
        next: () => {
          this.loadAnnouncements();
          this.closeDetailModal();
        },
        error: (error) => {
          console.error('Error deleting announcement:', error);
          alert('Failed to delete announcement');
        }
      });
    }
  }

  markAllAsRead(): void {
    this.announcementsService.markAllAsRead().subscribe(() => {
      this.loadAnnouncements();
    });
  }

  openCreateModal(): void {
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.selectedAnnouncement = null;
  }

  resetForm(): void {
    this.newAnnouncement = {
      title: '',
      body: '',
      date: new Date().toISOString(),
      priority: 'medium',
      category: 'general'
    };
  }

  getPriorityClass(priority?: string): string {
    switch (priority) {
      case 'urgent': return 'badge bg-danger';
      case 'high': return 'badge bg-warning';
      case 'medium': return 'badge bg-info';
      case 'low': return 'badge bg-secondary';
      default: return 'badge bg-secondary';
    }
  }

  getCategoryIcon(category?: string): string {
    switch (category) {
      case 'maintenance': return 'bi-tools';
      case 'feature': return 'bi-star';
      case 'security': return 'bi-shield-check';
      case 'event': return 'bi-calendar-event';
      default: return 'bi-info-circle';
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
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const days = Math.floor(hours / 24);
    
    if (days > 0) return `${days} day${days > 1 ? 's' : ''} ago`;
    if (hours > 0) return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    return 'Just now';
  }

  getUrgentCount(): number {
    return this.announcements.filter(a => a.priority === 'urgent').length;
  }
}