import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { ValidationService } from '../../../core/service/validation/validation.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { AuthenticationService } from '../../../core/service/authentication/authentication.service';
import { RegisterRequest } from '../../../core/models/authentication/register.request';
import { MessageType } from '../../../core/models/modals/success-message.types';
import { AppNavigation } from '../../shared/constants/routing.constants';
import {
  BIRTH_DATE_INVALID,
  EMAIL_INVALID_FORMAT,
  FIELD_REQUIRED,
  FIRSTNAME_INVALID_FORMAT,
  LASTNAME_INVALID_FORMAT,
  PasswordMessages,
  USERNAME_INVALID_FORMAT
} from '../../shared/constants/messages.constants';
import { Auth, SUCCESS } from '../../shared/constants/pop-up.constants';
import { BIRTH_DATE_MAX, BIRTH_DATE_MIN } from '../../shared/constants/validation.constants';
import { FieldNamesConstants } from '../../shared/constants/fields.constants';

@Component({
  selector: 'rec-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  protected readonly requiredMessage: string = FIELD_REQUIRED;
  protected readonly usernameMessage: string = USERNAME_INVALID_FORMAT;
  protected readonly firstNameMessage: string = FIRSTNAME_INVALID_FORMAT;
  protected readonly lastNameMessage: string = LASTNAME_INVALID_FORMAT
  protected readonly emailMessage: string = EMAIL_INVALID_FORMAT;
  protected readonly minDate: string = BIRTH_DATE_MIN;
  protected readonly maxDate: string = BIRTH_DATE_MAX;
  protected readonly passwordMessages: any = PasswordMessages;
  protected readonly bornOnMessage: any = BIRTH_DATE_INVALID;
  protected readonly fieldNames: any = FieldNamesConstants;

  public isLoading: boolean = false;
  public isPasswordVisible: boolean = false;
  public isConfirmPasswordVisible: boolean = false;
  public registerForm: ModelFormGroup<RegisterRequest> = toFormGroup<RegisterRequest>(RegisterRequest)!;

  constructor(
    public validationService: ValidationService,
    private authService: AuthenticationService,
    private messageService: StatusMessageService,
    private router: Router
  ) { }

  public ngOnInit(): void {
    this.registerForm.reset();
  }

  public isValidForm(): boolean {
    return this.registerForm.valid &&
      !this.validationService.validateDateRange(this.registerForm, this.fieldNames.BORN_ON, this.minDate, this.maxDate) &&
      !this.validationService.checkPasswordsMatch(this.registerForm);
  }

  public registerUser(): void {
    if (this.registerForm.invalid || !this.registerForm.value) {
      return;
    }

    this.isLoading = true;

    this.authService.registerUser(this.registerForm.value).subscribe({
      next: (): void => {
        this.isLoading = false;
        this.messageService.initiate({
          title: SUCCESS,
          content: Auth.REGISTER_SUCCESS,
          type: MessageType.success
        });

        this.registerForm.reset();

        this.router.navigate([AppNavigation.LOGIN]);
      },
      error: (error: Error): void => {
        this.isLoading = false;
        this.messageService.initiate({
          title: Auth.REGISTER_FAILED,
          content: error.message,
          type: MessageType.error
        });
      }
    });
  }

}
