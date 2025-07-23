import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsageInsightsComponent } from './usage-insights.component';

describe('UsageInsightsComponent', () => {
  let component: UsageInsightsComponent;
  let fixture: ComponentFixture<UsageInsightsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsageInsightsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UsageInsightsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
