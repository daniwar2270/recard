import { Component, OnInit } from '@angular/core';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { DeckService } from '../../../core/service/deck/deck.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { FilterService } from '../../../core/service/filter/filter.service';
import { DeckResponse } from '../../../core/models/deck/deck.response';
import { ERROR } from '../../shared/constants/pop-up.constants';
import { CARD_BACK_PATH } from '../../shared/constants/card.constants';
import { MessageType } from '../../../core/models/modals/success-message.types';

import { Filter } from '../../../core/models/filter';

@Component({
  selector: 'rec-player-decks',
  templateUrl: './player-decks.component.html',
  styleUrls: ['./player-decks.component.scss'],
  providers: [ FilterService ]
})
export class PlayerDecksComponent implements OnInit {

  public readonly CARD_BACK_PATH: string = CARD_BACK_PATH;
  private readonly filtersForm: ModelFormGroup<Filter> = toFormGroup<Filter>(Filter)!;

  public isLoading: boolean = false;
  public selectedDeck: DeckResponse | null = null;
  public filterService: FilterService = new FilterService(this.filtersForm);

  constructor(
    private deckService: DeckService,
    private messageService: StatusMessageService
  ) { }

  public ngOnInit(): void {
    this.getDecks();
  }

  public getDecks(): void {
    this.filterService.filtersForm.controls['page'].setValue(this.filterService.startPage);

    this.isLoading = true;

    this.deckService.getPlayerDecks(this.filterService.filtersForm.value!).subscribe({
      next: (response: DeckResponse[]) => {
        if (response.length === 0) {
          this.filterService.isScrollingFinished = true;
          this.filterService.startPage = 0;
        }

        this.filterService.itemList.push(...response);
        this.filterService.startPage++;
        this.isLoading = false;
      },
      error: (error: Error) => {
        this.filterService.itemList = [];
        this.isLoading = false;
        this.filterService.startPage = 0;

        this.messageService.initiate({
          title: ERROR,
          content: error.message,
          type: MessageType.error
        });
      }
    })
  }

  public selectDeck(deck: DeckResponse): void {
    this.selectedDeck = deck;
  }

  public resetSelectedDeck() : void {
    this.selectedDeck = null;
  }

}
