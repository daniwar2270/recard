import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from '../../../core/service/localStorage/local-storage.service';
import { AuthenticationService } from '../../../core/service/authentication/authentication.service';
import { AppNavigation } from '../constants/routing.constants';
import { Tokens } from '../constants/auth.constants';

@Component({
  selector: 'rec-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  protected readonly AppNavigation: any = AppNavigation;

  public navHiddenClass: string = '';
  private _isNavOpen: boolean = true;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private localStorageService: LocalStorageService
  ) { }

  public ngOnInit(): void {
    this.onWindowResize();
  }

  public get isAuth(): boolean {
    return this.authService.isAuthenticated();
  }

  public get isNavOpen(): boolean {
    return this._isNavOpen;
  }

  public logout(): void {
    this.localStorageService.removeItem(Tokens.ACCESS_TOKEN);
    this.localStorageService.removeItem(Tokens.REFRESH_TOKEN);
    this.router.navigate([AppNavigation.LOGIN]);
  }

  @HostListener('window:resize', ['$event'])
  public onWindowResize(): void {
    if (window.innerWidth <= 768) {
      this.setOpenNav(false);
    } else {
      this.setOpenNav(true);
    }
  }

  public setOpenNav(isOpen: boolean): void {
    this._isNavOpen = isOpen;

    if (!this._isNavOpen) {
      this.navHiddenClass = 'hidden';
    } else {
      this.navHiddenClass = '';
    }
  }

}
