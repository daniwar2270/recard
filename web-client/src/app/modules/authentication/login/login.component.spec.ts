import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { of, throwError } from 'rxjs';
import { AuthenticationService } from '../../../core/service/authentication/authentication.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { LocalStorageService } from '../../../core/service/localStorage/local-storage.service';
import { LoginComponent } from './login.component';
import { LoaderComponent } from '../../shared/loader/loader.component';
import { SuccessMessageData } from '../../../core/models/modals/success-message.data';
import { Tokens } from '../../shared/constants/auth.constants';
import { Auth } from '../../shared/constants/pop-up.constants';
import { MessageType } from '../../../core/models/modals/success-message.types';
import { LoginRequestMock } from '../../shared/constants/mock-data/login.request.mock';
import { LoginResponse } from '../../../core/models/authentication/login.response';

describe('LoginComponent', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthenticationService;
  let messageService: StatusMessageService;
  let localStorageService: LocalStorageService;
  let routerSpy: jasmine.SpyObj<Router>;

  const loginFormMock: ModelFormGroup<LoginRequestMock> = toFormGroup<LoginRequestMock>(LoginRequestMock);

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      declarations: [
        LoginComponent,
        LoaderComponent
       ],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      providers: [
        {
          provide: 'loginForm',
          useValue: loginFormMock
        },
        {
          provide: Router,
          useValue: routerSpy
        }
      ]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();

    authService = TestBed.inject(AuthenticationService);
    messageService = TestBed.inject(StatusMessageService);
    localStorageService = TestBed.inject(LocalStorageService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not send login request when form data is invalid', () => {
    spyOn(authService, 'loginUser');
    component.loginUser();
    expect(component.loginForm.valid).toBeFalse();
    expect(authService.loginUser).not.toHaveBeenCalled();
  });

  it('should reset the form when we initiate the component', ()=> {
    spyOn(component.loginForm, 'reset');
    component.ngOnInit();
    expect(component.loginForm.reset).toHaveBeenCalled();
  });

  it('should save received tokens in localStorage', () => {
    component.loginForm = loginFormMock;
    loginFormMock.patchValue({
      username: 'validName',
      password: 'validPass'
    });
    const loginResponseMock: LoginResponse = {
      accessToken: 'access-token',
      refreshToken: 'refresh-token'
    };
    spyOn(authService, 'loginUser').and.returnValue(of(loginResponseMock));
    component.loginUser();
    expect(localStorageService.getItem(Tokens.ACCESS_TOKEN)).toEqual(loginResponseMock.accessToken);
    expect(localStorageService.getItem(Tokens.REFRESH_TOKEN)).toEqual(loginResponseMock.refreshToken);
  });

  it('should display error message when login fails', () => {
    component.loginForm = loginFormMock;
    loginFormMock.patchValue({
      username: 'validName',
      password: 'validPass'
    });
    const errorMsgMock: Error = new Error('No such user exists');
    const messageDataMock: SuccessMessageData = {
      title: Auth.LOGIN_FAILED,
      content: errorMsgMock.message,
      type: MessageType.error
    };
    spyOn(authService, 'loginUser').and.returnValue(throwError(() => errorMsgMock));
    spyOn(messageService, 'initiate');
    component.loginUser();
    expect(messageService.initiate).toHaveBeenCalledWith(messageDataMock);
  });

});
