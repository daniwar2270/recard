import { TestBed } from '@angular/core/testing';
import { StatusMessageService } from './status-message.service';
import { SuccessMessageData } from '../../models/modals/success-message.data';

describe('StatusMessageService', () => {

  let service: StatusMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  beforeEach(() => {
    service = TestBed.inject(StatusMessageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should clear data when hide method is called', () => {
    const dataMock: SuccessMessageData = {
      title: '',
      content: '',
      show: false,
      progressWidth: 100
    };
    service.data = {
      title: 'Title-mock',
      content: 'Content-mock',
      show: true,
      progressWidth: 80
    };
    service.hide();
    expect(service.data).toEqual(dataMock);
  });

  it('should set correct data when initiate is called', () => {
    const dataMock: SuccessMessageData = {
      title: 'Title-mock',
      content: 'Content-mock',
      show: true,
      progressWidth: 100
    };
    service.initiate({
      title: 'Title-mock',
      content: 'Content-mock',
    });
    expect(service.data).toEqual(dataMock);
  });

});
