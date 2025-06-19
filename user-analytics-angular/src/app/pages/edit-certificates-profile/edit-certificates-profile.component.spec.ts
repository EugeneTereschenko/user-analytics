import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCertificatesProfileComponent } from './edit-certificates-profile.component';

describe('EditCertificatesProfileComponent', () => {
  let component: EditCertificatesProfileComponent;
  let fixture: ComponentFixture<EditCertificatesProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditCertificatesProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditCertificatesProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
