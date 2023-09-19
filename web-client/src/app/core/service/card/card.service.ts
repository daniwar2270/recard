import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { CardResponse } from '../../models/card/card.response';
import { CardFilter } from '../../models/card/card-filter';
import { HttpConstants } from '../../../modules/shared/constants/http.constants';

@Injectable({
  providedIn: 'root'
})
export class CardService {

  constructor(
    private http: HttpClient
  ) { }

  public getAllCards(data: CardFilter): Observable<CardResponse[]> {
    const params: HttpParams = this.setCardFilterParams(data);

    return this.http.get<any>(HttpConstants.API_ALL_CARDS, { params }).pipe(
      map((response) => {
        return response.content;
      })
    )
  }

  public getCardById(data: number): Observable<CardResponse> {
    return this.http.get<CardResponse>(`${HttpConstants.API_ALL_CARDS}/${data}`);
  }

  public getPlayerCards(data: CardFilter): Observable<CardResponse[]> {
    const params: HttpParams = this.setCardFilterParams(data);

    return this.http.get<any>(HttpConstants.API_PLAYER_CARDS, { params }).pipe(
      map((response) => {
        return response.content;
      })
    )
  }

  private setCardFilterParams(data: CardFilter): HttpParams {
    let httpParams: HttpParams = new HttpParams();

    for (const [key, value] of Object.entries(data)) {
      if(value !== null) {
        httpParams = httpParams.set(key, value);
      }
    }

    return httpParams;
  }

}
