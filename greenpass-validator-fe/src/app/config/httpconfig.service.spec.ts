import { TestBed } from '@angular/core/testing';

import { HttpconfigService } from './httpconfig.service';

describe('HttpconfigService', () => {
  let service: HttpconfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpconfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
