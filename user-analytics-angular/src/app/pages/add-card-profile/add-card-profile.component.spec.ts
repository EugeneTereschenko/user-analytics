import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCardProfileComponent } from './add-card-profile.component';

describe('AddCardProfileComponent', () => {
  let component: AddCardProfileComponent;
  let fixture: ComponentFixture<AddCardProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCardProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCardProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
