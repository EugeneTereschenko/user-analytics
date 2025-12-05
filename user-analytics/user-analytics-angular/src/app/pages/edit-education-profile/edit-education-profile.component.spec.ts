import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditEducationProfileComponent } from './edit-education-profile.component';

describe('EditEducationProfileComponent', () => {
  let component: EditEducationProfileComponent;
  let fixture: ComponentFixture<EditEducationProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditEducationProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditEducationProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
