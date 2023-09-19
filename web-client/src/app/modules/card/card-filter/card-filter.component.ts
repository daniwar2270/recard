import { Component, EventEmitter, Output } from '@angular/core';
import { Card } from '../../shared/constants/validation.constants';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { CardFilter } from '../../../core/models/card/card-filter';

@Component({
  selector: 'rec-card-filter',
  templateUrl: './card-filter.component.html',
  styleUrls: ['./card-filter.component.scss']
})
export class CardFilterComponent {

  protected readonly Card = Card;

  @Output() applyFilters: EventEmitter<void> = new EventEmitter();
  @Output() clearFilters: EventEmitter<void> = new EventEmitter();

  public filtersForm: ModelFormGroup<CardFilter> = toFormGroup<CardFilter>(CardFilter)!;

  public submitFilters(): void {
    this.applyFilters.emit();
  }

  public resetFilters(): void {
    this.clearFilters.emit();
  }

}
