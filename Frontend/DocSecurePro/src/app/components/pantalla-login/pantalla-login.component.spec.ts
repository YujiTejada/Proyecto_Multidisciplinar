import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PantallaLoginComponent } from './pantalla-login.component';

describe('PantallaLoginComponent', () => {
  let component: PantallaLoginComponent;
  let fixture: ComponentFixture<PantallaLoginComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PantallaLoginComponent]
    });
    fixture = TestBed.createComponent(PantallaLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
