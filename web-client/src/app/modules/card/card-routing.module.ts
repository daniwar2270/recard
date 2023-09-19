import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AppRoutes } from '../shared/constants/routing.constants';
import { GameCardsComponent } from './game-cards/game-cards.component';

const routes: Routes = [
  {
    path: AppRoutes.HOME,
    component: GameCardsComponent,
    pathMatch: 'full'
  },
  {
    path: '**',
    component: GameCardsComponent
  }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class CardRoutingModule { }
