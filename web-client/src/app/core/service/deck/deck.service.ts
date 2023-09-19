import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { DeckResponse } from '../../models/deck/deck.response';
import { DeckRequest } from '../../models/deck/deck.request';
import { DeckCardsRequest } from '../../models/deck/deck-cards.request';
import { CardResponse } from '../../models/card/card.response';
import { HttpConstants } from '../../../modules/shared/constants/http.constants';

@Injectable({
  providedIn: 'root'
})
export class DeckService {

  constructor(
    private http: HttpClient
  ) { }

  public getDeckCards(data: DeckCardsRequest): Observable<CardResponse[]> {
    return this.http.get<any>(`${HttpConstants.API_DECK}/${data}`).pipe(
      map((response) => {
        return response.content;
      })
    )
  }

  public getPlayerDecks(data: number): Observable<DeckResponse[]> {
    const params: HttpParams = new HttpParams();
    params.set('page', data);

    return this.http.get<any>(HttpConstants.API_DECK).pipe(
      map((response) => {
        return response.content;
      })
    )
  }

  public addCardsToDeck(data: DeckRequest): Observable<DeckResponse> {
    const { id, cards } = { ...data };

    return this.http.post<DeckResponse>(`${HttpConstants.API_DECK}/${id}`, { cards });
  }

  public removeCardsFromDeck(data: DeckRequest): Observable<DeckResponse> {
    const { id, cards } = { ...data };

    return this.http.delete<DeckResponse>(`${HttpConstants.API_DECK}/${id}`, { body: cards });
  }

  public changeDeckName(data: DeckRequest): Observable<DeckResponse> {
    const { name, id } = { ...data };

    return this.http.patch<DeckResponse>(`${HttpConstants.API_DECK}/${id}`, { name })
  }

}
