import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDetailsProfileComponent } from './edit-details-profile.component';

describe('EditDetailsProfileComponent', () => {
  let component: EditDetailsProfileComponent;
  let fixture: ComponentFixture<EditDetailsProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditDetailsProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditDetailsProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
