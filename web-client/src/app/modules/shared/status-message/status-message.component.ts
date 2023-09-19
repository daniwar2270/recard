import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { animate, state, style, transition, trigger, } from '@angular/animations';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { SuccessMessageData } from '../../../core/models/modals/success-message.data';
import { MessageType } from '../../../core/models/modals/success-message.types';

@Component({
  selector: 'rec-status-message',
  templateUrl: './status-message.component.html',
  styleUrls: ['./status-message.component.scss'],
  animations: [
    trigger('openClose', [
      state(
        'closed',
        style({
          visibility: 'none',
          right: '-2rem',
        })
      ),
      state(
        'open',
        style({
          right: '2rem',
        }),
      ),
      transition('open <=> closed', [animate('0.5s ease-in-out')]),
    ]),
  ],
  encapsulation: ViewEncapsulation.None,
})
export class StatusMessageComponent implements OnInit {

  protected readonly ModalTypes: any = MessageType;

  constructor(
    public modalService: StatusMessageService
  ) { }

  public ngOnInit(): void {
    this.showStatusMessage();
  }

  public showStatusMessage(): void {
    this.modalService.open.subscribe((data: SuccessMessageData): void => {
      if (data.show) {
        this.countDown();
      }
    });
  }

  public countDown(): void {
    const progressInterval = setInterval(() => {
      if (this.modalService.data.progressWidth) {
        this.modalService.data.progressWidth -= 1;

        if (this.modalService.data.progressWidth <= 0) {
          this.modalService.hide();
          clearInterval(progressInterval);
        }
      }
    }, 50);
  }

  public closeModal(): void {
    this.modalService.data.progressWidth = 0;
    this.modalService.hide();
  }

}
