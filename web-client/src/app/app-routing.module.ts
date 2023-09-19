import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppRoutes } from './modules/shared/constants/routing.constants';

const routes: Routes = [
  {
    path: AppRoutes.HOME,
    pathMatch: 'full',
    loadChildren: () => import('./modules/shared/shared.module').then(m => m.SharedModule),
  },
  {
    path: AppRoutes.AUTH,
    loadChildren: () => import('./modules/authentication/authentication.module').then(m => m.AuthenticationModule)
  },
  {
    path: AppRoutes.PROFILE,
    loadChildren: () => import('./modules/profile/profile.module').then(m => m.ProfileModule)
  },
  {
    path: AppRoutes.GAME_CARDS,
    loadChildren: () => import('./modules/card/card.module').then(m => m.CardModule)
  },
  {
    path: '**',
    redirectTo: AppRoutes.PAGE_NOT_FOUND
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
