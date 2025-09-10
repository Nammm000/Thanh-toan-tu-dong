import { TestBed } from '@angular/core/testing';
import { HttpInterceptor } from '@angular/common/http';

import { TokenInterceptorInterceptor } from './token-interceptor.interceptor';

describe('TokenInterceptorInterceptor', () => {
  const interceptor: HttpInterceptor = (req, next) => 
    TestBed.runInInjectionContext(() => new TokenInterceptorInterceptor(req, next));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });
});
