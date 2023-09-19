import { FormControlTarget } from '@codexio/ngx-reactive-forms-generator';
import { Validators } from '@angular/forms';
import { Deck } from '../../../modules/shared/constants/validation.constants';

export class DeckNameRequest {

  @FormControlTarget([
    Validators.required,
    Validators.minLength(Deck.MIN_LENGTH),
    Validators.maxLength(Deck.MAX_LENGTH),
    Validators.pattern(Deck.PATTERN)
  ])
  public name: string = '';

}
