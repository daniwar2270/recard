import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthenticationService } from '../../../core/service/authentication/authentication.service';
import { LocalStorageService } from '../../../core/service/localStorage/local-storage.service';
import { NavigationComponent } from './navigation.component';
import { Tokens } from '../constants/auth.constants';
import { AppNavigation } from '../constants/routing.constants';

describe('NavigationComponent', () => {

  let component: NavigationComponent;
  let fixture: ComponentFixture<NavigationComponent>;
  let authService: AuthenticationService;
  let localStorageService: LocalStorageService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NavigationComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule
      ]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NavigationComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();

    authService = TestBed.inject(AuthenticationService);
    localStorageService = TestBed.inject(LocalStorageService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show or hide navigation when we initiate the component', () => {
    spyOn(component, 'onWindowResize');
    component.ngOnInit();
    expect(component.onWindowResize).toHaveBeenCalled();
  });

  it('should return user is authenticated', () => {
    spyOn(authService, 'isAuthenticated');
    component.isAuth;
    expect(authService.isAuthenticated).toHaveBeenCalled();
  });

  it('should return navigation is open ', () => {
    expect(component.isNavOpen).toBeTrue();
  });

  it('should logout user', () => {
    spyOn(localStorageService, 'removeItem');
    spyOn(router, 'navigate');
    component.logout();
    expect(localStorageService.removeItem).toHaveBeenCalledWith(Tokens.ACCESS_TOKEN);
    expect(localStorageService.removeItem).toHaveBeenCalledWith(Tokens.REFRESH_TOKEN);
    expect(router.navigate).toHaveBeenCalledWith(AppNavigation.LOGIN);
  });

  it('should hide navigation on tablet resolution', () => {
    spyOn(component, 'setOpenNav');
    spyOnProperty(window, 'innerWidth', 'get').and.returnValue(768);
    component.onWindowResize();
    expect(component.setOpenNav).toHaveBeenCalledWith(false);
  });

  it('should open navigation on laptop resolution', () => {
    spyOn(component, 'setOpenNav');
    spyOnProperty(window, 'innerWidth', 'get').and.returnValue(769);
    component.onWindowResize();
    expect(component.setOpenNav).toHaveBeenCalledWith(true);
  });

  it('should open navigation', () => {
    component.setOpenNav(true);
    expect(component.isNavOpen).toBeTrue();
    expect(component.navHiddenClass).toBe('');
  });

  it('should hide navigation', () => {
    component.setOpenNav(false);
    expect(component.isNavOpen).toBeFalse();
    expect(component.navHiddenClass).toBe('hidden');
  });

});
