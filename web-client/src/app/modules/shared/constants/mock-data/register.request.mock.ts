import { FormControlTarget } from '@codexio/ngx-reactive-forms-generator';

export class RegisterRequestMock {

  @FormControlTarget()
  public firstName: string = '';

  @FormControlTarget()
  public lastName: string = '';

  @FormControlTarget()
  public username: string = '';

  @FormControlTarget()
  public email: string = '';

  @FormControlTarget()
  public bornOn: string = '';

  @FormControlTarget()
  public password: string = '';

  @FormControlTarget()
  public confirm: string = '';

}
