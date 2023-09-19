import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { LocalStorageService } from '../localStorage/local-storage.service';
import { LoginRequest } from '../../models/authentication/login.request';
import { RegisterRequest } from '../../models/authentication/register.request';
import { LoginResponse } from '../../models/authentication/login.response';
import { RegisterResponse } from '../../models/authentication/register.response';
import { HttpConstants } from '../../../modules/shared/constants/http.constants';
import { Tokens } from '../../../modules/shared/constants/auth.constants';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(
    private http: HttpClient,
    private localStorageService: LocalStorageService
  ) { }

  public get token(): string | null {
    return this.localStorageService.getItem(Tokens.ACCESS_TOKEN);
  }

  public get refreshToken(): string | null {
    return this.localStorageService.getItem(Tokens.REFRESH_TOKEN);
  }

  public isAuthenticated(): boolean {
    return !!this.token;
  }

  public loginUser(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(HttpConstants.API_LOGIN, data, { observe: 'response' })
      .pipe(
        map((responseData: HttpResponse<LoginResponse>) => {
            const loginResponse: LoginResponse = <LoginResponse>{};

            loginResponse.accessToken = responseData.headers.get(Tokens.AUTHORIZATION) || '';
            loginResponse.refreshToken = responseData.body?.refreshToken || '';

            return loginResponse;
          }
        )
      );
  }

  public refreshAccessToken(): Observable<LoginResponse> {
    return this.http.put<LoginResponse>(HttpConstants.API_REFRESH, null, { observe: 'response' })
      .pipe(
        map((responseData: HttpResponse<LoginResponse>) => {
            const loginResponse: LoginResponse = <LoginResponse>{};

            loginResponse.accessToken = responseData.headers.get(Tokens.AUTHORIZATION) || '';
            loginResponse.refreshToken = responseData.body?.refreshToken || '';

            return loginResponse;
          }
        )
      );
  }

  public registerUser(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(HttpConstants.API_REGISTER, data);
  }

}
