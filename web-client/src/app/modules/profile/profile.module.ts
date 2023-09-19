import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { ProfileRoutingModule } from './profile-routing.module';
import { CardModule } from '../card/card.module';
import { DeckModule } from '../deck/deck.module';
import { ProfileComponent } from './profile.component';
import { ProfileSidebarComponent } from './profile-sidebar/profile-sidebar.component';
import { ProfileAccountComponent } from './profile-account/profile-account.component';

@NgModule({
  declarations: [
    ProfileComponent,
    ProfileSidebarComponent,
    ProfileAccountComponent
  ],
  imports: [
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    ProfileRoutingModule,
    SharedModule,
    CardModule,
    DeckModule
  ]
})
export class ProfileModule { }
