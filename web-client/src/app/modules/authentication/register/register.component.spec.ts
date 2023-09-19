import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { of, throwError } from 'rxjs';
import { ValidationService } from '../../../core/service/validation/validation.service';
import { AuthenticationService } from '../../../core/service/authentication/authentication.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { RegisterComponent } from './register.component';
import { LoaderComponent } from '../../shared/loader/loader.component';
import { RegisterResponse } from '../../../core/models/authentication/register.response';
import { MessageType } from '../../../core/models/modals/success-message.types';
import { SuccessMessageData } from '../../../core/models/modals/success-message.data';
import { Auth, SUCCESS } from '../../shared/constants/pop-up.constants';
import { RegisterRequestMock } from '../../shared/constants/mock-data/register.request.mock';
import { AppNavigation } from '../../shared/constants/routing.constants';

describe('RegisterComponent', () => {

  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let validationService: ValidationService;
  let authService: AuthenticationService;
  let messageService: StatusMessageService;
  let routerSpy: jasmine.SpyObj<Router>;

  const registerFormMock: ModelFormGroup<RegisterRequestMock> = toFormGroup<RegisterRequestMock>(RegisterRequestMock);

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      declarations: [
        RegisterComponent,
        LoaderComponent
      ],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      providers: [
        {
          provide: Router,
          useValue: routerSpy
        }
      ]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();

    authService = TestBed.inject(AuthenticationService);
    messageService = TestBed.inject(StatusMessageService);
    validationService = TestBed.inject(ValidationService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should reset the form when we initiate the component', () => {
    spyOn(component.registerForm, 'reset');
    component.ngOnInit();
    expect(component.registerForm.reset).toHaveBeenCalled();
  });

  it('should not validate when the form is invalid', () => {
    spyOnProperty(component.registerForm, 'valid', 'get').and.returnValue(false);
    spyOn(validationService, 'validateDateRange').and.returnValue(false);
    spyOn(validationService, 'checkPasswordsMatch').and.returnValue(false);
    expect(component.isValidForm()).toBeFalse();
  });

  it('should not validate when date is not in range', () => {
    spyOnProperty(component.registerForm, 'valid', 'get').and.returnValue(true);
    spyOn(validationService, 'validateDateRange').and.returnValue(true);
    spyOn(validationService, 'checkPasswordsMatch').and.returnValue(false);
    expect(component.isValidForm()).toBeFalse();
  });

  it('should not validate when passwords not match', () => {
    spyOnProperty(component.registerForm, 'valid', 'get').and.returnValue(true);
    spyOn(validationService, 'validateDateRange').and.returnValue(false);
    spyOn(validationService, 'checkPasswordsMatch').and.returnValue(true);
    expect(component.isValidForm()).toBeFalse();
  });

  it('should validate all if form is valid', () => {
    spyOnProperty(component.registerForm, 'valid', 'get').and.returnValue(true);
    spyOn(validationService, 'validateDateRange').and.returnValue(false);
    spyOn(validationService, 'checkPasswordsMatch').and.returnValue(false);
    expect(component.isValidForm()).toBeTrue();
  });

  it('should not register user if form is invalid', () => {
    component.registerUser();
    expect(component.registerForm.invalid).toBeTrue();
  });

  it('should register user', () => {
    component.registerForm = registerFormMock;
    registerFormMock.patchValue({
      username: 'usernameMock',
      firstName: 'firstNameMock',
      lastName: 'lastNameMock',
      email: 'emailMock',
      bornOn: '1999-04-18',
      password: 'passwordMock',
      confirm: 'passwordMock'
    });
    const registerResponseMock: RegisterResponse = {
      id: 1,
      username: 'usernameMock'
    };
    const messageDataMock: SuccessMessageData = {
      title: SUCCESS,
      content: Auth.REGISTER_SUCCESS,
      type: MessageType.success
    };
    spyOn(authService, 'registerUser').and.returnValue(of(registerResponseMock));
    spyOn(messageService, 'initiate');
    spyOn(component.registerForm, 'reset');
    component.registerUser();
    expect(messageService.initiate).toHaveBeenCalledWith(messageDataMock);
    expect(routerSpy.navigate).toHaveBeenCalledWith([AppNavigation.LOGIN]);
  });

  it('should not register user', () => {
    component.registerForm = registerFormMock;
    registerFormMock.patchValue({
      username: 'usernameMock',
      firstName: 'firstNameMock',
      lastName: 'lastNameMock',
      email: 'emailMock',
      bornOn: '1999-04-18',
      password: 'passwordMock',
      confirm: 'passwordMock'
    });
    const errorMsgMock: string = 'A user with the given username already exists.';
    const messageDataMock: SuccessMessageData = {
      title: Auth.REGISTER_FAILED,
      content: `${errorMsgMock}`,
      type: MessageType.error
    };
    spyOn(authService, 'registerUser').and.returnValue(throwError(new Error(errorMsgMock)));
    spyOn(messageService, 'initiate');
    component.registerUser();
    expect(messageService.initiate).toHaveBeenCalledWith(messageDataMock);
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

});
