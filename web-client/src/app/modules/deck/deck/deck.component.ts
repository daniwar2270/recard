import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { DeckService } from '../../../core/service/deck/deck.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { ValidationService } from '../../../core/service/validation/validation.service';
import { DeckNameRequest } from '../../../core/models/deck/deck-name.request';
import { DeckRequest } from '../../../core/models/deck/deck.request';
import { DeckResponse } from '../../../core/models/deck/deck.response';
import { CardResponse } from '../../../core/models/card/card.response';
import { Deck, ERROR, SUCCESS } from '../../shared/constants/pop-up.constants';
import { DECK_INVALID_NAME, FIELD_REQUIRED } from '../../shared/constants/messages.constants';
import { MessageType } from '../../../core/models/modals/success-message.types';

@Component({
  selector: 'rec-deck',
  templateUrl: './deck.component.html',
  styleUrls: ['./deck.component.scss']
})
export class DeckComponent implements OnInit {

  protected readonly FIELD_REQUIRED: string = FIELD_REQUIRED;
  protected readonly DECK_INVALID_NAME: string = DECK_INVALID_NAME;

  @Input() deck: DeckResponse = <DeckResponse>({});

  @Output() resetSelectedDeck: EventEmitter<void> = new EventEmitter<void>();

  public isLoading: boolean = false;
  public isReadOnly: boolean = true;
  public deckCards: CardResponse[] = [];
  public deckNewNameRequest: DeckRequest = <DeckRequest>({});
  public changeNameForm: ModelFormGroup<DeckNameRequest> = toFormGroup<DeckNameRequest>(DeckNameRequest)!;

  constructor(
    public validationService: ValidationService,
    private deckService: DeckService,
    private messageService: StatusMessageService
  ) { }

  public ngOnInit(): void {
    this.changeNameForm.setValue({
      name: this.deck.name
    });
  }

  public goBack(): void {
    this.resetSelectedDeck.emit();
  }

  public clearForm(): void {
    this.isReadOnly=true;

    this.changeNameForm.setValue({
      name: this.deck.name
    });
  }

  public changeDeckName(): void {
    if (!this.changeNameForm.value || this.changeNameForm.invalid) {
      return;
    }

    if (this.deck.name === this.changeNameForm.get(['name'])?.value) {
      return;
    }

    this.deckNewNameRequest = {
      id: this.deck.id,
      name: this.changeNameForm.get(['name'])?.value
    };
    this.isLoading = true;

    this.deckService.changeDeckName(this.deckNewNameRequest).subscribe({
      next: (response: DeckResponse) => {
        this.deck = response;
        this.isLoading = false;
        this.clearForm();

        this.messageService.initiate({
          title: SUCCESS,
          content: Deck.NAME_CHANGED,
          type: MessageType.success
        });
      },
      error: (error: Error) => {
        this.isLoading = false;
        this.clearForm();

        this.messageService.initiate({
          title: ERROR,
          content: error.message,
          type: MessageType.error
        });
      }
    })
  }

}
