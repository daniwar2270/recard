import { Validators } from '@angular/forms';
import { FormControlTarget, FormGroupTarget } from '@codexio/ngx-reactive-forms-generator';
import { Card } from '../../../modules/shared/constants/validation.constants';
import { CardAvailability } from './card-availability';

@FormGroupTarget()
export class CardFilter {

  @FormControlTarget(Validators.required)
  public page: number = 0;

  @FormControlTarget()
  public name: string | null = null;

  @FormControlTarget()
  public availability: CardAvailability | null = null;

  @FormControlTarget([
    Validators.min(Card.MIN_ATTACK),
    Validators.max(Card.MAX_ATTACK)
  ])
  public minAttack: number | null = null;

  @FormControlTarget([
    Validators.min(Card.MIN_ATTACK),
    Validators.max(Card.MAX_ATTACK)
  ])
  public maxAttack: number | null = null;

  @FormControlTarget([
    Validators.min(Card.MIN_DEFENCE),
    Validators.max(Card.MAX_DEFENCE)
  ])
  public minDef: number| null = null;

  @FormControlTarget([
    Validators.min(Card.MIN_DEFENCE),
    Validators.max(Card.MAX_DEFENCE)
  ])
  @FormControlTarget()
  public maxDef: number | null = null;

}
