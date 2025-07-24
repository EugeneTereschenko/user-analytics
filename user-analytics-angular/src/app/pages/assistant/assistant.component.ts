import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AssistantService } from '../../services/assistant.service';

interface Message {
  sender: string;
  text: string;
}

@Component({
  selector: 'app-assistant',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assistant.component.html',
  styleUrl: './assistant.component.css'
})
export class AssistantComponent {

  messages = [{ sender: 'Bot', text: 'Hello! How can I help you today?' }];
  userInput = '';

  constructor(private assistantService: AssistantService) { }
  ngOnInit() {
    this.loadAssistantData();
  }

  loadAssistantData() {
    // Placeholder for loading assistant data, implement as needed
    // Example: this.assistantService.getInitialMessages().subscribe(...)
  }

  sendMessage() {
  if (!this.userInput.trim()) return;
  this.messages.push({ sender: 'You', text: this.userInput });

  // Send the message to the assistant
  this.assistantService.sendMessage(this.userInput)
    .subscribe(
      (response: any) => {
        const botText = typeof response === 'string' ? response : (response?.text ?? JSON.stringify(response));
        this.messages.push({ sender: 'Bot', text: botText });
        this.userInput = ''; // <-- clear input only after successful send
      },
      error => {
        console.error('Error sending message:', error);
        const errMsg = error?.error?.error || 'Sorry, I could not process your request.';
        this.messages.push({ sender: 'Bot', text: errMsg });
        // Optionally clear input here too
        this.userInput = '';
      }
    );
}

}
