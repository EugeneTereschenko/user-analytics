import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CardEditService } from '../../card-edit.service';

@Component({
  selector: 'app-add-card-profile',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './add-card-profile.component.html',
  styleUrl: './add-card-profile.component.css'
})
export class AddCardProfileComponent {
  cardNumber: string = '5136 1845 5468 3894';
  expirationDate: string = '05/20';
  cvv: string = '123';
  cardName: string = 'VALDIMIR BEREZOVKIY';
  Submit_Payment: string = 'Submit Payment';

  constructor(private cardEditService: CardEditService) { }

  onSubmitCard() {
    const cardData = {
      cardNumber: this.cardNumber,
      expirationDate: this.expirationDate,
      cvv: this.cvv,
      cardName: this.cardName
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
