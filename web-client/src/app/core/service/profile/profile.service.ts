import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable } from 'rxjs';
import { ProfileRequest } from '../../models/profile/profile.request';
import { ProfileResponse } from '../../models/profile/profile.response';
import { ImageUploadResponse } from '../../models/profile/image-upload.response';
import { UsersMeResponse } from '../../models/profile/users-me.response';
import { HttpConstants } from '../../../modules/shared/constants/http.constants';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(
    private http: HttpClient
  ) { }

  public getUserAuthInfo(): Observable<UsersMeResponse> {
    return this.http.get<UsersMeResponse>(HttpConstants.API_USERS_ME);
  }

  public getUserProfileInfo(): Observable<ProfileResponse> {
    return this.http.get<ProfileResponse>(HttpConstants.API_PROFILE);
  }

  public getUserInfo(): Observable<ProfileResponse> {
    return forkJoin([this.getUserAuthInfo(), this.getUserProfileInfo()])
      .pipe(
        map(([authInfo, profileInfo]) => {
          return <ProfileResponse>{ ...authInfo, ...profileInfo };
        })
      );
  }

  public editUserProfile(data: ProfileRequest): Observable<ProfileResponse> {
    return this.http.put<ProfileResponse>(HttpConstants.API_PROFILE, data);
  }

  public uploadImage(data: File): Observable<ImageUploadResponse> {
    const formData = new FormData();
    formData.append('file', data);

    return this.http.post<ImageUploadResponse>(HttpConstants.API_PROFILE_IMAGE_UPLOAD, formData);
  }

}
