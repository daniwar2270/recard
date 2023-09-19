import { Component, OnInit, Input } from '@angular/core';
import { ModelFormGroup, toFormGroup } from '@codexio/ngx-reactive-forms-generator';
import { ProfileService } from '../../../core/service/profile/profile.service';
import { ValidationService } from '../../../core/service/validation/validation.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { MessageType } from '../../../core/models/modals/success-message.types';
import { ProfileResponse } from '../../../core/models/profile/profile.response';
import { ProfileRequest } from '../../../core/models/profile/profile.request';
import {
  FIELD_REQUIRED,
  FIRSTNAME_INVALID_FORMAT,
  LASTNAME_INVALID_FORMAT
} from '../../shared/constants/messages.constants';
import { Profile, SUCCESS } from '../../shared/constants/pop-up.constants';
import { BIRTH_DATE_MAX, BIRTH_DATE_MIN } from '../../shared/constants/validation.constants';

@Component({
  selector: 'rec-profile-account',
  templateUrl: './profile-account.component.html',
  styleUrls: ['./profile-account.component.scss']
})
export class ProfileAccountComponent implements OnInit {

  protected readonly requiredMessage: string = FIELD_REQUIRED;
  protected readonly firstNameMessage: string = FIRSTNAME_INVALID_FORMAT;
  protected readonly lastNameMessage: string = LASTNAME_INVALID_FORMAT;
  protected readonly minDate: string = BIRTH_DATE_MIN;
  protected readonly maxDate: string = BIRTH_DATE_MAX;

  @Input() profileInformation: ProfileResponse = <ProfileResponse>{};

  public editProfileForm: ModelFormGroup<ProfileRequest> = toFormGroup<ProfileRequest>(ProfileRequest);
  public editMode: boolean = false;
  public isLoading: boolean = false;

  constructor(
    public validationService: ValidationService,
    private profileService: ProfileService,
    private messageService: StatusMessageService
  ) { }

  public ngOnInit(): void {
    this.editMode = false;
    this.editProfileForm.reset();
  }

  public switchEditMode(): void {
    this.editMode = !this.editMode;
    this.editProfileForm.reset();
  }

  public isFormEmpty(): boolean {
    return !this.editProfileForm.value?.firstName
      && !this.editProfileForm.value?.lastName
      && !this.editProfileForm.value?.bornOn;
  }

  public saveUserData(): void {
    if (this.editProfileForm.invalid || !this.editProfileForm.value || this.isFormEmpty()) {
      return;
    }

    this.isLoading = true;

    this.profileService.editUserProfile(this.editProfileForm.value).subscribe({
      next: (response: ProfileResponse): void => {
        this.isLoading = false;

        this.profileInformation = {
          ...this.profileInformation,
          firstName: response.firstName,
          lastName: response.lastName,
          bornOn: response.bornOn
        };

        this.messageService.initiate({
          title: SUCCESS,
          content: Profile.EDIT_SUCCESS,
          type: MessageType.success
        });

        this.switchEditMode();
      },
      error: (error: Error): void => {
        this.isLoading = false;

        this.messageService.initiate({
          title: Profile.EDIT_FAILED,
          content: error.message,
          type: MessageType.error
        });
      }
    });
  }

}
