import { TestBed } from '@angular/core/testing';

import { CardEditService } from './card-edit.service';

describe('CardEditService', () => {
  let service: CardEditService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CardEditService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
