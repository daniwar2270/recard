import { Validators } from '@angular/forms';
import { FormControlTarget } from '@codexio/ngx-reactive-forms-generator';

export class LoginRequest {

  @FormControlTarget(Validators.required)
  public username: string = '';

  @FormControlTarget(Validators.required)
  public password: string = '';

}
