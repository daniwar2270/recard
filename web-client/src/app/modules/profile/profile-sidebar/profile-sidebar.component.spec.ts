import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { ProfileService } from '../../../core/service/profile/profile.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { ProfileSidebarComponent } from './profile-sidebar.component';
import { MessageType } from '../../../core/models/modals/success-message.types';
import { SuccessMessageData } from '../../../core/models/modals/success-message.data';
import { ERROR, ImageUpload, SUCCESS } from '../../shared/constants/pop-up.constants';
import { ProfileNavigation } from '../../shared/constants/profile.constants';

describe('ProfileSidebarComponent', () => {

  let component: ProfileSidebarComponent;
  let fixture: ComponentFixture<ProfileSidebarComponent>;
  let messageService: StatusMessageService;
  let profileService: ProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfileSidebarComponent],
      imports: [
        HttpClientTestingModule
      ]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileSidebarComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    profileService = TestBed.inject(ProfileService) as jasmine.SpyObj<ProfileService>;
    messageService = TestBed.inject(StatusMessageService) as jasmine.SpyObj<StatusMessageService>;

    component.tempImage = 'temp_image_mock.jpg';
    component.profileImage = 'profile_image_mock.jpg';
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set file when onFileChange is called', () => {
    const event = { target: { files: [new File([''], 'imagePathMock.jpg')] } };
    component.onFileChange(event);
    expect(component['file']).toBeDefined();
  });

  it('should reset file when we upload image', () => {
    const fileNameMock: string = 'imagePathMock.jpg';
    component['file'] = new File([''], fileNameMock);
    spyOn(profileService, 'uploadImage').and.returnValue(of({
      imageUrl: fileNameMock
    }));
    component.uploadImage();
    expect(component['file']).toBeNull();
  });

  it('should display success message when upload is successful', () => {
    const fileNameMock: string = 'imagePathMock.jpg';
    const messageDataMock: SuccessMessageData = {
      title: SUCCESS,
      content: ImageUpload.UPLOAD_SUCCESS,
      type: MessageType.success
    };
    component['file'] = new File([''], fileNameMock);
    spyOn(messageService, 'initiate');
    spyOn(profileService, 'uploadImage').and.returnValue(of({
      imageUrl: fileNameMock
    }));
    component.uploadImage();
    expect(messageService.initiate).toHaveBeenCalledWith(messageDataMock);
  });

  it('should display error message when upload fails', () => {
    const errorMock: Error = new Error('Image could not be uploaded');
    const messageDataMock: SuccessMessageData = {
      title: ERROR,
      content: errorMock.message,
      type: MessageType.error
    };
    component['file'] = new File([''], 'imagePathMock.jpg');
    spyOn(messageService, 'initiate');
    spyOn(profileService, 'uploadImage').and.returnValue(throwError(() => errorMock));
    component.uploadImage();
    expect(messageService.initiate).toHaveBeenCalledWith(messageDataMock);
  });

  it('should clear temporary image when we cancel uploading', () => {
    component.cancelImage();
    expect(component.tempImage).toBeNull();
  });

  it('should change selected section', () => {
    const section: string = ProfileNavigation.ACCOUNT;
    spyOn(component.selectSectionEvent, 'emit');
    component.selectSection(section);
    expect(component.selectedSection).toEqual(section);
    expect(component.selectSectionEvent.emit).toHaveBeenCalledWith(section);
  });

});
