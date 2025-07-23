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

    // Simulated response (replace with real API later)
    this.messages.push({ sender: 'Bot', text: `You said: "${this.userInput}" 🤖` });
    this.userInput = '';

    // Send the message to the assistant
    this.assistantService.sendMessage(this.userInput).subscribe((response: { text?: string } | string) => {
      const botReply = typeof response === 'string' ? response : (response.text ?? JSON.stringify(response));
      this.messages.push({ sender: 'Bot', text: botReply });
    });
  }

}
