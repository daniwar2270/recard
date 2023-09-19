import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { SuccessMessageData } from '../../models/modals/success-message.data';

@Injectable({
  providedIn: 'root'
})
export class StatusMessageService {

  public data: SuccessMessageData = {
    title: '',
    content: '',
    show: false,
    progressWidth: 100
  };

  public open: Subject<SuccessMessageData> = new Subject<SuccessMessageData>();

  private reset(): void {
    this.data = {
      title: '',
      content: '',
      show: false,
      progressWidth: 100
    };
  }

  public initiate(data: SuccessMessageData): void {
    this.data = {
      ...data,
      show: true,
      progressWidth: 100
    };
    this.open.next(this.data);
  }

  public hide(): void {
    this.data = {
      ...this.data,
      show: false,
      progressWidth: 0
    };
    this.open.next(this.data);
    this.reset();
  }

}
