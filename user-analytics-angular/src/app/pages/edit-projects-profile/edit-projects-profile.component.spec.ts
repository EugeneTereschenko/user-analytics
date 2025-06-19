import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProjectsProfileComponent } from './edit-projects-profile.component';

describe('EditProjectsProfileComponent', () => {
  let component: EditProjectsProfileComponent;
  let fixture: ComponentFixture<EditProjectsProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditProjectsProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditProjectsProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
