import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { of, throwError } from 'rxjs';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { ProfileService } from '../../../core/service/profile/profile.service';
import { ProfileAccountComponent } from './profile-account.component';
import { LoaderComponent } from '../../shared/loader/loader.component';
import { SuccessMessageData } from '../../../core/models/modals/success-message.data';
import { Profile, SUCCESS } from '../../shared/constants/pop-up.constants';
import { MessageType } from '../../../core/models/modals/success-message.types';
import { ProfileRequestMock } from '../../shared/constants/mock-data/profile.request.mock';
import { ProfileResponse } from '../../../core/models/profile/profile.response';

describe('ProfileAccountComponent', () => {

  let component: ProfileAccountComponent;
  let fixture: ComponentFixture<ProfileAccountComponent>;
  let profileService: ProfileService;
  let messageService: StatusMessageService;

  const editProfileFormMock: ModelFormGroup<ProfileRequestMock> = toFormGroup<ProfileRequestMock>(ProfileRequestMock);

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        ProfileAccountComponent,
        LoaderComponent
      ],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      providers: [
        {
          provide: 'editProfileForm',
          useValue: editProfileFormMock
        }
      ]
    })
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileAccountComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();

    profileService = TestBed.inject(ProfileService);
    messageService = TestBed.inject(StatusMessageService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should switch to editMode', () => {
    spyOn(component.editProfileForm, 'reset');
    component.switchEditMode();
    expect(component.editMode).toBeTrue();
    expect(component.editProfileForm.reset).toHaveBeenCalled();
  });

  it('should not send edit request when form is invalid', () => {
    component.editProfileForm = editProfileFormMock;
    editProfileFormMock.patchValue({
      firstName: null,
      lastName: null,
      bornOn: null
    });
    spyOn(profileService, 'editUserProfile');
    component.saveUserData();
    expect(profileService.editUserProfile).not.toHaveBeenCalled();
  });

  it('should edit profile data when data is valid', () => {
    component.editProfileForm = editProfileFormMock;
    editProfileFormMock.patchValue({
      firstName: 'FirstNameMock',
      bornOn: '2000-11-11'
    });
    const profileResponseMock: ProfileResponse = {
      firstName: 'FirstNameMock',
      lastName: '',
      bornOn: '2000-11-11'
    };
    spyOn(profileService, 'editUserProfile').and.returnValue(of(profileResponseMock));
    component.saveUserData();
    fixture.detectChanges();
    expect(component.profileInformation).toEqual(profileResponseMock);
  });

  it('should pass data to the messageService', () => {
    component.editProfileForm = editProfileFormMock;
    editProfileFormMock.patchValue({
      firstName: 'Changed username',
      lastName: 'Changed lastname',
      bornOn: '2000-11-11'
    });
    const profileResponseMock: ProfileResponse = {
      username: '',
      email: '',
      firstName: 'Changed username',
      lastName: 'Changed lastname',
      bornOn: '2000-11-11'
    };
    const messageDataMock: SuccessMessageData = {
      title: SUCCESS,
      content: Profile.EDIT_SUCCESS,
      type: MessageType.success
    };
    spyOn(profileService, 'editUserProfile').and.returnValue(of(profileResponseMock));
    spyOn(messageService, 'initiate');
    component.saveUserData();
    fixture.detectChanges();
    expect(messageService.initiate).toHaveBeenCalledWith(messageDataMock);
  });

  it('should pass error data to the messageService', () => {
    component.editProfileForm = editProfileFormMock;
    editProfileFormMock.patchValue({
      firstName: 'Changed username',
      lastName: 'Changed lastname',
      bornOn: '2000-11-11'
    });
    const mockError: Error = new Error('Mock error message');
    const messageDataMock = {
      title: Profile.EDIT_FAILED,
      content: mockError.message,
      type: MessageType.error
    };
    spyOn(profileService, 'editUserProfile').and.returnValue(throwError(() => mockError));
    spyOn(messageService, 'initiate');
    component.saveUserData();
    fixture.detectChanges();
    expect(messageService.initiate).toHaveBeenCalledWith(messageDataMock);
  });

});
