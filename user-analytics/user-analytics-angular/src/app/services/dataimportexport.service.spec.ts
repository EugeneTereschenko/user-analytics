import { TestBed } from '@angular/core/testing';

import { DataimportexportService } from './dataimportexport.service';

describe('DataimportexportService', () => {
  let service: DataimportexportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DataimportexportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
