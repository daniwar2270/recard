import { TestBed } from '@angular/core/testing';
import { LocalStorageService } from './local-storage.service';

describe('LocalStorageService', () => {

  let service: LocalStorageService;
  const keyMock: string = 'token-key';
  const valueMock: string = 'token-value';

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  beforeEach(() => {
    service = TestBed.inject(LocalStorageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add item to localStorage', () =>  {
    service.setItem(keyMock, valueMock);
    expect(localStorage.getItem(keyMock)).toEqual(valueMock);
  });

  it('should get item from localStorage', () =>  {
    localStorage.setItem(keyMock, valueMock);
    expect(service.getItem(keyMock)).toEqual(valueMock);
  });

  it('should clear localStorage', () =>  {
    localStorage.setItem(keyMock, valueMock);
    service.clear();
    expect(localStorage.length).toEqual(0);
  });

  it('should remove item from localStorage', () =>  {
    localStorage.setItem(keyMock, valueMock);
    service.removeItem(keyMock);
    expect(localStorage.getItem(keyMock)).toBeNull();
  });

});
