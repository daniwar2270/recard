import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { AuthenticationService } from '../../../core/service/authentication/authentication.service';
import { LocalStorageService } from '../../../core/service/localStorage/local-storage.service';
import { ValidationService } from '../../../core/service/validation/validation.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { LoginRequest } from '../../../core/models/authentication/login.request';
import { LoginResponse } from '../../../core/models/authentication/login.response';
import { FIELD_REQUIRED } from '../../shared/constants/messages.constants';
import { AppRoutes } from '../../shared/constants/routing.constants';
import { Tokens } from '../../shared/constants/auth.constants';
import { Auth } from '../../shared/constants/pop-up.constants';
import { MessageType } from '../../../core/models/modals/success-message.types';

@Component({
  selector: 'login-page',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  protected readonly requiredMessage: string = FIELD_REQUIRED;

  public isLoading: boolean = false;
  public isPasswordVisible: boolean = false;
  public loginForm: ModelFormGroup<LoginRequest> = toFormGroup<LoginRequest>(LoginRequest)!;

  public ngOnInit(): void {
    this.loginForm.reset();
  }

  constructor(
    public validationService: ValidationService,
    private authService: AuthenticationService,
    private messageService: StatusMessageService,
    private router: Router,
    private localStorageService: LocalStorageService
  ) { }

  public loginUser(): void {
    if (this.loginForm.invalid || !this.loginForm.value) {
      return;
    }

    this.isLoading = true;

    this.authService.loginUser(this.loginForm.value).subscribe({
      next: (response: LoginResponse): void => {
        this.isLoading = false;
        this.localStorageService.setItem(Tokens.ACCESS_TOKEN, response.accessToken);
        this.localStorageService.setItem(Tokens.REFRESH_TOKEN, response.refreshToken);

        this.loginForm.reset();

        this.router.navigate([AppRoutes.PROFILE]);
      },
      error: (error: Error): void => {
        this.isLoading = false;

        this.messageService.initiate({
          title: Auth.LOGIN_FAILED,
          content: error.message,
          type: MessageType.error
        });
      }
    });
  }

}
