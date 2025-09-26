import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { SnackbarService } from './snackbar.service';
import { jwtDecode } from 'jwt-decode';
import { GlobalConstants } from '../shared/global-constants';
import { Location } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService {

  constructor(public auth: AuthService,
    public router: Router,
    private location: Location, 
    private snacBarService: SnackbarService) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {

    let expectedRoleArray = route.data.expectedRole;
    // console.log("expectedRoleArray: " + expectedRoleArray);
    const token: any = localStorage.getItem('token');
    var tokenPayload: any;

    try {
      tokenPayload = jwtDecode(token);
      // console.log("tokenPayload: " + tokenPayload);
    } catch (err) {
      localStorage.clear();
      this.router.navigate(['/'])
    }

    let expectedRole = ''; //?
    for (let i = 0; i < expectedRoleArray.length; i++) {
      if (expectedRoleArray[i] == tokenPayload.role) {
        expectedRole = tokenPayload.role;
      }
    }

    if (tokenPayload.role == 'USER' || tokenPayload.role == 'ADMIN') {
      if (this.auth.isAuthenticated() && tokenPayload.role == expectedRole) {
        // if (this.location.path().includes('auth/')) {
        //   this.router.navigate(['/cafe/dashboard']);
        //   return false;
        // }
        
        return true;
      }
      // console.log("this.auth.isAuthenticated(): " + this.auth.isAuthenticated());
      // console.log("tokenPayload.role: " + tokenPayload.role);
      // console.log("expectedRole: " + expectedRole);
      this.snacBarService.openSnackBar(GlobalConstants.unauthorized, GlobalConstants.error);
      this.router.navigate(['/cafe/dashboard']);
      return false;
    } else {
      this.router.navigate(['/']);
      localStorage.clear();
      return false;
    }
  }
}
