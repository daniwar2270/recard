import { FormControlTarget } from '@codexio/ngx-reactive-forms-generator';
import { Validators } from '@angular/forms';
import { FirstName, LastName } from '../../../modules/shared/constants/validation.constants';

export class ProfileRequest {

  @FormControlTarget([
    Validators.minLength(FirstName.MIN_LENGTH),
    Validators.maxLength(FirstName.MAX_LENGTH),
    Validators.pattern(FirstName.PATTERN)
  ])
  public firstName: string = '';

  @FormControlTarget([
    Validators.minLength(LastName.MIN_LENGTH),
    Validators.maxLength(LastName.MAX_LENGTH),
    Validators.pattern(LastName.PATTERN)
  ])
  public lastName: string = '';

  @FormControlTarget()
  public bornOn: string = '';

}
