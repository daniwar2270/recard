import { FormControlTarget, FormGroupTarget } from '@codexio/ngx-reactive-forms-generator';
import { Validators } from '@angular/forms';

@FormGroupTarget()
export class Filter {

  @FormControlTarget(Validators.required)
  public page: number = 0;

  @FormControlTarget()
  public name: string | null = null;

}
