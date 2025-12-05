import { TestBed } from '@angular/core/testing';

import { SystemstatusService } from './systemstatus.service';

describe('SystemstatusService', () => {
  let service: SystemstatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SystemstatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
