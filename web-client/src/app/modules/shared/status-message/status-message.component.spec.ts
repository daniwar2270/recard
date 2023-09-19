import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StatusMessageComponent } from './status-message.component';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { SuccessMessageData } from '../../../core/models/modals/success-message.data';

describe('StatusMessageComponent', () => {

  let component: StatusMessageComponent;
  let fixture: ComponentFixture<StatusMessageComponent>;
  let messageService: StatusMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StatusMessageComponent],
      imports: [BrowserAnimationsModule]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusMessageComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();

    messageService = TestBed.inject(StatusMessageService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call method to show message when we initiate the component', () => {
    spyOn(component, 'showStatusMessage');
    component.ngOnInit();
    expect(component.showStatusMessage).toHaveBeenCalled();
  });

  it('should call hide method from service when we want to close the modal', () => {
    spyOn(messageService, 'hide');
    component.closeModal();
    expect(messageService.hide).toHaveBeenCalled();
  });

  it('should start countDown when we want to show status message', () => {
    const data: SuccessMessageData = {
      title: 'Title-mock',
      content: 'Content-mock',
      show: true
    };
    spyOn(component, 'countDown');
    messageService.open.next(data);
    expect(component.countDown).toHaveBeenCalled();
  });

  it('should not start countDown when the data we pass has property show that is false', () => {
    const data: SuccessMessageData = {
      title: 'Title-mock',
      content: 'Content-mock',
      show: false
    };
    spyOn(component, 'countDown');
    messageService.open.next(data);
    expect(component.countDown).not.toHaveBeenCalled();
  });

  it('should decrease progressWidth and hide modal when it reaches zero', fakeAsync(() => {
    component.modalService.data.progressWidth = 10;
    spyOn(component.modalService, 'hide');
    component.countDown();
    tick(51 * 10);
    expect(component.modalService.hide).toHaveBeenCalled();
  }));

});
