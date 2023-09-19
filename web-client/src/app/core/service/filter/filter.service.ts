import { Injectable } from '@angular/core';
import { ModelFormGroup } from '@codexio/ngx-reactive-forms-generator';

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  public itemList: any[] = [];

  private _filtersForm: ModelFormGroup<any>;
  private _startPage: number = 0;
  private _isScrollingFinished: boolean = false;

  constructor(filtersForm: ModelFormGroup<any>) {
    this._filtersForm = filtersForm;
  }

  public get startPage(): number {
    return this._startPage;
  }

  public set startPage(page: number) {
    this._startPage = page;
  }

  public get isScrollingFinished(): boolean {
    return this._isScrollingFinished;
  }

  public set isScrollingFinished(isFinished: boolean) {
    this._isScrollingFinished = isFinished;
  }

  public get filtersForm(): ModelFormGroup<any> {
    return this._filtersForm;
  }

  public applyFilters(getItems: () => void): void {
    this.itemList = [];
    this.startPage = 0;
    this.isScrollingFinished = true;
    getItems();
  }

  public clearFilters(getItems: () => void): void {
    this.filtersForm.reset();
    this.itemList = [];
    this.startPage = 0;
    this.isScrollingFinished = true;
    getItems();
  }

  public loadNextItems(getItems: () => void): void {
    if (!this.isScrollingFinished) {
      getItems();
    }
  }

}
