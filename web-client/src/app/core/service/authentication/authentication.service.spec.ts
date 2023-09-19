import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { AuthenticationService } from './authentication.service';
import { LoginResponse } from '../../models/authentication/login.response';
import { HttpConstants } from '../../../modules/shared/constants/http.constants';
import { RegisterResponse } from '../../models/authentication/register.response';
import { LocalStorageService } from '../localStorage/local-storage.service';
import { LoginRequest } from '../../models/authentication/login.request';
import { RegisterRequest } from '../../models/authentication/register.request';

describe('AuthenticationService', () => {

  let service: AuthenticationService;
  let localStorageService: LocalStorageService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
  });

  beforeEach(() => {
    service = TestBed.inject(AuthenticationService);
    localStorageService = TestBed.inject(LocalStorageService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return the access token from localStorage', () => {
    const accessTokenMock: string = 'AccessTokenMock';
    spyOn(localStorageService, 'getItem').and.returnValue(accessTokenMock);
    expect(service.token).toBe(accessTokenMock);
  });

  it('should return null when the access token is not present in localStorage', () => {
    spyOn(localStorageService, 'getItem').and.returnValue(null);
    expect(service.token).toBeNull();
  });

  it('should return the refresh token from localStorage', () => {
    const refreshTokenMock: string = 'RefreshTokenMock';
    spyOn(localStorageService, 'getItem').and.returnValue(refreshTokenMock);
    expect(service.refreshToken).toBe(refreshTokenMock);
  });

  it('should return null when the refresh token is not present in localStorage', () => {
    spyOn(localStorageService, 'getItem').and.returnValue(null);
    expect(service.refreshToken).toBeNull();
  });

  it('should authenticate user', () => {
    const accessTokenMock: string = 'AccessTokenMock';
    spyOnProperty(service, 'token', 'get').and.returnValue(accessTokenMock);
    expect(service.isAuthenticated()).toBeTrue();
  });

  it('should not authenticate user', () => {
    spyOnProperty(service, 'token', 'get').and.returnValue(null);
    expect(service.isAuthenticated()).toBeFalse();
  });

  it('should make a request to login a user and return tokens', () => {
    const loginRequestMock: LoginRequest = {
      username: 'usernameMock',
      password: 'passwordMock'
    };
    const loginResponseMock: LoginResponse = {
      accessToken: 'AccessTokenMock',
      refreshToken: 'RefreshTokenMock'
    };
    let responseMock!: LoginResponse;
    service.loginUser(loginRequestMock).subscribe((res: LoginResponse) => {
      responseMock = res;
    });
    const reqMock: TestRequest = httpTestingController.expectOne(HttpConstants.API_LOGIN);
    reqMock.flush(loginResponseMock, {
      headers: { 'Authorization': loginResponseMock.accessToken }
    });
    expect(reqMock.request.method).toBe('POST');
    expect(responseMock).toEqual(loginResponseMock);
  });

  it('should return empty tokens if response data is missing', () => {
    const loginRequestMock: LoginRequest = {
      username: 'usernameMock',
      password: 'passwordMock'
    };
    let response!: LoginResponse;
    service.loginUser(loginRequestMock).subscribe((res: LoginResponse) => {
      response = res;
    });
    const req: TestRequest = httpTestingController.expectOne(HttpConstants.API_LOGIN);
    req.flush(null);
    expect(req.request.method).toBe('POST');
    expect(response.accessToken).toBe('');
    expect(response.refreshToken).toBe('');
  });

  it('should make a request to refresh the access token and return new tokens', () => {
    const loginResponseMock: LoginResponse = {
      accessToken: 'AccessTokenMock',
      refreshToken: 'RefreshTokenMock'
    };
    let responseMock!: LoginResponse;
    service.refreshAccessToken().subscribe((res: LoginResponse) => {
      responseMock = res;
    });
    const reqMock: TestRequest = httpTestingController.expectOne(HttpConstants.API_REFRESH);
    reqMock.flush(loginResponseMock, {
      headers: { 'Authorization': loginResponseMock.accessToken }
    });
    expect(reqMock.request.method).toBe('PUT');
    expect(responseMock).toEqual(loginResponseMock);
  });

  it('should make a request to refresh the access token and return empty tokens', () => {
    let responseMock!: LoginResponse;
    service.refreshAccessToken().subscribe((res: LoginResponse) => {
      responseMock = res;
    });
    const reqMock: TestRequest = httpTestingController.expectOne(HttpConstants.API_REFRESH);
    reqMock.flush(null);
    expect(reqMock.request.method).toBe('PUT');
    expect(responseMock.accessToken).toBe('');
    expect(responseMock.refreshToken).toBe('');
  });

  it('should make a request to register a user and return the register response', () => {
    const registerRequestMock: RegisterRequest = {
      username: 'usernameMock',
      firstName: 'firstNameMock',
      lastName: 'lastNameMock',
      email: 'emailMock',
      bornOn: '1999-04-18',
      password: 'passwordMock',
      confirm: 'passwordMock'
    };
    const registerResponseMock: RegisterResponse = {
      id: 1,
      username: 'usernameMock'
    };
    let responseMock!: RegisterResponse;
    service.registerUser(registerRequestMock).subscribe((res: RegisterResponse) => {
      responseMock = res;
    });
    const reqMock: TestRequest = httpTestingController.expectOne(HttpConstants.API_REGISTER);
    reqMock.flush(registerResponseMock);
    expect(reqMock.request.method).toBe('POST');
    expect(responseMock).toEqual(registerResponseMock);
  });

});
