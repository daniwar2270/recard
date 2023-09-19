import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AppRoutes } from '../shared/constants/routing.constants';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
  {
    path: AppRoutes.HOME,
    redirectTo: AppRoutes.LOGIN,
    pathMatch: 'full'
  },
  {
    path: AppRoutes.LOGIN,
    component: LoginComponent
  },
  {
    path: AppRoutes.REGISTER,
    component: RegisterComponent
  },
  {
    path: '**',
    redirectTo: AppRoutes.LOGIN,
  }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class AuthenticationRoutingModule { }
