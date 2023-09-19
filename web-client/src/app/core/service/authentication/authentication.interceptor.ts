import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { catchError, finalize, Observable, switchMap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { AuthenticationService } from './authentication.service';
import { StatusMessageService } from '../modal/status-message.service';
import { LocalStorageService } from '../localStorage/local-storage.service';
import { AppNavigation } from '../../../modules/shared/constants/routing.constants';
import { DEFAULT_ERR } from '../../../modules/shared/constants/messages.constants';
import { Tokens } from '../../../modules/shared/constants/auth.constants';
import { Auth } from '../../../modules/shared/constants/pop-up.constants';
import { LoginResponse } from '../../models/authentication/login.response';
import { MessageType } from '../../models/modals/success-message.types';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

  private isRefreshing: boolean = false;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private messageService: StatusMessageService,
    private localStorageService: LocalStorageService
  ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    request = request.clone({
      url: `${environment.API_HOST}${request.url}`
    });

    if (this.authService.isAuthenticated()) {
      const token: string = this.isRefreshing ? `${Tokens.BEARER} ${this.authService.refreshToken!}` : this.authService.token!;

      request = request.clone({
        headers: request.headers.set(Tokens.AUTHORIZATION, token)
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 0) {
          return throwError(() => DEFAULT_ERR);
        }

        if (error.status === 401 && !this.isRefreshing) {
          this.handle401Error(request, next);
        } else if (error.status === 401 && this.isRefreshing) {
          this.messageService.initiate({
            title: Auth.SESSION_EXPIRED,
            content: Auth.LOGIN_AGAIN,
            type: MessageType.error
          });
          this.localStorageService.clear();
          this.router.navigate([AppNavigation.LOGIN]);
        }

        return throwError(() => error.error);
      })
    );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): void {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.authService.refreshAccessToken().pipe(
        switchMap((response: LoginResponse) => {
          localStorage.setItem(Tokens.ACCESS_TOKEN, response.accessToken);
          localStorage.setItem(Tokens.REFRESH_TOKEN, response.refreshToken);

          request = request.clone({
            headers: request.headers.set(Tokens.AUTH_HEADER, this.authService.token!)
          });

          return next.handle(request);
        }),
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error);
        }),
        finalize(() => {
          this.isRefreshing = false;
        })
      ).subscribe();
    }
  }

}
