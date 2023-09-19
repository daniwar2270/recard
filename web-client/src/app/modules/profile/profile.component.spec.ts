import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';
import { ProfileService } from '../../core/service/profile/profile.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LoaderComponent } from '../shared/loader/loader.component';
import { ProfileNavigation } from '../shared/constants/profile.constants';
import { of } from 'rxjs';
import { ProfileResponse } from '../../core/models/profile/profile.response';

describe('ProfileComponent', () => {

  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let profileService: ProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        ProfileComponent,
        LoaderComponent
      ],
      imports: [
        HttpClientTestingModule
      ]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();

    profileService = TestBed.inject(ProfileService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update the selected section with the provided value', () => {
    const selectedSection: string = ProfileNavigation.ACCOUNT;
    component.receiveSelectedSection(selectedSection);
    expect(component.selectedSection).toEqual(selectedSection);
  });

  it('should get profile information and set it in the component', () => {
    const profileInfoMock: ProfileResponse = {
      username: 'usernameMock',
      email: 'emailMock@gmail.com',
      firstName: 'firstNameMock',
      lastName: 'lastNameMock',
      bornOn: '01-01-1990'
    };
    spyOn(profileService, 'getUserInfo').and.returnValue(of(profileInfoMock));
    component.getUserProfile();
    expect(component.profileInformation).toBe(profileInfoMock);
  });

});
