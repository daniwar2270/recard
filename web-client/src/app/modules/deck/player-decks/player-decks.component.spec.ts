import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlayerDecksComponent } from './player-decks.component';

describe('PlayerDecksComponent', () => {

  let component: PlayerDecksComponent;
  let fixture: ComponentFixture<PlayerDecksComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlayerDecksComponent]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayerDecksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
