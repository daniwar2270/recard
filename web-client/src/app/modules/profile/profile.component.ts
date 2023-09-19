import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProfileService } from '../../core/service/profile/profile.service';
import { ProfileResponse } from '../../core/models/profile/profile.response';
import { DEFAULT_AVATAR_PATH, ProfileNavigation } from '../shared/constants/profile.constants';
import { AppNavigation } from '../shared/constants/routing.constants';

@Component({
  selector: 'rec-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

  protected readonly ProfileNavigation: any = ProfileNavigation;
  public readonly defaultAvatar: string = DEFAULT_AVATAR_PATH;

  public selectedSection: string = ProfileNavigation.ACCOUNT;
  public profileInformation: ProfileResponse = <ProfileResponse>{}
  public isLoading: boolean = false;

  constructor(
    private profileService: ProfileService,
    private router: Router
  ) { }

  public ngOnInit(): void {
    this.getUserProfile();
  }

  public receiveSelectedSection($event: string): void {
    this.selectedSection = $event;
  }

  public getUserProfile(): void {
    this. isLoading = true;

    this.profileService.getUserInfo().subscribe({
      next: (response: ProfileResponse): void => {
        this.profileInformation = response;
        this.isLoading = false;
      },
      error: (): void => {
        this.isLoading = false;
        this.router.navigate([AppNavigation.PAGE_NOT_FOUND]);
      }
    });
  }

}
