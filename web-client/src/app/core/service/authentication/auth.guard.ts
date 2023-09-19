import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';
import { AppRoutes } from '../../../modules/shared/constants/routing.constants';

export const AuthGuard: CanActivateFn = (): boolean | Promise<boolean> => {

  const authService: AuthenticationService = inject(AuthenticationService);
  const router: Router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  return router.navigate([AppRoutes.AUTH, AppRoutes.LOGIN]);

};
