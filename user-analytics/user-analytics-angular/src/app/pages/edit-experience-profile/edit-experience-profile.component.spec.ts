import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditExperienceProfileComponent } from './edit-experience-profile.component';

describe('EditExperienceProfileComponent', () => {
  let component: EditExperienceProfileComponent;
  let fixture: ComponentFixture<EditExperienceProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditExperienceProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditExperienceProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
