import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameCardsComponent } from './game-cards/game-cards.component';
import { CardService } from '../../core/service/card/card.service';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { CardRoutingModule } from './card-routing.module';
import { PlayerCardsComponent } from './player-cards/player-cards.component';
import { CardFilterComponent } from './card-filter/card-filter.component';

@NgModule({
  declarations: [
    GameCardsComponent,
    PlayerCardsComponent,
    CardFilterComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    CardRoutingModule
  ],
  exports: [
    PlayerCardsComponent
  ],
  providers: [
    CardService
  ]
})
export class CardModule { }
