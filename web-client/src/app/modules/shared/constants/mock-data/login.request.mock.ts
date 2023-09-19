import { FormControlTarget } from '@codexio/ngx-reactive-forms-generator';

export class LoginRequestMock {

  @FormControlTarget()
  public username: string = '';

  @FormControlTarget()
  public password: string = '';

}
