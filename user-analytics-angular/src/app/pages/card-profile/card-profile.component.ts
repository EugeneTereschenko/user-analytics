import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CardEditService } from '../../services/card-edit.service';

@Component({
  selector: 'app-card-profile',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './card-profile.component.html',
  styleUrl: './card-profile.component.css'
})
export class CardProfileComponent {
  cardNumber: string = '';
  expiryDate: string = '';
  cvc: string = '';

  constructor(private cardEditService: CardEditService) { }

  onSubmitCard() {
    const cardData = {
      cardNumber: this.cardNumber,
      expirationDate: this.expiryDate,
      cvv: this.cvc,
      cardName: '' // This can be made dynamic if needed
    };
    this.cardEditService.sendCardData(cardData).subscribe({
      next: (res: any) => {
        console.log('Card data sent successfully', res);
      },
      error: (err: any) => {
        console.error('Failed to send card data', err);
      }
    });
  }
}
