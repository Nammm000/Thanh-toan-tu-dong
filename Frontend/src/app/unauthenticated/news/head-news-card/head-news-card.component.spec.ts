import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeadNewsCardComponent } from './head-news-card.component';

describe('HeadNewsCardComponent', () => {
  let component: HeadNewsCardComponent;
  let fixture: ComponentFixture<HeadNewsCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeadNewsCardComponent]
    });
    fixture = TestBed.createComponent(HeadNewsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
