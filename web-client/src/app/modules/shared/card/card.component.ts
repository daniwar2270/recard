import { Component, Input } from '@angular/core';
import { CardResponse } from '../../../core/models/card/card.response';

@Component({
  selector: 'rec-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent {

  @Input() cardInformation: CardResponse = <CardResponse>{};

}
