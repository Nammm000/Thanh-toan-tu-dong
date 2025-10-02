import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ChangePasswordComponent } from 'src/app/material-component/dialog/change-password/change-password.component';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';
import { jwtDecode } from 'jwt-decode';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { UserService } from 'src/app/service/user.service';

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
    private ngxService: NgxUiLoaderService,
    private userService: UserService,
    private dialog: MatDialog ) {
    if(this.token) {
      this.tokenPaload = jwtDecode(this.token);
      this.userRole = this.tokenPaload?.role + '';
      // console.log("true: " + this.userRole);
    }
    // console.log("this.userRole");
    
  }

  logoutAction() {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.data = {
          message: 'logout',
          confirmation: true
        };
        const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
        const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
          (response: any) => {
            this.logout();
            
            dialogRef.close();
          }
        );
      }
    
      logout() {
        this.ngxService.start();
        this.userService.logout().subscribe((response: any) => {
          localStorage.clear();
          this.ngxService.stop();
          this.router.navigate(['/']);
        }, (error: any) => {
          localStorage.clear();
          this.ngxService.stop();
          this.router.navigate(['/']);
        });
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
