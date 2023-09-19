import { Component, Input } from '@angular/core';
import { CardResponse } from '../../../core/models/card/card.response';

@Component({
  selector: 'rec-cards-list',
  templateUrl: './cards-list.component.html',
  styleUrls: ['./cards-list.component.scss']
})
export class CardsListComponent {

  @Input() cardsList : CardResponse[] = [];

}
