import { Validators } from '@angular/forms';
import { FormControlTarget } from '@codexio/ngx-reactive-forms-generator';
import { FirstName, LastName, Password, Username } from '../../../modules/shared/constants/validation.constants';

export class RegisterRequest {

  @FormControlTarget([
    Validators.required,
    Validators.minLength(FirstName.MIN_LENGTH),
    Validators.maxLength(FirstName.MAX_LENGTH),
    Validators.pattern(FirstName.PATTERN)
  ])
  public firstName: string = '';

  @FormControlTarget([
    Validators.required,
    Validators.minLength(LastName.MIN_LENGTH),
    Validators.maxLength(LastName.MAX_LENGTH),
    Validators.pattern(LastName.PATTERN)
  ])
  public lastName: string = '';

  @FormControlTarget([
    Validators.required,
    Validators.minLength(Username.MIN_LENGTH),
    Validators.maxLength(Username.MAX_LENGTH),
    Validators.pattern(Username.PATTERN)
  ])
  public username: string = '';

  @FormControlTarget([
    Validators.required,
    Validators.email
  ])
  public email: string = '';

  @FormControlTarget(Validators.required)
  public bornOn: string = '';

  @FormControlTarget([
    Validators.required,
    Validators.minLength(Password.MIN_LENGTH),
    Validators.maxLength(Password.MAX_LENGTH),
    Validators.pattern(Password.PATTERN)
  ])
  public password: string = '';

  @FormControlTarget(Validators.required)
  public confirm: string = '';

}
