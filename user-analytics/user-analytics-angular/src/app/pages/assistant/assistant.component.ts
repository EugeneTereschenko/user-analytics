// ============================================
// Enhanced Assistant Component TypeScript
// ============================================
import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AssistantService } from '../../services/assistant.service';

interface Message {
  sender: string;
  text: string;
  timestamp: Date;
  type?: 'user' | 'assistant';
}

@Component({
  selector: 'app-assistant',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assistant.component.html',
  styleUrls: ['./assistant.component.css']
})
export class AssistantComponent implements OnInit, AfterViewChecked {
  @ViewChild('chatBox') private chatBox!: ElementRef;

  messages: Message[] = [];
  userInput: string = '';
  loading: boolean = false;
  private shouldScroll = false;

  // Quick commands
  quickCommands = [
    { label: '📊 Users Today', command: '/users today' },
    { label: '📈 Weekly Stats', command: '/stats weekly' },
    { label: '👤 Active User', command: '/active' },
    { label: '❓ Help', command: '/help' }
  ];

  constructor(private assistantService: AssistantService) {}

  ngOnInit(): void {
    this.loadConversationHistory();
    
    // Show welcome message if no history
    if (this.messages.length === 0) {
      this.showWelcomeMessage();
    }
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  /**
   * Send a message to the assistant
   */
  sendMessage(): void {
    if (!this.userInput.trim() || this.loading) {
      return;
    }

    const userMessage = this.userInput.trim();
    
    // Add user message
    this.addMessage('You', userMessage, 'user');
    this.userInput = '';
    this.loading = true;

    // Send to backend using AssistantService
    this.assistantService.sendMessage(userMessage, this.getSessionId()).subscribe({
      next: (response) => {
        this.addMessage('Assistant', response.text, 'assistant');
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        const errorMessage = error.error?.text || 'Sorry, I encountered an error. Please try again.';
        this.addMessage('Assistant', errorMessage, 'assistant');
        this.loading = false;
      }
    });
  }

  /**
   * Send a quick command
   */
  sendQuickCommand(command: string): void {
    this.userInput = command;
    this.sendMessage();
  }

  /**
   * Add a message to the conversation
   */
  private addMessage(sender: string, text: string, type: 'user' | 'assistant'): void {
    const message: Message = {
      sender,
      text,
      timestamp: new Date(),
      type
    };
    
    this.messages.push(message);
    this.saveConversationHistory();
    this.shouldScroll = true;
  }

  /**
   * Show welcome message
   */
  private showWelcomeMessage(): void {
    this.messages = [{
      sender: 'Assistant',
      text: '👋 Hello! I\'m your analytics assistant.\n\n' +
            'I can help you with:\n' +
            '• User statistics\n' +
            '• Weekly reports\n' +
            '• Active user information\n\n' +
            'Try clicking a quick command below or ask me anything!',
      timestamp: new Date(),
      type: 'assistant'
    }];
    this.shouldScroll = true;
  }

  /**
   * Clear conversation history
   */
  clearHistory(): void {
    if (confirm('Clear conversation history?')) {
      this.messages = [];
      localStorage.removeItem('assistant_conversation');
      this.showWelcomeMessage();
    }
  }

  /**
   * Save conversation to localStorage
   */
  private saveConversationHistory(): void {
    try {
      localStorage.setItem('assistant_conversation', JSON.stringify(this.messages));
    } catch (error) {
      console.error('Failed to save conversation:', error);
    }
  }

  /**
   * Load conversation from localStorage
   */
  private loadConversationHistory(): void {
    try {
      const saved = localStorage.getItem('assistant_conversation');
      if (saved) {
        this.messages = JSON.parse(saved).map((m: any) => ({
          ...m,
          timestamp: new Date(m.timestamp)
        }));
      }
    } catch (error) {
      console.error('Failed to load conversation:', error);
    }
  }

  /**
   * Scroll to bottom
   */
  private scrollToBottom(): void {
    try {
      if (this.chatBox) {
        this.chatBox.nativeElement.scrollTop = this.chatBox.nativeElement.scrollHeight;
      }
    } catch (error) {
      console.error('Scroll error:', error);
    }
  }

  /**
   * Format timestamp
   */
  formatTime(date: Date): string {
    return new Date(date).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Get session ID
   */
  private getSessionId(): string {
    let sessionId = sessionStorage.getItem('assistant_session_id');
    if (!sessionId) {
      sessionId = `session_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
      sessionStorage.setItem('assistant_session_id', sessionId);
    }
    return sessionId;
  }

  /**
   * Check if text is a command
   */
  isCommand(text: string): boolean {
    return text.trim().startsWith('/');
  }
}
