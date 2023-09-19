import { FormControlTarget } from '@codexio/ngx-reactive-forms-generator';

export class ProfileRequestMock {

  @FormControlTarget()
  public firstName: string = '';

  @FormControlTarget()
  public lastName: string = '';

  @FormControlTarget()
  public bornOn: string = '';

}
