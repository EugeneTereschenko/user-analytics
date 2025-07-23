import { TestBed } from '@angular/core/testing';

import { SupportService } from './support-service.service'; // Adjust the import path as necessary

describe('SupportService', () => {
  let service: SupportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
