import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSkillsProfileComponent } from './edit-skills-profile.component';

describe('EditSkillsProfileComponent', () => {
  let component: EditSkillsProfileComponent;
  let fixture: ComponentFixture<EditSkillsProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditSkillsProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditSkillsProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
