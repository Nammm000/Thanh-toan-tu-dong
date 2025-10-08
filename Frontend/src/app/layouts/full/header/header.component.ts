import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ChangePasswordComponent } from 'src/app/material-component/dialog/change-password/change-password.component';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';
import { jwtDecode } from 'jwt-decode';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: []
})
export class AppHeaderComponent {

  userRole: any = "";
  token: any = localStorage.getItem('token') || '';
  tokenPaload: any;

  constructor() {
    this.setUserRole();
    window.addEventListener('storage', (ev: StorageEvent) => {
      if (ev.storageArea === localStorage && ev.key === 'token') {
        this.token = ev.newValue;
        this.setUserRole();
      }
    });
  }

  setUserRole() {
    if (this.token) {
      try {
        this.tokenPaload = jwtDecode(this.token);
        this.userRole = this.tokenPaload.role;
        // console.log(this.tokenPaload);
      } catch (err) {
      }
    }
  }

  isLoggedIn(userRole: any): boolean {
    if (userRole) {
      // console.log("true: " + userRole);
      return true;
    }
    // console.log("false: " + userRole);
    return false;
  }
}
