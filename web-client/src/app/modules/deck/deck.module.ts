import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlayerDecksComponent } from './player-decks/player-decks.component';
import { SharedModule } from '../shared/shared.module';
import { DeckComponent } from './deck/deck.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    PlayerDecksComponent,
    DeckComponent
  ],
  exports: [
    PlayerDecksComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule
  ]
})
export class DeckModule { }
