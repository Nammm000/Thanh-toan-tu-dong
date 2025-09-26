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

  constructor(private router: Router,
   private dialog: MatDialog ) {
    if(this.token) {
      this.tokenPaload = jwtDecode(this.token);
      this.userRole = this.tokenPaload?.role + '';
      // console.log("true: " + this.userRole);
    }
    // console.log("this.userRole");
    
  }


  logout() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'Logout',
      confirmation: true
    };

    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe((response) => {
      dialogRef.close();
      localStorage.clear();
      this.router.navigate(['/']);
    })
  }
  
  changePassword() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "550px";
    this.dialog.open(ChangePasswordComponent, dialogConfig);
  }

  isLoggedIn(): boolean {
    if (this.userRole) {
      // console.log("true: " + this.userRole);
      return true;
    }
    // console.log("false: " + this.userRole);
    return false;
  }
}
