import { NgModule } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './footer/footer.component';
import { NavigationComponent } from './navigation/navigation.component';
import { StatusMessageComponent } from './status-message/status-message.component';
import { LoaderComponent } from './loader/loader.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import { SharedRoutingModule } from './shared-routing.module';
import { ScrollTrackerDirective } from '../../core/directives/scroll-tracker.directive';
import { CardComponent } from './card/card.component';
import { CardsListComponent } from './cards-list/cards-list.component';

@NgModule({
  declarations: [
    FooterComponent,
    NavigationComponent,
    LoaderComponent,
    NavigationComponent,
    StatusMessageComponent,
    ErrorPageComponent,
    ScrollTrackerDirective,
    CardComponent,
    CardsListComponent
  ],
  imports: [
    RouterLink,
    RouterLinkActive,
    CommonModule,
    SharedRoutingModule
  ],
  exports: [
    FooterComponent,
    NavigationComponent,
    LoaderComponent,
    NavigationComponent,
    StatusMessageComponent,
    ScrollTrackerDirective,
    CardComponent,
    CardsListComponent
  ]
})
export class SharedModule { }
