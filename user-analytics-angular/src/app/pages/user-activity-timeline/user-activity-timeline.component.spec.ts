import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserActivityTimelineComponent } from './user-activity-timeline.component';

describe('UserActivityTimelineComponent', () => {
  let component: UserActivityTimelineComponent;
  let fixture: ComponentFixture<UserActivityTimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserActivityTimelineComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserActivityTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
