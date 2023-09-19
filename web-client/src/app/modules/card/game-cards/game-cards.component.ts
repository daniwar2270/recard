import { Component, OnInit } from '@angular/core';
import { CardResponse } from '../../../core/models/card/card.response';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { CardFilter } from '../../../core/models/card/card-filter';
import { CardService } from '../../../core/service/card/card.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { ERROR } from '../../shared/constants/pop-up.constants';
import { MessageType } from '../../../core/models/modals/success-message.types';
import { FilterService } from '../../../core/service/filter/filter.service';

@Component({
  selector: 'rec-game-cards',
  templateUrl: './game-cards.component.html',
  styleUrls: ['./game-cards.component.scss'],
  providers: [ FilterService ]
})
export class GameCardsComponent implements OnInit {

  private readonly filtersForm: ModelFormGroup<CardFilter> = toFormGroup<CardFilter>(CardFilter)!;

  public isFilterVisible: boolean = false;
  public filterService: FilterService = new FilterService(this.filtersForm);

  constructor(
    private cardService: CardService,
    private messageService: StatusMessageService
  ) { }

  public ngOnInit(): void {
    this.getCards();
  }

  public getCards(): void {
    this.filterService.filtersForm.controls['page'].setValue(this.filterService.startPage);

    this.cardService.getAllCards(this.filterService.filtersForm.value!).subscribe({
        next: (response: CardResponse[]) => {
          if (response.length === 0) {
            this.filterService.isScrollingFinished = true;
            this.filterService.startPage = 0;
          }
          this.filterService.itemList.push(...response);
          this.filterService.startPage++;
        },
        error: (error: Error) => {
          this.filterService.itemList = [];

          this.messageService.initiate({
            title: ERROR,
            content: error.message,
            type: MessageType.error
          });
        }
      }
    )
  }

}
